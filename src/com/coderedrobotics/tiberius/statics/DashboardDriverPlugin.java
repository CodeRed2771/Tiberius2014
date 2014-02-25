package com.coderedrobotics.tiberius.statics;

import com.coderedrobotics.lib.dash.DashBoard;

/**
 *
 * @author Michael
 */
public class DashboardDriverPlugin {

    private static DashBoard dash;

    private static final String pickupAngleStream = "pickupAngle";
    private static final String cockingStatusStream = "cockingStatus";
    private static final String petalsMovingStatusStream = "petalsMoving";
    private static final String stringPotStatusStream = "stringPot";
    private static final String pickupWheelsStatusStream = "pickupWheels";
    private static final String pickupMovingStatusStream = "pickupMoving";
    private static final String singleControllerModeStatusStream = "singleController";
    private static final String hallEncoderStatusStream = "hallEncoder";
    private static final String pickupReadyStatusStream = "pickupReady";
    private static final String petalsReadyStatusStream = "petalsReady";
    private static final String cockedStatusStream = "cocked";
    private static final String batteryVoltageStream = "batteryVoltage";

    public static void init(DashBoard dash) {
        DashboardDriverPlugin.dash = dash;
    }

    public static void updatePickupAngle(double val) {
        sendUpdate(val, pickupAngleStream);
    }

    public static void updateCockingStatus(double val) {
        sendUpdate(val, cockingStatusStream);
    }

    public static void updatePedalsMovingStatus(double val) {
        sendUpdate(val, petalsMovingStatusStream);
    }

    public static void updateStringPotStatus(double val) {
        sendUpdate(val, stringPotStatusStream);
    }

    public static void updatePickupWheelsStatus(double val) {
        sendUpdate(val, pickupWheelsStatusStream);
    }

    public static void updatePickupMovingStatus(double val) {
        sendUpdate(val, pickupMovingStatusStream);
    }

    public static void updateSingleControllerModeStatus(double val) {
        sendUpdate(val, singleControllerModeStatusStream);
    }

    public static void updateHallEncodersStatus(double val) {
        sendUpdate(val, hallEncoderStatusStream);
    }

    public static void updatePickupReadyStatus(double val) {
        sendUpdate(val, pickupReadyStatusStream);
    }

    public static void updatePedalsReadyStatus(double val) {
        sendUpdate(val, petalsReadyStatusStream);
    }

    public static void updateCockedStatus(double val) {
        sendUpdate(val, cockedStatusStream);
    }

    public static void updateBatteryVoltage(double val) {
        sendUpdate(val, batteryVoltageStream);
    }

    private static void sendUpdate(double val, String stream) {
        if (dash != null) {
            dash.streamPacket(val, stream);
        }
    }
}
