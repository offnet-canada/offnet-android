package ca.offnet.offnetnews;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private ImageView BackImg;
    private TextView PhoneNum;
    private Button Update;
    private Button Reset;

    private String PhoneStr;

    public final String SHARED_PREF = "settings";
    public final String PHONE_NUM = "phoneNum";
    public final String CREDITS = "credits";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        PhoneNum = (TextView) findViewById(R.id.PhoneNum);

        Reset = (Button) findViewById(R.id.Reset);
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetCredits();
            }
        });

        Update = (Button) findViewById(R.id.Update);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateNum();
            }
        });

        BackImg = (ImageView) findViewById(R.id.settings_back);
        BackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LoadData();
        UpdateView();
    }

    public void UpdateNum() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString(PHONE_NUM, PhoneNum.getText().toString());
        editor.apply();
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT);
    }

    public void ResetCredits() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CREDITS, 0);
        editor.apply();

    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        PhoneStr = sharedPreferences.getString(PHONE_NUM, getString(R.string.offnet_number));
    }

    public void UpdateView() {
        PhoneNum.setText(PhoneStr);
    }
}
