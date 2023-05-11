package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.utils.ObjectMap;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PlatformTests {

    public static final List<Class<? extends PlatformTest>> tests = Arrays.asList(
            RotateTest.class,
            MoveTest.class,
            ScaleTest.class,
            ColorTest.class,
            ZoomTest.class
//            ShakeTest.class
    );

    public static final ObjectMap<String, Class> mappedTests;

    static {
        mappedTests = new ObjectMap<>(tests.size());
        for (Class c : tests) {
            mappedTests.put(c.getSimpleName(), c);
        }
    }

    public static List<String> getNames() {
        List<String> names = new ArrayList<String>(tests.size());

        for (ObjectMap.Entry<String, Class> e : mappedTests) {
            names.add(e.key);
        }

        Collections.sort(names);
        return names;
    }

    public static Class<? extends PlatformTest> forName(String name) {
        return mappedTests.get(name);
    }

    public static PlatformTest newTest(String testName) {
        try {
            return forName(testName).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
