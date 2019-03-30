package ca.offnet.offnetnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView NewsImg, SettingsImg, WeatherImg;
    private TextView Credits;
    private int used;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherImg = (ImageView) findViewById(R.id.Weather);
        WeatherImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Weather feature is under development", Toast.LENGTH_LONG).show();
            }
        });

        NewsImg = (ImageView) findViewById(R.id.News);
        NewsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewsActivity.class));
            }
        });
        SettingsImg = (ImageView) findViewById(R.id.Settings);
        SettingsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });


        Credits = (TextView) findViewById(R.id.Credits);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.settings), MODE_PRIVATE);
        used = sharedPreferences.getInt(getString(R.string.settings_credits), 0);
        Credits.setText("Credits Used: " + used);
    }
}
