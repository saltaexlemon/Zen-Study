package com.example.zenstudy;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppLifeCycle extends Application implements Application.ActivityLifecycleCallbacks {

    private int activityReferences = 0;

    @Override
    public void onCreate(){
        super.onCreate();
        registerActivityLifecycleCallbacks(this);

    }
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        activityReferences++;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        activityReferences--;


        if (activityReferences == 0 && HomeMain.isPaused == false){
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
