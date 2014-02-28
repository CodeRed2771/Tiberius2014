package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.DerivativeCalculator;
import com.coderedrobotics.tiberius.libs.SmartDigitalInput;
import com.coderedrobotics.tiberius.libs.dash.DSMListener;
import com.coderedrobotics.tiberius.libs.dash.DashBoard;
import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author dvanvoorst
 */
public class Petals {

    // CONTROLLERS
    Talon petalMotorLeft;
    Talon petalMotorRight;
    // MOVEMENT SPEEDS
    private final double manualOpenSpeed = -0.8;
    private final double manualCloseSpeed = 0.5;
    // LIMIT SWITCHES
    private final SmartDigitalInput leftExtend;
    private final SmartDigitalInput rightExtend;
    //POTS
    private final AnalogPotentiometer leftPotentiometer;
    private final AnalogPotentiometer rightPotentiometer;
    private final DerivativeCalculator leftDerivativeCalculator;
    private final DerivativeCalculator rightDerivativeCalculator;
    // STRING POT CALIBRATIONS
    private final double leftOuterLimit = 2.250603587;
    private final double leftInnerLimit = 2.821572935;
    private final double rightOuterLimit = 2.374034308;
    private final double rightInnerLimit = 2.631222508;
    //LIMITS
    private final double pedalsInLimit = -0.7;
    private final double pedalsOutLimit = 0.9;
    // OBJECT VARIABLES
    private int mode;
    private static final int MANUAL = 0;
    private static final int OPEN = 1;
    private static final int CLOSE = 2;
    private static final int CLOSE_ON_BALL = 3;
    private boolean enabled = false;
    private long timeout;

    public Petals(DashBoard dashBoard) {
        leftDerivativeCalculator = new DerivativeCalculator(70);
        rightDerivativeCalculator = new DerivativeCalculator(70);
        leftPotentiometer = new AnalogPotentiometer(6);
        rightPotentiometer = new AnalogPotentiometer(7);
        petalMotorLeft = new Talon(Wiring.petalsMotorLeftPort);
        petalMotorRight = new Talon(Wiring.petalsMotorRightPort);
        leftExtend = new SmartDigitalInput(
                Wiring.leftPetalsExtendSensor, Tiberius.enableVirtualInputs);
        rightExtend = new SmartDigitalInput(
                Wiring.rightPetalsExtendSensor, Tiberius.enableVirtualInputs);
    }

    public void step() {
        switch (mode) {
            case CLOSE_ON_BALL:
                if (timeout < System.currentTimeMillis()) {
                    mode = MANUAL;
                    set(0);
                } else {
                    set(1);
                }
                break;
            case OPEN:
                if (timeout < System.currentTimeMillis()) {
                    mode = MANUAL;
                    set(0);
                } else {
                    set(-1);
                }
                break;
        }
    }

    public void closeOntoBall() {
        if (bothAreOpen()) {
            mode = CLOSE_ON_BALL;
            timeout = System.currentTimeMillis() + 400;
        }
    }

    public void open() {
        mode = OPEN;
        timeout = System.currentTimeMillis() + 2500;
    }

    public void close() {
//        mode = CLOSE;
    }

    public void manualOpen() {
        manualSet(manualOpenSpeed);
    }

    public void manualClose() {
        manualSet(manualCloseSpeed);
    }

    public void manualStop() {
        manualSet(0);
    }

    public void manualSet(double speed) {
        if (speed != 0 || mode == MANUAL) {
            mode = MANUAL;
            set(speed);
        }
    }

    public void setEnabledState(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            stop();
        }
    }

    private void set(double speed) {
        setLeft(speed);
        setRight(speed);
    }

    private void setLeft(double speed) {
        if (enabled) {
            if (speed < 0 && leftIsOpen()) {
                petalMotorLeft.set(0);
            } else if (speed > 0 && leftIsClosed()) {
                petalMotorLeft.set(0);
            } else if (speed > 0 && Math.abs(leftPotChange()) < 0.0002) {
                //setLeft(manualOpenSpeed);
            } else {
                petalMotorLeft.set(speed);
            }
        } else {
            petalMotorLeft.set(0);
        }
    }

    private void setRight(double speed) {
        if (enabled) {
            if (speed < 0 && rightIsOpen()) {
                petalMotorRight.set(0);
            } else if (speed > 0 && rightIsClosed()) {
                petalMotorRight.set(0);
            } else if (speed > 0 && Math.abs(rightPotChange()) < 0.0002) {
                //setRight(manualOpenSpeed);
            } else {
                petalMotorRight.set(speed);
            }
        } else {
            petalMotorRight.set(0);
        }
    }

    public void stop() {
        set(0);
    }

    public boolean leftIsOpen() {
        return leftExtend.get() || (getLeftPot() > pedalsOutLimit);
    }

    public boolean rightIsOpen() {
        return rightExtend.get() || (getRightPot() > pedalsOutLimit);
    }

    public boolean leftIsClosed() {
        return getLeftPot() < pedalsInLimit;
    }

    public boolean rightIsClosed() {
        return getRightPot() < pedalsInLimit;
    }

    public boolean bothAreOpen() {
        return rightIsOpen() && leftIsOpen();
    }

    private double scale(double low, double high, double d) {
        double dif = high - low;
        if (dif == 0) {
            return 0;
        }
        return (((d - low) / dif) * 2) - 1;
    }

    private double getLeftPot() {
        return scale(
                leftInnerLimit,
                leftOuterLimit,
                leftPotentiometer.get());
    }

    private double getRightPot() {
        return scale(
                rightInnerLimit,
                rightOuterLimit,
                rightPotentiometer.get());
    }

    private double leftPotChange() {
        return leftDerivativeCalculator.calculate(getLeftPot());
    }

    private double rightPotChange() {
        return rightDerivativeCalculator.calculate(getRightPot());
    }

//    public void pidWrite(double d) {
//        if (mode != MANUAL) {
//            double right = getRightPot();
//            double left = getLeftPot();
//            double dif = right - left;
//            dif *= 1;
//            right += dif;
//            left += -dif;
//            setRight(right);
//            setLeft(left);
//        }
//    }
//    public double pidGet() {
//        return (getLeftPot() + getRightPot()) / 2;
//    }
}
