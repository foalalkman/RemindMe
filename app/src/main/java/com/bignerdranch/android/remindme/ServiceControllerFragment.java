package com.bignerdranch.android.remindme;

import android.app.Activity;

/**
 * Created by annika on 2017-08-18.
 */

public abstract class ServiceControllerFragment extends android.support.v4.app.Fragment {

    protected ServiceController serviceController;

    protected interface ServiceController {
        void serviceControl();
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);

        try {
            serviceController = (ServiceController) a;

        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString() + " does not implement ServiceController interface");
        }
    }

}
