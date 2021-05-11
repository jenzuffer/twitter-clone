package dk.cphbusiness.mrv.twitterclone.impl;

import com.google.gson.Gson;
import dk.cphbusiness.mrv.twitterclone.contract.UserManagement;
import dk.cphbusiness.mrv.twitterclone.dto.UserCreation;
import dk.cphbusiness.mrv.twitterclone.dto.UserOverview;
import dk.cphbusiness.mrv.twitterclone.dto.UserUpdate;
import dk.cphbusiness.mrv.twitterclone.util.Time;
import redis.clients.jedis.AccessControlUser;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserManagementImpl implements UserManagement {

    private Jedis jedis;

    public UserManagementImpl(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public boolean createUser(UserCreation userCreation) {
        String json = jedis.get(userCreation.username);
        //var respone = jedis.sismember("users", userCreation.username);
        if (json == null || json.isEmpty()) {
            Gson gson = new Gson();
            json = gson.toJson(userCreation);
            jedis.set(userCreation.username, json);
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
        Gson gson = new Gson();
        UserCreation userCreation = gson.fromJson(json, UserCreation.class);
        System.out.println("users: " + userCreation.username);
        return new UserOverview(userCreation.username, userCreation.firstname, userCreation.lastname, 0, 0);
    }

    @Override
    public boolean updateUser(UserUpdate userUpdate) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean followUser(String username, String usernameToFollow) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean unfollowUser(String username, String usernameToUnfollow) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Set<String> getFollowedUsers(String username) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Set<String> getUsersFollowing(String username) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
