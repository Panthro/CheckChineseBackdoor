package panthro.github.com.chinesebackdoordetector;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final List<String> MALWARES = Arrays.asList("com.adups.fota.sysoper", "com.adups.fota");
    private TextView textView;
    private AnimatedCircleLoadingView animatedCircleLoadingView;
    private View button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);

        textView = (TextView) findViewById(R.id.textCheck);

        button = findViewById(R.id.buttonCheck);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatedCircleLoadingView.resetLoading();
                animatedCircleLoadingView.startDeterminate();
                new CheckTask().execute((Void[]) null);

            }
        });
    }

    public class CheckTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            final PackageManager pm = getPackageManager();
            //get a list of installed apps.
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);


            for (int i = 0; i < packages.size(); i++) {
                ApplicationInfo packageInfo = packages.get(i);
                if (MALWARES.contains(packageInfo.packageName)) {
                    return true;
                }
                publishProgress((int) ((i / (float) packages.size()) * 100));

            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            animatedCircleLoadingView.setPercent(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean detected) {
            if (detected) {
                textView.setText(R.string.detected);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorError));
                animatedCircleLoadingView.stopFailure();
            } else {
                textView.setText(R.string.clean);
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorClean));
                animatedCircleLoadingView.stopOk();
            }
            button.setEnabled(false);
        }
    }


}
