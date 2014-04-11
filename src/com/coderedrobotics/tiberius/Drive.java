package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.HallEncoder;
import com.coderedrobotics.tiberius.libs.dash.DashBoard;
import com.coderedrobotics.tiberius.libs.dash.PIDControllerAIAO;
import com.coderedrobotics.tiberius.statics.DashboardDriverPlugin;
import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Michael
 */
public class Drive {

    Talon left;
    Talon right;
    HallEncoder leftEncoder;
    HallEncoder rightEncoder;
    PIDControllerAIAO leftController;
    PIDControllerAIAO rightController;
    boolean speed = true;

    double calibrateStartTime;
    boolean calibrating;
    private final double encoderRevolutionsPerInch = 3;

    public Drive(DashBoard dashBoard) {
        left = new Talon(Wiring.leftDriveTalonPort);
        leftEncoder = new HallEncoder(Wiring.leftDriveEncoderAPort,
                Wiring.leftDriveEncoderBPort, dashBoard, "left");
        leftController = new PIDControllerAIAO(
                60, 0, 0, leftEncoder, left, 0.005, dashBoard, "left");
        leftController.enable();
        right = new Talon(Wiring.rightDriveTalonPort);
        rightEncoder = new HallEncoder(Wiring.rightDriveEncoderAPort,
                Wiring.rightDriveEncoderBPort, dashBoard, "right");
        rightController = new PIDControllerAIAO(
                60, 0, 0, rightEncoder, right, 0.005, dashBoard, "right");
        rightController.enable();
    }

    public void move(double left, double right) {
//        System.out.println("Swag: " + leftEncoder.pidGet() + "\t" + rightEncoder.pidGet());
        if (speed) {
            leftController.setSetpoint((-left * Math.abs(left)) * 0.05);
            if (leftController.getSetpoint() > 0) {
                leftController.setOutputRange(0, 1);
            } else if (leftController.getSetpoint() < 0) {
                leftController.setOutputRange(-1, 0);
            } else {
                leftController.setOutputRange(0, 0);
            }
            rightController.setSetpoint((right * Math.abs(right)) * 0.05);
            if (rightController.getSetpoint() > 0) {
                rightController.setOutputRange(0, 1);
            } else if (rightController.getSetpoint() < 0) {
                rightController.setOutputRange(-1, 0);
            } else {
                rightController.setOutputRange(0, 0);
            }
        } else {
            this.left.set(-left * Math.abs(left) * 0.952018367);
            this.right.set(right * Math.abs(right));
        }
    }

    public void calibrate() {
        if (!calibrating) {
            calibrating = true;
            calibrateStartTime = System.currentTimeMillis();
        }

        if (!isCalibrated()) {
            move(.5, .5);
        }
    }

    public boolean isCalibrated() {
        // calibration is complete (hopefully) after 400ms
        return System.currentTimeMillis() - calibrateStartTime > 400;
    }

    public double getDistanceTraveledInches() {
        return leftEncoder.getRaw() / encoderRevolutionsPerInch;
    }

    public void disableSpeedControllers() {
        if (speed) {
            rightController.disable();
            leftController.disable();
            speed = false;
        }
        DashboardDriverPlugin.updateHallEncodersStatus(0);
    }

    public void enableSpeedControllers() {
        if (!speed) {
            speed = true;
            rightController.reset();
            leftController.reset();
            rightController.enable();
            leftController.enable();
        }
        DashboardDriverPlugin.updateHallEncodersStatus(1);
    }

    public void toggleSpeedContrllers() {
        if (speed) {
            disableSpeedControllers();
        } else {
            enableSpeedControllers();
        }
    }
}
