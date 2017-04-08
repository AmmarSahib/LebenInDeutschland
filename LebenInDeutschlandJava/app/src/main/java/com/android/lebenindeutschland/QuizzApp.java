package com.android.lebenindeutschland;

/**
 * Created by awitwit on 08.03.17.
 */

import android.app.Application;

public class QuizzApp  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.getDatabase();
    }

}
