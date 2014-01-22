package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Michael
 */
public class Drive {

    Talon left;
    Talon right;

    public Drive() {
        left = new Talon(Wiring.leftTalonPort);
        right = new Talon(Wiring.rightDriveTalonPort);
    }

    public void move(double left, double right) {
        this.left.set(left);
        this.right.set(right);
    }
}
