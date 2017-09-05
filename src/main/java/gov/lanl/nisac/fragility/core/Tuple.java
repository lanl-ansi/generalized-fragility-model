package gov.lanl.nisac.fragility.core;

public final class Tuple<H,V> {

    private final H hazard;
    private final V value;

    public Tuple(H hazard, V value) {
        this.hazard = hazard;
        this.value = value;
    }

    public static <H,V> Tuple<H,V> with (H hazard, V value) {
        return new Tuple<H,V>(hazard,value);
    }

    public H getHazard() {
        return hazard;
    }

    public V getValue() {
        return value;
    }
}
