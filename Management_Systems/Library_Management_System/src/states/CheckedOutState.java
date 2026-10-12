package states;

import entities.BookCopy;
import entities.Member;
import services.TransactionService;

public class CheckedOutState implements ItemState {
    @Override public void checkout(BookCopy c, Member m) { System.out.println(c.getId() + " is already checked out."); }

    @Override
    public void returnItem(BookCopy copy) {
        TransactionService.getInstance().endLoan(copy);
        System.out.println(copy.getId() + " returned.");
        if (copy.getItem().hasObservers()) {
            copy.setState(new OnHoldState());
            copy.getItem().notifyObservers();
        } else {
            copy.setState(new AvailableState());
        }
    }

    @Override
    public void placeHold(BookCopy copy, Member member) {
        copy.getItem().addObserver(member);
        System.out.println(member.getName() + " placed a hold on '" + copy.getItem().getTitle() + "'");
    }
}
