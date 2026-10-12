package core;

import entities.Inventory;
import entities.Item;
import enums.Coin;
import states.IdleState;
import states.VendingMachineState;

public class VendingMachine {
    private static volatile VendingMachine INSTANCE;
    private final Inventory inventory;
    private VendingMachineState currentState;
    private int balance;
    private String selectedItemCode;

    private VendingMachine() {
        this.inventory = new Inventory();
        this.currentState = new IdleState();
        this.balance = 0;
        this.selectedItemCode = null;
    }

    public static VendingMachine getInstance() {
        if (INSTANCE == null) {
            synchronized (VendingMachine.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VendingMachine();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized void insertCoin(Coin coin) {
        currentState.insertCoin(this, coin);
    }

    public synchronized void selectItem(String code) {
        currentState.selectItem(this, code);
    }

    public synchronized void dispense() {
        currentState.dispense(this);
    }

    public synchronized void refund() {
        currentState.refund(this);
    }

    public synchronized Item addItem(String code, String name, int price, int quantity) {
        Item item = new Item(code, name, price);
        inventory.addItem(code, item, quantity);
        return item;
    }

    public void dispenseItem() {
        Item item = inventory.getItem(selectedItemCode);
        if (balance >= item.getPrice()) {
            inventory.reduceStock(selectedItemCode);
            balance -= item.getPrice();
            System.out.println("Dispensed: " + item.getName());
            if (balance > 0) {
                System.out.println("Returning change: " + balance);
            }
        }
    }

    public void addBalance(int value) {
        balance += value;
    }

    public Item getSelectedItem() {
        return inventory.getItem(selectedItemCode);
    }

    public String getSelectedItemCode() {
        return selectedItemCode;
    }

    public int getBalance() {
        return balance;
    }

    public VendingMachineState getCurrentState() {
        return currentState;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setCurrentState(VendingMachineState vendingMachineState) {
        this.currentState = vendingMachineState;
    }

    public void setBalance(int value) {
        this.balance = balance;
    }

    public void setSelectedItemCode(String code) {
        this.selectedItemCode = code;
    }
}
