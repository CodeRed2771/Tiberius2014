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
    double driveStartingPosition = 0;
    boolean inAutonomousShootPosition = false;
    DashBoard dashBoard;

    public void robotInit() {
        Debug.println("[INFO] TIBERIUS CODE DOWNLOAD COMPLETE.", Debug.STANDARD);

        // Comment out the line 30 to deactivate the dashboard.  There is no need
        // to comment out anything else, because the rest of the code uses null checks.
        dashBoard = new DashBoard();
        DashboardDriverPlugin.init(dashBoard);

        keyMap = new KeyMap();
        keyMap.setSingleControllerMode(false); // For ease of testing
        drive = new Drive(dashBoard);
        chooChoo = new ChooChoo();
        pickup = new Pickup(dashBoard);
        petals = new Petals();
    }

    public void autonomousInit() {
        drive.calibrate();
    }

    public void autonomousPeriodic() {
        if (drive.isCalibrated()) {
            if (driveStartingPosition == 0) {
                Debug.println("Calibration complete, starting movement");
                driveStartingPosition = drive.getDistanceTraveledInches();
            }
            if (drive.getDistanceTraveledInches() - driveStartingPosition < 36) {
                drive.move(.8, .8);
            } else {
                drive.move(0, 0);
                inAutonomousShootPosition = true;
            }
        }

        if (inAutonomousShootPosition) {
            if (petals.bothAreOpen()) {
                if (pickup.isClear()) {
                    chooChoo.fire();
                    Debug.println("AUTONOMOUS SHOT FIRED");
                } else {
                    pickup.pickupIn();
                }
            } else {
                petals.open();
            }
        }
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
        } else {
            pickup.stopWheels();
        }

        if (keyMap.getManualPickupExtendButton()) {
            pickup.movePickup(-0.5);
        } else if (keyMap.getManualPickupRetractButton()) {
            pickup.movePickup(0.5);
        } else {
            pickup.movePickup(0);
        }

        if (keyMap.getPickupToPostionTwoButton()) {
            pickup.pickupIn();
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

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }
}
