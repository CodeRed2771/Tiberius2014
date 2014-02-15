package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.HallEncoder;
import com.coderedrobotics.tiberius.libs.dash.DashBoard;
import com.coderedrobotics.tiberius.libs.dash.PIDControllerAIAO;
import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.Talon;
import com.sun.squawk.util.MathUtils;

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
        leftEncoder = new HallEncoder(Wiring.leftDriveEncoderAPort, Wiring.leftDriveEncoderBPort, dashBoard, "left");
        leftController = new PIDControllerAIAO(
                50, 0, 0, leftEncoder, left, dashBoard, "left");
        leftController.enable();
        right = new Talon(Wiring.rightDriveTalonPort);
        rightEncoder = new HallEncoder(Wiring.rightDriveEncoderAPort, Wiring.rightDriveEncoderBPort, dashBoard, "right");
        rightController = new PIDControllerAIAO(
                50, 0, 0, rightEncoder, right, dashBoard, "right");
        rightController.enable();
    }

    public void move(double left, double right) {
        if (speed) {
            leftController.setSetpoint(-left * 0.025);
            rightController.setSetpoint(right * 0.025);
        } else {
            this.left.set(-left);
            this.right.set(right);
        }
    }

    public void disableSpeedControllers() {
        if (speed) {
            rightController.disable();
            leftController.disable();
            speed = false;
        }
    }

    public void enableSpeedControllers() {
        if (!speed) {
            speed = true;
            rightController.reset();
            leftController.reset();
            rightController.enable();
            leftController.enable();
        }
    }

    public void toggleSpeedContrllers() {
        if (speed) {
            disableSpeedControllers();
        } else {
            enableSpeedControllers();
        }
    }
}
