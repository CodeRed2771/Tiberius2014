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
    private DigitalInput leftInput;
    private DigitalInput rightInput;

    public Petals() {
        petalMotorLeft = new Talon(Wiring.petalsMotorLeftPort);
        petalMotorRight = new Talon(Wiring.petalsMotorRightPort);
        leftInput = new DigitalInput(8);
        rightInput = new DigitalInput(7);
    }

    public void extendLeftPetals() {
        if (!leftInput.get()) {
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
        if (!rightInput.get()) {
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
