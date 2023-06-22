package request;

import model.*;

import java.util.List;

/**
 * Stores the data for a load service request
 */
public class LoadRequest{

    /**
     * The Authtoken data for the request
     */
    private List<Authtoken> authtokens;

    /**
     * The User data for the request
     */
    private List<User> users;

    /**
     * The ItemList data for the request
     */
    private List<ItemList> lists;

    /**
     * The Collection data for the request
     */
    private List<Collection> collections;

    /**
     * The Recipe data for the request
     */
    private List<Recipe> recipes;

    /**
     * The ListPermissions data for the request
     */
    private List<List<String>> listPermissions;

    /**
     * The RecipePermissions data for the request
     */
    private List<List<String>> recipePermissions;

    /**
     * The CollectionRecipes data for the request
     */
    private List<List<String>> collectionRecipes;

    /**
     * A flag that indicates whether the database should be cleared before loading
     */
    private boolean clearDatabase;

    /**
     * Creates a LoadRequest object with the given data
     *
     * @param authtokens The Authtoken data for the request
     * @param users The User data for the request
     * @param lists The ItemList data for the request
     * @param collections The Collection data for the request
     * @param recipes The Recipe data for the request
     * @param listPermissions The ListPermissions data for the request
     * @param recipePermissions The RecipePermissions data for the request
     * @param collectionRecipes The CollectionRecipes data for the request
     */
    public LoadRequest(List<Authtoken> authtokens, List<User> users, List<ItemList> lists,
                       List<Collection> collections, List<Recipe> recipes,
                       List<List<String>> listPermissions, List<List<String>> recipePermissions,
                       List<List<String>> collectionRecipes) {
        this.authtokens = authtokens;
        this.users = users;
        this.lists = lists;
        this.collections = collections;
        this.recipes = recipes;
        this.listPermissions = listPermissions;
        this.recipePermissions = recipePermissions;
        this.collectionRecipes = collectionRecipes;
        this.clearDatabase = false;
    }

    public List<Authtoken> getAuthtokens() {
        return authtokens;
    }

    public void setAuthtokens(List<Authtoken> authtokens) {
        this.authtokens = authtokens;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<ItemList> getLists() {
        return lists;
    }

    public void setLists(List<ItemList> lists) {
        this.lists = lists;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<List<String>> getListPermissions() {
        return listPermissions;
    }

    public void setListPermissions(List<List<String>> listPermissions) {
        this.listPermissions = listPermissions;
    }

    public List<List<String>> getRecipePermissions() {
        return recipePermissions;
    }

    public void setRecipePermissions(List<List<String>> recipePermissions) {
        this.recipePermissions = recipePermissions;
    }

    public List<List<String>> getCollectionRecipes() {
        return collectionRecipes;
    }

    public void setCollectionRecipes(List<List<String>> collectionRecipes) {
        this.collectionRecipes = collectionRecipes;
    }

    public boolean isClearDatabase() {
        return clearDatabase;
    }

    public void setClearDatabase(boolean clearDatabase) {
        this.clearDatabase = clearDatabase;
    }
}