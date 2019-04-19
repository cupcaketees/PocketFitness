package uk.ac.tees.cupcake.feed;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Post  {

    private String image;
    private String description;
    private String userUid;

    @Exclude
    private String postId;

    @ServerTimestamp
    private Date timeStamp;

    public Post() {
        // required empty constructor for fire base reflection.
    }

    public Post(String userUid, String image, String description){
        this.userUid = userUid;
        this.image = image;
        this.description = description;
    }

    public String getUserUid() { return userUid; }

    public String getPostId() { return postId; }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public Date getTimeStamp(){
        return timeStamp;
    }

    public void setId(String id) {
        this.postId = id;
    }

    public void setTimeStamp(Date timeStamp){
        this.timeStamp = timeStamp;
    }

}