/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Michael
 */
public class Pickup {

    Talon elevationMotor;
    Talon spinWheelsMotor;

    public Pickup() {
        elevationMotor = new Talon(Wiring.pickupElevation);
        spinWheelsMotor = new Talon(Wiring.pickupWheels);
    }

    public void togglePickupPosition() {

    }

    public void spinWheels(boolean backwards) {
        int value = 1;
        if (backwards) {
            value = -value;
        }

        spinWheelsMotor.set(value);
    }

    public void stopWheels() {
        spinWheelsMotor.set(0);
    }
}
