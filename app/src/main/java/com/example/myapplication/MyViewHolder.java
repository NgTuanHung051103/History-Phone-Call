package com.example.myapplication;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{
    CardView cardView;
    TextView tv_ph_num, tv_contact_name, tv_call_type, tv_call_date, tv_call_duration;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_ph_num = itemView.findViewById(R.id.layout_call_log_ph_no);
        tv_contact_name = itemView.findViewById(R.id.layout_call_log_contact_name);
        tv_call_type = itemView.findViewById(R.id.layout_call_log_type);
        tv_call_date = itemView.findViewById(R.id.layout_call_log_date);
        tv_call_duration = itemView.findViewById(R.id.layout_call_log_duration);
        cardView = itemView.findViewById(R.id.layout_call_log_cardview);
    }
}