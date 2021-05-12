package dk.cphbusiness.mrv.twitterclone.impl;

import com.google.gson.Gson;
import dk.cphbusiness.mrv.twitterclone.contract.UserManagement;
import dk.cphbusiness.mrv.twitterclone.dto.UserCreation;
import dk.cphbusiness.mrv.twitterclone.dto.UserOverview;
import dk.cphbusiness.mrv.twitterclone.dto.UserUpdate;
import dk.cphbusiness.mrv.twitterclone.util.Time;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserManagementImpl implements UserManagement {

    private Jedis jedis;
    private Subscriber subscriber;

    public UserManagementImpl(Jedis jedis) {
        this.subscriber = new Subscriber();
        this.jedis = jedis;
    }

    public void setUserCreation(UserCreation userCreation) {
        Gson gson = new Gson();
        String json = gson.toJson(userCreation);
        jedis.set(userCreation.username, json);
    }

    @Override
    public boolean createUser(UserCreation userCreation) {
        String json = jedis.get(userCreation.username);
        //var respone = jedis.sismember("users", userCreation.username);
        if (json == null || json.isEmpty()) {
            setUserCreation(userCreation);
            /*
            try (var tran = jedis.multi()) {
                tran.sadd("users", userCreation.username);

                /*
                tran.sadd("users", userCreation.firstname);
                tran.sadd("users", userCreation.lastname);
                tran.sadd("users", userCreation.birthday);
                tran.sadd("users", userCreation.passwordHash);


                tran.exec();
            }
             */
            return true;
        }
        return false;
    }

    @Override
    public UserOverview getUserOverview(String username) {
        String json = jedis.get(username);
        if (json == null || json.isEmpty()) return null;
        Gson gson = new Gson();
        UserCreation userCreation = gson.fromJson(json, UserCreation.class);
        String strFollow = "user " + username;
        String strFollowers = "following " + username;
        System.out.println("getUserOverview: " + userCreation.toString());
        UserOverview userOverview = new UserOverview(username, userCreation.firstname, userCreation.lastname,
                jedis.smembers(strFollowers).size(), jedis.smembers(strFollow).size());
        return userOverview;
    }

    @Override
    public boolean updateUser(UserUpdate userUpdate) {
        String json = jedis.get(userUpdate.username);
        if (json == null || json.isEmpty()) return false;
        Gson gson = new Gson();
        UserCreation userCreation = gson.fromJson(json, UserCreation.class);
        String birthday = userUpdate.birthday;
        if (birthday != null)
            userCreation.birthday = birthday;
        String firstname = userUpdate.firstname;
        if (firstname != null)
            userCreation.firstname = firstname;
        String lastname = userUpdate.lastname;
        if (lastname != null)
            userCreation.lastname = lastname;
        System.out.println("updateUser: " + userCreation.toString());
        json = gson.toJson(userCreation);
        jedis.set(userCreation.username, json);
        return true;
    }

    @Override
    public boolean followUser(String username, String usernameToFollow) {
        String json = jedis.get(username);
        String json1 = jedis.get(usernameToFollow);
        if (json == null || json.isEmpty() || json1 == null || json1.isEmpty()) return false;

        String strFollowing = "user " + username;
        String strFollowers = "following " + usernameToFollow;
        jedis.sadd(strFollowing, usernameToFollow);
        //jedis.subscribe(subscriber, username, usernameToFollow);
        jedis.sadd(strFollowers, username);
        return true;
    }

    @Override
    public boolean unfollowUser(String username, String usernameToUnfollow) {
        String json = jedis.get(username);
        String json1 = jedis.get(usernameToUnfollow);
        if (json == null || json.isEmpty() || json1 == null || json1.isEmpty()) return false;
        String strFollowing = "user " + username;
        jedis.srem(strFollowing, usernameToUnfollow);
        return true;
    }

    @Override
    public Set<String> getFollowedUsers(String username) {
        String strFollow = "user " + username;
        String json = jedis.get(username);
        if (json == null || json.isEmpty()) return null;
        return jedis.smembers(strFollow);
    }

    @Override
    public Set<String> getUsersFollowing(String username) {
        String json = jedis.get(username);
        if (json == null || json.isEmpty()) return null;
        String strFollowers = "following " + username;
        return jedis.smembers(strFollowers);
    }

}
