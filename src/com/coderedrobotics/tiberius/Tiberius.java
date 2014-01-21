package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.statics.KeyMap;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Tiberius extends IterativeRobot {

    Drive drive;
    KeyMap keyMap;
    ChooChoo chooChoo;
    Pickup pickup;

    public void robotInit() {
        keyMap = new KeyMap();
        drive = new Drive();
        chooChoo = new ChooChoo();
        pickup = new Pickup();
    }

    public void autonomousPeriodic() {

    }

    public void teleopPeriodic() {
        drive.move(keyMap.getLeftDriveAxis(), keyMap.getRightDriveAxis());

        chooChoo.step(keyMap.getFireBallButton());
        
//        if (keyMap.getSpinPickupWheelsButton()){
//            pickup.spinWheels(false);
//        } else if (keyMap.getSpinPickupWheelsBackwardsButton()){
//            pickup.spinWheels(true);
//        } else {
//            pickup.stopWheels();
//        }
    }

    public void testPeriodic() {

    }
}
