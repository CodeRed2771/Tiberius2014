/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coderedrobotics.libs;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author laptop
 */
public class GenericJoystick {

    Joystick joystick;

    public GenericJoystick(int port) {
        joystick = new Joystick(port);
    }

    public boolean button(Button button) {
        return joystick.getRawButton(button.button);
    }

    public boolean button(AxisButton axis) {
        return axis(axis.axis) * (axis.direction ? 1 : -1) > 0.2;
    }

    public double axis(Axis axis) {
        double result = joystick.getRawAxis(axis.axis);
        boolean sign = result > 0d;
        result = Math.abs(result);
        result -= axis.deadZone;
        if (result < 0d) {
            return 0d;
        }
        result = result * (1d / (1d - axis.deadZone));
        result *= sign ? 1d : -1d;
        return result;
    }

    public boolean buttonReleased(Button button, ButtonState state) {
        boolean b = button(button);
        boolean result = !b && state.state;
        state.state = b;
        return result;
    }

    public boolean buttonReleased(AxisButton axisButton, ButtonState state) {
        boolean b = button(axisButton);
        boolean result = !b && state.state;
        state.state = b;
        return result;
    }

    public boolean buttonPressed(Button button, ButtonState state) {
        boolean b = button(button);
        boolean result = b && !state.state;
        state.state = b;
        return result;
    }

    public boolean buttonPressed(AxisButton axisButton, ButtonState state) {
        boolean b = button(axisButton);
        boolean result = b && !state.state;
        state.state = b;
        return result;
    }

    public boolean buttonToggled(
            Button button,
            ButtonState pressState,
            ButtonState toggleState) {
        if (buttonPressed(button, pressState)) {
            toggleState.state = !toggleState.state;
        }
        return toggleState.state;
    }

    public boolean buttonToggled(
            AxisButton axisButton,
            ButtonState pressState,
            ButtonState toggleState) {
        if (buttonPressed(axisButton, pressState)) {
            toggleState.state = !toggleState.state;
        }
        return toggleState.state;
    }

    public static ButtonState newButtonState() {
        return new ButtonState();
    }

    public static class Button {

        private int button;

        Button(int button) {
            this.button = button;
        }
    }

    public static class Axis {

        private int axis;
        private double deadZone = 0;

        Axis(int axis) {
            this.axis = axis;
        }

        Axis(int axis, double deadZone) {
            this.axis = axis;
            this.deadZone = deadZone;
        }
    }

    public static class AxisButton {

        private Axis axis;
        private boolean direction;

        AxisButton(Axis axis, boolean direction) {
            this.axis = axis;
            this.direction = direction;
        }
    }

    public static class ButtonState {

        private boolean state = false;

        private ButtonState() {
        }
    }
}
