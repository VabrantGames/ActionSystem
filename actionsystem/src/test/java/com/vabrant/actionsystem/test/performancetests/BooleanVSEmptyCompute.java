package com.vabrant.actionsystem.test.performancetests;

import com.badlogic.gdx.math.MathUtils;

public class BooleanVSEmptyCompute {

    private final int iterations = 1000;
    PerformanceTimer timer = new PerformanceTimer(2);

    public BooleanVSEmptyCompute() {
        // timer.setName("Test");
    }

    public void runTest() {
        timer.clear();
        testOne();
        testTwo();
        System.out.println(timer.toString());
    }

    private void testOne() {
        boolean dontCompute = true;
        timer.start();
        for (int i = 0; i < iterations; i++) {
            if (dontCompute) continue;
        }
        timer.end();
    }

    private void testTwo() {
        float value = 0;
        timer.start();
        for (int i = 0; i < iterations; i++) {
            value = MathUtils.lerp(0, 0, 0);
        }
        timer.end();
    }
}
