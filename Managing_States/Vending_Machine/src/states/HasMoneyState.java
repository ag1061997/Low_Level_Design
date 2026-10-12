package states;

import core.VendingMachine;
import enums.Coin;

public class HasMoneyState implements VendingMachineState {
    @Override
    public void insertCoin(VendingMachine vendingMachine, Coin coin) {
        System.out.println("Already received full amount.");
    }

    @Override
    public void selectItem(VendingMachine vendingMachine, String code) {
        System.out.println("Item already selected.");
    }

    @Override
    public void dispense(VendingMachine vendingMachine) {
        vendingMachine.setCurrentState(new DispensingState());
        vendingMachine.getCurrentState().dispense(vendingMachine);
    }

    @Override
    public void refund(VendingMachine vendingMachine) {
        System.out.println("Refunding: " + vendingMachine.getBalance());
        vendingMachine.setBalance(0);
        vendingMachine.setSelectedItemCode(null);
        vendingMachine.setCurrentState(new IdleState());
    }
}
