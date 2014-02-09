/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author dvanvoorst
 */
public class Petals {

    Talon petalMotor;
    private final double petalsExtend = 0.6;
    private final double petalsRetract = -0.6;

    public Petals() {
        petalMotor = new Talon(Wiring.petalsMotorPort);
    }

    public void extendPetals() {
        petalMotor.set(petalsExtend);
    }
    
    public void retractPetals() {
       petalMotor.set(petalsRetract);
    }
    
    public void stop() {
        petalMotor.set(0);
    }
}
