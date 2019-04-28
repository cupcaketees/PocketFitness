package uk.ac.tees.cupcake.feed;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Post  {

    private String imageUrl;

    private String description;

    private String userUid;

    @ServerTimestamp
    private Date timeStamp;

    private String postId;

    private String locationName;

    public Post() {
        // required empty constructor for fire base reflection.
    }

    public Post(String userUid, String description, String locationName){
        this.userUid = userUid;
        this.description = description;
        this.locationName = locationName;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public Date getTimeStamp(){
        return timeStamp;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setTimeStamp(Date timeStamp){
        this.timeStamp = timeStamp;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }
}