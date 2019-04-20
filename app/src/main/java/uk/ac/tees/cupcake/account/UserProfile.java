package uk.ac.tees.cupcake.account;

import java.io.Serializable;

import uk.ac.tees.cupcake.home.health.Sex;

/**
 * User Profile class
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class UserProfile implements Serializable {
    
    private String firstName;
    private String lastName;
    
    private String profilePictureUrl;
    private String coverPhotoUrl;
    
    private String accountCreated;
    private String bio;
    
    private int height;
    private int weight;
    private Sex sex;

    public UserProfile(){
        // required empty constructor for fire base reflection.
    }

    public UserProfile(String firstName, String lastName, String profilePictureUrl, String accountCreated){
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureUrl = profilePictureUrl;
        this.accountCreated = accountCreated;
    }

    public String getBio() { return bio; }

    public String getFirstName() {
        return firstName.substring(0,1).toUpperCase() + firstName.substring(1);
    }

    public String getLastName() {
        return lastName.substring(0,1).toUpperCase() + lastName.substring(1);
    }

    public String getProfilePictureUrl(){
        return profilePictureUrl;
    }

    public String getCoverPhotoUrl() { return coverPhotoUrl; }

    public String getAccountCreated() {
        return accountCreated;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public void setSex(Sex sex) {
        this.sex = sex;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public Sex getSex() {
        return sex;
    }
}