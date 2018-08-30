package com.example.ngenge.popularmovies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngenge.popularmovies.R;
import com.example.ngenge.popularmovies.models.Trailer;

import java.util.List;

public class TrailersRecyclerAdapter extends RecyclerView.Adapter<TrailersRecyclerAdapter.ViewHolder> {

    private List<Trailer> trailers;


    public TrailersRecyclerAdapter(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.trailerText.setText(String.format("Trailer %s", String.valueOf(position+1)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Trailer trailer = trailers.get(position);
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                try {
                    holder.itemView.getContext().startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    holder.itemView.getContext().startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView playButton;
        public TextView trailerText;

        public ViewHolder(View itemView) {
            super(itemView);
            playButton = itemView.findViewById(R.id.playButton);
            trailerText = itemView.findViewById(R.id.trailerNumber);


        }
    }
}
