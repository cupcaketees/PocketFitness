package uk.ac.tees.cupcake.account;

/*
 * User Profile
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class UserProfile {
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private String accountCreated;
    private String coverPhotoUrl;

    public UserProfile(){
        //  empty constructor required for firebase.
    }

    public UserProfile(String firstName, String lastName, String profileImageUrl, String accountCreated){
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
        this.accountCreated = accountCreated;
    }

    public UserProfile(String profileImageUrl, String coverPhoto, String firstName, String lastName, String temp){
        this.profileImageUrl = profileImageUrl;
        this.coverPhotoUrl = coverPhoto;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }

    public String getAccountCreated() {
        return accountCreated;
    }

    public String getCoverPhotoUrl() { return coverPhotoUrl; }

}
