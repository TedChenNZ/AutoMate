package com.automates.automate.service.model;

import android.content.Context;

import com.automates.automate.model.Pattern;
import com.automates.automate.sqlite.PatternDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ted on 1/09/2014.
 */
public class PatternService {
    private static PatternService instance = null;

    private static PatternDB patternDB;
    private static List<Pattern> patternList;

    private PatternService() {}

    public static PatternService getInstance() {
        if (instance == null) {
            instance = new PatternService();
        }
        return instance;
    }

    public static void init(Context context) {
        patternDB = new PatternDB(context);
        patternList = new ArrayList<Pattern>();
        patternList = patternDB.getAllPatterns();
    }

    public boolean add(Pattern p) {
        return patternList.add(patternDB.addPattern(p));
    }

    public boolean remove(Object o) {
        patternDB.deletePattern((Pattern) o);
        return patternList.remove(o);
    }

    public Pattern set(int index, Pattern p) {
        patternDB.updatePattern(p);
        return patternList.set(index, p);
    }

    public Pattern addPattern(Pattern p) {
        Pattern pattern = patternDB.addPattern(p);
        patternList.add(p);
        return pattern;
    }

    public Pattern getPattern(int id) {
        Pattern pattern = null;
        for (Pattern p: patternList) {
            if (p.getId() == id) {
                pattern = p;
                break;
            }
        }

        return pattern;
    }

    public int updatePattern(Pattern pattern) {
        int id = patternDB.updatePattern(pattern);
        for (int i = 0; i < patternList.size(); i++) {
            if (pattern.getId() == patternList.get(i).getId()) {
                patternList.set(i, pattern);
                break;
            }
        }
        return id;
    }
    public List<Pattern> getAllPatterns() {
        return patternList;
    }

}
