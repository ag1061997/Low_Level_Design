package states;

import core.ATM;
import enums.ATMState;
import exceptions.ATMException;
import models.Card;

public class AuthenticatedState implements ATMStateHandler {
    @Override
    public void insertCard(ATM atm, Card card) {
        throw new ATMException("A card is already inserted");
    }

    @Override
    public void authenticate(ATM atm, String pin) {
        throw new ATMException("Already authenticated");
    }

    @Override
    public void withdraw(ATM atm, double amount) {
        atm.performWithdrawal(amount);
    }

    @Override
    public void deposit(ATM atm, double amount) {
        atm.performDeposit(amount);
    }

    @Override
    public double checkBalance(ATM atm) {
        return atm.performBalanceInquiry();
    }

    @Override
    public void ejectCard(ATM atm) {
        atm.setCurrentCard(null);
        atm.setCurrentAccount(null);
        atm.setState(ATMState.IDLE);
        System.out.println("Card ejected successfully");
    }
}
