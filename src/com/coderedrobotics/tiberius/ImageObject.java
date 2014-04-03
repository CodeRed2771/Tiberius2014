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
    private boolean hot = false;
    private final int areaThreshold = 25;
    private final int brightnessThreshold = 30;

    ImageObject() {
        thread = new Thread(this);
        try {
            //System.out.println("How are you again?");
            camera = AxisCamera.getInstance();
            camera.writeResolution(AxisCamera.ResolutionT.k320x240);
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
                hot = GetImage();
                System.out.println(hot);
                Debug.println("Image Acquisition Time: "
                        + (System.currentTimeMillis() - startTime),
                        Debug.STANDARD);
                cancelRequest();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
    }

    private boolean GetImage() {

        if (camera != null && DriverStation.getInstance().isEnabled()) {

            ColorImage image = null;

            try {
                image = camera.getImage();
            } catch (AxisCameraException ex) {
            } catch (NIVisionException ex) {
                Debug.println("error getting image", Debug.WARNING);
            }

            ParticleAnalysisReport[] particles;

            try {
                BinaryImage thresholdImage
                        = image.thresholdRGB(
                                brightnessThreshold, 255,
                                brightnessThreshold, 255,
                                brightnessThreshold, 255);   // keep only bright objects
                particles = thresholdImage.getOrderedParticleAnalysisReports();

                Debug.println("Number of particles: " + new Integer(particles.length), Debug.EXTENDED);
                for (int i = 0; i < particles.length; ++i) {
                    if (((ParticleAnalysisReport) particles[i]).particleArea
                            > areaThreshold
                            && ((ParticleAnalysisReport) particles[i]).boundingRectWidth
                            / ((ParticleAnalysisReport) particles[i]).boundingRectHeight
                            > 2d) {
                        Debug.println("Accepted" + i, Debug.EXTENDED);
                        thresholdImage.free();
                        image.free();
                        return true;
                    } else {
                        Debug.println("Rejected" + i, Debug.EXTENDED);
                    }
                }

                thresholdImage.free();
                image.free();
            } catch (NIVisionException ex) {
            }
        }
        return false;
    }

    public void cancelRequest() {
        gettingImage = false;
    }

    public void request() {
        gettingImage = true;
        reset();
    }

    public boolean isHot() {
        return hot;
    }

    public void reset() {
        hot = false;
    }
}
