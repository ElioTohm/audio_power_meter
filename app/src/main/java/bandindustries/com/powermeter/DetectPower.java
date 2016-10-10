package bandindustries.com.powermeter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetectPower extends AppCompatActivity {
    //initializing maxamplitude 40000 is the average among commun devices
    static int maxamplitude = 40000;
    static int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    TextView result;
    SoundMeter soundMeter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_power);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        result = (TextView) findViewById(R.id.result);
        /*
        * checking if permission to use audio record is granted
        * */
        checkPermissions();

        /*
        * customizing actionbar
        */
        ViewGroup actionBarLayout = (ViewGroup) this.getLayoutInflater().inflate(R.layout.custom_actionbar_main, null);
        TextView title = (TextView) actionBarLayout.findViewById(R.id.title);
        title.setText(this.getString(R.string.app_name));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(actionBarLayout);


    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

        } else {
            startMic();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    startMic();

                } else {

                    // permission denied
                    result.setText("This App will not run without granting permission to use the micro please" +
                            " relaunch the app to ask you again or grant permission manually");
                }
                return;
            }
        }
    }

    private void startMic() {
        /*
        * creating object
        * setting handler to repeat the get amplitude function to constantly update
        * both text and progress bar since the code run in milliseconds there for running
        * the check every 50 millisecond will be ambivalent to checking 20 times every second
        * */
        soundMeter = new SoundMeter();
        soundMeter.start();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                double decibels = soundMeter.getAmplitude();
                double percentage = (decibels * 100) / maxamplitude;
                progressBar.setProgress((int) percentage);
                result.setText("A: " + String.valueOf(decibels));
                handler.postDelayed(this, 50);
            }
        }, 0);
    }
}