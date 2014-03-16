package com.coderedrobotics.tiberius;

import com.coderedrobotics.tiberius.libs.Debug;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import java.util.Vector;

public class ImageObject implements Runnable {

    private AxisCamera camera;
    private Thread thread;
    private boolean gettingImage = false;
    private boolean hot = true;
    private int areaThreshold = 25;
    private int brightnessThreshold = 160;
    private long threshhold;

    ImageObject() {
        thread = new Thread(this);
        try {
            camera = AxisCamera.getInstance();
            //myCamera.writeResolution(AxisCamera.ResolutionT.k320x240);
            thread.start();
        } catch (Exception ex) {
            Debug.println("CAMERA - FAILED TO INITIALIZE", Debug.WARNING);
        }
    }

    public void run() {
        while (true) {
            if (gettingImage) {
                Debug.println("getting image", Debug.STANDARD);

                long startTime = System.currentTimeMillis();
                GetImage();
                Debug.println("Image Acquisition Time: "
                        + (System.currentTimeMillis() - startTime),
                        Debug.EXTENDED);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
    }

    public boolean GetImage() {

        if (camera != null && DriverStation.getInstance().isEnabled()) {

            ColorImage image = null;

            try {
                image = camera.getImage();
            } catch (AxisCameraException ex) {
            } catch (NIVisionException ex) {
                Debug.println("error getting image", Debug.WARNING);
            }

            Vector particles = new Vector();

            try {
                BinaryImage thresholdImage
                        = image.thresholdRGB(
                                brightnessThreshold, 255,
                                brightnessThreshold, 255,
                                brightnessThreshold, 255);   // keep only bright objects
                particles.copyInto(thresholdImage.getOrderedParticleAnalysisReports());

                Debug.println("Number of particles: " + new Integer(particles.size()), Debug.STANDARD/*change to ex*/);
    //endTime = System.currentTimeMillis();106

                //imageAcquisitionMS = endTime - startTime;
                //System.out.println("Image Acquisition Time: " + imageAcquisitionMS);
                //System.out.println("particles found: " + particles.length);
                // Sort the particles so the one highest in the image is first
                for (int i = 0; i < particles.size(); ++i) {
                    if (particles.elementAt(i). > areaThreshold) {
                        Debug.println("Accepted" + i, Debug.STANDARD/*change to ex*/);
                        thresholdImage.free();
                        image.free();
                        return true;
                    } else {particleArea
                        Debug.println("Rejected" + i, Debug.STANDARD/*change to ex*/);
                    }
                }

                thresholdImage.free();
                image.free();
            } catch (NIVisionException ex) {
            }
        }
        return false;
    }

    public void update() {
        imageReady = false;
        gettingImage = true;
    }

    public void cancelRequest() {
        gettingImage = false;
    }

    public double reset() {
        threshhold = System.currentTimeMillis() + 4500;
    }

    public double getDistance() {
        imageReady = false;
        return distance;
    }

    public double getDistanceRawData() {
        return (getDistance() * 12);
    }

    public boolean isReady() {
        return imageReady;
    }

    private double GetDistance() {
        double DISTANCE_FUDGE_FACTOR = -1;

        double dst = 0;
        if (GetMaxHeight() != 0) {
            // le original fancy formula
            dst = (((326.04 - (1.6 * GetMaxHeight())) / 12) + DISTANCE_FUDGE_FACTOR); //calculating inches, returning feet - Fudge factor is a manual refinement
        }
        return dst;
    }

    public int GetHeight() {
        return GetHeight(0);
    }

    public int GetHeight(int idx) {
        if (idx < 0) {
            return 0;
        } else {
            return particles[idx].boundingRectHeight;
        }
    }

    public int GetMaxHeight() {
        int maxHeight = 0;
        if (particles.length > 0) {
            for (int i = 0; i < particles.length; i++) {
                if (particles[i].boundingRectHeight > maxHeight) {
                    maxHeight = particles[i].boundingRectHeight;
                }
            }
        }
        return maxHeight;
    }

    public int GetWidth() {
        return GetWidth(0);
    }

    public int GetWidth(int idx) {
        if (idx < 0) {
            return 0;
        } else {
            return particles[idx].boundingRectWidth;
        }
    }

    public double GetXPos() {
        return GetXPos(0);
    }

    public double GetXPos(int idx) {
        if (idx < 0) {
            return 0;
        } else {
            return particles[idx].boundingRectLeft;
        }
    }

    public int ParticleCount() {
        return particles.length;
    }

    public int GetParticleMidPointX() {
        return GetParticleMidPointX(0);
    }

    public int GetParticleMidPointX(int idx) {
        if (idx < 0) {
            return 0;
        } else {
            return particles[idx].boundingRectLeft + (particles[idx].boundingRectWidth / 2);
        }
    }

    public int GetParticleMidPointY() {
        return GetParticleMidPointY(0);
    }

    public int GetParticleMidPointY(int idx) {
        if (idx < 0) {
            return 0;
        } else {
            return particles[idx].boundingRectTop + (particles[idx].boundingRectHeight / 2);
        }
    }

    public int GetImageWidth() {
        return GetImageWidth(0);
    }

    public int GetImageWidth(int idx) {
        if (idx < 0) {
            return 0;
        } else {
            return particles[idx].imageWidth;
        }
    }

    public int GetParticleOffsetX() {
        return GetParticleOffsetX(0);
    }

    public int GetParticleOffsetX(int idx) {
        return GetParticleMidPointX(idx) - (particles[idx].imageWidth / 2);
    }

    public double GetOffsetPercentX() {
        return GetOffsetPercentX(0);
    }

    public double GetOffsetPercentX(int idx) {
        return ((double) GetParticleOffsetX(idx)) / ((double) GetImageWidth(idx) / 2);
    }

    public void PrintParticles() {
        if (ParticleCount() > 0) {
            for (int i = 0; i < ParticleCount(); i++) {
                System.out.println("Particular: " + i);
                System.out.println("X: " + particles[i].boundingRectLeft);
                System.out.println("Y: " + particles[i].boundingRectTop);
                System.out.println("H: " + particles[i].boundingRectHeight);
                System.out.println("AREA: " + particles[i].particleArea);
                System.out.println("MAX PART HEIGHT: " + GetMaxHeight());
            }
        }
    }

    public void setThreePoint(boolean threePoint) {
        this.threePoint = threePoint;
    }

    private class Filter {

        double val = 0;
        double weight = 0;

        public void filter(double input) {
            val = ((val * weight) + input) / (weight + 1);
            weight += 1;
        }

        public double get() {
            return val;
        }
    }
}
