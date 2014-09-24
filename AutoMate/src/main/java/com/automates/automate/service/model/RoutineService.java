package com.automates.automate.service.model;

import android.content.Context;

import com.automates.automate.model.Routine;
import com.automates.automate.sqlite.RoutineDB;

import java.util.ArrayList;
import java.util.List;
/**
 * An intermediary service between the database and the application.
 * Saves data in memory to reduce database calls.
 *
 * Created by Ted on 1/09/2014.
 */
public class RoutineService {
	private static RoutineDB routineDB;
    private static List<Routine> routineList;
    private static RoutineService instance = null;

	private RoutineService() {
	}
    /**
     * Singleton getter
     * @return
     */
    public static RoutineService getInstance() {
        if(instance == null) {
            instance = new RoutineService();
        }
        return instance;
    }
    /**
     * Initialize singleton with context
     * @param context
     */
    public static void init(Context context) {
        routineDB = new RoutineDB(context);
        routineList = new ArrayList<Routine>();
        routineList.addAll(routineDB.getAllRoutines());
    }




	public boolean add(Routine r) {
		return routineList.add(routineDB.addRoutine(r));
	}

	public boolean remove(Object o) {
		routineDB.deleteRoutine((Routine) o);
		return routineList.remove(o);
	}

	public Routine set(int index, Routine r) {
		routineDB.updateRoutine(r);
		return routineList.set(index, r);
	}
	
	public Routine addRoutine(Routine r) {
		Routine routine = routineDB.addRoutine(r);
        routineList.add(routine);
		return routine;
	}
	
	public Routine getRoutine(int id) {
		Routine routine = null;
		for (Routine r: routineList) {
			if (r.getId() == id) {
				routine = r;
				break;
			}
		}
		
		return routine;
	}

    public List<Routine> getAllRoutines() {
        return routineList;
    }

}
