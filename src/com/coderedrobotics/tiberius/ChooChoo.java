package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.SmartDigitalInput;
import com.coderedrobotics.tiberius.statics.DashboardDriverPlugin;
import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Michael
 */
public class ChooChoo {

    SmartDigitalInput sensor;
    Talon chooChooMotor;
    public final double motorSpinSpeed = 1;
    public final int shooterRetractedValue = 465;
    private boolean isFiring = false;
    private boolean isCocking = false;
    private long fireTimeStamp = 0;

    private boolean lastSentIsCocked;
    private boolean lastSentIsCocking;

    public ChooChoo() {
        sensor = new SmartDigitalInput(Wiring.chooChooArmedSensor, Tiberius.enableVirtualInputs);
        chooChooMotor = new Talon(Wiring.chooChooMotorPort);
    }

    public void fire() {
        isFiring = true;
        fireTimeStamp = System.currentTimeMillis();
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

        if (isFiring && System.currentTimeMillis() - fireTimeStamp > 600) {
            isFiring = false;
            cock();
        }

        if (isCocking && isCocked()) {
            isCocking = false;
        }

        if (lastSentIsCocked != isCocked()) {
            DashboardDriverPlugin.updateCockingStatus(isCocking ? 1 : 0);
        }
        if (lastSentIsCocking != isCocking) {
            DashboardDriverPlugin.updateCockedStatus(isCocked() ? 1 : 0);
        }
    }

    public boolean isCocked() {
        return sensor.get();
    }
    
    public boolean isCocking(){
        return isCocking;
    }
}
