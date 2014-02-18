package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.Debug;
import com.coderedrobotics.tiberius.libs.dash.DashBoard;
import com.coderedrobotics.tiberius.statics.KeyMap;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Tiberius extends IterativeRobot {

    Drive drive;
    KeyMap keyMap;
    ChooChoo chooChoo;
    Pickup pickup;
    DashBoard dashBoard;
    Petals petals;
    int testStage = 0;
    long testStartTime = 0;

    public void robotInit() {
        Debug.println("[INFO] TIBERIUS CODE DOWNLOAD COMPLETE.", Debug.STANDARD);
        keyMap = new KeyMap();
        keyMap.setSingleControllerMode(false); // For ease of testing
        dashBoard = new DashBoard();
        drive = new Drive(dashBoard);
        chooChoo = new ChooChoo();
        pickup = new Pickup();
        petals = new Petals();
    }

    public void autonomousInit() {
    }

    public void autonomousPeriodic() {
        streamBattInfo();
    }

    public void teleopInit() {
        chooChoo.cock();
    }

    public void teleopPeriodic() {
        streamBattInfo();

        drive.move(keyMap.getLeftDriveAxis(), keyMap.getRightDriveAxis());

        if (keyMap.getPetalExtendButton()) {
            petals.extendPetals();
        } else if (keyMap.getPetalRetractButton()) {
            petals.retractPetals();
        } else {
            petals.stop();
        }

        if (keyMap.getFireBallButton()) {
//            pickup.setShootingPosition();
//            if (pickup.isSafeForShooting()) {
            chooChoo.fire();
//            }
        }
        chooChoo.step();

        if (keyMap.getSpinPickupWheelsButton()) {
            pickup.spinWheels(pickup.pickupWheelsForward);
        } else if (keyMap.getSpinPickupWheelsBackwardsButton()) {
            pickup.spinWheels(pickup.pickupWheelsReverse);
        } else if (keyMap.getSpinPickupWheelsStopButton()) {
            pickup.stopWheels();
        }

        if (keyMap.getPickupRetractButton()) {
            pickup.movePickup(pickup.pickupArmRetract);
        } else if (keyMap.getPickupExtendButton()) {
            pickup.movePickup(pickup.pickupArmExtend);
        } else {
            pickup.movePickup(pickup.pickupArmStop);
        }

        if (keyMap.getPickupToggleButton()) {
            pickup.togglePickup();
        }

        if (keyMap.getSwitchControllerModeButtons()) {
            keyMap.toggleSingleControllerMode();
        }

        pickup.step();
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

    private void streamBattInfo() {
        if (dashBoard != null) {
            dashBoard.streamPacket(
                    DriverStation.getInstance().getBatteryVoltage(),
                    "batteryVoltage");
            // TODO: Stream a calculated pickup angle
        }
    }
}
