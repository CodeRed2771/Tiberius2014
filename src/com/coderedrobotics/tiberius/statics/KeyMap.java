package com.coderedrobotics.tiberius.statics;

import com.coderedrobotics.lib.HID.HID;
import com.coderedrobotics.lib.HID.HID.Axis;
import com.coderedrobotics.lib.HID.HID.Button;
import com.coderedrobotics.lib.HID.LogitechF310;

/**
 *
 * @author Michael
 */
public class KeyMap {

    // HIDS
    public static HID gamepad1 = new HID(1);
    public static HID gamepad2 = new HID(2);

    // MANAGEMENT BOOLEANS
    private boolean reverseDrive = false;
    private boolean singleControllerMode = false;

    // CONTROLLER 1
    private final Axis leftDriveAxis = LogitechF310.STICK_LEFT_Y;
    private final Axis rightDriveAxis = LogitechF310.STICK_RIGHT_Y;
    private final Button reverseDriveButton = LogitechF310.TRIGGER_LEFT;

    // CONTROLLER 2
    private final Button manualPickupExtend = LogitechF310.DPAD_RIGHT;
    private final Button manualPickupRetract = LogitechF310.DPAD_LEFT;
    private final Button manualPetalExtend = LogitechF310.STICK_LEFT_RIGHT;
    private final Button manualPetalRetract = LogitechF310.STICK_LEFT_RIGHT;
    private final Button pickupToPositionTwo = LogitechF310.TRIGGER_LEFT;
    private final Button petalsToGrabPosition = LogitechF310.BACK;
    private final Button petalsBoostAndExtend = LogitechF310.START;
    private final Button wheelsMovingOut = LogitechF310.Y;
    private final Button wheelsMovingIn = LogitechF310.B;
    private final Button wheelsStop = LogitechF310.X;
    private final Button pickupMode = LogitechF310.A;

    // CONTROLLERS SHARED
    private final Button fire = LogitechF310.TRIGGER_RIGHT;

    // BUTTON STATES
    private final HID.ButtonState reverseDriveButtonState = HID.newButtonState();

    public KeyMap() {
    }

    private HID getHID(int port) {
        if (!singleControllerMode) {
            switch (port) {
                case 1:
                    return gamepad1;
                case 2:
                    return gamepad2;
                default:
                    return null;
            }
        } else {
            return gamepad1;
        }
    }

    public double getLeftDriveAxis() { // CHECK
        if (reverseDrive) {
            return -getHID(1).axis(rightDriveAxis);
        } else {
            return getHID(1).axis(leftDriveAxis);
        }
    }

    public double getRightDriveAxis() { // CHECK
        if (reverseDrive) {
            return -getHID(1).axis(leftDriveAxis);
        } else {
            return getHID(1).axis(rightDriveAxis);
        }
    }

    public boolean getReverseDriveButton() { // CHECK
        return getHID(1).buttonPressed(reverseDriveButton, reverseDriveButtonState);
    }

    public void toggleReverseDrive() { // CHECK
        reverseDrive = !reverseDrive;
    }

    public void setSingleControllerMode(boolean state) { // DONE
        singleControllerMode = state;
    }

    public boolean getSingleControllerMode() { // DONE
        return singleControllerMode;
    }

    public void toggleSingleControllerMode() { // DONE
        singleControllerMode = !singleControllerMode;
    }

    public boolean getManualPickupExtendButton() { // DONT TOUCH
        return getHID(2).button(manualPickupExtend);
    }

    public boolean getManualPickupRetractButton() { // DONT TOUCH
        return getHID(2).button(manualPickupRetract);
    }

    public boolean getManualPetalsExtendButton() { // CHECK
        return getHID(2).button(manualPetalExtend);
    }

    public boolean getManualPetalsRetractButton() { // CHECK
        return getHID(2).button(manualPetalRetract);
    }

    public boolean getPickupToPostionTwoButton() { // DONT TOUCH
        return getHID(2).button(pickupToPositionTwo);
    }

    public boolean getPetalsToGrabPostion() { // CHECK
        return getHID(2).button(petalsToGrabPosition);
    }

    public boolean getPetalsBoostAndExtendButton() { // CHECK
        return getHID(2).button(petalsBoostAndExtend);
    }

    public boolean getWheelsMovingOutButton() {
        return getHID(2).button(wheelsMovingOut);
    }

    public boolean getWheelsMovingInButton() {
        return getHID(2).button(wheelsMovingIn);
    }

    public boolean getWheelsStopButton() {
        return getHID(2).button(wheelsStop);
    }

    public boolean getPickupModeButton() {
        return getHID(2).button(pickupMode);
    }

    public boolean getFireButton() {
        return (getHID(1).button(fire) || getHID(2).button(fire));
    }
}
