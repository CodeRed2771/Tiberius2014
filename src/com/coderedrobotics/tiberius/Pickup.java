/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * test comment.
 */
package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.Debug;
import com.coderedrobotics.tiberius.statics.Wiring;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

/**
 *
 * @author Michael
 */
public class Pickup {

    Talon elevationMotor;
    Talon spinWheelsMotor;
    AnalogPotentiometer armPositionSensor;
    
    public double pickupWheelsForward = 0.4;
    public double pickupWheelsReverse = -0.4;
    public double pickupArmExtend = 0.4;
    public double pickupArmRetract = -0.4;
    public double pickupArmStop = 0;
    private boolean isExtending;
    private boolean isRetracting;

    public Pickup() {
        elevationMotor = new Talon(Wiring.pickupElevation);
        spinWheelsMotor = new Talon(Wiring.pickupWheels);
        armPositionSensor = new AnalogPotentiometer(Wiring.armPositionSensorPort);
        isExtending = false;
        isRetracting = false;
    }
    
   public void step() {
        if (isExtending) {
            movePickup(pickupArmExtend);
        } else if (isRetracting) {
            movePickup(pickupArmRetract);
        } else {
            movePickup(pickupArmStop);
        }
        
        Debug.println("ANALOG SENSOR OUTPUT: " + armPositionSensor.get(), Debug.STANDARD);

    }
    
    public boolean isRetracted() {
        return true;
    }
    
    public boolean isExtended() {
        return true;
    }
    public void movePickup(double value) {
        if (!(isExtending || isRetracting)) {
            elevationMotor.set(value);
        }
    }

    public void extendPickup() {
        isExtending = true;
        isRetracting = false;
        spinWheels(pickupWheelsForward);
    }

    public void retractPickup() {
        isRetracting = true;
        isExtending = false;
    }

    public void togglePickup() {
        if (isExtending || isExtended()) {
            retractPickup();
        } else {
            extendPickup();
        }
    }

    public void spinWheels(double direction) {
        spinWheelsMotor.set(direction);
    }

    public void stopWheels() {
        spinWheelsMotor.set(0);
    }
}
