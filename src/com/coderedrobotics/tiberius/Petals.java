package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.DerivativeCalculator;
import com.coderedrobotics.tiberius.libs.SmartDigitalInput;
import com.coderedrobotics.tiberius.libs.dash.DashBoard;
import com.coderedrobotics.tiberius.statics.Calibration;
import com.coderedrobotics.tiberius.statics.DashboardDriverPlugin;
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
    private final double manualOpenSpeed = -1;
    private final double manualCloseSpeed = 1;
    // LIMIT SWITCHES
    private final SmartDigitalInput leftExtend;
    private final SmartDigitalInput rightExtend;
    //POTS
    public final AnalogPotentiometer leftPotentiometer;
    public final AnalogPotentiometer rightPotentiometer;
    private final DerivativeCalculator leftDerivativeCalculator;
    private final DerivativeCalculator rightDerivativeCalculator;
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
        closeOntoBall();
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
        DashboardDriverPlugin.updatePetalsMovingStatus(speed != 0 ? 1 : 0);
    }

    private void setLeft(double speed) {
        if (enabled) {
            if (speed < 0 && leftIsOpen()) {
                petalMotorLeft.set(0);
            } else if (speed > 0 && leftIsClosed()) {
                petalMotorLeft.set(0);
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
        return !leftExtend.get() || (leftPotentiometer.get() < Calibration.petalLeftOuterLimit);
    }

    public boolean rightIsOpen() {
        return rightExtend.get() || (rightPotentiometer.get() < Calibration.petalRightOuterLimit);
    }

    public boolean leftIsClosed() {
        return leftPotentiometer.get() > Calibration.petalLeftInnerLimit;
    }

    public boolean rightIsClosed() {
        return rightPotentiometer.get() > Calibration.petalRightInnerLimit;
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

//    private double getLeftPot() {
//        return scale(
//                leftInnerLimit,
//                leftOuterLimit,
//                leftPotentiometer.get());
//    }
//
//    private double getRightPot() {
//        return scale(
//                rightInnerLimit,
//                rightOuterLimit,
//                rightPotentiometer.get());
//    }
}
