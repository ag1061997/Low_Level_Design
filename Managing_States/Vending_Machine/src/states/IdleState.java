package states;

import core.VendingMachine;
import enums.Coin;

public class IdleState implements VendingMachineState {
    @Override
    public void insertCoin(VendingMachine vendingMachine, Coin coin) {
        System.out.println("Please select an item before inserting money.");
    }

    @Override
    public void selectItem(VendingMachine vendingMachine, String code) {
        if(!vendingMachine.getInventory().isAvailable(code)) {
            System.out.println("Item not available.");
            return;
        }
        vendingMachine.setCurrentState(new ItemSelectedState());
        vendingMachine.setSelectedItemCode(code);
        System.out.println("Item selected: " + code);
    }

    @Override
    public void dispense(VendingMachine vendingMachine) {
        System.out.println("No item selected.");
    }

    @Override
    public void refund(VendingMachine vendingMachine) {
        System.out.println("No money to refund.");
    }
}
