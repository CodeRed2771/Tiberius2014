package com.coderedrobotics.tiberius;

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

    public Drive() {
        left = new Talon(Wiring.leftDriveTalonPort);
        right = new Talon(Wiring.rightDriveTalonPort);
    }

    public void move(double left, double right) {
        this.left.set(MathUtils.pow(left,3));
        this.right.set(-MathUtils.pow(right,3));
    }
}
