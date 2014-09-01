package com.automates.automate.sqlite;

import com.automates.automate.pattern.Pattern;

import java.util.List;

public interface PatternManager {

    //CRUD operations
    public abstract Pattern addPattern(Pattern pattern);
    public abstract Pattern getPattern(int id);
    public abstract int updatePattern(Pattern pattern);
    public abstract void deletePattern(Pattern pattern);
    
    //get all
    public abstract List<Pattern> getAllPatterns();

}
