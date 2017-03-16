package com.group.neusoft.moviesurfer.coco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.group.neusoft.moviesurfer.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar= (Toolbar) findViewById(R.id.settings_toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((CheckBox)findViewById(R.id.setting_loadimg)).setChecked(Settings.isListloadPic());
        ((CheckBox)findViewById(R.id.setting_loadimg)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Settings.setListloadPic(b);
            }
        });
    }
}
