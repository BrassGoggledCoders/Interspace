package xyz.brassgoggledcoders.interspace.api.interspace;

public class InterspaceVolume {
    private final int volume;
    private final double weight;

    public InterspaceVolume(int volume, double weight) {
        this.volume = volume;
        this.weight = weight;
    }

    public int getVolume() {
        return volume;
    }

    public double getWeight() {
        return weight;
    }
}
