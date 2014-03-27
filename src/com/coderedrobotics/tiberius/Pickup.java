package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.SmartAnalogPotentiometer;
import com.coderedrobotics.tiberius.libs.dash.DashBoard;
import com.coderedrobotics.tiberius.libs.dash.PIDControllerAIAO;
import com.coderedrobotics.tiberius.statics.Calibration;
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
    public final SmartAnalogPotentiometer positionSensor;
    private final PIDControllerAIAO controller;
    private int mode;

    private static final int OUT = 2;
    private static final int IN = 1;
    private static final int MANUAL = 0;

    public final double WheelsInSpeed = -.8;
    public final double WheelsOutSpeed = .8;

    public Pickup(DashBoard dashBoard) {
        armMotor = new Talon(Wiring.pickupArmMotorPort);
        wheelsMotor = new Talon(Wiring.pickupWheelsMotorPort);
        positionSensor = new SmartAnalogPotentiometer(Wiring.armPositionSensorPort, Tiberius.enableVirtualInputs);
        controller = new PIDControllerAIAO(-8, 0, 4.5, positionSensor, this, dashBoard, "pickup");
        controller.enable();
        mode = MANUAL;
    }

    private void setMode(int mode) {
        switch (mode) {
            case IN:
                controller.setSetpoint(Calibration.pickupClearSetpoint);
                break;
            case OUT:
                controller.setSetpoint(Calibration.pickupExtendedSetpoint);
                break;
        }
        this.mode = mode;
    }

    private void setPickup(double value) {
        if (positionSensor.get() > Calibration.pickupExtendedLimit && value < 0) {
            armMotor.set(0);
            return;
        }
        if (positionSensor.get() < Calibration.pickupRetractedLimit && value > 0) {
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
        return positionSensor.get() > Calibration.pickupClearLimit;
    }

    public boolean isDown() {
        return positionSensor.get() > Calibration.pickupExtendedLimit;
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
