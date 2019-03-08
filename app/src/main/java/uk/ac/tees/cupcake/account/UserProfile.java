package uk.ac.tees.cupcake.account;

/**
 * Author Bradley Hunter.
 */

public class UserProfile {
    private String firstName;
    private String lastName;
    private String profileImageUrl;

    public UserProfile(){
        //  Firestore requires empty constructor
    }

    public UserProfile(String firstName, String lastName, String profileImageUrl){
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;

    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){return lastName;}
    public String getProfileImageUrl(){return profileImageUrl;}
}
