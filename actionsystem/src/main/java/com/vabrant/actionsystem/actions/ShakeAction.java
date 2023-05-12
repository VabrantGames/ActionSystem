/**
 *	Copyright 2019 John Barton
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import make.some.noise.Noise;

public class ShakeAction extends PercentAction<Shakable, ShakeAction> {

    public static ShakeAction obtain() {
        return obtain(ShakeAction.class);
    }

    public static <T extends ShakeLogicData> ShakeAction shakeX(
            Shakable shakable, ShakeLogic<T> logic, T data, float amount, float duration, Interpolation interpolation) {
        return obtain().shakeX(amount)
                .set(shakable, duration, interpolation)
                .setLogic(logic)
                .setLogicData(data);
    }

    public static <T extends ShakeLogicData> ShakeAction shakeY(
            Shakable shakable, ShakeLogic<T> logic, T data, float amount, float duration, Interpolation interpolation) {
        return obtain().shakeY(amount)
                .set(shakable, duration, interpolation)
                .setLogic(logic)
                .setLogicData(data);
    }

    public static <T extends ShakeLogicData> ShakeAction shakeAngle(
            Shakable shakable,
            ShakeLogic<T> logic,
            T data,
            float maxAngle,
            float duration,
            Interpolation interpolation) {
        return obtain().shakeAngle(maxAngle)
                .set(shakable, duration, interpolation)
                .setLogic(logic)
                .setLogicData(data);
    }

    public static <T extends ShakeLogicData> ShakeAction shake(
            Shakable shakable,
            ShakeLogic<T> logic,
            T data,
            float xAmount,
            float yAmount,
            float angleAmount,
            float duration,
            Interpolation interpolation) {
        return obtain().shake(xAmount, yAmount, angleAmount)
                .set(shakable, duration, interpolation)
                .setLogic(logic)
                .setLogicData(data);
    }

    public static RandomShakeLogic RANDOM_SHAKE_LOGIC = new RandomShakeLogic();
    public static CosSinShakeLogic COS_SIN_SHAKE_LOGIC =
            new CosSinShakeLogic(15 * MathUtils.PI, 11 * MathUtils.PI, 6 * MathUtils.PI);

    private boolean usePercent;
    private float xAmount;
    private float yAmount;
    private float angleAmount;
    private boolean shakeX = false;
    private boolean shakeY = false;
    private boolean shakeAngle = false;
    private ShakeLogic logic;
    private ShakeLogicData data;

    public ShakeAction shakeX(float amount) {
        this.xAmount = amount;
        shakeX = true;
        return this;
    }

    public ShakeAction shakeY(float amount) {
        this.yAmount = amount;
        shakeY = true;
        return this;
    }

    public ShakeAction shakeAngle(float amount) {
        this.angleAmount = amount;
        shakeAngle = true;
        return this;
    }

    public ShakeAction shake(float xAmount, float yAmount, float angle) {
        this.xAmount = xAmount;
        this.yAmount = yAmount;
        this.angleAmount = angle;
        shakeX = shakeY = shakeAngle = true;
        return this;
    }

    /** The x value used in the shake. What this value means depends on the {@link ShakeLogic} used. It can be a maximum
     * displacement or a multiplier.
     * @return */
    public float getXValue() {
        return xAmount;
    }

    /** The y value used in the shake. What this value means depends on the {@link ShakeLogic} used. It can be a maximum
     * displacement or a multiplier.
     * @return */
    public float getYValue() {
        return yAmount;
    }

    /** The angle value used in the shake. What this value means depends on the {@link ShakeLogic} used. It can be a maximum
     * displacement or a multiplier.
     * @return */
    public float getAngleValue() {
        return angleAmount;
    }

    public ShakeAction setLogic(ShakeLogic logic) {
        if (logic == null) {
            this.logic = RANDOM_SHAKE_LOGIC;
        } else {
            this.logic = logic;
        }
        return this;
    }

    private ShakeAction setLogicData(ShakeLogicData data) {
        this.data = data;
        return this;
    }

    public <T extends ShakeLogicData> T getLogicData() {
        return (T) data;
    }

    public <T extends ShakeLogic> T getLogic() {
        return (T) logic;
    }

    public boolean isShakingX() {
        return shakeX;
    }

    public boolean isShakingY() {
        return shakeY;
    }

    public boolean isShakingAngle() {
        return shakeAngle;
    }

    public boolean usePercent() {
        return usePercent;
    }

    public ShakeAction setUsePercent(boolean usePercent) {
        this.usePercent = usePercent;
        return this;
    }

    @Override
    protected void percent(float percent) {
        if (!usePercent) percent = 1;
        logic.percent(data);
        if (shakeX) percentable.setShakeX(logic.getX());
        if (shakeY) percentable.setShakeY(logic.getY());
        if (shakeAngle) percentable.setShakeAngle(logic.getAngle());
    }

    @Override
    protected void startLogic() {
        if (logic == null) throw new RuntimeException("Logic has to be set before action is ran.");
        data.setAction(this);
        super.startLogic();
        data.onActionStart();
    }

    @Override
    public void killLogic() {
        super.killLogic();
        percentable.setShakeX(0);
        percentable.setShakeY(0);
        percentable.setShakeAngle(0);
    }

    @Override
    public void endLogic() {
        super.endLogic();
        percentable.setShakeX(0);
        percentable.setShakeY(0);
        percentable.setShakeAngle(0);
        data.onActionEnd();
    }

    @Override
    public ShakeAction setup() {
        return this;
    }

    @Override
    public void clear() {
        super.clear();
        xAmount = 0;
        yAmount = 0;
        angleAmount = 0;
        usePercent = false;
        shakeX = false;
        shakeY = false;
        shakeAngle = false;
    }

    @Override
    public void reset() {
        super.reset();
        data.setAction(null);
        data.onActionEnd();
        logic = null;
        data = null;
    }

    public interface ShakeLogic<T extends ShakeLogicData> {
        float getX();

        float getY();

        float getAngle();

        void percent(T data);
    }

    public abstract static class ShakeLogicData {

        private ShakeAction action;

        void setAction(ShakeAction action) {
            this.action = action;
        }

        public ShakeAction getAction() {
            return action;
        }

        protected abstract void onActionStart();

        protected abstract void onActionEnd();
    }

    public static class DefaultShakeLogicData extends ShakeLogicData {

        @Override
        protected void onActionStart() {}

        @Override
        protected void onActionEnd() {}
    }

    public static class RandomShakeLogic implements ShakeLogic<DefaultShakeLogicData> {

        private float percent;
        private ShakeLogicData data;

        @Override
        public float getX() {
            ShakeAction action = data.getAction();
            return MathUtils.random(-action.xAmount, action.xAmount) * percent;
        }

        @Override
        public float getY() {
            ShakeAction action = data.getAction();
            return MathUtils.random(-action.yAmount, action.yAmount) * percent;
        }

        @Override
        public float getAngle() {
            ShakeAction action = data.getAction();
            return MathUtils.random(-action.angleAmount, action.angleAmount) * percent;
        }

        @Override
        public void percent(DefaultShakeLogicData data) {
            this.data = data;

            ShakeAction action = data.getAction();
            percent = action.getPercent();
            percent = action.usePercent() ? (1 - percent) : percent;
        }
    }

    public static class CosSinShakeLogic implements ShakeLogic<DefaultShakeLogicData> {

        private float xFrequency;
        private float yFrequency;
        private float angleFrequency;
        private float percent;
        private ShakeLogicData data;

        public CosSinShakeLogic(float xFrequency, float yFrequency, float angleFrequency) {
            set(xFrequency, yFrequency, angleFrequency);
        }

        public void set(float xFrequency, float yFrequency, float angleFrequency) {
            this.xFrequency = xFrequency;
            this.yFrequency = yFrequency;
            this.angleFrequency = angleFrequency;
        }

        @Override
        public float getX() {
            ShakeAction action = data.getAction();
            return MathUtils.cos(action.getCurrentTime() * xFrequency) * action.xAmount * percent;
        }

        @Override
        public float getY() {
            ShakeAction action = data.getAction();
            return MathUtils.sin(action.getCurrentTime() * yFrequency) * action.yAmount * percent;
        }

        @Override
        public float getAngle() {
            ShakeAction action = data.getAction();
            return MathUtils.sin(action.getCurrentTime() * angleFrequency) * action.angleAmount * percent;
        }

        @Override
        public void percent(DefaultShakeLogicData data) {
            this.data = data;
            ShakeAction action = data.getAction();
            float percent = action.getPercent();
            this.percent = action.usePercent() ? (1 - percent) : percent;
        }
    }

    public static class GDQShakeLogic implements ShakeLogic<GDQShakeLogic.GDQShakeLogicData> {

        private float trauma;
        private float traumaPower = 2;
        private float shakeAmount;
        private final Noise noise;
        private GDQShakeLogicData data;

        public GDQShakeLogic(float trauma) {
            this(MathUtils.random(0, Integer.MAX_VALUE - 1), trauma, 2);
        }

        public GDQShakeLogic(int seed, float trauma, float traumaPower) {
            setTrauma(trauma);
            setTraumaPower(traumaPower);
            noise = new Noise(seed, 1f / 32f, Noise.SIMPLEX, 2);
        }

        public void setTrauma(float trauma) {
            this.trauma = trauma;
        }

        public void setTraumaPower(float traumaPower) {
            this.traumaPower = MathUtils.clamp(traumaPower, 2, 3);
        }

        public void setSeed(int seed) {
            noise.setSeed(seed);
        }

        @Override
        public float getX() {
            return data.getAction().getXValue()
                    * shakeAmount
                    * noise.getConfiguredNoise(noise.getSeed() * 2, data.noiseY);
        }

        @Override
        public float getY() {
            return data.getAction().getYValue()
                    * shakeAmount
                    * noise.getConfiguredNoise(noise.getSeed() * 3, data.noiseY);
        }

        @Override
        public float getAngle() {
            return data.getAction().getAngleValue()
                    * shakeAmount
                    * noise.getConfiguredNoise(noise.getSeed(), data.noiseY);
        }

        @Override
        public void percent(GDQShakeLogicData data) {
            this.data = data;
            data.noiseY += 1;
            ShakeAction action = data.getAction();
            float trauma = action.usePercent() ? this.trauma * (1 - action.getPercent()) : this.trauma;
            shakeAmount = (float) Math.pow(trauma, traumaPower);
        }

        public static class GDQShakeLogicData extends ShakeLogicData {
            float noiseY;

            @Override
            protected void onActionStart() {
                noiseY = 0;
            }

            @Override
            protected void onActionEnd() {}
        }
    }
}
