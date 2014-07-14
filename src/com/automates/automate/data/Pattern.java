package com.automates.automate.data;
/**
 * This class defines the structure for a pattern to be entered into the database. Also contains methods to compare for unique patterns.
 * @author Dhanish
 *
 */

public class Pattern {

    
    private int id;
    private String event;
    private String eventCategory;
    private int time;
    private long actualTime;
    private int day;
    private String location;
    private String wifi;
    private String mData;
    private double weekWeight;
    private double weight;
    private int statusCode;
 
    public Pattern(){}
 
    public Pattern(String eventCategory, String event, int time, int actualTime, int day, String location, String wifi, String mData, double weekWeight, double weight, int statusCode) {
        this.eventCategory = eventCategory;
        this.event = event;
        this.time = time;
        this.actualTime = actualTime;
        this.day = day;
        this.location = location;
        this.wifi = wifi;
        this.mData = mData;
        this.weekWeight = weekWeight;
        this.weight = weight;
        this.statusCode = statusCode;
    }
 
    /**
     * Used to check if the unique values of a pattern match with another 
     * @param p The pattern to be compared against.
     * @return a boolean indicating the match.
     */
    public boolean compare(Pattern p){
	if(p.getEventCategory().equals(this.getEventCategory()) &&
		p.getEvent().equals(this.getEvent()) &&
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

    public String getEvent() {
	return this.event;
    }
    

    public String getEventCategory() {
	return this.eventCategory;
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

    public void setEvent(String input){
	this.event = input;
    }
    
    public void setEventCategory(String input){
	this.eventCategory = input;
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

    public double getWeekWeight() {
        return weekWeight;
    }

    public void setWeekWeight(double weekWeight) {
        this.weekWeight = weekWeight;
    }

    @Override
    public String toString() {
	return "Pattern [id=" + id + ", action=" + event + ", actionCategory="
		+ eventCategory + ", time=" + time + ", actualTime="
		+ actualTime + ", day=" + day + ", location=" + location
		+ ", wifi=" + wifi + ", mData=" + mData + ", weekWeight="
		+ weekWeight + ", weight=" + weight + ", statusCode="
		+ statusCode + "]";
    }


}