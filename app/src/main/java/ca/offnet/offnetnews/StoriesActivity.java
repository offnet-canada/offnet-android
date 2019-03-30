package ca.offnet.offnetnews;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class StoriesActivity extends AppCompatActivity {
    private List<String> titles = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    private String jsonReturned = "";
    private ProgressDialog progress;
    private ImageView BackImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        //Grabing context intent info
        Intent intent = getIntent();
        String source = intent.getStringExtra(CustomAdapter.SOURCE);
        String feed = intent.getStringExtra(CustomAdapter.FEED);

        setTitle(source + " " + feed + " stories");

        //Prevent double send
        if (source != null && feed != null) {
            requestData(source, feed);
        }

        //Gathering recycler info
        recyclerView = (RecyclerView) findViewById(R.id.stories);
        mAdapter = new CustomAdapter(titles, source, feed);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //Add divider line
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
        recyclerView.addItemDecoration(dividerItemDecoration);

        //Loading
        progress = new ProgressDialog(StoriesActivity.this);
        progress.setTitle("Loading");
        progress.setMessage("Waiting to receive SMS ...");
        progress.setCancelable(false);
        progress.show();

        BackImg = (ImageView) findViewById(R.id.stories_back);
        BackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Building recycler view
        prepareTitleData();
    }

    private void prepareTitleData() {
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
                Log.w("JSON DECODING", s);
                if (isJSONValid(s)) {
                    progress.hide();
                    try {
                        JSONObject jObject = new JSONObject(s);
                        if (jObject.has("error")) {
                            ErrorDialog dialog = new ErrorDialog(StoriesActivity.this, jObject.getString("code"), jObject.getString("error"));
                        } else {
                            for (int i = 1; i < 11; i++) {
                                try {
                                    titles.add(jObject.getString(""+i));
                                } catch (JSONException e) {
                                    Toast.makeText(StoriesActivity.this, "JSON COUNTING ERROR", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(StoriesActivity.this, "JSON ERROR", Toast.LENGTH_LONG).show();
                    }
                    jsonReturned = ""; //Reset
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void requestData(String source, String feed) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.settings), MODE_PRIVATE);
        String phone = sharedPreferences.getString(getString(R.string.settings_phone), getString(R.string.offnet_number));
        //Send sms
        Intent intent=new Intent(StoriesActivity.this, CustomAdapter.class);
        PendingIntent pi=PendingIntent.getActivity(StoriesActivity.this, 0, intent,0);
        SmsManager smsManager = SmsManager.getDefault();
        String message = "{\"s\": \"rss\",\"p\": {\"source\": \"" + source + "\",\"feed\": \"" + feed + "\"}}";
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
