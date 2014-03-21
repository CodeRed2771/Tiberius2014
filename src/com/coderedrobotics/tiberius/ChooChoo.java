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
    private boolean switchState = false;
    private int stage = 2;

    public ChooChoo() {
        sensor = new SmartDigitalInput(Wiring.chooChooArmedSensor, Tiberius.enableVirtualInputs);
        chooChooMotor = new Talon(Wiring.chooChooMotorPort);
    }

    public void fire() {
        stage = 1;
    }

    public void cock() {
        stage = 2;
    }

    public void step() {
        //  Debug.println("Infrared distance sensor" + positionSensor.getVoltage() + "    value: " + positionSensor.getValue());
        switch (stage) {
            case 0:
                chooChooMotor.set(0);
                break;
            case 1:
                chooChooMotor.set(motorSpinSpeed);
                if (isReleased()) {
                    stage = 2;
                }
                break;
            case 2:
                chooChooMotor.set(motorSpinSpeed);
                if (sensor.get()) {
                    stage = 0;
                }
                break;
        }
    }

    public boolean isCocked() {
        return stage == 0;
    }

    public boolean isCocking() {
        return stage == 2;
    }

    private boolean isReleased() {
        boolean state = sensor.get();
        boolean result = !state && switchState;
        switchState = state;
        return result;
    }
}
