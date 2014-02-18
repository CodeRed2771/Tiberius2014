package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author dvanvoorst
 */
public class Petals {

    Talon petalMotorLeft;
    Talon petalMotorRight;
    private final double petalsExtend = -0.5;
    private final double petalsRetract = 0.3;
    private DigitalInput leftRetract;
    private DigitalInput leftExtend;
    private DigitalInput rightRetract;
    private DigitalInput rightExtend;

    public Petals() {
        petalMotorLeft = new Talon(Wiring.petalsMotorLeftPort);
        petalMotorRight = new Talon(Wiring.petalsMotorRightPort);
        leftExtend = new DigitalInput(8);
        rightExtend = new DigitalInput(7);
    }

    public void extendLeftPetals() {
        if (!leftExtend.get()) {
            petalMotorLeft.set(petalsExtend);
        }
        
    }

    public void retractLeftPetals() {
        petalMotorLeft.set(petalsRetract);
    }

    public void stopLeftPetals() {
        petalMotorLeft.set(0);
    }

    public void extendRightPetals() {
        if (!rightExtend.get()) {
            petalMotorRight.set(petalsExtend);
        }
    }

    public void retractRightPetals() {
        petalMotorRight.set(petalsRetract);
    }

    public void stopRightPetals() {
        petalMotorRight.set(0);
    }
}
