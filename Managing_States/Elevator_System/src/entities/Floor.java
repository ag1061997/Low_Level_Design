package entities;

import observers.Display;

public class Floor {
    private final int floorNumber;
    private boolean upButtonPressed;
    private boolean downButtonPressed;
    private final Display display;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.upButtonPressed = false;
        this.downButtonPressed = false;
        this.display = new Display(0);
    }

    public void pressUpButton() {
        this.upButtonPressed = true;
    }

    public void pressDownButton() {
        this.downButtonPressed = true;
    }

    public void resetButtons() {
        this.downButtonPressed = false;
        this.upButtonPressed = false;
    }

    public int getFloorNumber() { return floorNumber; }
    public boolean isUpButtonPressed() { return upButtonPressed; }
    public boolean isDownButtonPressed() { return downButtonPressed; }
    public Display getDisplay() { return display; }
}
