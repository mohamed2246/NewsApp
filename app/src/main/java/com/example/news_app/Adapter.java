package com.example.news_app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.news_app.Models.Article;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.view_holder> {
    private List<Article> articles;
    public Context context;
    private OnItemClickListener onItemClickListener;

    public Adapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
       return new view_holder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final view_holder holder, int position) {
        Article model = articles.get(position);
       // Picasso.get().load(model.getUrlToImage()).into(holder.imageView);

        RequestOptions requestOptions =new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context).load(model.getUrlToImage()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).transition(DrawableTransitionOptions.withCrossFade())

                .into(holder.imageView);

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.author.setText(model.getAuthor());

        holder.published_ad.setText(model.getPublishedAt());
        holder.source.setText(Utils.DateFormat(model.getPublishedAt()));
        holder.time.setText(" \u2022 " + Utils.DateToTimeFormat(model.getPublishedAt()) );




    }



    @Override
    public int getItemCount() {
        return articles.size();
    }

    class view_holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, description, author, published_ad, source, time;
        ImageView imageView;
        ProgressBar progressBar;
        OnItemClickListener onItemClickListener;

        public view_holder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.desc);
            author = itemView.findViewById(R.id.athor);
            published_ad = itemView.findViewById(R.id.published_at);
            source = itemView.findViewById(R.id.source);
            time = itemView.findViewById(R.id.time);
            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.progress);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }




    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    void set_on_item_click_lisner(OnItemClickListener on_item_click_lisner) {
        this.onItemClickListener = on_item_click_lisner;
    }

}
