package ca.offnet.offnetnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FeedActiviy extends AppCompatActivity {
    private List<String> titles = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    private ImageView BackImg;
    private TextView Title;
    private ErrorDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Intent intent = getIntent();
        String source = intent.getStringExtra(CustomAdapter.SOURCE);
        Title = (TextView) findViewById(R.id.feed_title);
        Title.setText(source);

        recyclerView = (RecyclerView) findViewById(R.id.feeds);

        mAdapter = new CustomAdapter(titles, source);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        BackImg = (ImageView) findViewById(R.id.feed_back);
        BackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prepareTitleData(source);
    }

    private void prepareTitleData(String title) {
        String[] feeds = getResources().getStringArray(getResources().getIdentifier(title,"array", getPackageName()));
        for(int i = 0; i < feeds.length; i++) {
            titles.add(feeds[i]);
        }
        mAdapter.notifyDataSetChanged();
    }

}
