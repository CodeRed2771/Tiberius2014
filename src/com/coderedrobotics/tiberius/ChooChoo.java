/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.statics.KeyMap;
import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Michael
 */
public class ChooChoo {

    DigitalInput sensor;
    Talon chooChooMotor;

    private boolean isFiring = false;

    public ChooChoo() {
        sensor = new DigitalInput(Wiring.chooChooArmedSensor);
        chooChooMotor = new Talon(Wiring.chooChooMotor);
    }

    public void step(boolean fire) {
        if (fire) {
            isFiring = true;
        }
        
        if (isFiring || !sensor.get()) {
            chooChooMotor.set(1);
        } else {
            chooChooMotor.set(0);
        }
        
        if (!sensor.get()) {
            isFiring = false;
        }
    }
}
