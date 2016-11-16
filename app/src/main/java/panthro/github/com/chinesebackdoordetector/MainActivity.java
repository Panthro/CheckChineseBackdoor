package panthro.github.com.chinesebackdoordetector;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final List<String> MALWARES = Arrays.asList("com.adups.fota.sysoper", "com.adups.fota");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.textCheck);

        findViewById(R.id.buttonCheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PackageManager pm = getPackageManager();
                //get a list of installed apps.
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                Set<String> detected = new HashSet<>();


                for (ApplicationInfo packageInfo : packages) {
                    if (MALWARES.contains(packageInfo.packageName)) {
                        detected.add(packageInfo.packageName);
                    }
                    Log.d(TAG, "Installed package :" + packageInfo.packageName);
//                    Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
//                    Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
                }

                if (!detected.isEmpty()) {
                    textView.setText(R.string.detected);
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorError));
                }else{
                    textView.setText(R.string.clean);
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorClean));

                }
            }
        });
    }


}
