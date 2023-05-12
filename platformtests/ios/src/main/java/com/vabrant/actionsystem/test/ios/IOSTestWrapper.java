package com.vabrant.actionsystem.test.ios;

import com.vabrant.actionsystem.platformtests.tests.AbstractTestWrapper;
import com.vabrant.actionsystem.platformtests.tests.PlatformTest;
import com.vabrant.actionsystem.platformtests.tests.PlatformTests;

public class IOSTestWrapper extends AbstractTestWrapper {

    @Override
    protected Instancer[] getTestList() {
        Instancer[] tests = new Instancer[PlatformTests.tests.size()];
        int i = 0;
        for (final Class<? extends PlatformTest> aClass : PlatformTests.tests) {
            tests[i] = new IosInstancer(aClass);
            i++;
        }
        return tests;
    }

    class IosInstancer implements Instancer {
        final Class<? extends PlatformTest> clazz;

        IosInstancer(Class<? extends PlatformTest> clazz) {
            this.clazz = clazz;
        }

        @Override
        public PlatformTest instance() {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String getSimpleName() {
            return clazz.getSimpleName();
        }
    }
}
