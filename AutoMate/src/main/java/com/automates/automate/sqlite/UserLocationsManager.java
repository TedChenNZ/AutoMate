package com.automates.automate.sqlite;

import java.util.List;

import com.automates.automate.model.UserLocation;

public interface UserLocationsManager {

    /**
     * CRUD operations (create "add", read "get", update, delete) location + get all locations + delete all locations
     * @return 
     */

    public abstract UserLocation add(UserLocation ul);

    public abstract UserLocation get(int id);

    // Get All Books
    public abstract List<UserLocation> getAll();

    // Updating single object
    public abstract int update(UserLocation ul);

    // Deleting single UL
    public abstract void remove(UserLocation ul);

}