package com.automates.automate.sqlite;

import java.util.List;

import com.automates.automate.data.Pattern;

public interface PatternManager {

    //CRUD operations
    public void addPattern(Pattern pattern);
    public Pattern getPattern(int id);
    public int updatePattern(Pattern pattern);
    public void deletePattern(Pattern pattern);
    
    //get all
    public List<Pattern> getAllPatterns();

}
