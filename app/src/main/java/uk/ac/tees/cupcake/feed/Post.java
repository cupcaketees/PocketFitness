package uk.ac.tees.cupcake.feed;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by s6065431 on 12/02/2019.
 */
public class Post  {

    private String username;

    private String image;
    
    private String description;

    private String date;

    public Post() {
        // required empty constructor for fire base reflection.
    }

    public Post(String username, String image, String description, String date){
        this.username = username;
        this.image = image;
        this.description = description;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

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