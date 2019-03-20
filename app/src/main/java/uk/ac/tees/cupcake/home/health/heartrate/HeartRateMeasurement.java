package uk.ac.tees.cupcake.home.health.heartrate;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class HeartRateMeasurement implements Serializable {
    
    /**
     * Time stamp of when this measurement was taken.
     */
    private long timestamp;
    
    /**
     * The beats per minute value of this measurement.
     */
    private int bpm;
    
    /**
     * Types being general, resting, after exercise and before exercise.
     */
    private String measurementType;
    
    /**
     * Required for {@link DocumentSnapshot#toObject}
     */
    public HeartRateMeasurement() {}
    
    /**
     * Constructs new HeartRateMeasurement.
     *
     * @param timestamp when the measurement was taken.
     * @param bpm the beats per minute of the measurement.
     * @param measurementType the measurement type (e.g. resting, before or after exercise...)
     */
    public HeartRateMeasurement(long timestamp, int bpm, String measurementType) {
        this.timestamp = timestamp;
        this.bpm = bpm;
        this.measurementType = measurementType;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public int getBpm() {
        return bpm;
    }
    
    public void setBpm(int bpm) {
        this.bpm = bpm;
    }
    
    public String getMeasurementType() {
        return measurementType;
    }
    
    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }
    
    @Override
    public String toString() {
        return timestamp + " : " + bpm;
    }
    
}