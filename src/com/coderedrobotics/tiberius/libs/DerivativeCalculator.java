/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coderedrobotics.tiberius.libs;

/**
 *
 * @author austin
 */
public class DerivativeCalculator {

    long time = 0;
    double old = 0;

    public double calculate(double value) {
        return (old - (old = value))
                / (time - (time = System.currentTimeMillis()));
    }
}
