package uk.ac.tees.cupcake.feed;

import java.util.ArrayList;

/**
 * Created by User on 12/02/2019.
 */
public class Post  {

    private final String username;

    private final String image;
    
    private final String description;

    public Post(String username, String image, String description){
        this.username = username;
        this.image = image;
        this.description = description;
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
    
    /**
     * Temporary function to generate feed data.
     */
    public static ArrayList<Post> createPostsList(int posts) {
        ArrayList<Post> contacts = new ArrayList<Post>();
        
        for (int i = 1; i <= posts; i++) {
            contacts.add(new Post("User " + i, "https://cdn4.vectorstock.com/i/1000x1000/20/68/cartoon-bodybuilder-vector-5592068.jpg",  "Post number " + i));
        }
        
        return contacts;
    }
    
}