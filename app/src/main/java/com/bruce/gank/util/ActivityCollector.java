package com.bruce.gank.util;


import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    
    private static ActivityCollector sInstance;
    
    private ActivityCollector() {
        mActivityList = new ArrayList<>();
    }
    
    public static ActivityCollector getInsance() {
        if(sInstance == null) {
            synchronized (ActivityCollector.class) {
                if(sInstance == null) {
                    sInstance = new ActivityCollector();
                }
            }
        }
        return sInstance;
    }
    
    private List<Activity> mActivityList;

    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    public Activity getTopActivity() {
        if (mActivityList.isEmpty()) {
            return null;
        } else {
            return mActivityList.get(mActivityList.size() - 1);
        }
    }

    public int size() {
        return mActivityList.size();
    }

    public List<Activity> getList() {
        return mActivityList;
    }

    public boolean isLastActivity() {
        return mActivityList.size() == 1;
    }
}
