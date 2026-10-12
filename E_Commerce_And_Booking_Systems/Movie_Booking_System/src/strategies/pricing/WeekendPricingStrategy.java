package strategies.pricing;

import entities.Seat;
import enums.SeatType;

import java.util.List;
import java.util.Map;

public class WeekendPricingStrategy implements PricingStrategy {
    private static final double WEEKEND_SURCHARGE = 1.2; // 20% surcharge

    @Override
    public double calculatePrice(List<Seat> seats, Map<SeatType, Double> seatPrices) {
        double basePrice = seats.stream().mapToDouble(seat -> seatPrices.get(seat.getType())).sum();
        return basePrice * WEEKEND_SURCHARGE;
    }
}
