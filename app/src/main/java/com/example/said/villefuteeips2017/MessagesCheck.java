package com.example.said.villefuteeips2017;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by idriss on 1/27/18.
 */

public class MessagesCheck extends Thread {
    private String receiver;
    public MessagesCheck(String rec){
        this.receiver=rec;
    }

    public void run(){

    }


}
