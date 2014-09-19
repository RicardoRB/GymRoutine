package com.ricardorb.drawer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ricardorb.gymroutine.R;

public class About extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView txtVersion = (TextView) findViewById(R.id.textVersionNum);
            txtVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("tag", e.getMessage());
        }

        final Button facebook = (Button) findViewById(R.id.btnFacebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ricardoromebeni")));
            }
        });

        final Button twitter = (Button) findViewById(R.id.btnTwitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/DaitoRB")));
            }
        });

        final Button linkedin = (Button) findViewById(R.id.btnLinkedin);

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/pub/ricardo-romero-ben%C3%ADtez/8a/404/b4a")));
            }
        });
    }

}
