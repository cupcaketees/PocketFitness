package uk.ac.tees.cupcake.feed;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public Post() {
        // required empty constructor for fire base reflection.
    }

    public Post(String userUid, String image, String description, String date, String firstName, String lastName, String profilePictureUrl, String id){
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

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getProfilePictureUrl() { return profilePictureUrl;}

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() { return date; }

    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "E, dd MMM yyyy HH:mm";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }
}