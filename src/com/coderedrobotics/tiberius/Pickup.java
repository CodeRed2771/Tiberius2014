package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.dash.DashBoard;
import com.coderedrobotics.tiberius.libs.dash.PIDControllerAIAO;
import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 *
 * @author Michael
 */
public class Pickup implements PIDOutput {

    private final Talon armMotor, wheelsMotor;
    private final AnalogPotentiometer positionSensor;
    private final PIDControllerAIAO controller;
    private int mode;

    private static final int OUT = 3;
    private static final int IN = 1;
    private static final int MANUAL = 0;

    private final double retractedPosition = 0.19;
    private final double petalsClearPosition = 0.61;
    private final double extendedPosition = 1.19;

    private final double petalsClearSetpoint = 0.65;
    private final double extendedSetpoint = 1.14;

    public final double WheelsInSpeed = -0.8;
    public final double WheelsOutSpeed = 0.8;

    public Pickup(DashBoard dashBoard) {
        armMotor = new Talon(Wiring.pickupArmMotorPort);
        wheelsMotor = new Talon(Wiring.pickupWheelsMotorPort);
        positionSensor = new AnalogPotentiometer(Wiring.armPositionSensorPort);
        controller = new PIDControllerAIAO(-0.12, 0, 0, positionSensor, this, dashBoard, "pickup");
        controller.enable();
        mode = MANUAL;
    }

    private void setMode(int mode) {
        switch (mode) {
            case IN:
                controller.setSetpoint(petalsClearSetpoint);
                break;
            case OUT:
                controller.setSetpoint(extendedSetpoint);
                break;
        }
        this.mode = mode;
    }

    private void setPickup(double value) {
        if (positionSensor.get() > extendedPosition && value < 0) {
            armMotor.set(0);
            return;
        }
        if (positionSensor.get() < retractedPosition && value > 0) {
            armMotor.set(0);
            return;
        }
        armMotor.set(value);
    }

    public void pidWrite(double d) {
        if (mode != MANUAL) {
            setPickup(d);
        }
    }

    public boolean isClear() {
        return positionSensor.get() > petalsClearPosition;
    }

    public void pickupIn() {
        setMode(IN);
    }

    public void pickupOut() {
        setMode(OUT);
    }

    public void movePickup(double speed) {
        setMode(MANUAL);
        setPickup(speed);
    }

    public void wheelsIn() {
        setWheels(WheelsInSpeed);
    }

    public void wheelsOut() {
        setWheels(WheelsOutSpeed);
    }
    
    public void stopWheels() {
        setWheels(0);
    }

    public void setWheels(double speed) {
        wheelsMotor.set(speed);
    }
}
