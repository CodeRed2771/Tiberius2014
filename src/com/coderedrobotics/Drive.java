package com.coderedrobotics;

import com.coderedrobotics.statics.Wiring;
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
        right = new Talon(Wiring.rightTalonPort);
    }

    public void move(double left, double right) {
        this.left.set(left);
        this.right.set(right);
    }
}
