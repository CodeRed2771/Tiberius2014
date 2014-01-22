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
    private final Button spinPickupWheelsBackwardsButton = LogitechF310.B;
    private final Button elevationUp = LogitechF310.DPAD_UP;
    private final Button elevationDown = LogitechF310.DPAD_DOWN;
    private final Button togglePickup = LogitechF310.A;
    
    private HID getHID(int port){
        if (!singleControllerMode){
            switch (port){ 
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
    
    public void setSingleControllerMode(boolean state){
        singleControllerMode = state;
    }
    
    public boolean getSingleControllerMode(){
        return singleControllerMode;
    }
    
    public double getLeftDriveAxis() {
        return getHID(1).axis(leftDriveAxis);
    }
    
    public double getRightDriveAxis() {
        return getHID(1).axis(rightDriveAxis);
    }
    
    public boolean getFireBallButton(){
        return getHID(2).button(fireBallButton);
    }
    
    public boolean getSpinPickupWheelsButton(){
        return getHID(2).button(spinPickupWheelsButton);
    }
    
    public boolean getSpinPickupWheelsBackwardsButton(){
        return getHID(2).button(spinPickupWheelsBackwardsButton);
    }
    
    public boolean getTogglePickupButton(){
        return getHID(2).button(togglePickup);
    }
    
    public boolean getElevationUpButton(){
        return getHID(2).button(elevationUp);
    }
    
    public boolean getElevationDownButton(){
        return getHID(2).button(elevationDown);
    }
}
