package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.DerivativeCalculator;
import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Michael
 */
public class Drive {

    Talon left;
    Talon right;
    DerivativeCalculator leftCalculator;
    DerivativeCalculator rightCalculator;
    
    public Drive() {
        left = new Talon(Wiring.leftTalonPort);
        right = new Talon(Wiring.rightDriveTalonPort);
        leftCalculator = new DerivativeCalculator();
        rightCalculator = new DerivativeCalculator();
    }

    public void move(double left, double right) {
        this.left.set(leftCalculator.calculate(left));
        this.right.set(rightCalculator.calculate(right));
        
    }
}
