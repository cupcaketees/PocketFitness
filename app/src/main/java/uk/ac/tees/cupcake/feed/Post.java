package uk.ac.tees.cupcake.feed;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 12/02/2019.
 */
public class Post  {

    private String username;

    private String image;
    
    private String description;

    private String date;

    public Post() {
    }

    public Post(String username, String image, String description, String date){
        this.username = username;
        this.image = image;
        this.description = description;


        this.date = date ;
    }



    public String getUsername(){
        return username;
    }

    public String getImage(){
        return image;
    }

    public String getDescription(){
        return description;
    }

    public String getDate() { return date; }

    
    /**
     * Temporary function to generate feed data.
     */
    public static ArrayList<Post> createPostsList(int posts) {
        ArrayList<Post> contacts = new ArrayList<Post>();

        String[] imageUrls = new String[] {
                "https://www.yellowblissroad.com/wp-content/uploads/2015/07/lemon-chicken-fb.jpg",
                "https://cdn.images.dailystar.co.uk/dynamic/21/photos/701000/620x/Chilech-before-and-after-542139.jpg",
                "https://img1.cookinglight.timeinc.net/sites/default/files/styles/4_3_horizontal_-_1200x900/public/image/2017/10/main/cl_digi_58.jpg?itok=C2WHk01D",
                "https://southernbite.com/wp-content/uploads/2018/02/Ultimate-Chicken-Spaghetti-5.jpg",
                "https://www.fitneass.com/wp-content/uploads/2014/10/weight-loss-story-before-and-after-2.jpg",
        };

        for (int i = 1; i <= posts; i++) {
            contacts.add(new Post("User " + i,  imageUrls[(i - 1) % imageUrls.length],  "Post number " + i, getCurrentTimeUsingDate()));
        }

        return contacts;

    }

    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "E, dd MMM yyyy HH:mm";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }
}