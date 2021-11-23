package com.example.pantryparserbackend.Websockets;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.users.User;
import com.example.pantryparserbackend.users.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.hibernate.LazyInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Websocket for favorites
 */
@Controller
@ServerEndpoint("/websocket/{user_id}")
@Api(value = "Favorite Socket", description = "Websocket for favoriting")
public class FavoriteSocket {
    private static RecipeRepository recipeRepository;
    private static UserRepository userRepository;
    public static final String FAVORITE_CONS = "favorite";
    public static final String UNFAVORITE_CONS = "unfavorite";

    /**
     * wires the recipeRepository to the static variable
     * @param repo RecipeRepository
     */
    @Autowired
    public void setRecipeRepository(RecipeRepository repo) {
        recipeRepository = repo;
    }

    /**
     * wires the userRepository to the static variable
     * @param repo UserRepository
     */
    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;
    }

    private static Map<Session, Integer> sessionUserMap = new Hashtable<>();
    private static Map<Integer, Session> userSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(FavoriteSocket.class);

    /**
     * Runs on the opening of the websocket
     * @param session client
     * @param user_id user id of client
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("user_id") int user_id) throws IOException {
        logger.info("Opening a connection");
        User user = userRepository.findById(user_id);

        if(user == null) {
            session.getBasicRemote().sendText("Error: That is not a valid user, this is probably a bug");
            session.close();
        } else {
            sessionUserMap.put(session, user_id);
            userSessionMap.put(user_id, session);

            List<String> history = getFavoriteHistory(user);
            for(String s : history) {
                sendToUser(user, s);
            }
            String message = user.getDisplayName() + "has just came online";
            broadcast(message);
        }
    }

    /**
     * When a client sends a message, do the operation and send the proper messages
     * possible operations: "favorite", "unfavorite"
     * syntax of message: "METHOD:recipe_id"
     * @param session
     * @param message
     * @throws IOException
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        User user = userRepository.findById(sessionUserMap.get(session));

        //format should be operation:id
        //ex favorite:1
        //possible operations are "favorite" and "unfavorite"
        String[] chunks = message.split(":");
        Recipe recipe = recipeRepository.findById(Integer.parseInt(chunks[1]));
        if(recipe == null) {
            logger.info("provided recipe did not exist");
            sendToUser(user, "that recipe doesn't exist");
        }

        switch (chunks[0]) {
            case FAVORITE_CONS:
                onFavorite(recipe, user);
                break;
            case UNFAVORITE_CONS:
                onUnfavorite(recipe, user);
                break;
            default:
                logger.info("provided operation did not exist");
                session.getBasicRemote().sendText("that operation doesn't exist");
                break;
        }

    }

    /**
     * closes the session with the client
     * @param session client
     */
    @OnClose
    public void onClose(Session session) {
        logger.info("User is leaving");
        User user = userRepository.findById(sessionUserMap.get(session));
        sessionUserMap.remove(session);
        userSessionMap.remove(user.getId());
        String message = user.getDisplayName() + " has logged off";
        broadcast(message);
    }

    /**
     * error handling
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("Got an error");
        throwable.printStackTrace();
    }

    /**
     * sends a message to all users online
     * @param message
     */
    private void broadcast(String message) {
        sessionUserMap.forEach((session, user) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("Eception: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * sends a message to a specific user
     * @param user user to send message to
     * @param message text to send
     */
    private void sendToUser(User user, String message) {
        try {
            userSessionMap.get(user.getId()).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("Eception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * just general statistics on a user's recipes
     * @param user the user that owns the recipes
     * @return list of statistics
     */
    private List<String> getFavoriteHistory(User user) {
        ArrayList<String> output = new ArrayList<>();
        List<Recipe> recipes = recipeRepository.getUserCreated(user);
        for (Recipe r : recipes) {
            List<User> users = userRepository.findByFavorited(r);
            output.add(users.size() + " total users favorited your recipe " + r.getName());
        }

        return output;
    }

    /**
     * things to do when running a favorite operation
     * @param recipe recipe to favorite
     * @param user user that is favoriting
     */
    public void onFavorite(Recipe recipe, User user) {
        try {
            userRepository.addFavorite(user.getId(), recipe.getId());
            user.favorite(recipe);
            sendToUser(user,"Successfully favorited " + recipe.getName());

            logger.info("Sending a favorite message");
            //we only want to send a message if the creator is on the app
            if(userSessionMap.containsKey(recipe.getCreator().getId())) {
                String message = user.getDisplayName() + " has just favorited your recipe " + recipe.getName() + "!";
                sendToUser(recipe.getCreator(), message);
            }

        } catch (DataIntegrityViolationException e) {
            sendToUser(user, "You've already favorited that recipe!");
            e.printStackTrace();
        }


    }

    /**
     * operations to do when unfavoriting
     * @param recipe recipe to unfavorite
     * @param user user that is unfavoriting
     */
    public void onUnfavorite(Recipe recipe, User user) {
        List<Integer> favorites = recipeRepository.getUserFavoriteIds(user.getId());
        if (favorites.contains(recipe.getId())) {
            userRepository.deleteFavorite(user.getId(), recipe.getId());
            user.unfavorite(recipe);
            sendToUser(user, "Successfully unfavorited " + recipe.getName());

            logger.info("Sending a unfavorite message");
            //we only want to send a message if the creator is on the app
            if(userSessionMap.containsKey(recipe.getCreator().getId())) {
                String message = user.getDisplayName() + " thinks your " + recipe.getName() + " sucks!";
                sendToUser(recipe.getCreator(), message);
            }
        } else {
            sendToUser(user, "You haven't favorited that recipe yet");
        }
    }
}
