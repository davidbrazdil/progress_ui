package com.example.progressui;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.widget.SeekBar;

public class MainActivity extends Activity {

    private SeekBar viewSeekBar;
    private ProgressCircle viewProgressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewSeekBar = (SeekBar) this.findViewById(R.id.seekBar);
        viewProgressCircle = (ProgressCircle) this.findViewById(R.id.progressCircle);

        viewSeekBar.setMax(100);
        viewSeekBar.setProgress(viewProgressCircle.getValue());
        viewSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewProgressCircle.setValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
