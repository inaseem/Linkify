package softpro.bot;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;

/**
 * Created by Naseem on 11-12-2017.
 * Using Multiple Views Inside The Recycler View, Just To Partition The Content
 */

public class LinksAdapter extends RecyclerView.Adapter{

    private ArrayList<LinkModel> links;
    private Context context;

    public LinksAdapter(Context context,ArrayList<LinkModel> links) {
        this.links = links;
        this.context=context;
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder{
        //Thid ViewHolder Holds The Normal Downloads Element
        public BootstrapButton typeButton;
        public BootstrapButton downloadButton;
        public BootstrapButton qualityLabel;

        public NormalViewHolder(View itemView) {
            super(itemView);
            typeButton=(BootstrapButton)itemView.findViewById(R.id.typeButton);
            downloadButton=(BootstrapButton)itemView.findViewById(R.id.downloadButton);
            qualityLabel=(BootstrapButton)itemView.findViewById(R.id.qualityLabel);
        }
    }

    public static class SeparatedViewHolder extends RecyclerView.ViewHolder{
        //Thid ViewHolder Holds The Seperated Downloads Seperator
        public SeparatedViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class AvailableViewHolder extends RecyclerView.ViewHolder{
        //Thid ViewHolder Holds The Available Downloads Seperator
        public AvailableViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v;
        switch (viewType){
            case Constants.NORMAL:
                v=inflater.inflate(R.layout.link_item,parent,false);
                return new NormalViewHolder(v);
            case Constants.SEPERATED:
                v=inflater.inflate(R.layout.separator,parent,false);
                return new SeparatedViewHolder(v);
            case Constants.AVAILABLE:
                v=inflater.inflate(R.layout.available,parent,false);
                return new AvailableViewHolder(v);
        }
        // set the view's size, margins, paddings and layout parameters
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return links.get(position).getItemType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LinkModel linkModel=links.get(position);
        switch (linkModel.getItemType()){
            case Constants.NORMAL:
                ((NormalViewHolder)holder).typeButton.setText(linkModel.getTypeText());
                ((NormalViewHolder)holder).downloadButton.setText(linkModel.getDownloadText());
                ((NormalViewHolder)holder).qualityLabel.setText(linkModel.getQualityLabel());
                ((NormalViewHolder)holder).typeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkModel.getTypeLink()));
                        context.startActivity(myIntent);
                    }
                });
                ((NormalViewHolder)holder).downloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkModel.getDownloadLink()));
                        context.startActivity(myIntent);
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return links.size();
    }
}
