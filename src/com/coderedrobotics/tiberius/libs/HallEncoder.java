/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coderedrobotics.tiberius.libs;

import edu.wpi.first.wpilibj.AnalogChannel;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.PIDSource;

/**
 *
 * @author austin
 */
public class HallEncoder implements Runnable, PIDSource {

    private AnalogChannel a, b;
    private Scaler aScaler, bScaler, xScaler, yScaler;
    private double angle, oldAngle;
    private int pollDelay;
    private Thread thread;
    private long oldTime;
    private boolean isRunning = false;
    private DerivativeCalculator dc;

    public HallEncoder(AnalogChannel a, AnalogChannel b, int pollDelay) {
        this.a = a;
        this.b = b;
        aScaler = new Scaler();
        bScaler = new Scaler();
        xScaler = new Scaler();
        yScaler = new Scaler();
        dc = new DerivativeCalculator();
        thread = new Thread(this);
        thread.setPriority(Thread.MAX_PRIORITY);
        restart(pollDelay);
    }

    public HallEncoder(int a, int b, int pollDelay) {
        this(new AnalogChannel(a), new AnalogChannel(b), pollDelay);
    }

    public HallEncoder(AnalogChannel a, AnalogChannel b) {
        this(a, b, -1);
    }

    public HallEncoder(int a, int b) {
        this(new AnalogChannel(a), new AnalogChannel(b));
    }

    public double pidGet() {
        return dc.calculate(getRaw());
    }

    public double getDeg() {
        return getRaw() * 360;
    }

    public double getRad() {
        return getRaw() * 2 * Math.PI;
    }

    public double getRaw() {
        poll();
        return angle;
    }

    public void destroy() {
        stop();
        a.free();
        b.free();
    }

    public void restart(int pollDelay) {
        stop();
        this.pollDelay = pollDelay;
        thread.start();
    }

    public void stop() {
        pollDelay = 0;
        while (isRunning) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
            }
        }
        reset();
    }

    public void reset() {
        oldAngle = 0;
        oldTime = 0;
        angle = 0;
    }

    private void poll() {
        double a = this.a.getValue();
        double b = this.b.getValue();
        double trim;
        trim = aScaler.checkCaps(a);
        xScaler.trim(trim);
        yScaler.trim(trim);
        trim = bScaler.checkCaps(b);
        xScaler.trim(trim);
        yScaler.trim(-trim);
        a = aScaler.scale(a);
        b = bScaler.scale(b);
        double x = a + b;
        double y = a - b;
        xScaler.checkCaps(x);
        yScaler.checkCaps(y);
        x = xScaler.scale(x);
        y = xScaler.scale(y);
        int phase;
        double z;
        if (x * x > y * y) {
            if (x > 0) {
                phase = 1;
            } else {
                phase = 3;
            }
            z = MathUtils.asin(y);
        } else {
            if (y > 0) {
                phase = 2;
            } else {
                phase = 0;
            }
            z = MathUtils.asin(x);
        }
        z = z / Math.PI;
        z = z / 2;
        z += 0.125d;
        if (phase > 1) {
            z = 0.25d - z;
        }
        z += phase * 0.25;
        double old = angle % 1;
        double dif = z - old;
        dif = dif % 1;
        if (dif > 0.5) {
            dif -= 1;
        }
        angle += dif;
    }

    public void run() {
        isRunning = true;
        while (pollDelay != 0) {
            poll();
            if (pollDelay == -1) {
                long time = System.currentTimeMillis();
                int delay;
                if (angle != oldAngle) {
                    delay = (int) ((time - oldTime) / ((angle - oldAngle) * 6));
                } else {
                    delay = 50;
                }
                delay = Math.min(Math.abs(delay), 50);
                oldAngle = angle;
                oldTime = time;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                }
            } else {
                try {
                    Thread.sleep(pollDelay);
                } catch (InterruptedException ex) {
                }
            }
        }
        isRunning = false;
    }

    private class Scaler {

        double high = 0, low = 0;
        boolean init = false;

        private double checkCaps(double d) {
            if (!init) {
                low = d;
                high = d;
                init = true;
            }
            if (d > high) {
                double result = ((d - low) / (low - high)) + 1;
                high = d;
                return result;
            }
            if (d < low) {
                double result = ((high - d) / (high - low)) - 1;
                low = d;
                return result;
            }
            return 0;
        }

        public double scale(double d) {
            double dif = high - low;
            if (dif == 0) {
                return 0;
            }
            return (((d - low) / dif) * 2) - 1;
        }

        public void trim(double d) {
            if (d > 0) {
                low += d;
            }
            if (d < 0) {
                high += d;
            }
        }
    }
}
