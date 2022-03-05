
package com.example.deleteme;


import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DiffService {
    private static String fxx;
    private List<Double> listX;
    private List<Double> listY;
    private List<Double> listErrorRate;
    private List<Double> listN;

    public static Double calculateFunction(double x) {
        return Math.pow(Math.E, Math.sin((3 * x)) * (x * x - 1) / (x * x + 2));
    }

    public static Double calculateFxFunction(double x, String str) {
        try {
            Expression e = new ExpressionBuilder(str)
                    .variables("x")
                    .build()
                    .setVariable("x", x);

            return e.evaluate();
        } catch (NullPointerException nullPointerException) {
            throw new NullPointerException();
        }
    }

    public static Double calculateErrorRate(double x0, double h) throws NullPointerException {
        double k1 = h / 3 * calculateFxFunction(x0, fxx);
        double k3 = h * calculateFxFunction(x0 + h / 3, fxx);
        double k4 = k1 + 4 * h / 3 * calculateFxFunction(x0 + h / 2, fxx);
        double k5 = h / 3 * calculateFxFunction(x0 + h, fxx);
        return Math.abs((2 * k4 - 3 * k3 - k5) / 10);
    }

    public static Double calculateNextY(double x0, double h, double y) throws NullPointerException {
        double k1 = h / 3 * calculateFxFunction(x0, fxx);
        double k4 = k1 + 4 * h / 3 * calculateFxFunction(x0 + h / 2, fxx);
        double k5 = h / 3 * calculateFxFunction(x0 + h, fxx);
        return y + (k4 + k5) / 2;
    }

    public List<Double> getListX() {
        return listX;
    }

    public void setListX(List<Double> listX) {
        this.listX = listX;
    }

    public List<Double> getListY() {
        return listY;
    }

    public void setListY(List<Double> listY) {
        this.listY = listY;
    }

    public List<Double> getListErrorRate() {
        return listErrorRate;
    }

    public void setListErrorRate(List<Double> listErrorRate) {
        this.listErrorRate = listErrorRate;
    }

    public List<Double> getListN() {
        return listN;
    }

    public void setListN(List<Double> listN) {
        this.listN = listN;
    }

    public String getFxx() {
        return fxx;
    }

    public void setFxx(String fxx) {
        DiffService.fxx = fxx;
    }

    public void runApp(double T, double n, double standartErrorRate, double x0, String fx) {
        listX = new ArrayList<>();
        listY = new ArrayList<>();
        listErrorRate = new ArrayList<>();
        listN = new ArrayList<>();
        fxx = fx;
        double h = T / n;
        double yn = 0;
        for (double i = x0; i <= T; i += h) {
            double errorRate = calculateErrorRate(i, h);
            listErrorRate.add(errorRate);
            do {
                h /= 2;
                if (32 * calculateErrorRate(i, h) < standartErrorRate) {
                    h *= 2;
                }
            } while (errorRate > standartErrorRate);
            double rusDaiPyaterky = calculateNextY(i, h, yn);
            yn = rusDaiPyaterky;
            listN.add(h);
            listX.add(i);
            listY.add(yn);
        }
    }
}

