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
    public final double motorSpinSpeed = .7;
    public final int shooterRetractedValue = 465;
    private boolean isFiring = false;
    private boolean isCocking = false;
    private long fireTimeStamp = 0;

    public ChooChoo() {
        sensor = new DigitalInput(Wiring.chooChooArmedSensor);
        chooChooMotor = new Talon(Wiring.chooChooMotorPort);
    }

    public void fire() {
        isFiring = true;
        fireTimeStamp = System.currentTimeMillis();
        Debug.println("[INFO] Fire Button Pressed", Debug.STANDARD);
    }

    public void stop() {
        isFiring = false;
        chooChooMotor.set(0);
    }

    public void cock() {
        isCocking = true;
        if (!isCocked()) {
            chooChooMotor.set(motorSpinSpeed);
        } else {
            isCocking = false;
        }
    }

    public void step() {

        //  Debug.println("Infrared distance sensor" + positionSensor.getVoltage() + "    value: " + positionSensor.getValue());
        if (isFiring || isCocking) {
            chooChooMotor.set(motorSpinSpeed);
        } else {
            chooChooMotor.set(0);
        }

        if (isFiring && System.currentTimeMillis() - fireTimeStamp > 300) {
            isFiring = false;
            cock();
        }
        
        if (isCocking && isCocked()) {
            isCocking = false;
        }

    }
    
    private boolean isCocked() {
        return sensor.get();
    }
}
