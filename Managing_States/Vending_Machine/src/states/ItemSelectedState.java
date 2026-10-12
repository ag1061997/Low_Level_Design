package states;

import core.VendingMachine;
import enums.Coin;

public class ItemSelectedState implements VendingMachineState {
    @Override
    public void insertCoin(VendingMachine vendingMachine, Coin coin) {
        vendingMachine.addBalance(coin.getValue());
        System.out.println("Coin Inserted: " + coin.getValue());

        int price = vendingMachine.getSelectedItem().getPrice();
        if (vendingMachine.getBalance() >= price) {
            System.out.println("Sufficient money received.");
            vendingMachine.setCurrentState(new HasMoneyState());
        }
    }

    @Override
    public void selectItem(VendingMachine vendingMachine, String code) {
        System.out.println("Item already selected.");
    }

    @Override
    public void dispense(VendingMachine vendingMachine) {
        System.out.println("Please insert sufficient money.");
    }

    @Override
    public void refund(VendingMachine vendingMachine) {
        System.out.println("Refunding: " + vendingMachine.getBalance());
        vendingMachine.setCurrentState(new IdleState());
        vendingMachine.setBalance(0);
        vendingMachine.setSelectedItemCode(null);
    }
}
