package com.example.mobileappandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder>{

    private ArrayList<Record> records;
    private Context context;


    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record,parent,false);
        RecordViewHolder recordViewHolder = new RecordViewHolder(v);
        context = parent.getContext();
        return recordViewHolder;
    }

    public RecordAdapter(ArrayList<Record> records){
        this.records=records;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record current_record = records.get(position);
        int record_position = position;
        holder.tv_amount.setText(String.format("%.2f", current_record.getAmount())+"HKD");
        holder.iv_category.setImageResource(current_record.getCategory());
        holder.tv_date.setText(current_record.getDate());
        holder.tv_description.setText(current_record.getDescription());
        if(current_record.isConsume()){
            holder.tv_type.setText(R.string.is_consume);

            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.card_bg_red));
        }
        else{
            holder.tv_type.setText(R.string.is_revenue);
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.card_bg_green));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecordDetailPage.class);
                intent.putExtra("record_info", current_record);
                intent.putExtra("record_position", record_position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_amount;
        public TextView tv_date;
        public TextView tv_description;
        public TextView tv_type;
        public ImageView iv_category;
        public Button bt_remove;
        public CardView card;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_amount=itemView.findViewById(R.id.record_amount);
            iv_category=itemView.findViewById(R.id.record_category);
            bt_remove=itemView.findViewById(R.id.remove_button);
            tv_date=itemView.findViewById(R.id.record_date);
            tv_description=itemView.findViewById(R.id.record_description);
            tv_type=itemView.findViewById(R.id.record_type);
            card=itemView.findViewById(R.id.Card_of_record);
            bt_remove.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int index=getAdapterPosition();
//            Log.d("position to be remove",Integer.toString(getAdapterPosition()));
//            Log.d("records size",Integer.toString(records.size()-1));
            if (index != records.size() - 1) {
                for (int i = index; i < records.size() - 1; i++) {
                    records.set(i, records.get(i + 1));
                }
            }
            records.remove(records.size() - 1);
            SharedPreferences sp=context.getSharedPreferences("Records",context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            Gson gson = new Gson();
            String json=gson.toJson(records);
            editor.putString("record_data",json);
            editor.apply();
            notifyDataSetChanged();
        }
    }
}
