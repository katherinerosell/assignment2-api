package edu.quinnipiac.assignment2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
Button b1;
LinearLayout work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        b1=(Button)findViewById(R.id.Button);

        work=(LinearLayout)findViewById(R.id.linearlayout);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                work.setBackgroundColor(Color.BLUE);
            }
        });

    }
}
