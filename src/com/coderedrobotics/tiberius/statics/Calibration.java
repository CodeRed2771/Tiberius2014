package com.coderedrobotics.tiberius.statics;

import com.coderedrobotics.tiberius.Petals;
import com.coderedrobotics.tiberius.Pickup;
import com.coderedrobotics.tiberius.libs.dash.DashBoard;
import com.coderedrobotics.tiberius.libs.dash.SRAListener;

/**
 *
 * @author Michael
 */
public class Calibration implements SRAListener {

    // PICKUP
    public static double pickupClearSetpoint = 0.7;
    public static double pickupExtendedSetpoint = 1.191;
    public static double pickupExtendedLimit = 1.195;
    public static double pickupRetractedLimit = 0.2867;
    public static double pickupClearLimit = 0.6312;

    // PETALS
    public static double petalLeftOuterLimit = 2.51;//2.421
    public static double petalLeftInnerLimit = 2.96;
    public static double petalRightOuterLimit = 2.26;
    public static double petalRightInnerLimit = 2.55;

    // TEMPS
    public static double pickupExtendedTemp;
    public static double pickupRetractedTemp;
    public static double petalLeftOuterLimitTemp;
    public static double petalLeftInnerLimitTemp;
    public static double petalRightOuterLimitTemp;
    public static double petalRightInnerLimitTemp;

    // OTHER
    public static boolean pickupCalsDisabled = false;
    public static boolean petalsCalsDisabled = false;

    Pickup pickup;
    Petals petals;
    DashBoard dash;

    public Calibration(Pickup pickup, Petals petals, DashBoard dash) {
        this.pickup = pickup;
        this.petals = petals;
        this.dash = dash;
    }

    public void alertToSRAUpdates() {
        // PICKUP
        if (dash.getRegister("pickup-retractedPosition") == -1) {
            dash.setRegister("pickup-retractedPosition", pickup.positionSensor.get());
            pickupRetractedLimit = pickup.positionSensor.get();
        }
        if (dash.getRegister("pickup-extendedPosition") == -1) {
            dash.setRegister("pickup-extendedPosition", pickup.positionSensor.get());
            pickupExtendedLimit = pickup.positionSensor.get();
        }
        if (dash.getRegister("pickup-clearPosition") == -1) {
            dash.setRegister("pickup-clearPosition", pickup.positionSensor.get());
            pickupClearLimit = pickup.positionSensor.get();
        }
        if (dash.getRegister("pickup-retractedSetpoint") == -1) {
            dash.setRegister("pickup-retractedSetpoint", pickup.positionSensor.get());
            pickupClearSetpoint = pickup.positionSensor.get();
        }
        if (dash.getRegister("pickup-extendedSetpoint") == -1) {
            dash.setRegister("pickup-extendedSetpoint", pickup.positionSensor.get());
            pickupExtendedSetpoint = pickup.positionSensor.get();
        }

        // PETALS
        if (dash.getRegister("petals-RInner") == -1) {
            dash.setRegister("petals-RInner", petals.rightPotentiometer.get());
            petalRightInnerLimit = petals.rightPotentiometer.get();
        }
        if (dash.getRegister("petals-ROuter") == -1) {
            dash.setRegister("petals-ROuter", petals.rightPotentiometer.get());
            petalRightOuterLimit = petals.rightPotentiometer.get();
        }
        if (dash.getRegister("petals-LInner") == -1) {
            dash.setRegister("petals-LInner", petals.leftPotentiometer.get());
            petalLeftInnerLimit = petals.leftPotentiometer.get();
        }
        if (dash.getRegister("petals-LOuter") == -1) {
            dash.setRegister("petals-LOuter", petals.leftPotentiometer.get());
            petalLeftOuterLimit = petals.leftPotentiometer.get();
        }

        // RESET & DISABLE CALIBRATION
        if (dash.getRegister("pickup-reset") == 1) {
            pickupClearSetpoint = Hardcoded.pickupClearSetpoint;
            pickupExtendedSetpoint = Hardcoded.pickupExtendedSetpoint;
            pickupExtendedLimit = Hardcoded.pickupExtendedLimit;
            pickupRetractedLimit = Hardcoded.pickupRetractedLimit;
            pickupClearLimit = Hardcoded.pickupClearLimit;
            dash.setRegister("pickup-reset", 0);
        }
        if (dash.getRegister("pickup-disableCalibration") == 1 && !pickupCalsDisabled) {
            pickupExtendedTemp = pickupExtendedLimit;
            pickupRetractedTemp = pickupRetractedLimit;
            pickupExtendedLimit = 1000;
            pickupRetractedLimit = 0;
            pickupCalsDisabled = true;
        } else if (dash.getRegister("pickup-disableCalibration") == 0 && pickupCalsDisabled) {
            pickupExtendedLimit = pickupExtendedTemp;
            pickupRetractedLimit = pickupRetractedTemp;
            pickupCalsDisabled = false;
        }
        if (dash.getRegister("petals-reset") == 1) {
            petalLeftOuterLimit = Hardcoded.petalLeftOuterLimit;
            petalLeftInnerLimit = Hardcoded.petalLeftInnerLimit;
            petalRightOuterLimit = Hardcoded.petalRightOuterLimit;
            petalRightInnerLimit = Hardcoded.petalRightInnerLimit;
            dash.setRegister("petals-reset", 0);
        }
        if (dash.getRegister("petals-disableCalibration") == 1 && !petalsCalsDisabled) {
            petalLeftOuterLimitTemp = petalLeftOuterLimit;
            petalLeftInnerLimitTemp = petalLeftInnerLimit;
            petalRightOuterLimitTemp = petalRightOuterLimit;
            petalRightInnerLimitTemp = petalRightInnerLimit;
            petalLeftOuterLimit = 10;
            petalLeftInnerLimit = 0;
            petalRightOuterLimit = 10;
            petalRightInnerLimit = 0;
            petalsCalsDisabled = true;
        } else if (dash.getRegister("petals-disableCalibration") == 0 && petalsCalsDisabled) {
            petalLeftOuterLimit = petalLeftOuterLimitTemp;
            petalLeftInnerLimit = petalLeftInnerLimitTemp;
            petalRightOuterLimit = petalRightOuterLimitTemp;
            petalRightInnerLimit = petalRightInnerLimitTemp;
            petalsCalsDisabled = false;
        }
    }

    private static class Hardcoded {

        public static double pickupClearSetpoint = 0.7;
        public static double pickupExtendedSetpoint = 1.191;
        public static double pickupExtendedLimit = 1.195;
        public static double pickupRetractedLimit = 0.2867;
        public static double pickupClearLimit = 0.6312;

        public static double petalLeftOuterLimit = 2.51;//2.421
        public static double petalLeftInnerLimit = 2.96;
        public static double petalRightOuterLimit = 2.31;
        public static double petalRightInnerLimit = 2.55;
    }
}
