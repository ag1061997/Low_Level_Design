package strategies.pricing;

import entities.Seat;
import enums.SeatType;

import java.util.List;
import java.util.Map;

public interface PricingStrategy {
    double calculatePrice(List<Seat> seats, Map<SeatType, Double> seatPrices);
}
