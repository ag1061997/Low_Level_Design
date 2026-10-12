package states;

import core.VendingMachine;
import enums.Coin;

public interface VendingMachineState {
    public void insertCoin(VendingMachine vendingMachine, Coin coin);
    public void selectItem(VendingMachine vendingMachine, String code);
    public void dispense(VendingMachine vendingMachine);
    public void refund(VendingMachine vendingMachine);
}
