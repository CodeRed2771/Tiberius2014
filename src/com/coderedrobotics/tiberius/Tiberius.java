package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.Debug;
import com.coderedrobotics.tiberius.statics.KeyMap;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Tiberius extends IterativeRobot {

    Drive drive;
    KeyMap keyMap;
    ChooChoo chooChoo;
    Pickup pickup;

    public void robotInit() {
        Debug.println("[INFO] TIBERIUS CODE DOWNLOAD COMPLETE.", Debug.STANDARD);
        keyMap = new KeyMap();
        drive = new Drive();
        chooChoo = new ChooChoo();
        pickup = new Pickup();
    }

    public void autonomousInit() {

    }

    public void autonomousPeriodic() {
    }

    public void teleopInit() {
    }

    public void teleopPeriodic() {
        drive.move(keyMap.getLeftDriveAxis(), keyMap.getRightDriveAxis());

        chooChoo.step(keyMap.getFireBallButton());

        if (keyMap.getSpinPickupWheelsButton()) {
            pickup.spinWheels(false);
        } else if (keyMap.getSpinPickupWheelsBackwardsButton()) {
            pickup.spinWheels(true);
        } else {
            pickup.stopWheels();
        }

        if (keyMap.getElevationUpButton()) {
            pickup.movePickup(1);
        } else if (keyMap.getElevationDownButton()) {
            pickup.movePickup(-1);
        } else {
            pickup.movePickup(0);
        }

        keyMap.checkFor2Players();

        Debug.printDriverStationMessage(1, 1, "LEFT DRIVE: " + String.valueOf(keyMap.getLeftDriveAxis()));
        Debug.printDriverStationMessage(2, 1, "RIGHT DRIVE: " + String.valueOf(keyMap.getRightDriveAxis()));
        Debug.printDriverStationMessage(3, 1, String.valueOf(keyMap.getElevationUpButton()));
        Debug.printDriverStationMessage(3, 10, String.valueOf(keyMap.getElevationDownButton()));
    }

    public void testInit() {
    }

    public void testPeriodic() {
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }

}
