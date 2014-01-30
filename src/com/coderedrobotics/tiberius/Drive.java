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
    PIDControllerAIAO controller;

    public Drive(DashBoard dashBoard) {
        left = new Talon(Wiring.leftDriveTalonPort);
        right = new Talon(Wiring.rightDriveTalonPort);
        leftEncoder = new HallEncoder(5, 6);
        controller = new PIDControllerAIAO(
                0, 0, 0, leftEncoder, left, dashBoard, "left");
    }

    public void move(double left, double right) {
        //this.left.set(-MathUtils.pow(left, 3));
        this.right.set(MathUtils.pow(right, 3));
        System.out.println(leftEncoder.getRaw());
    }
}
