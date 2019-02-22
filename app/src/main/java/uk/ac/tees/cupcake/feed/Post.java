package uk.ac.tees.cupcake.feed;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 12/02/2019.
 */
public class Post  {

    private final String username;

    private final String image;
    
    private final String description;

    private final String date;

    public Post(String username, String image, String description, String date){
        this.username = username;
        this.image = image;
        this.description = description;
        this.date = date;
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
                "https://images.pexels.com/photos/5548/red-bouquet-roses-bridal.jpg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "https://mhviraf.files.wordpress.com/2008/11/1794dubaiplans1140metretalltower_pic1.jpg",
                "https://images.pexels.com/photos/67567/bridal-bouquet-bride-bridal-bouquet-67567.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "https://images.pexels.com/photos/119603/bouquet-roses-cloves-wedding-119603.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "https://images.pexels.com/photos/134989/pexels-photo-134989.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "https://upload.wikimedia.org/wikipedia/commons/3/38/Tampa_FL_Sulphur_Springs_Tower_tall_pano01.jpg"
        };

        for (int i = 1; i <= posts; i++) {
            contacts.add(new Post("User " + i,  imageUrls[(i - 1) % imageUrls.length],  "Post number " + i, getCurrentTimeUsingDate()));
        }

        return contacts;

    }

    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }
}