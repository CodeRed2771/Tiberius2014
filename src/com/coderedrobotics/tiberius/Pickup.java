package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.SmartAnalogPotentiometer;
import com.coderedrobotics.tiberius.libs.dash.DashBoard;
import com.coderedrobotics.tiberius.libs.dash.PIDControllerAIAO;
import com.coderedrobotics.tiberius.statics.DashboardDriverPlugin;
import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 *
 * @author Michael
 */
public class Pickup implements PIDOutput {

    private final Talon armMotor, wheelsMotor;
    private final SmartAnalogPotentiometer positionSensor;
    private final PIDControllerAIAO controller;
    private int mode;

    private static final int OUT = 2;
    private static final int IN = 1;
    private static final int MANUAL = 0;

    private final double retractedPosition = 0.286703492;
    private final double petalsClearPosition = 0.6312255990000001;
    private final double extendedPosition = 1.244;

    private final double petalsClearSetpoint = 0.7;
    private final double extendedSetpoint = extendedPosition;

    public final double WheelsInSpeed = -.8;
    public final double WheelsOutSpeed = .8;

    public Pickup(DashBoard dashBoard) {
        armMotor = new Talon(Wiring.pickupArmMotorPort);
        wheelsMotor = new Talon(Wiring.pickupWheelsMotorPort);
        positionSensor = new SmartAnalogPotentiometer(Wiring.armPositionSensorPort, Tiberius.enableVirtualInputs);
        controller = new PIDControllerAIAO(-3, 0, 0, positionSensor, this, dashBoard, "pickup");
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
        armMotor.set(-value);
        DashboardDriverPlugin.updatePickupMovingStatus(value);
    }

    public void pidWrite(double d) {
        if (mode != MANUAL) {
            setPickup(d);
        }
    }

    public boolean isClear() {
        return positionSensor.get() > petalsClearPosition;
    }

    public boolean isDown() {
        return positionSensor.get() > extendedPosition;
    }

    public void pickupIn() {
        setMode(IN);
    }

    public void pickupOut() {
        setMode(OUT);
    }

    public void movePickup(double speed) {
        if (mode == MANUAL || speed != 0) {
            setMode(MANUAL);
            setPickup(speed);
        }
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
        DashboardDriverPlugin.updatePickupWheelsStatus(speed);
    }
}
