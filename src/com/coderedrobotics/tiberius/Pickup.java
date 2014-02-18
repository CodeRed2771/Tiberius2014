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

    Talon pickupArmMotor;
    Talon spinWheelsMotor;
    AnalogPotentiometer armPositionSensor;
    private static final double pickupArmSensorRetractedReading = 1.07;// (.17v per inch)
    private static final double pickupArmSensorExtendedReading = 1.48;
    private static final double pickupArmSensorShootPosition = 1.36;
    
    public final double pickupWheelsForward = 0.75;
    public final double pickupWheelsReverse = -0.75;
    public final double pickupArmExtend = -0.12;
    public final double pickupArmRetract = 0.12;
    public final double pickupArmStop = 0;
    private boolean isExtending;
    private boolean isRetracting;
    private boolean isMovingToShootPosition;

    /**
     * The Pickup object controls the robot's Pickup arm and all of its
     * components. This includes the pickup wheels and all sensors required to
     * automate the pickup process.
     * 
     */
    public Pickup() {
        pickupArmMotor = new Talon(Wiring.pickupArmMotorPort);
        spinWheelsMotor = new Talon(Wiring.pickupWheelsMotorPort);
        armPositionSensor = new AnalogPotentiometer(Wiring.armPositionSensorPort);
        isExtending = false;
        isRetracting = false;
        isMovingToShootPosition = false;
    }

    public void step() {
        
        //System.out.println(armPositionSensor.get());

        // now check if any movement is called for
        if (isExtending) {
            if (isExtended()) {
                Debug.println("Pickup.togglePickup: EXTENSION COMPLETE", Debug.STANDARD);
                isExtending = false;
                pickupArmMotor.set(0);
            } else {
                pickupArmMotor.set(pickupArmExtend);
            }
        } else if (isRetracting) {
            if (isRetracted()) {
                Debug.println("Pickup.togglePickup: RETRACTION COMPLETE", Debug.STANDARD);
                isRetracting = false;
                stopWheels();
                pickupArmMotor.set(0);
            } else {
                pickupArmMotor.set(pickupArmRetract);
            }
        } else if (isMovingToShootPosition) {
            if (isSafeForShooting()) {                
                Debug.println("Pickup.MovingToShoot: SHOOTING EXTENSION COMPLETE", Debug.STANDARD);
                pickupArmMotor.set(0);
                isMovingToShootPosition = false;
            } else {
                pickupArmMotor.set(pickupArmExtend);
            }
        }

       // Debug.println("Pickup.Step armPos: " + armPositionSensor.get() + " isRetracted: " + isRetracted() + " isExtended: " + isExtended() + " isRetracting: " + isRetracting + " isExtending: " + isExtending, Debug.STANDARD);
    }

    public boolean isRetracted() {
        return false; //we don't have pot
        //return (armPositionSensor.get() <= pickupArmSensorRetractedReading);
    }

    public boolean isExtended() {
        return false; //we don't have pot
        //return (armPositionSensor.get() >= pickupArmSensorExtendedReading);
    }

    public boolean isSafeForShooting() {
        return (armPositionSensor.get() >= pickupArmSensorShootPosition);
    }
    public void setShootingPosition() {
        isRetracting = false;
        isExtending = false;
        isMovingToShootPosition = true;
    }
    public void movePickup(double value) {
        /*
         * if the value is 0, we don't want to pass it through if we are
         * currently extending or retracting automatically because the 0 is
         * coming from the main program due to lack of controller input.
         */
        if (value == 0) {
            if (!(isExtending || isRetracting)) {
                pickupArmMotor.set(value);
            }
        } else {
            if (value == pickupArmExtend) {
                if (isExtended()) {
                    pickupArmMotor.set(0); // we are extended, so stop the motor
                } else {
                    pickupArmMotor.set(value);
                }
            } else if (value == pickupArmRetract) {
                if (isRetracted()) {
                    pickupArmMotor.set(0); // we are retracted, so stop the motor
                } else {
                    pickupArmMotor.set(value);
                }
            }
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
            Debug.println("Pickup.togglePickup: Now RETRACTING", Debug.STANDARD);
            retractPickup();
        } else {
            Debug.println("Pickup.togglePickup: Now EXTENDING", Debug.STANDARD);

            extendPickup();
        }
    }

    public void spinWheels(double direction) {
        spinWheelsMotor.set(direction);
    }

    public void stopWheels() {
        spinWheelsMotor.set(0);

        isRetracting = false;    // for safety sake, we'll stop the arm too
        isExtending = false;     // for safety sake, we'll stop the arm too
        pickupArmMotor.set(0);   // for safety sake, we'll stop the arm too
    }
}
