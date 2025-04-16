package br.com.stilldistribuidora.stillrtc;

import android.app.Application;


/**
 * Created by Still Technology and Development Team on 25/06/2017.
 */

public class App extends Application {



    private String someVariable;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }


    public static final String CHANNEL_ID = "exampleServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }


    private void createNotificationChannel() {

    }
}
