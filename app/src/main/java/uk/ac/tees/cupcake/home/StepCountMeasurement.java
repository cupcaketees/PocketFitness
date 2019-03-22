package uk.ac.tees.cupcake.home;

import java.util.Date;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class StepCountMeasurement {
    
    private Date timestamp;
    
    private int count;
    
    public StepCountMeasurement(Date timestamp, int count) {
        this.timestamp = timestamp;
        this.count = count;
    }
    
    /**
     * Required no-args constructor for firestore.
     */
    public StepCountMeasurement() {}
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
}