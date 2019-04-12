package uk.ac.tees.cupcake.feed;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Post  {
    private String image;
    private String description;
    private String date;
    private String userUid;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private String postId;

    @ServerTimestamp
    private Date timeStamp;

    public Post() {
        // required empty constructor for fire base reflection.
    }

    public Post(String userUid, String image, String description, String firstName, String lastName, String profilePictureUrl, String id){
        this.postId = id;
        this.userUid = userUid;
        this.image = image;
        this.description = description;
        this.date = date;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUserUid() { return userUid; }

    public String getPostId() { return postId;}

    public String getFirstName() {
        return firstName.substring(0,1).toUpperCase() + firstName.substring(1);
    }

    public String getLastName() {
        return lastName.substring(0,1).toUpperCase() + lastName.substring(1);
    }

    public String getProfilePictureUrl() { return profilePictureUrl;}

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public void setTimeStamp(Date timeStamp){
        this.timeStamp = timeStamp;
    }

    public Date getTimeStamp(){
        return timeStamp;
    }
}