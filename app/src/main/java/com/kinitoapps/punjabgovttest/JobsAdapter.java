package com.kinitoapps.punjabgovttest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by HP INDIA on 14-Sep-18.
 */

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {
    private Context mCtx;
    private List<Jobs> jobsList;
    public JobsAdapter(Context mCtx,List<Jobs> jobsList){
        this.mCtx = mCtx;
        this.jobsList = jobsList;
    }
    @NonNull
    @Override
    public JobsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.single_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Jobs jobs = jobsList.get(position);
        holder.textViewName.setText(jobs.getName());
        holder.textViewCompanyName.setText(jobs.getCompanyName());
        Glide.with(mCtx)
                .load(jobs.getLogo())
                .listener(new RequestListener<Drawable>() {
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
                })
                .apply(new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_no_logo))
                .into(holder.imageViewLogo);



    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewCompanyName;
        ImageView imageViewLogo;
        ProgressBar progressBar;

        public ViewHolder(final View itemView) {
            super(itemView);
            textViewCompanyName = itemView.findViewById(R.id.company_name);
            textViewName = itemView.findViewById(R.id.job_name);
            imageViewLogo = itemView.findViewById(R.id.imageView2);
            progressBar = itemView.findViewById(R.id.progress);

        }
    }
}
