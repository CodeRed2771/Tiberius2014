/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private final Button fireBallButton = LogitechF310.A;
    private final Button spinPickupWheelsButton = LogitechF310.X;
    private final Button spinPickupWheelsBackwardsButton = LogitechF310.Y;
    
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
        return getHID(1).axis(LogitechF310.STICK_LEFT_Y);
    }
    
    public double getRightDriveAxis() {
        return getHID(1).axis(LogitechF310.STICK_RIGHT_Y);
    }
    
    public boolean getFireBallButton(){
        return getHID(2).button(LogitechF310.A);
    }
    
    public boolean getSpinPickupWheelsButton(){
        return getHID(2).button(LogitechF310.X);
    }
    
    public boolean getSpinPickupWheelsBackwardsButton(){
        return getHID(2).button(LogitechF310.Y);
    }
}
