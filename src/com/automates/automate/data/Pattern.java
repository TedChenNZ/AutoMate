package com.automates.automate.data;

import java.lang.reflect.Field;
/**
 * This class defines the structure for a pattern to be entered into the database. Also contains methods to compare for unique patterns.
 * @author Dhanish
 *
 */

public class Pattern {

    
    private String action;
    private String actionCategory;
    private long actualTime;
    private int day;
    private int id;
    private String location;
    private String mData;
    private int statusCode;
    private int time;
    private double weight;
    private String wifi;
 
    public Pattern(){}
 
    public Pattern(String actionCategory, String action, int time, int actualTime, int day, String location, String wifi, String mData, double weight, int statusCode) {
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
 
    /**
     * Used to check if the unique values of a pattern match with another 
     * @param p The pattern to be compared against.
     * @return a boolean indicating the match.
     */
    public boolean compare(Pattern p){
	if(p.getActionCategory().equals(this.getActionCategory()) &&
		p.getAction().equals(this.getAction()) &&
		p.getTime() == this.getTime()){
	    return true;
	}
	return false;
    }

//    /**
//     * Overridden toString method that prints out each field in the Pattern class.
//     */
//    @Override
//    public String toString() {
//	  StringBuilder result = new StringBuilder();
//	  String newLine = System.getProperty("line.separator");
//
//	  result.append( this.getClass().getName() );
//	  result.append( " Object {" );
//	  result.append(newLine);
//
//	  //determine fields declared in this class only (no fields of superclass)
//	  Field[] fields = this.getClass().getDeclaredFields();
//
//	  //print field names paired with their values
//	  for ( Field field : fields  ) {
//	    result.append("  ");
//	    try {
//	      result.append( field.getName() );
//	      result.append(": ");
//	      //requires access to private field:
//	      result.append( field.get(this) );
//	    } catch ( IllegalAccessException ex ) {
//	      System.out.println(ex);
//	    }
//	    result.append(newLine);
//	  }
//	  result.append("}");
//
//	  return result.toString();
//    }
//    
  //getters & setters

    public String getAction() {
	return this.action;
    }
    

    public String getActionCategory() {
	return this.actionCategory;
    }

    public long getActualTime() {
	return this.actualTime;
    }

    public String getData() {
	return this.mData;
    }
    
    public int getDay() {
	return this.day;
    }

    public int getId() {
        return this.id;
    }

    public String getLocation() {
	return this.location;
    }
    
    public int getStatusCode() {
	return this.statusCode;
    }
    
    public int getTime() {
	return this.time;
    }

    public double getWeight() {
	return this.weight;
    }
    
    public String getWifi() {
	return this.wifi;
    }

    public void setAction(String input){
	this.action = input;
    }
    
    public void setActionCategory(String input){
	this.actionCategory = input;
    }

    public void setActualTime(long l){
	this.actualTime = l;
    }
    
    public void setData(String input){
	this.mData = input;
    }

    public void setDay(int weekDay){
	this.day = weekDay;
    }
    
    public void setId(int input){
	this.id = input;
    }

    public void setLocation(String input){
	this.location = input;
    }
    
    public void setStatusCode(int input){
	this.statusCode = input;
    }

    public void setTime(int input){
	this.time = input;
    }
    
    public void setWeight(double input){
	this.weight = input;
    }
    
    public void setWifi(String input){
	this.wifi = input;
    }

    @Override
    public String toString() {
	return "Pattern [id=" + id + ", actionCategory=" + actionCategory
		+ ", action=" + action + ", time=" + time + ", actualTime="
		+ actualTime + ", day=" + day + ", location=" + location
		+ ", wifi=" + wifi + ", mData=" + mData + ", weight=" + weight
		+ ", statusCode=" + statusCode + "]";
    }
}