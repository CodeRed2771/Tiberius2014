package com.coderedrobotics.tiberius.statics;

import com.coderedrobotics.tiberius.libs.Debug;
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
    private final Button pickupRetract = LogitechF310.DPAD_UP;
    private final Button pickupExtend = LogitechF310.DPAD_DOWN;
    private final Button pickupAutoRetract = LogitechF310.BACK;
    private final Button togglePickup = LogitechF310.A;
    private HID.ButtonState pickupButtonToggleState = HID.newButtonState();
    private HID.ButtonState pickupButtonPressedState = HID.newButtonState();

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
        return (getHID(2).buttonPressed(fireBallButton, pickupButtonPressedState));
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
}
