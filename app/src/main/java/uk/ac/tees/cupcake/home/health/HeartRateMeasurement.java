package uk.ac.tees.cupcake.home.health;

import com.github.mikephil.charting.data.Entry;

import java.io.Serializable;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class HeartRateMeasurement implements Serializable {
    
    /**
     * Time stamp of when this measurement was taken.
     */
    private final long timestamp;
    
    /**
     * The beats per minute value of this measurement.
     */
    private final int bpm;
    
    /**
     * Constructs new HeartRateMeasurement.
     *
     * @param timestamp when the measurement was taken.
     * @param bpm the beats per minute of the measurement.
     */
    public HeartRateMeasurement(long timestamp, int bpm) {
        this.timestamp = timestamp;
        this.bpm = bpm;
    }
    
    public int getBpm() {
        return bpm;
    }
    
    /**
     * Constructs a new {@link Entry} object for this {@link HeartRateMeasurement}.
     *
     * @return an {@link Entry} instance for this {@link HeartRateMeasurement}.
     */
    public Entry toChartEntry() {
        return new Entry(timestamp, bpm, null);
    }
    
}