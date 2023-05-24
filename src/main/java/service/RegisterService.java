package service;

import dao.*;
import model.Authtoken;
import model.Item;
import model.ItemList;
import model.User;
import result.RegisterResult;
import request.RegisterRequest;

import java.sql.Connection;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * The class responsible for handling user Registration requests.
 */
public class RegisterService {

    /**
     * The default items to be added to the My Day list when a new user is registered.
     */
    private final String[] myDayDefaultItems = {
        "Get to bed",
        "Brush my teeth",
        "Feed the hog",
        "Still got some homework to do",
        "Do the laundry",
        "Wash the car",
        "Still got those bills to pay"
    };

    /**
     * Registers a new user in the database
     *
     * @param request The RegisterRequest object containing the information needed to register a new user
     * @return The RegisterResult object containing the result of the registration
     */
    public RegisterResult register(RegisterRequest request) {
        // Check if request is valid
        if (!checkRequest(request)) {
            return new RegisterResult("ERROR: Invalid request.");
        }

        Database db = new Database();

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            // Check if username is already taken
            if (uDao.find(request.username()) != null) {
                db.closeConnection(false);
                return new RegisterResult("ERROR: Username already taken.");
            } else {
                // Create new user
                User newUser = new User(request.username(), request.password(),
                        request.firstName(), request.lastName(), ZonedDateTime.now());

                // Insert new user into database
                uDao.insert(newUser);

                // Create and insert new authtoken for the new user
                Authtoken newToken = new Authtoken(UUID.randomUUID().toString(), request.username());
                AuthtokenDAO aDao = new AuthtokenDAO(conn);
                aDao.insert(newToken);

                // Create default lists for the new user
                createDefaultLists(newUser.getUsername(), conn);

                // Close connection and commit changes to database
                db.closeConnection(true);

                // Return result
                return new RegisterResult(newToken.getToken(), newToken.getUserID());
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new RegisterResult("ERROR: Internal server error (" + e.getMessage() + ").");
        }
    }

    /**
     * Creates the default lists (My Day, Shopping List, and Favorited Items) for the user being registered.
     * @param username The username of the user being registered
     * @param conn The connection to the database
     */
    private void createDefaultLists(String username, Connection conn) {
        // Create a MyDay list for the user and fill with default items
        ItemList myDay = new ItemList(UUID.randomUUID().toString(), username + "_MyDay", username);
        for (String item : myDayDefaultItems) {
            myDay.getItems().add(new Item(UUID.randomUUID().toString(), item, username, myDay.getId()));
        }

        // Create a Shopping List for the user
        ItemList shoppingList = new ItemList(UUID.randomUUID().toString(), username + "_ShoppingList", username);

        // Create a Favorited Items List for the user
        ItemList favoritedItems = new ItemList(UUID.randomUUID().toString(), username + "_FavoritedItems", username);

        // Insert the lists into the database
        ItemListDAO lDao = new ItemListDAO(conn);
        try {
            lDao.insert(myDay);
            lDao.insert(shoppingList);
            lDao.insert(favoritedItems);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the request is valid.
     *
     * @param request The request to check
     * @return True if the request is valid, false otherwise
     */
    private boolean checkRequest(RegisterRequest request) {
        // Check if request is null or any of the fields are null
        return request != null &&
                request.username() != null &&
                request.password() != null &&
                request.firstName() != null &&
                request.lastName() != null;
    }
}
