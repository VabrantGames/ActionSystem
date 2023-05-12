package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class HSBColorAction extends ColorAction<HSBColorAction> {

    public static HSBColorAction obtain() {
        return obtain(HSBColorAction.class);
    }

    public static HSBColorAction changeColor(
            Colorable colorable,
            float hue,
            float saturation,
            float brightness,
            float duration,
            Interpolation interpolation) {
        return obtain().changeColor(hue, saturation, brightness).set(colorable, duration, interpolation);
    }

    public static HSBColorAction changeColor(
            Colorable colorable,
            float hue,
            float saturation,
            float brightness,
            float alpha,
            float duration,
            Interpolation interpolation) {
        return obtain().changeColor(hue, saturation, brightness, alpha).set(colorable, duration, interpolation);
    }

    public static HSBColorAction changeHue(
            Colorable colorable, float hue, float duration, Interpolation interpolation) {
        return obtain().changeHue(hue).set(colorable, duration, interpolation);
    }

    public static HSBColorAction changeSaturation(
            Colorable colorable, float saturation, float duration, Interpolation interpolation) {
        return obtain().changeSaturation(saturation).set(colorable, duration, interpolation);
    }

    public static HSBColorAction changeBrightness(
            Colorable colorable, float brightness, float duration, Interpolation interpolation) {
        return obtain().changeBrightness(brightness).set(colorable, duration, interpolation);
    }

    public static HSBColorAction changeAlpha(
            Colorable colorable, float endAlpha, float duration, Interpolation interpolation) {
        return ColorAction.changeAlpha(obtain(), colorable, endAlpha, duration, interpolation);
    }

    // Bits
    // soloChannel = 0;
    // hueChannel = 1
    // saturationChannel = 2
    // brightnessChannel = 3
    private float[] startHSB;
    private float[] endHSB;
    private int options;

    public HSBColorAction() {
        startHSB = new float[3];
        endHSB = new float[3];
    }

    public HSBColorAction changeColor(float hue, float saturation, float brightness) {
        setChannels(1, 1, 1);
        endHSB[0] = hue;
        endHSB[1] = saturation;
        endHSB[2] = brightness;
        return this;
    }

    public HSBColorAction changeColor(float hue, float saturation, float brightness, float alpha) {
        changeColor(hue, saturation, brightness);
        changeAlpha(alpha);
        return this;
    }

    public HSBColorAction changeHue(float hue) {
        setChannels(1, 0, 0);
        endHSB[0] = hue;
        return this;
    }

    public HSBColorAction changeSaturation(float saturation) {
        setChannels(0, 1, 0);
        endHSB[1] = saturation;
        return this;
    }

    public HSBColorAction changeBrightness(float brightness) {
        setChannels(0, 0, 1);
        endHSB[2] = brightness;
        return this;
    }

    public HSBColorAction soloChannels(boolean solo) {
        options = ColorAction.setBit(options, 0, solo ? 1 : 0);
        return this;
    }

    private void setChannels(int hue, int saturation, int brightness) {
        options = ColorAction.setBit(options, 1, hue);
        options = ColorAction.setBit(options, 2, saturation);
        options = ColorAction.setBit(options, 3, brightness);
    }

    @Override
    public HSBColorAction setup() {
        if (setupAction) {
            Color c = percentable.getColor();
            startHSB[0] = getHue(c);
            startHSB[1] = getSaturation(c);
            startHSB[2] = getBrightness(c);
        }

        super.setup();
        return this;
    }

    @Override
    protected void startLogic() {
        super.startLogic();

        if (!ColorAction.isBitOn(options, 0)) {
            Color c = percentable.getColor();
            c.a = alphaChannel == 0 ? startColor.a : (!reverse ? startColor.a : endColor.a);
            float h = !ColorAction.isBitOn(options, 1) ? startHSB[0] : (!reverse ? startHSB[0] : endHSB[0]);
            float s = !ColorAction.isBitOn(options, 2) ? startHSB[1] : (!reverse ? startHSB[1] : endHSB[1]);
            float b = !ColorAction.isBitOn(options, 3) ? startHSB[2] : (!reverse ? startHSB[2] : endHSB[2]);
            c.set(HSBToRGB(endColor, h, s, b));
        }
    }

    @Override
    protected void percent(float percent) {
        super.percent(percent);

        Color c = percentable.getColor();
        float h = ColorAction.isBitOn(options, 1) ? MathUtils.lerp(startHSB[0], endHSB[0], percent) : getHue(c);
        float s = ColorAction.isBitOn(options, 2) ? MathUtils.lerp(startHSB[1], endHSB[1], percent) : getSaturation(c);
        float b = ColorAction.isBitOn(options, 3) ? MathUtils.lerp(startHSB[2], endHSB[2], percent) : getBrightness(c);
        HSBToRGB(endColor, h, s, b);
        c.set(endColor.r, endColor.g, endColor.b, c.a);
    }

    @Override
    public void reset() {
        super.reset();
        startHSB[0] = 0;
        startHSB[1] = 0;
        startHSB[2] = 0;
        endHSB[0] = 0;
        endHSB[1] = 0;
        endHSB[2] = 0;
        options = 0;
    }

    public static Color HSBToRGB(Color color, float hue, float saturation, float brightness) {
        hue = Math.max(0, hue) % 360;

        saturation = MathUtils.clamp(saturation, 0f, 1f);
        brightness = MathUtils.clamp(brightness, 0f, 1f);

        if (saturation == 0) {
            color.r = color.g = color.b = brightness;
            return color;
        }

        float h = hue / 60f;
        float c = brightness * saturation;
        float x = c * (1f - Math.abs(h % 2f - 1f));
        float m = brightness - c;

        switch ((int) h) {
            case 0:
                color.r = c + m;
                color.g = x + m;
                color.b = m;
                break;
            case 1:
                color.r = x + m;
                color.g = c + m;
                color.b = m;
                break;
            case 2:
                color.r = m;
                color.g = c + m;
                color.b = x + m;
                break;
            case 3:
                color.r = m;
                color.g = x + m;
                color.b = c + m;
                break;
            case 4:
                color.r = x + m;
                color.g = m;
                color.b = c + m;
                break;
            case 5:
                color.r = c + m;
                color.g = m;
                color.b = x + m;
                break;
        }
        return color;
    }

    public static float getHue(Color color) {
        float min = Math.min(Math.min(color.r, color.g), color.b);
        float max = Math.max(Math.max(color.r, color.g), color.b);
        float difference = max - min;

        if (difference == 0) {
            return 0f;
        } else if (max == color.r) {
            float h = 60f * (((color.g - color.b) / difference) % 6);
            return h < 0 ? h + 360f : h;
        } else if (max == color.g) {
            return 60f * (((color.b - color.r) / difference) + 2f);
        } else {
            return 60f * (((color.r - color.g) / difference) + 4f);
        }
    }

    public static float getSaturation(Color color) {
        float min = Math.min(Math.min(color.r, color.g), color.b);
        float max = Math.max(Math.max(color.r, color.g), color.b);
        float difference = max - min;
        return max == 0 ? 0 : difference / max;
    }

    public static float getBrightness(Color color) {
        return Math.max(Math.max(color.r, color.g), color.b);
    }
}
