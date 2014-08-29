package com.automates.automate.sqlite;

import java.util.List;

import com.automates.automate.pattern.Pattern;

public interface PatternManager {

    //CRUD operations
    public abstract void addPattern(Pattern pattern);
    public abstract Pattern getPattern(int id);
    public abstract int updatePattern(Pattern pattern);
    public abstract void deletePattern(Pattern pattern);
    
    //get all
    public abstract List<Pattern> getAllPatterns();

}
