package main.java.com.lift;

public class WeightSensor implements IWeightChecker {
    private float currentWeight;
    private final float weightLimit;

    public WeightSensor(float weightLimit) {
        this.weightLimit = weightLimit;
    }

    @Override
    public void updateWeight(float weight) { this.currentWeight = weight; }

    @Override
    public boolean isOverWeightLimit() { return currentWeight > weightLimit; }

    public float getCurrentWeight() { return currentWeight; }
    public float getWeightLimit()   { return weightLimit; }
}