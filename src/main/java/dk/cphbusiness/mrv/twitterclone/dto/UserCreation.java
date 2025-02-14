package dk.cphbusiness.mrv.twitterclone.dto;

import java.util.ArrayList;
import java.util.List;

public class UserCreation {
    public String username;
    public String firstname;
    public String lastname;
    public String passwordHash;
    public String birthday;
    public List<Post> posts;

    public UserCreation(String username,
                        String firstname,
                        String lastname,
                        String passwordHash,
                        String birthday) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.passwordHash = passwordHash;
        this.birthday = birthday;
        this.posts = new ArrayList<>();
    }

    public String toString(){
        return "username: " + username + " firstname: " + firstname + " lastname: " + lastname + " passwordhash: " +
                passwordHash + " birthday: " + birthday;
    }
}
