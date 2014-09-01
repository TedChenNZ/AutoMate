package com.automates.automate.settings;

import android.content.Context;

import com.automates.automate.locations.LocationTrackerService;
import com.automates.automate.locations.UserLocationService;
import com.automates.automate.pattern.PatternService;
import com.automates.automate.routines.RoutineApplierService;
import com.automates.automate.routines.RoutineService;

/**
 * Initializer is used to initialize services
 * Created by Ted on 1/09/2014.
 */
public class Initializer {
    private static boolean initialized;

    public static void init(Context context) {
        if (RoutineService.getInstance().getAllRoutines() == null) {
            RoutineService.getInstance().init(context);
        }

        if (UserLocationService.getInstance().getAllUserLocations() == null) {
            UserLocationService.getInstance().init(context);
        }

        if (PatternService.getInstance().getAllPatterns() == null) {
            PatternService.getInstance().init(context);
        }

        if (LocationTrackerService.isInstantiated() == false) {
            LocationTrackerService.getInstance().init(context);
            LocationTrackerService.getInstance().getLocation();
        }

        if (RoutineApplierService.isInstantiated() == false) {
            RoutineApplierService.getInstance().init(context);
        }

    }

    public static boolean isInitialized() {
        return initialized;
    }
}
