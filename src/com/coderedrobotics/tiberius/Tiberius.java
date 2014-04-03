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
    ImageObject imageObject;

    int testStage = 0;
    long testStartTime = 0;
    int autoStage = 0;
    long autoStartTime = 0;
    double driveStartingPosition = 0;
    boolean inAutonomousShootPosition = false;

    DashBoard dashBoard;

    // DISABLE THIS IN REAL MATCHES!!!!!!!!!!!!!!!!!!!!!!!!
    public static final boolean enableVirtualInputs = false;
    // NO REALLY.... please disable in real matches..... or else.

    public void robotInit() {
        Debug.println("[INFO] TIBERIUS CODE DOWNLOAD COMPLETE.", Debug.STANDARD);

        DashBoard.setConnectionAddress("socket://10.27.71.5:1180");
        dashBoard = new DashBoard();// Comment out this line to deactivate the dashboard.
        DashboardDriverPlugin.init(dashBoard);

        imageObject = new ImageObject();
        keyMap = new KeyMap();
        keyMap.setSingleControllerMode(false); // For ease of testing
        drive = new Drive(dashBoard);
        chooChoo = new ChooChoo();
        pickup = new Pickup(dashBoard);
        petals = new Petals(dashBoard);
    }

    public void autonomousInit() {
        autoStage = 0;
        autoStartTime = System.currentTimeMillis();
        drive.disableSpeedControllers();
        pickup.pickupIn();
        petals.open();
        petals.setEnabledState(true);
        pickup.wheelsIn();
        imageObject.reset();
        imageObject.request();
    }

    public void autonomousPeriodic() {
        switch (autoStage) {
            case 0:
                if (autoStartTime < System.currentTimeMillis() - 1000) {
                    autoStage++;
                }
                break;
            case 1:
                if (autoStartTime < System.currentTimeMillis() - 5000
                        || imageObject.isHot()) {
                    autoStage++;
                    autoStartTime = System.currentTimeMillis();
                }
                break;
            case 2:
                drive.move(-0.67, -0.7);
                if (autoStartTime < System.currentTimeMillis() - 200) {
                    autoStage++;
                }
                break;
            case 3:
                drive.enableSpeedControllers();
                drive.move(-0.67, -0.7);
                if (autoStartTime < System.currentTimeMillis() - 1600) {
                    autoStage++;
                    chooChoo.fire();
                }
                break;
            case 4:
                drive.move(0, 0);
                drive.enableSpeedControllers();
                pickup.stopWheels();
                break;
        }
        petals.step();
        chooChoo.step();
        DashboardDriverPlugin.updateBatteryVoltage(DriverStation.getInstance().getBatteryVoltage());
    }

    private void resetTimer(long t) {
        autoStartTime = System.currentTimeMillis() + t;
    }

    private void advanceWhenReady() {
        if (autoStartTime < System.currentTimeMillis()) {
            autoStage++;
        }
    }

    public void disabledInit() {
        drive.enableSpeedControllers();
    }

    public void teleopInit() {
        chooChoo.cock();
    }

    public void teleopPeriodic() {

        //System.out.println("left: " + petals.leftPotentiometer.get() + "\tright: " + petals.rightPotentiometer.get());
        //System.out.println("pickup: " + pickup.positionSensor.get());
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
            petals.manualStop();
        }

        if (keyMap.getPetalsBoostAndExtendButton()) {
            petals.closeOntoBall();
            pickup.pickupIn();
            pickup.stopWheels();
        }

        if (keyMap.getPetalsToGrabPostion()) {
            petals.closeOntoBall();
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
        if (keyMap.getWheelsMovingInButton()) {
            pickup.wheelsIn();
        } else if (keyMap.getWheelsMovingOutButton()) {
            pickup.wheelsOut();
        } else if (keyMap.getWheelsStopButton()) {
            pickup.stopWheels();
        }

        if (keyMap.getManualPickupExtendButton()) {
            pickup.movePickup(-0.9);
        } else if (keyMap.getManualPickupRetractButton()) {
            pickup.movePickup(0.5);
        } else {
            pickup.movePickup(0);
        }

        if (keyMap.getPickupToPostionTwoButton()) {
            pickup.pickupIn();
            petals.open();
            pickup.stopWheels();
        }

        if (keyMap.getPickupModeButton()) {
            pickup.pickupOut();
            pickup.wheelsIn();
            petals.open();
        }

        // STEP OBJECTS
        chooChoo.step();
        petals.step();

        // DASHBOARD STUFFS
        DashboardDriverPlugin.updateBatteryVoltage(DriverStation.getInstance().getBatteryVoltage());
        DashboardDriverPlugin.updateReverseDriveModeStatus(keyMap.getReverseDrive() ? 1 : 0);
        DashboardDriverPlugin.updateCockedStatus(chooChoo.isCocked() ? 1 : 0);
        DashboardDriverPlugin.updatePickupReadyStatus(pickup.isClear() ? 1 : 0);
        DashboardDriverPlugin.updatePetalsReadyStatus(petals.bothAreOpen() ? 1 : 0);
        DashboardDriverPlugin.updateCockingStatus(chooChoo.isCocking() ? 1 : 0);
        DashboardDriverPlugin.updateStringPotStatus(1);
        // Please note that not all of these are in this object, 
        // there are some in Petals, Drive, and Pickup.
    }

    public void testInit() {
        testStartTime = System.currentTimeMillis();
    }

    public void testPeriodic() {
        long elapsedTime = System.currentTimeMillis() - testStartTime;

        if (elapsedTime > 1300) {
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
                pickup.wheelsIn();
                break;
            case 4:
                pickup.wheelsOut();
                break;
            case 5:
                pickup.stopWheels();
                pickup.pickupOut();
                break;
            case 6:
                pickup.pickupIn();
                break;
            case 7:
                petals.open();
            case 8:
                petals.closeOntoBall();
            default:
                pickup.stopWheels();
                pickup.movePickup(0);
                petals.stop();
                drive.move(0, 0);
                break;
        }
    }

    public void disabledPeriodic() {
    }
}
