package com.automates.automate.data;

public interface WeightManager {

    public final static double initialZeroWeight = 0;
    public final static double initialWeight = 1;
    public final static double suggestedWeight = 3;
    
    public Pattern updatePattern();
}
