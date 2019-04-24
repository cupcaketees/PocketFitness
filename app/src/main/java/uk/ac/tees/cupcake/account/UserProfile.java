package uk.ac.tees.cupcake.account;

import java.io.Serializable;

import uk.ac.tees.cupcake.home.health.Sex;

/**
 * User Profile class
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class UserProfile implements Serializable {

    private String uid;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private String coverPhotoUrl;
    private String accountCreated;
    private String bio;
    private String emailAddress;
    private boolean privateProfile;

    private int height;
    private int weight;

    private Sex sex;

    public UserProfile(){
        // required empty constructor for fire base reflection.
    }

    public UserProfile(String uid, String firstName, String lastName, String profilePictureUrl, String accountCreated, String email){
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureUrl = profilePictureUrl;
        this.accountCreated = accountCreated;
        this.emailAddress = email;
        this.privateProfile = false;
    }

    public String getBio() { return bio; }

    public String getFirstName() {
        return firstName.substring(0,1).toUpperCase() + firstName.substring(1);
    }

    public String getLastName() {
        return lastName.substring(0,1).toUpperCase() + lastName.substring(1);
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public String getProfilePictureUrl(){
        return profilePictureUrl;
    }

    public String getCoverPhotoUrl() { return coverPhotoUrl; }

    public String getAccountCreated() {
        return accountCreated;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getUid() {
        return uid;
    }

    public boolean isPrivateProfile() {
        return privateProfile;
    }

    public void setPrivateProfile(boolean privateProfile) {
        this.privateProfile = privateProfile;
    }
}