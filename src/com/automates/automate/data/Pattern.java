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
    private String wifi;
    private String mData;
    private double weight;
    private int statusCode;
 
    public Pattern(){}
 
    public Pattern(String actionCategory, String action, int time, int actualTime, String day, String location, String wifi, String mData, double weight, int statusCode) {
        super();
        this.actionCategory = actionCategory;
        this.action = action;
        this.time = time;
        this.actualTime = actualTime;
        this.day = day;
        this.location = location;
        this.wifi = wifi;
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

    public String getActionCategory() {
	return this.actionCategory;
    }
    
    public void setActionCategory(String input){
	this.actionCategory = input;
    }

    public String getAction() {
	return this.action;
    }
    
    public void setAction(String input){
	this.action = input;
    }

    public int getTime() {
	return this.time;
    }

    public void setTime(int input){
	this.time = input;
    }
    
    public int getActualTime() {
	return this.actualTime;
    }
    
    public void setActualTime(int input){
	this.actualTime = input;
    }

    public String getDay() {
	return this.day;
    }
    
    public void setDay(String input){
	this.day = input;
    }

    public String getLocation() {
	return this.location;
    }
    
    public void setLocation(String input){
	this.location = input;
    }

    public String getWifi() {
	return this.wifi;
    }
    
    public void setWifi(String input){
	this.wifi = input;
    }

    public String getData() {
	return this.mData;
    }
    
    public void setData(String input){
	this.mData = input;
    }

    public double getWeight() {
	return this.weight;
    }
    
    public void setWeight(double input){
	this.weight = input;
    }

    public int getStatusCode() {
	return this.statusCode;
    }
    
    public void setStatusCode(int input){
	this.statusCode = input;
    }
    
    public void setId(int input){
	this.id = input;
    }

    public int getId() {
        return this.id;
    }
}