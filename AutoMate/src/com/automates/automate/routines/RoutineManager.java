package com.automates.automate.routines;

import java.util.ArrayList;
import com.automates.automate.sqlite.RoutineDB;

public class RoutineManager extends ArrayList<Routine>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8201557359143283892L;
	private RoutineDB routineDB;


	public RoutineManager(RoutineDB routineDB) {
		this.routineDB = routineDB;
		super.addAll(routineDB.getAllRoutines());
	}

	@Override
	public boolean add(Routine r) {
		return super.add(routineDB.addRoutine(r));
	}

	@Override
	public boolean remove(Object o) {
		routineDB.deleteRoutine((Routine) o);
		return super.remove(o);
	}

	@Override
	public Routine set(int index, Routine ul) {
		routineDB.updateRoutine(ul);
		return super.set(index, ul);
	}
	
	public Routine addRoutine(Routine r) {
		Routine routine = routineDB.addRoutine(r);
		super.add(routine);
		return routine;
	}
	
	public Routine getRoutine(int id) {
		Routine routine = null;
		for (Routine r: this) {
			if (r.getId() == id) {
				routine = r;
				break;
			}
		}
		
		return routine;
	}

}
