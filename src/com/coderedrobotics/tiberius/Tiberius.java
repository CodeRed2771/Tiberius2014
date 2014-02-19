package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.Debug;
import com.coderedrobotics.tiberius.libs.dash.DashBoard;
import com.coderedrobotics.tiberius.statics.DashboardDriverPlugin;
import com.coderedrobotics.tiberius.statics.KeyMap;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Tiberius extends IterativeRobot {

    Drive drive;
    KeyMap keyMap;
    ChooChoo chooChoo;
    Pickup pickup;
    Petals petals;
    int testStage = 0;
    long testStartTime = 0;

    DashBoard dashBoard;

    public void robotInit() {
        Debug.println("[INFO] TIBERIUS CODE DOWNLOAD COMPLETE.", Debug.STANDARD);
        keyMap = new KeyMap();
        keyMap.setSingleControllerMode(false); // For ease of testing
        dashBoard = new DashBoard();
        DashboardDriverPlugin.init(dashBoard);
        drive = new Drive(dashBoard);
        chooChoo = new ChooChoo();
        pickup = new Pickup();
        petals = new Petals();
    }

    public void autonomousInit() {
    }

    public void autonomousPeriodic() {
        DashboardDriverPlugin.updateBatteryVoltage(DriverStation.getInstance().getBatteryVoltage());
    }

    public void teleopInit() {
        chooChoo.cock();
    }

    public void teleopPeriodic() {
        // DRIVE OBJECT
        drive.move(keyMap.getLeftDriveAxis(), keyMap.getRightDriveAxis());

        if (keyMap.getReverseDriveButton()) {
            keyMap.toggleReverseDrive();
        }

        // PETALS OBJECT
        if (keyMap.getManualPetalsExtendButton()) {
            petals.manualExtendPetals();
        } else if (keyMap.getManualPetalsRetractButton()) {
            petals.manualRetractPetals();
        } else {
            petals.stopManualControl();
        }

        if (keyMap.getPetalsBoostAndExtendButton()) {
            petals.extendPetals();
            petals.boostPetalsOutwards();
        } else {
            petals.unboostPetalsOutwards();
        }
        
        if (keyMap.getPetalsToGrabPostion()) {
            petals.pulsePetalsInwards();
        }

        // CHOO CHOO OBJECT
//        if (keyMap.getFireButton()) {
//            pickup.setShootingPosition();
//            if (pickup.isSafeForShooting()) {
//            chooChoo.fire();
//            }
//        }
        // TODO: WRITE CODE WITH DEPENDECIEs
        // PICKUP OBJECT
        // TODO: WRITE NEW PICKUP CODE WHEN AUSTIN IS DONE
        // TODO: WRITE WHEEL MOVING CODE
        // STEP OBJECTS
        chooChoo.step();
        pickup.step();
        petals.step();
        // DASHBOARD STUFFS
        DashboardDriverPlugin.updateBatteryVoltage(DriverStation.getInstance().getBatteryVoltage());
    }

    public void testInit() {
        testStartTime = System.currentTimeMillis();
    }

    public void testPeriodic() {
        long elapsedTime = System.currentTimeMillis() - testStartTime;

        if (elapsedTime > 3000) {
            testStage++;
            testStartTime = System.currentTimeMillis();
        }

        switch (testStage) {
            case 0:
                drive.move(0.5, 0);
                break;
            case 1:
                drive.move(0, 0.5);
                break;
            case 2:
                drive.move(0, 0);
                chooChoo.fire();
                break;
            case 3:
                pickup.spinWheels(pickup.pickupWheelsForward);
                break;
            case 4:
                pickup.spinWheels(pickup.pickupWheelsReverse);
                break;
            case 5:
                pickup.stopWheels();
                pickup.movePickup(pickup.pickupArmExtend);
                break;
            case 6:
                pickup.movePickup(pickup.pickupArmRetract);
                break;
            default:
                pickup.stopWheels();
                pickup.movePickup(pickup.pickupArmStop);
                chooChoo.stop();
                drive.move(0, 0);
                break;
        }
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }
}
