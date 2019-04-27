package uk.ac.tees.cupcake.friends;

import com.google.firebase.firestore.ServerTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */
public class Comments {

    private String id;


    private String description;

    @ServerTimestamp
    private Date timestamp;

    public Comments() {
    }

    public Comments(String message, String id, Date timestamp) {
        this.id = id;
        this.description = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return description;
    }

    public String getId() {
        return id;
    }


    public Date getPostTime() {
        return timestamp;
    }
}
