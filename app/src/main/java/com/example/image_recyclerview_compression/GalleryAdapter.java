package com.example.image_recyclerview_compression;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.Imageholder>
{
    Context ctx;
//    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    ArrayList<String> totalGalleryImagesList = new ArrayList<>();

    public GalleryAdapter(Context ctx, ArrayList<String> totalGalleryImagesList) {
        this.ctx = ctx;
        this.totalGalleryImagesList = totalGalleryImagesList;
    }


//    public GalleryAdapter(Context ctx, ArrayList<Uri> mArrayUri) {
//        this.ctx = ctx;
//        this.mArrayUri = mArrayUri;
//    }

    @Override
    public Imageholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_image, parent, false);
        Imageholder ih = new Imageholder(v);
        return ih;
    }

    @Override
    public void onBindViewHolder(@NonNull Imageholder holder, int position)
    {

        Uri imgUri= Uri.parse(String.valueOf(totalGalleryImagesList.get(position)));
        holder.imageView.setImageURI(imgUri);



    }

    @Override
    public int getItemCount() {
        return totalGalleryImagesList.size();
    }

    public class Imageholder extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public Imageholder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_view);
        }
    }
}
