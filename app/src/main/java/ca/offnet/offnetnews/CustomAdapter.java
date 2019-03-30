package ca.offnet.offnetnews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static List<String> titles;
    public static String source;
    public static String feed;
    public static final String SOURCE = "ca.offnet.offnetnews.CustomAdapter.SOURCE";
    public static final String FEED = "ca.offnet.offnetnews.CustomAdapter.FEED";
    public static final String STORY = "ca.offnet.offnetnews.CustomAdapter.STORY";
    public static final String STORY_NUM = "ca.offnet.offnetnews.CustomAdapter.STORY_NUM";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.feed_title);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    if (context instanceof NewsActivity) {
                        Intent intent = new  Intent(context, FeedActiviy.class);
                        intent.putExtra(SOURCE, title.getText().toString());
                        context.startActivity(intent);
                    } else if (context instanceof FeedActiviy) {
                        Intent intent = new Intent(context, StoriesActivity.class);
                        intent.putExtra(SOURCE, source);
                        intent.putExtra(FEED, title.getText().toString());
                        context.startActivity(intent);
                    } else if (context instanceof StoriesActivity) {
                        Intent intent = new Intent(context, SummaryActiviy.class);
                        intent.putExtra(SOURCE, source);
                        intent.putExtra(FEED, feed);
                        String story = title.getText().toString();
                        intent.putExtra(STORY, story);
                        intent.putExtra(STORY_NUM, titles.indexOf(story));
                        context.startActivity(intent);
                    }
                }
            });
        }
        //public TextView getTextView() { return textView; }
    }

    //Set source and feed
    public CustomAdapter(List<String> dataSet, String s, String f) { this.titles = dataSet; this.source = s; this.feed = f;}

    //Set source
    public CustomAdapter(List<String> dataSet, String s) { this.titles = dataSet; this.source = s;}

    public CustomAdapter(List<String> dataSet) { this.titles = dataSet;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row, viewGroup, false);
        if (context instanceof StoriesActivity) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.story_titles, viewGroup, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String title = titles.get(position);
        viewHolder.title.setText(title);
    }

    @Override
    public int getItemCount() { return titles.size(); }
}
