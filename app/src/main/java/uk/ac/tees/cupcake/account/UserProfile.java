package uk.ac.tees.cupcake.account;

/**
 * Author Bradley Hunter.
 */

public class UserProfile {
    private String firstName;
    private String lastName;
    private String location;

    public UserProfile(){
        //  firestore requires empty constructor
    }

    public UserProfile(String firstName, String lastName, String location){
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location.isEmpty() ? "location not set" : location;
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getLocation(){ return location; }
}
