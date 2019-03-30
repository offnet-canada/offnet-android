package ca.offnet.offnetnews;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import javax.xml.transform.Source;

public class SummaryActiviy extends AppCompatActivity {
    private TextView title;
    private TextView summary;
    private int storyNum;
    private String jsonReturned = "";
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //Grabing context intent info
        Intent intent = getIntent();
        String source = intent.getStringExtra(CustomAdapter.SOURCE);
        String feed = intent.getStringExtra(CustomAdapter.FEED);
        String story = intent.getStringExtra(CustomAdapter.STORY);
        storyNum = intent.getIntExtra(CustomAdapter.STORY_NUM, 0);
        storyNum = storyNum+1;

        title = findViewById(R.id.story_title);
        summary = findViewById(R.id.story_summary);

        title.setText(story);

        //Prevent double send
        if (source != null && feed != null) {
            requestData(source, feed, storyNum);
        }

        //Loading
        progress = new ProgressDialog(SummaryActiviy.this);
        progress.setTitle("Loading");
        progress.setMessage("Waiting to receive SMS ...");
        progress.setCancelable(false);
        progress.show();

        prepareSummaryData();
    }

    private void prepareSummaryData() {
        //Listen for message
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                jsonReturned += messageText;
                String s = "";
                try {
                    s = new String(Base64.decode(jsonReturned, Base64.DEFAULT));
                } catch (IllegalArgumentException e) {
                    Log.w("Invalid base64", s);
                }
                Log.w("JSON SUMMARY", s);
                if (isJSONValid(s)) {
                    progress.hide();
                    try {
                        JSONObject jObject = new JSONObject(s);
                        if (jObject.has("error")) {
                            ErrorDialog dialog = new ErrorDialog(SummaryActiviy.this, jObject.getString("code"), jObject.getString("error"));
                        } else {
                            summary.setText(jObject.getString(""+storyNum));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(SummaryActiviy.this, "JSON ERROR", Toast.LENGTH_LONG).show();
                    }
                    jsonReturned = ""; //Reset
                }
            }
        });
    }

    private void requestData(String source, String feed, int story) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.settings), MODE_PRIVATE);
        String phone = sharedPreferences.getString(getString(R.string.settings_phone), getString(R.string.offnet_number));
        //Send sms
        Intent intent=new Intent(SummaryActiviy.this, CustomAdapter.class);
        PendingIntent pi=PendingIntent.getActivity(SummaryActiviy.this, 0, intent,0);
        SmsManager smsManager = SmsManager.getDefault();
        String message = "{\"s\": \"rss\",\"p\": {\"source\": \"" + source + "\",\"feed\": \"" + feed + "\",\"item\":\"" + story + "\"}}";
        message = URLEncoder.encode(message);
        smsManager.sendTextMessage(phone, null, message, pi, null);
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


}
