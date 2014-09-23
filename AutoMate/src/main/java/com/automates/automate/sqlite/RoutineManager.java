package com.automates.automate.sqlite;

import java.util.List;

import com.automates.automate.model.Routine;

public interface RoutineManager {

    public abstract Routine addRoutine(Routine routine);

    public abstract Routine getRoutine(int id);

    // Get All routines
    public abstract List<Routine> getAllRoutines();

    // Updating single routine
    public abstract int updateRoutine(Routine routine);

    // Deleting single routine
    public abstract void deleteRoutine(Routine routine);

}