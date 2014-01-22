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
        Debug.println("[INFO] Autonomous Init Called", Debug.STANDARD);
    }

    public void autonomousPeriodic() {
        Debug.println("[INFO] Autonomous Periodic Enabled", Debug.STANDARD);
    }

    public void teleopInit() {
        Debug.println("[INFO] Teleop Init Called", Debug.STANDARD);
    }

    public void teleopPeriodic() {
        Debug.println("[INFO] Teleop Periodic Enabled", Debug.STANDARD);
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
    }
    
    public void testInit(){
        Debug.println("[INFO] Test Init Called", Debug.STANDARD);
    }

    public void testPeriodic() {
        Debug.println("[INFO] Test Periodic Enabled", Debug.STANDARD);
    }

    public void disabledInit() {
        Debug.println("[INFO] Disabled Init Called", Debug.STANDARD);
    }

    public void disabledPeriodic() {
        Debug.println("[INFO] Disabled Periodic Enabled", Debug.STANDARD);
    }

}
