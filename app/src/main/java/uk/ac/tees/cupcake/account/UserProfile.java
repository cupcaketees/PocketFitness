package uk.ac.tees.cupcake.account;

/**
 * User Profile class
 * @author Bradley Hunter <s6263464@live.tees.ac.uk>
 */
public class UserProfile {
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private String coverPhotoUrl;
    private String accountCreated;
    private String bio;

    public UserProfile(){
        //  empty constructor required for fire base.
    }

    public UserProfile(String firstName, String lastName, String profilePictureUrl, String accountCreated){
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureUrl = profilePictureUrl;
        this.accountCreated = accountCreated;
    }

    public String getBio() { return bio; }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getProfilePictureUrl(){
        return profilePictureUrl;
    }

    public String getCoverPhotoUrl() { return coverPhotoUrl; }

    public String getAccountCreated() {
        return accountCreated;
    }

}
