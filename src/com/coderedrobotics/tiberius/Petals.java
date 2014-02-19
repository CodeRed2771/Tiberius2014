package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.DigitalInput;
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
    private final DigitalInput leftRetract;
    private final DigitalInput rightRetract;

    // OBJECT VARIABLES
    private int mode;
    private final int MANUAL = 0;
    private final int OPEN = 1;
    private final int CLOSE = 2;
    private boolean boost = false;
    private long closeEndTime;
    private boolean enabled;

    public Petals() {
        petalMotorLeft = new Talon(Wiring.petalsMotorLeftPort);
        petalMotorRight = new Talon(Wiring.petalsMotorRightPort);
        leftRetract = new DigitalInput(Wiring.leftPetalsExtendSensor);
        rightRetract = new DigitalInput(Wiring.rightPetalsExtendSensor);
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
            case CLOSE:
                set(closeSpeed);
                if (System.currentTimeMillis() >= closeEndTime) {
                    mode = MANUAL;
                    set(0);
                }
        }
    }

    private void close() {
        closeEndTime = System.currentTimeMillis() + closeTime;
        mode = CLOSE;
    }

    private void open() {
        mode = OPEN;
        set(openSpeed);
    }

    private void manualOpen() {
        mode = MANUAL;
        set(openSpeed);
    }

    private void manualClose() {
        mode = MANUAL;
        set(manualCloseSpeed);
    }

    private void setEnabledState(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            set(0);
        }
    }

    private void setBoost(boolean boost) {
        this.boost = boost;
    }

    private void set(double speed) {
        if (enabled) {
            if (speed < 0 && leftRetract.get()) {
                petalMotorLeft.set(0);
            } else {
                petalMotorLeft.set(speed);
            }
            if (speed < 0 && rightRetract.get()) {
                petalMotorRight.set(0);
            } else {
                petalMotorRight.set(speed);
            }
        } else {
            petalMotorLeft.set(0);
            petalMotorRight.set(0);
        }
    }
}
