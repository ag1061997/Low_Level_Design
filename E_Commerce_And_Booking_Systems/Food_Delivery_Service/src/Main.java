import core.FoodDeliveryService;
import entities.*;
import enums.OrderStatus;
import strategies.assignment.NearestAvailableAgentStrategy;
import strategies.search.RestaurantSearchStrategy;
import strategies.search.SearchByCityStrategy;
import strategies.search.SearchByMenuKeywordStrategy;
import strategies.search.SearchByProximityStrategy;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        FoodDeliveryService service = FoodDeliveryService.getInstance();
        service.setAssignmentStrategy(new NearestAvailableAgentStrategy());

        Address aliceAddress = new Address("123 Maple St", "Springfield", "12345", 40.7128, -74.0060);
        Address pizzaAddress = new Address("456 Oak Ave", "Springfield", "12345", 40.7138, -74.0070);
        Address burgerAddress = new Address("789 Pine Ln", "Springfield", "12345", 40.7108, -74.0050);
        Address tacoAddress = new Address("101 Elm Ct", "Shelbyville", "54321", 41.7528, -75.0160);

        Customer alice = service.registerCustomer("Alice", "123-4567-890", aliceAddress);
        Restaurant pizzaPalace = service.registerRestaurant("Pizza Palace", pizzaAddress);
        Restaurant burgerBarn = service.registerRestaurant("Burger Barn", burgerAddress);
        Restaurant tacoTown = service.registerRestaurant("Taco Town", tacoAddress);
        service.registerDeliveryAgent("Bob", "321-4567-880", new Address("1 B", "Springfield", "12345", 40.71, -74.00));

        pizzaPalace.addToMenu(new MenuItem("P001", "Margherita Pizza", 12.99));
        pizzaPalace.addToMenu(new MenuItem("P002", "Veggie Pizza", 11.99));
        burgerBarn.addToMenu(new MenuItem("B001", "Classic Burger", 8.99));
        tacoTown.addToMenu(new MenuItem("T001", "Crunchy Taco", 3.50));

        System.out.println("\n--- 1. Searching for Restaurants ---");

        System.out.println("\n(A) Restaurants in 'Springfield':");
        List<RestaurantSearchStrategy> citySearch = List.of(new SearchByCityStrategy("Springfield"));
        List<Restaurant> springfieldRestaurants = service.searchRestaurants(citySearch);
        springfieldRestaurants.forEach(r -> System.out.println("  - " + r.getName()));

        System.out.println("\n(B) Restaurants near Alice (within 0.01 distance units):");
        List<RestaurantSearchStrategy> proximitySearch = List.of(new SearchByProximityStrategy(aliceAddress, 0.01));
        List<Restaurant> nearbyRestaurants = service.searchRestaurants(proximitySearch);
        nearbyRestaurants.forEach(r -> System.out.printf("  - %s (Distance: %.4f)\n", r.getName(), aliceAddress.distanceTo(r.getAddress())));

        System.out.println("\n(C) Restaurants that serve 'Pizza':");
        List<RestaurantSearchStrategy> menuSearch = List.of(new SearchByMenuKeywordStrategy("Pizza"));
        List<Restaurant> pizzaRestaurants = service.searchRestaurants(menuSearch);
        pizzaRestaurants.forEach(r -> System.out.println("  - " + r.getName()));

        System.out.println("\n(D) Burger joints near Alice:");
        List<RestaurantSearchStrategy> combinedSearch = List.of(
                new SearchByProximityStrategy(aliceAddress, 0.01),
                new SearchByMenuKeywordStrategy("Burger")
        );
        List<Restaurant> burgerJointsNearAlice = service.searchRestaurants(combinedSearch);
        burgerJointsNearAlice.forEach(r -> System.out.println("  - " + r.getName()));

        System.out.println("\n--- 2. Browsing a Menu ---");
        System.out.println("\nMenu for 'Pizza Palace':");
        Menu pizzaMenu = service.getRestaurantMenu(pizzaPalace.getId());
        pizzaMenu.getItems().values().forEach(item ->
                System.out.printf("  - %s: $%.2f\n", item.getName(), item.getPrice())
        );

        System.out.println("\n--- 3. Placing an Order ---");
        if (!pizzaRestaurants.isEmpty()) {
            Restaurant chosenRestaurant = pizzaRestaurants.get(0);
            MenuItem chosenItem = chosenRestaurant.getMenu().getItem("P001");

            System.out.printf("\nAlice is ordering '%s' from '%s'.\n", chosenItem.getName(), chosenRestaurant.getName());
            var order = service.placeOrder(alice.getId(), chosenRestaurant.getId(), List.of(new OrderItem(chosenItem, 1)));

            System.out.printf("Order total: $%.2f\n", order.getTotalAmount());

            System.out.println("\n--- Restaurant confirms the order ---");
            service.updateOrderStatus(order.getId(), OrderStatus.CONFIRMED);

            System.out.println("\n--- Restaurant starts preparing the order ---");
            service.updateOrderStatus(order.getId(), OrderStatus.PREPARING);

            System.out.println("\n--- Order is ready for pickup ---");
            System.out.println("System will now find the nearest available delivery agent...");
            service.updateOrderStatus(order.getId(), OrderStatus.READY_FOR_PICKUP);

            System.out.println("\n--- Agent delivers the order ---");
            service.updateOrderStatus(order.getId(), OrderStatus.DELIVERED);
        }
    }
}