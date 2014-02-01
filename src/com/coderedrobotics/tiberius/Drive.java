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

    public Drive(DashBoard dashBoard) {
        left = new Talon(Wiring.leftDriveTalonPort);
        right = new Talon(Wiring.rightDriveTalonPort);
        leftEncoder = new HallEncoder(5, 6);
        leftController = new PIDControllerAIAO(
                80, 0, 0, leftEncoder, left, dashBoard, "left");
        leftController.enable();
        rightEncoder = new HallEncoder(3, 4);
        rightController = new PIDControllerAIAO(
                80, 0, 0, rightEncoder, right, dashBoard, "right");
        rightController.enable();
    }

    public void move(double left, double right) {
        //this.left.set(-MathUtils.pow(left, 3));
        //this.right.set(MathUtils.pow(right, 3));
        leftController.setSetpoint(-left*0.02);
        rightController.setSetpoint(right*0.02);
        //System.out.println(rightEncoder.getRaw());
    }
}
