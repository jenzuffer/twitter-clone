package dk.cphbusiness.mrv.twitterclone.impl;

import com.google.gson.Gson;
import dk.cphbusiness.mrv.twitterclone.contract.PostManagement;
import dk.cphbusiness.mrv.twitterclone.dto.Post;
import dk.cphbusiness.mrv.twitterclone.dto.UserCreation;
import dk.cphbusiness.mrv.twitterclone.util.Time;
import dk.cphbusiness.mrv.twitterclone.util.TimeImpl;
import redis.clients.jedis.AccessControlUser;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostManagementImpl implements PostManagement {
    private Jedis jedis;
    private Time time;

    public PostManagementImpl(Jedis jedis, Time time) {
        this.jedis = jedis;
        this.time = time;
    }

    @Override
    public boolean createPost(String username, String message) {
        String json = jedis.get(username);
        if (json == null || json.isEmpty()) return false;
        Gson gson = new Gson();
        UserCreation userCreation = gson.fromJson(json, UserCreation.class);
        long messageCreateTime = new TimeImpl().getCurrentTimeMillis();
        try {
            String created_at_ = message.split("created at ")[1].split(" by ")[0];
            messageCreateTime = Long.valueOf(created_at_);
        } catch (Exception ex) {

        }
        Post post = new Post(messageCreateTime, message);
        userCreation.posts.add(post);
        json = gson.toJson(userCreation);
        jedis.set(username, json);
        return true;
    }

    @Override
    public List<Post> getPosts(String username) {
        String json = jedis.get(username);
        if (json == null || json.isEmpty()) return null;
        Gson gson = new Gson();
        UserCreation userCreation = gson.fromJson(json, UserCreation.class);
        return userCreation.posts;
    }

    @Override
    public List<Post> getPostsBetween(String username, long timeFrom, long timeTo) {
        String json = jedis.get(username);
        if (json == null || json.isEmpty()) return null;
        Gson gson = new Gson();
        UserCreation userCreation = gson.fromJson(json, UserCreation.class);
        List<Post> returnPosts = new ArrayList<>();
        for (Post post : userCreation.posts) {
            if (timeFrom <= post.timestamp && post.timestamp <= timeTo) {
                returnPosts.add(post);
            }
        }
        return returnPosts;
    }
}
