import core.OnlineShoppingSystem;
import entities.*;
import enums.ProductCategory;
import strategies.CreditCardPaymentStrategy;
import strategies.UPIPaymentStrategy;

public class Main {
    public static void main(String[] args) {
        OnlineShoppingSystem system = OnlineShoppingSystem.getInstance();

        Product laptop = new Product.Builder("Dell XPS 15", 1499.99)
                .withDescription("A powerful and sleek laptop.")
                .withCategory(ProductCategory.ELECTRONICS)
                .build();
        Product book = new Product.Builder("The Pragmatic Programmer", 45.50)
                .withDescription("A classic book for software developers.")
                .withCategory(ProductCategory.BOOKS)
                .build();

        system.addProduct(laptop, 10);
        system.addProduct(book, 50);

        Address aliceAddress = new Address("123 Main St", "Anytown", "CA", "12345");
        Customer alice = system.registerCustomer("Alice", "alice@example.com", "password123", aliceAddress);

        System.out.println("--- Alice starts shopping ---");

        system.addToCart(alice.getId(), laptop, 1);
        System.out.println("Alice added a laptop to her cart.");

        system.addToCart(alice.getId(), book, 1);
        System.out.println("Alice added a book to her cart.");

        ShoppingCart aliceCart = system.getCustomerCart(alice.getId());
        System.out.printf("Alice's cart total: $%.2f%n", aliceCart.calculateTotal());

        System.out.println("\n--- Alice proceeds to checkout ---");
        Order aliceOrder = system.placeOrder(alice.getId(), new CreditCardPaymentStrategy("1234-5678-9876-5432"));
        if (aliceOrder == null) {
            System.out.println("Order placement failed.");
            return;
        }

        System.out.printf("Order #%s placed successfully for Alice.%n", aliceOrder.getId());

        System.out.println("\n--- Order processing starts ---");

        aliceOrder.shipOrder();
        aliceOrder.deliverOrder();
        aliceOrder.cancelOrder();

        System.out.println("\n--- Out of Stock Scenario ---");
        Customer bob = system.registerCustomer("Bob", "bob@example.com", "pass123", aliceAddress);

        system.addToCart(bob.getId(), laptop, 15);

        Order bobOrder = system.placeOrder(bob.getId(), new UPIPaymentStrategy("testupi@hdfc"));
        if (bobOrder == null) {
            System.out.println("Bob's order was correctly prevented due to insufficient stock.");
        }
    }
}