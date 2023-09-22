package com.cookandroid.travelerapplication.recommend;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;

public class PlaceScoreAdapter extends RecyclerView.Adapter<PlaceScoreAdapter.PlaceScoreViewHolder> {
    private ArrayList<PlaceScore> arrayList;
    private Context context;

    //아이템 뷰타입 설정
    private static final int VIEW_TYPE_SPECIAL = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    public PlaceScoreAdapter(ArrayList<PlaceScore> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public PlaceScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == VIEW_TYPE_SPECIAL){
            View view = inflater.inflate(R.layout.item_planning_recommend_item_special, parent, false);
            PlaceScoreViewHolder holder = new PlaceScoreViewHolder(view);
            return holder;
        } else{
            View view = inflater.inflate(R.layout.item_planning_recommend_item, parent, false);
            PlaceScoreViewHolder holder = new PlaceScoreViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceScoreViewHolder holder, int position) {
        holder.placeName.setText(arrayList.get(position).getPlaceName());
        holder.place_score.setText(arrayList.get(position).getScore());
        holder.place_address.setText(arrayList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PlaceScoreViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, place_score,place_address;
        public PlaceScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.textView_recommend_placeName);
            place_score = itemView.findViewById(R.id.textView_recommend_score);
            place_address = itemView.findViewById(R.id.textView_recommend_address);
        }
    }

    @Override
    public int getItemViewType(int position){
        return (position == 0) ? VIEW_TYPE_SPECIAL : VIEW_TYPE_NORMAL;
    }
}