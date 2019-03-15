package uk.ac.tees.cupcake.account;

/*
 * User Profile
 * @author Bradley Hunter <s6263464@tees.ac.uk>
 */

public class UserProfile {
    private String firstName;
    private String lastName;
    private String profileImageUrl;

    public UserProfile(){
        //  empty constructor required for firebase.
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
