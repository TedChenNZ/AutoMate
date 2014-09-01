package com.automates.automate.routines;

import android.content.Context;

import com.automates.automate.sqlite.RoutineDB;

import java.util.ArrayList;
import java.util.List;

public class RoutineService {
	/**
	 * 
	 */
	private static RoutineDB routineDB;
    private static List<Routine> routineList;
    private static RoutineService instance = null;

	private RoutineService() {
	}

    public static RoutineService getInstance() {
        if(instance == null) {
            instance = new RoutineService();
        }
        return instance;
    }

    public static void init(Context c) {
        routineDB = new RoutineDB(c);
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

	public Routine set(int index, Routine ul) {
		routineDB.updateRoutine(ul);
		return routineList.set(index, ul);
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
