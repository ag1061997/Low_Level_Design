package states;

import core.VendingMachine;
import enums.Coin;

public class DispensingState implements VendingMachineState {
    @Override
    public void insertCoin(VendingMachine vendingMachine, Coin coin) {
        System.out.println("Currently dispensing. Please wait.");
    }

    @Override
    public void selectItem(VendingMachine vendingMachine, String code) {
        System.out.println("Currently dispensing. Please wait.");
    }

    @Override
    public void dispense(VendingMachine vendingMachine) {
        vendingMachine.dispenseItem();
        vendingMachine.setBalance(0);
        vendingMachine.setSelectedItemCode(null);
        vendingMachine.setCurrentState(new IdleState());
    }

    @Override
    public void refund(VendingMachine vendingMachine) {
        System.out.println("Dispensing in progress. Refund not allowed.");
    }
}
