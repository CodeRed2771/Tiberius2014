package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.Debug;
import com.coderedrobotics.tiberius.libs.HID.HID;
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
        keyMap.setSingleControllerMode(true);
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
            pickup.spinWheels(pickup.pickupWheelsForward);
        } else if (keyMap.getSpinPickupWheelsBackwardsButton()) {
            pickup.spinWheels(pickup.pickupWheelsReverse);
        } else if (keyMap.getSpinPickupWheelsStopButton()){
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
      
        pickup.step();
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
