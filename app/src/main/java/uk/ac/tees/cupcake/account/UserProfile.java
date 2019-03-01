package uk.ac.tees.cupcake.account;

/**
 * Author Bradley Hunter.
 */

public class UserProfile {
    private String firstName;
    private String lastName;

    public UserProfile(){
        //  firestore requires empty constructor
    }

    public UserProfile(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
}
