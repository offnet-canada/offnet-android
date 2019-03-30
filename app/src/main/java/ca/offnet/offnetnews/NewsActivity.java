package ca.offnet.offnetnews;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private List<String> titles = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    private ImageView BackImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);

        recyclerView = (RecyclerView) findViewById(R.id.sources);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1);
        //recyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new CustomAdapter(titles);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        BackImg = (ImageView) findViewById(R.id.back);
        BackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        prepareTitleData();
    }

    private void prepareTitleData() {
        String[] sources = getResources().getStringArray(R.array.sources);
        for(int i = 0; i < sources.length; i++) {
            titles.add(sources[i]);
        }
        mAdapter.notifyDataSetChanged();
    }
}
