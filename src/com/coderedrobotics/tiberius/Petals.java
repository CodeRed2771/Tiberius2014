package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author dvanvoorst
 */
public class Petals {

    Talon petalMotorLeft;
    Talon petalMotorRight;
    private final double petalsExtend = 0.4;
    private final double petalsRetract = -0.4;

    public Petals() {
        petalMotorLeft = new Talon(Wiring.petalsMotorLeftPort);
        petalMotorRight = new Talon(Wiring.petalsMotorRightPort);
    }

    public void extendLeftPetals() {
        petalMotorLeft.set(petalsExtend);
    }
    
    public void retractLeftPetals() {
       petalMotorLeft.set(petalsRetract);
    }
    
    public void stopLeftPetals() {
        petalMotorLeft.set(0);
    }
    
    public void extendRightPetals() {
        petalMotorRight.set(petalsExtend);
    }
    
    public void retractRightPetals() {
       petalMotorRight.set(petalsRetract);
    }
    
    public void stopRightPetals() {
        petalMotorRight.set(0);
    }
}
