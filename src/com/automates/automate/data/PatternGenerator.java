package com.automates.automate.data;

import com.automates.automate.PhoneState;

public class PatternGenerator {
    
    public PatternGenerator(){}
    
    public Pattern generatePattern(){
	Pattern p = new Pattern();
	
	//p.setActionCategory = '' - w/e Ted's method is
	//p.setAction = use PhoneState to get
	//p.setTime =   //use actual time to transform
	//p.setActualTime = in format 
	//p.setDay = 
	p.setLocation(PhoneState.getLocation().toString());
	p.setWifi(Boolean.toString(PhoneState.isWifiEnabled()));
	p.setData(Boolean.toString(PhoneState.isDataEnabled()));
	
	//TODO weight and statuscode checking
	p.setWeight(1);
	p.setStatusCode(0);
	return p;
	
    }
    
    
    

}
