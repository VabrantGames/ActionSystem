package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TestUtils {

    public static final int DEFAULT_WIDTH = 960;
    public static final int DEFAULT_HEIGHT = 640;

    public static final String SEPARATOR = System.getProperty("line.separator");

    // private static final String pattern = "//----------//";

    public static void printTestHeader(String name) {
        System.out.println();
        System.out.println("//----------" + ' ' + name + ' ' + "----------//");
    }

    public static void centerTestObject(TestObject object, Viewport viewport) {
        float x = (viewport.getWorldWidth() - object.width) / 2;
        float y = (viewport.getWorldHeight() - object.height) / 2;
        object.setPosition(x, y);
    }

    @SafeVarargs
    public static <T> T[] toArray(T... ar) {
        return ar;
    }

    public static Object executePrivateMethod(
            String name, Class klass, Class[] parameterTypes, Object object, Object[] args) {
        try {
            Method m = ClassReflection.getDeclaredMethod(klass, name, parameterTypes);
            m.setAccessible(true);
            return m.invoke(object, args);
        } catch (ReflectionException e) {
            e.printStackTrace();
            Gdx.app.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
