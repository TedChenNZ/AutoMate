package com.automates.automate.pattern;

public interface WeightManager {

    public final static double initialZeroWeight = 0;
    public final static double initialWeight = 1;
    public final static double suggestedWeight = 1.5;
    
    public final int oneMinute = 60000; //one minute in milliseconds (used for timeDivision calculation)
    public final int timeDivision = 60 * oneMinute;
    
    public Pattern updatePattern();
}
