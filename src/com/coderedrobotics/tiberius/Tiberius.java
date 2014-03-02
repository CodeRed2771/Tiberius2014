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

        dashBoard = new DashBoard();// Comment out this line to deactivate the dashboard.
        DashboardDriverPlugin.init(dashBoard);

        keyMap = new KeyMap();
        keyMap.setSingleControllerMode(false); // For ease of testing
        drive = new Drive(dashBoard);
        chooChoo = new ChooChoo();
        pickup = new Pickup(dashBoard);
        petals = new Petals(dashBoard);
    }

    public void autonomousInit() {
        autoStage = 1;
        drive.disableSpeedControllers();
    }

    public void autonomousPeriodic() {
        switch (autoStage) {
            case 1:
                petals.open();
                pickup.pickupOut();
                autoStage++;
            case 2:
                if (petals.bothAreOpen() && pickup.isDown()) {
                    resetTimer(100);
                    petals.open();
                    autoStage++;
                }
                break;
            case 3:
                advanceWhenReady();
                break;
            case 4:
                petals.closeOntoBall();
                pickup.setWheels(-0.55);//-0.7
                resetTimer(300);//820
                autoStage++;
            case 5:
                advanceWhenReady();
                break;
            case 6:
                pickup.setWheels(-0.7);//-0.7
                drive.move(-0.3, -0.3);
                resetTimer(300);//820
                autoStage++;
            case 7:
                advanceWhenReady();
                break;
            case 8:
                pickup.setWheels(-0.5);//-0.7
                drive.move(-0.6, -0.6);
                resetTimer(440);//820
                autoStage++;
            case 9:
                advanceWhenReady();
                break;
            case 10:
                pickup.setWheels(-0.3);
                resetTimer(100);
                autoStage++;
            case 11:
                advanceWhenReady();
                break;
            case 12:
                drive.move(-0.8, -0.8);
                resetTimer(1500);
                autoStage++;
            case 13:
                advanceWhenReady();
                break;
            case 14:
                drive.enableSpeedControllers();
                drive.move(0, 0);
                petals.open();
                resetTimer(1000);
                autoStage++;
            case 15:
                advanceWhenReady();
                break;
            case 16:
                pickup.setWheels(-0.5);
                chooChoo.fire();
                pickup.pickupIn();
                resetTimer(800);
                autoStage++;
                break;
            case 17:
                advanceWhenReady();
                chooChoo.cock();
                break;
            case 18:
                if (chooChoo.isCocked()) {
                    autoStage++;
                }
                break;
            case 19:
                petals.close();
                resetTimer(200);
                autoStage++;
            case 20:
                advanceWhenReady();
                break;
            case 21:
                chooChoo.fire();
                pickup.stopWheels();
                autoStage++;
                break;
        }
        petals.setEnabledState(pickup.isClear());
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
            //petals.setBoost(true);
        } else {
            //petals.setBoost(false);
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
            pickup.movePickup(-1);
        } else if (keyMap.getManualPickupRetractButton()) {
            pickup.movePickup(1);
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
                chooChoo.stop();
                drive.move(0, 0);
                break;
        }
    }

    public void disabledPeriodic() {
    }
}
