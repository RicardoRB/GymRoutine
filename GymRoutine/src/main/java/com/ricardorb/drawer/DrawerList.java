package com.ricardorb.drawer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.ricardorb.gymroutine.R;

/**
 * Created by Ricardo Romero on 12/09/2014.
 * @version 1
 * @author Ricardo Romero
 */
public class DrawerList {
    private final String[] emailMain = {"ricardoromebeni@gmail.com"};
    private String subject;
    private Activity activity;

    public DrawerList(Activity activity){
        subject = "GymRoutine - FeedBack";
        this.activity = activity;
    }

    public void sendEmail(String message){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailMain);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        activity.startActivity(Intent.createChooser(emailIntent, "Email "));
    }

    public void sendRate(){
        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.share_text)+ " http://play.google.com/store/apps/details?id=" + activity.getPackageName());
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent, ""));
    }

    public void about(){
        activity.startActivity(new Intent(activity, About.class));
    }
}
