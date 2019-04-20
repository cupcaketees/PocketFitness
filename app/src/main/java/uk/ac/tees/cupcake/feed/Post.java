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
    
    public Post() {
        // required empty constructor for fire base reflection.
    }

    public Post(String userUid, String description){
        this.userUid = userUid;
        this.description = description;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public Date getTimeStamp(){
        return timeStamp;
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