/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coderedrobotics.tiberius.libs;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;

/**
 *
 * @author laptop
 */
public class SpeedEncoderShell implements PIDSource{

    Encoder encoder = null;
    private long lastTime;
    private double lastPos = 0;
    private double currentSpeed = 0;

    public SpeedEncoderShell(Encoder encoder) {
        if (encoder == null) {
            throw new NullPointerException();
        }
        this.encoder = encoder;
        encoder.reset();
        encoder.start();
        lastTime = System.currentTimeMillis();
    }

    public double pidGet() {
        currentSpeed = (lastPos - encoder.getRaw()) / ((int) (lastTime - System.currentTimeMillis()));
        lastPos = encoder.getRaw();
        lastTime = System.currentTimeMillis();
        return currentSpeed;
    }
}
