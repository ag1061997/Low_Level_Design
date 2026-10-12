package core;

import entities.Seat;
import entities.Show;
import enums.SeatStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class SeatLockManager {
    private final Map<Show, Map<Seat, String>> lockedSeats = new ConcurrentHashMap<>();
    private final Map<Show, Map<Seat, ScheduledFuture<?>>> expiryTasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final long LOCK_TIMEOUT_MS = 500;

    public boolean lockSeats(Show show, List<Seat> seats, String userId) {
        synchronized (show.getLock()) {
            for (Seat seat : seats) {
                if (seat.getStatus() != SeatStatus.AVAILABLE) {
                    System.out.println("Seat " + seat.getId() + " is not available.");
                    return false;
                }
            }

            for (Seat seat : seats) {
                seat.setStatus(SeatStatus.LOCKED);
            }
            lockedSeats.computeIfAbsent(show, k -> new HashMap<>());
            for (Seat seat : seats) {
                lockedSeats.get(show).put(seat, userId);
            }

            ScheduledFuture<?> task = scheduler.schedule(() -> unlockSeats(show, seats, userId), LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            expiryTasks.computeIfAbsent(show, k -> new HashMap<>());
            for (Seat seat : seats) {
                expiryTasks.get(show).put(seat, task);
            }

            System.out.println("Locked seats: " + seats.stream().map(Seat::getId).collect(Collectors.toList()) + " for user " + userId);
            return true;
        }
    }

    public boolean confirmSeats(Show show, List<Seat> seats, String userId) {
        synchronized (show.getLock()) {
            Map<Seat, String> showLocks = lockedSeats.get(show);
            if (showLocks == null) {
                return false;
            }
            for (Seat seat : seats) {
                if (!userId.equals(showLocks.get(seat))) {
                    return false;
                }
            }
            for (Seat seat : seats) {
                seat.setStatus(SeatStatus.BOOKED);
                showLocks.remove(seat);
                cancelExpiry(show, seat);
            }
            if (showLocks.isEmpty()) {
                lockedSeats.remove(show);
            }
            return true;
        }
    }

    public void unlockSeats(Show show, List<Seat> seats, String userId) {
        synchronized (show.getLock()) {
            Map<Seat, String> showLocks = lockedSeats.get(show);
            if (showLocks == null) {
                return;
            }
            for (Seat seat : seats) {
                if (userId.equals(showLocks.get(seat))) {
                    showLocks.remove(seat);
                    cancelExpiry(show, seat);
                    if (seat.getStatus() == SeatStatus.LOCKED) {
                        seat.setStatus(SeatStatus.AVAILABLE);
                        System.out.println("Released lock on seat: " + seat.getId());
                    }
                }
            }
            if (showLocks.isEmpty()) {
                lockedSeats.remove(show);
            }
        }
    }

    private void cancelExpiry(Show show, Seat seat) {
        Map<Seat, ScheduledFuture<?>> tasks = expiryTasks.get(show);
        if (tasks != null) {
            ScheduledFuture<?> task = tasks.remove(seat);
            if (task != null) {
                task.cancel(false);
            }
            if (tasks.isEmpty()) {
                expiryTasks.remove(show);
            }
        }
    }

    public void shutdown() {
        System.out.println("Shutting down SeatLockManager scheduler.");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
