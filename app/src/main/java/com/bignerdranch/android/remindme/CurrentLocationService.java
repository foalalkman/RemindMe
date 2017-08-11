package com.bignerdranch.android.remindme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by annika on 2017-08-11.
 */

public class CurrentLocationService extends Service {




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
