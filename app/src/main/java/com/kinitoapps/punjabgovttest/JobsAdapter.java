package com.kinitoapps.punjabgovttest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

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


    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewCompanyName;

        public ViewHolder(final View itemView) {
            super(itemView);
            textViewCompanyName = itemView.findViewById(R.id.company_name);
            textViewName = itemView.findViewById(R.id.job_name);
        }
    }
}
