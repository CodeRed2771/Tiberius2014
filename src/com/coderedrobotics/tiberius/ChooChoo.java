package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.Debug;
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
        chooChooMotor = new Talon(Wiring.chooChooMotorPort);
    }

    public void step(boolean fire) {
        if (fire) {
            isFiring = true;
            Debug.println("[INFO] Fire Button Pressed", Debug.STANDARD);

        }

        if (isFiring || !sensor.get()) {
            chooChooMotor.set(.3);
        } else {
            chooChooMotor.set(0);
        }

        if (!sensor.get()) {
            isFiring = false;
        }
    }
}
