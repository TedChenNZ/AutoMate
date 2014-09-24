package com.automates.automate.service.pattern;

import com.automates.automate.model.Pattern;

/**
 * This set of variables manages the thresholds and parameters for the learning algorithm.
 * @author Dhanish
 */

public interface WeightManager {

    public final static double initialZeroWeight = 0;
    public final static double initialWeight = 1;
    public final static double suggestedWeight = 2.0;
    
    public final int oneMinute = 60000; //one minute in milliseconds (used for timeDivision calculation)
    public final int timeDivision = 30 * oneMinute;
    
    public Pattern updatePattern();
}
