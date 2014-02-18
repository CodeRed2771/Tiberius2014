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

    public Drive(DashBoard dashBoard) {
        left = new Talon(Wiring.leftDriveTalonPort);
        leftEncoder = new HallEncoder(Wiring.leftDriveEncoderAPort,
                Wiring.leftDriveEncoderBPort, dashBoard, "left");
        leftController = new PIDControllerAIAO(
                30, 0, 0, leftEncoder, left, 0.01, dashBoard, "left");
        leftController.enable();
        right = new Talon(Wiring.rightDriveTalonPort);
        rightEncoder = new HallEncoder(Wiring.rightDriveEncoderAPort,
                Wiring.rightDriveEncoderBPort, dashBoard, "right");
        rightController = new PIDControllerAIAO(
                30, 0, 0, rightEncoder, right, 0.01, dashBoard, "right");
        rightController.enable();
    }

    public void move(double left, double right) {
        if (speed) {
            leftController.setSetpoint((-left*Math.abs(left)) * 0.05);
            rightController.setSetpoint((right*Math.abs(right)) * 0.05);
        } else {
            this.left.set(-left*Math.abs(left));
            this.right.set(right*Math.abs(right));
        }
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
