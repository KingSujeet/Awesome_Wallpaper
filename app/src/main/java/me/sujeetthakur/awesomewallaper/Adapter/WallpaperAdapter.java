package me.sujeetthakur.awesomewallaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.sujeetthakur.awesomewallaper.FullScreenWallpaperActivity;
import me.sujeetthakur.awesomewallaper.Model.WallpaperModel;
import me.sujeetthakur.awesomewallaper.R;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperViewholder> {


    private Context context;
    private List<WallpaperModel> wallpaperModelList;

    public WallpaperAdapter(Context context, List<WallpaperModel> wallpaperModelList) {
        this.context = context;
        this.wallpaperModelList = wallpaperModelList;
    }

    @NonNull
    @Override
    public WallpaperViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.image_item,parent,false);

        return new WallpaperViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewholder holder, final int position) {

        Glide.with(context).load(wallpaperModelList.get(position).getMediumUrl()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, FullScreenWallpaperActivity.class)
                        .putExtra("originalUrl",wallpaperModelList.get(position).getOriginalUrl()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperModelList.size();
    }
}

class WallpaperViewholder extends RecyclerView.ViewHolder {

    ImageView imageView;
    public WallpaperViewholder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageViewItem);
    }
}
