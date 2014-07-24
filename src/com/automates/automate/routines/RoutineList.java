package com.automates.automate.routines;

import java.util.ArrayList;
import com.automates.automate.sqlite.RoutineDB;

public class RoutineList extends ArrayList<Routine>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8201557359143283892L;
	private RoutineDB routineDB;
//	private List<Routine> currentList;


	public RoutineList(RoutineDB routineDB) {
		this.routineDB = routineDB;
//		currentList = new ArrayList<Routine>();
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


}
