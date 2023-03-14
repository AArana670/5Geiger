package com.example.a5geigir;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5geigir.db.Signal;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    private List<Signal> signalList;
    private Context context;

    public ListAdapter(List<Signal> signalList, Context context) {
        this.signalList = signalList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String moment = signalList.get(position).moment;
        holder.signalDate.setText(moment.split(" ")[0]);
        holder.signalTime.setText(moment.split(" ")[1]);
        holder.signalDBm.setText(signalList.get(position).dBm+" dBm");
        holder.signalBar.setProgress(80);

        holder.signalPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MeasurementActivity.class);
                i.putExtra("moment",signalList.get(holder.getAdapterPosition()).moment+"");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return signalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView signalDate;
        private TextView signalTime;
        private TextView signalDBm;
        private ProgressBar signalBar;
        private View signalPanel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            signalDate = (TextView) itemView.findViewById(R.id.signal_date);
            signalTime = (TextView) itemView.findViewById(R.id.signal_time);
            signalDBm = (TextView) itemView.findViewById(R.id.signal_dBm);
            signalBar = (ProgressBar) itemView.findViewById(R.id.signal_bar);
            signalPanel = itemView.findViewById(R.id.signal_panel);
        }
    }

}
