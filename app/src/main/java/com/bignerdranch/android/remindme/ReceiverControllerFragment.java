package com.bignerdranch.android.remindme;

import android.app.Activity;

/**
 * Created by annika on 2017-08-18.
 */

public class ReceiverControllerFragment extends android.support.v4.app.Fragment {

    protected ReceiverController receiverController;

    protected interface ReceiverController {
        void onUpdateStore();
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);

        try {
            receiverController = (ReceiverControllerFragment.ReceiverController) a;

        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString() + " does not implement ReceiverController interface");
        }
    }

}
