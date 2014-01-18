/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coderedrobotics.statics;

import com.coderedrobotics.libs.Gamepad;
import com.coderedrobotics.libs.GenericJoystick;

/**
 *
 * @author Michael
 */
public class KeyMap {
    
    public static GenericJoystick gamepad1 = new GenericJoystick(1);
    public static GenericJoystick gamepad2 = new GenericJoystick(2);
    
    public double getLeftDriveAxis() {
        return gamepad1.axis(Gamepad.STICK_LEFT_Y);
    }
    
    public double getRightDriveAxis() {
        return gamepad1.axis(Gamepad.STICK_RIGHT_Y);
    }
}
