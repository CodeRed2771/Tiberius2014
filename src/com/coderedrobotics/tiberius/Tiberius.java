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
        pickup = new Pickup(dashBoard);
        petals = new Petals();
    }

    public void autonomousInit() {
        drive.calibrate();
    }

    public void autonomousPeriodic() {
//         if (drive.isCalibrated()){
//             if(driveStartingPosition == 0){
//                 Debug.println("Calibration complete, starting movement");
//                 driveStartingPosition = drive.getDistanceTraveledInches();
//             }
//             if(drive.getDistanceTraveledInches() - driveStartingPosition < 36) {
//                 drive.move(.8, .8);
//             } else {
//                 drive.move(0, 0);
//                 inAutonomousShootPosition = true;
//             }
//         }
//         
//         if (inAutonomousShootPosition){
//             // extend petals
//             // extend arm
//             // shoot
//             Debug.println("AUTONOMOUS SHOT FIRED");
//         }
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
        petals.setEnabledState(pickup.isClear());

        if (keyMap.getManualPetalsExtendButton()) {
            petals.manualOpen();
        } else if (keyMap.getManualPetalsRetractButton()) {
            petals.manualClose();
        } else {
            petals.stop();
        }

        if (keyMap.getPetalsBoostAndExtendButton()) {
            petals.open();
            petals.setBoost(true);
        } else {
            petals.setBoost(false);
        }

        if (keyMap.getPetalsToGrabPostion()) {
            petals.close();
        }

        // CHOO CHOO OBJECT
        if (keyMap.getFireButton()) {
            if (pickup.isClear()) {
                chooChoo.fire();
            } else {
                pickup.pickupIn();
            }
        }

        // PICKUP OBJECT
        if (keyMap.getWheelsMovingInButton()){
            pickup.wheelsIn();
        } else if (keyMap.getWheelsMovingOutButton()){
            pickup.wheelsOut();
        } else {
            pickup.setWheels(0);
        }
        
        if (keyMap.getManualPickupExtendButton()){
            pickup.movePickup(-0.5);
        } else if (keyMap.getManualPickupRetractButton()){
            pickup.movePickup(0.5);
        } else {
            pickup.movePickup(0);
        }
        
        if (keyMap.getPickupToPostionTwoButton()){
            pickup.pickupIn();
        }
        
        if (keyMap.getPickupModeButton()){
            pickup.pickupOut();
            pickup.wheelsIn();
            petals.open();
        }
        
        // STEP OBJECTS
        chooChoo.step();
        petals.step();
        
        // DASHBOARD STUFFS
        DashboardDriverPlugin.updateBatteryVoltage(DriverStation.getInstance().getBatteryVoltage());
    }

    public void testInit() {
        testStartTime = System.currentTimeMillis();
    }

    public void testPeriodic() {
//        long elapsedTime = System.currentTimeMillis() - testStartTime;
//
//        if (elapsedTime > 3000) {
//            testStage++;
//            testStartTime = System.currentTimeMillis();
//        }
//
//        switch (testStage) {
//            case 0:
//                drive.move(0.5, 0);
//                break;
//            case 1:
//                drive.move(0, 0.5);
//                break;
//            case 2:
//                drive.move(0, 0);
//                chooChoo.fire();
//                break;
//            case 3:
//                pickup.spinWheels(pickup.pickupWheelsForward);
//                break;
//            case 4:
//                pickup.spinWheels(pickup.pickupWheelsReverse);
//                break;
//            case 5:
//                pickup.stopWheels();
//                pickup.movePickup(pickup.pickupArmExtend);
//                break;
//            case 6:
//                pickup.movePickup(pickup.pickupArmRetract);
//                break;
//            default:
//                pickup.stopWheels();
//                pickup.movePickup(pickup.pickupArmStop);
//                chooChoo.stop();
//                drive.move(0, 0);
//                break;
//        }
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }
}
