package com.coderedrobotics.tiberius.statics;

import com.coderedrobotics.tiberius.libs.HID.HID;
import com.coderedrobotics.tiberius.libs.HID.HID.Axis;
import com.coderedrobotics.tiberius.libs.HID.HID.Button;
import com.coderedrobotics.tiberius.libs.HID.LogitechF310;

/**
 *
 * @author Michael
 */
public class KeyMap {

    public static HID gamepad1 = new HID(1);
    public static HID gamepad2 = new HID(2);

    private boolean singleControllerMode;

    private final Axis leftDriveAxis = LogitechF310.STICK_LEFT_Y;
    private final Axis rightDriveAxis = LogitechF310.STICK_RIGHT_Y;
    private final Button fireBallButton = LogitechF310.TRIGGER_RIGHT;
    private final Button spinPickupWheelsButton = LogitechF310.Y;
    private final Button spinPickupWheelsStopButton = LogitechF310.X;
    private final Button spinPickupWheelsBackwardsButton = LogitechF310.B;
    private final Button pickupRetract = LogitechF310.DPAD_LEFT;
    private final Button pickupExtend = LogitechF310.DPAD_RIGHT;
    private final Button pickupAutoRetract = LogitechF310.BACK;
    private final Button togglePickup = LogitechF310.A;
    private final Button toggleSpeedControllers = LogitechF310.START;

    private final HID.ButtonState pickupButtonToggleState = HID.newButtonState();
    private final HID.ButtonState pickupButtonPressedState = HID.newButtonState();
    private final HID.ButtonState controllersToggleState = HID.newButtonState();

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

    public void setSingleControllerMode(boolean state) {
        singleControllerMode = state;
    }

    public boolean getSingleControllerMode() {
        return singleControllerMode;
    }

    public void toggleSingleControllerMode() {
        singleControllerMode = !singleControllerMode;
    }

    public boolean getSwitchControllerModeButtons() {
        return (getHID(1).button(LogitechF310.BACK) && getHID(2).button(LogitechF310.BACK));
    }

    public double getLeftDriveAxis() {
        return getHID(1).axis(leftDriveAxis);
    }

    public double getRightDriveAxis() {
        return getHID(1).axis(rightDriveAxis);
    }

    public boolean getFireBallButton() {
        return getHID(2).button(fireBallButton);
    }

    public boolean getSpinPickupWheelsButton() {
        return getHID(2).button(spinPickupWheelsButton);
    }

    public boolean getSpinPickupWheelsStopButton() {
        return getHID(2).button(spinPickupWheelsStopButton);
    }

    public boolean getSpinPickupWheelsBackwardsButton() {
        return getHID(2).button(spinPickupWheelsBackwardsButton);
    }

    public boolean getPickupToggleButton() {
        return (getHID(2).buttonPressed(togglePickup, pickupButtonPressedState));
    }

    public boolean getPickupRetractAutoButton() {
        return getHID(2).button(pickupAutoRetract);
    }

    public boolean getPickupRetractButton() {
        return getHID(2).button(pickupRetract);
    }

    public boolean getPickupExtendButton() {
        return getHID(2).button(pickupExtend);
    }

    public boolean toggleSpeedControllers() {
        return getHID(1).buttonPressed(toggleSpeedControllers, controllersToggleState);
    }
}
