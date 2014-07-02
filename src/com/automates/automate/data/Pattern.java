package com.automates.automate.data;

import java.lang.reflect.Field;

public class Pattern {

    
    private int id;
    private String actionCategory;
    private String action;
    private int time;
    private int actualTime;
    private String day;
    private String location;
    private String mData;
    private int weight;
    private int statusCode;
 
    public Pattern(){}
 
    public Pattern(String actionCategory, String action, int time, int actualTime, String day, String location, String mData, int weight, int statusCode) {
        super();
        this.actionCategory = actionCategory;
        this.action = action;
        this.time = time;
        this.actualTime = actualTime;
        this.day = day;
        this.location = location;
        this.mData = mData;
        this.weight = weight;
        this.statusCode = statusCode;
    }
 
//getters & setters
 
    @Override
    public String toString() {
	  StringBuilder result = new StringBuilder();
	  String newLine = System.getProperty("line.separator");

	  result.append( this.getClass().getName() );
	  result.append( " Object {" );
	  result.append(newLine);

	  //determine fields declared in this class only (no fields of superclass)
	  Field[] fields = this.getClass().getDeclaredFields();

	  //print field names paired with their values
	  for ( Field field : fields  ) {
	    result.append("  ");
	    try {
	      result.append( field.getName() );
	      result.append(": ");
	      //requires access to private field:
	      result.append( field.get(this) );
	    } catch ( IllegalAccessException ex ) {
	      System.out.println(ex);
	    }
	    result.append(newLine);
	  }
	  result.append("}");

	  return result.toString();
    }
}