package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.SmartDigitalInput;
import com.coderedrobotics.tiberius.statics.Wiring;
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
    private final double openSpeed = -0.5;
    private final double boostSpeed = -1;
    private final double manualCloseSpeed = 0.3;
    private final double closeSpeed = 0.7;
    private final int closeTime = 250;
    // LIMIT SWITCHES
    private final SmartDigitalInput leftExtend;
    private final SmartDigitalInput rightExtend;
    // OBJECT VARIABLES
    private int mode;
    private static final int MANUAL = 0;
    private static final int OPEN = 1;
    private static final int CLOSE_ONTO_BALL = 2;
    private boolean boost = false;
    private long closeEndTime;
    private boolean enabled;

    public Petals() {
        petalMotorLeft = new Talon(Wiring.petalsMotorLeftPort);
        petalMotorRight = new Talon(Wiring.petalsMotorRightPort);
        leftExtend = new SmartDigitalInput(Wiring.leftPetalsExtendSensor, Tiberius.enableVirtualInputs);
        rightExtend = new SmartDigitalInput(Wiring.rightPetalsExtendSensor, Tiberius.enableVirtualInputs);
    }

    public void step() {
        switch (mode) {
            case OPEN:
                if (boost) {
                    set(boostSpeed);
                } else {
                    set(openSpeed);
                }
                break;
            case CLOSE_ONTO_BALL:
                set(closeSpeed);
                if (System.currentTimeMillis() >= closeEndTime) {
                    mode = MANUAL;
                    set(0);
                }
        }
    }

    public void closeOntoBall() {
        closeEndTime = System.currentTimeMillis() + closeTime;
        mode = CLOSE_ONTO_BALL;
    }

    public void open() {
        mode = OPEN;
        set(openSpeed);
    }

    public void manualOpen() {
        mode = MANUAL;
        set(openSpeed);
    }

    public void manualClose() {
        mode = MANUAL;
        set(manualCloseSpeed);
    }

    public void setEnabledState(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            set(0);
        }
    }

    public void setBoost(boolean boost) {
        this.boost = boost;
    }

    private void set(double speed) {
        if (enabled) {
            if (speed < 0 && leftExtend.get()) {
                petalMotorLeft.set(0);
            } else {
                petalMotorLeft.set(speed);
            }
            if (speed < 0 && rightExtend.get()) {
                petalMotorRight.set(0);
            } else {
                petalMotorRight.set(speed);
            }
        } else {
            petalMotorLeft.set(0);
            petalMotorRight.set(0);
        }
    }

    public void stop() {
        set(0);
    }

    public boolean leftIsOpen() {
        return leftExtend.get();
    }

    public boolean rightIsOpen() {
        return rightExtend.get();
    }

    public boolean bothAreOpen() {
        return rightExtend.get() && leftExtend.get();
    }
}
