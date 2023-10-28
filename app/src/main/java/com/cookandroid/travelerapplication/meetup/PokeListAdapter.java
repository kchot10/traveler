package com.cookandroid.travelerapplication.meetup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.chat.ChatRoomActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;

import java.util.ArrayList;

public class PokeListAdapter extends RecyclerView.Adapter<PokeListAdapter.PokeViewHolder> {

    private ArrayList<PokeItem> arrayList;
    private Context context;

    public PokeListAdapter(ArrayList<PokeItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PokeListAdapter.PokeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poke, parent, false);
        PokeListAdapter.PokeViewHolder holder = new PokeListAdapter.PokeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PokeListAdapter.PokeViewHolder holder, int position) {
        PokeItem item = arrayList.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);

    }

    public class PokeViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePhoto;
        TextView userName;
        ImageView userSex;
        TextView userBirth; // 사용자 생년월일 TextView 추가
        TextView meetupSuccessNum;
        TextView meetupFailNum;
        TextView oneLineReview;
        TextView brithDate;
        Button acceptBtn;

        public PokeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profilePhoto = itemView.findViewById(R.id.profilePhoto);
            this.userName = itemView.findViewById(R.id.userName);
            this.userSex = itemView.findViewById(R.id.userSex);
            this.userBirth = itemView.findViewById(R.id.userBirth); // 추가된 부분
            this.meetupSuccessNum = itemView.findViewById(R.id.meetupSuccessNum);
            this.meetupFailNum = itemView.findViewById(R.id.meetupFailNum);
            this.oneLineReview = itemView.findViewById(R.id.oneLineReview);
            this.acceptBtn = itemView.findViewById(R.id.acceptBtn);
            this.brithDate = itemView.findViewById(R.id.userBirth);
            this.profilePhoto.setClipToOutline(true);

            this.acceptBtn.setOnClickListener(v -> {
                FileHelper fileHelper = new FileHelper(context);
                fileHelper.writeToFile("user_id", "1");
                fileHelper.writeToFile("nickname", "테스트");

                int curpos = getAbsoluteAdapterPosition();
                Intent intent;
                intent = new Intent(context, ChatRoomActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("request_user_id", arrayList.get(curpos).getRequest_user_id());
                intent.putExtra("meet_up_post_id", arrayList.get(curpos).getMeet_up_post_id());
                intent.putExtra("first_chat", true);
                context.startActivity(intent);
            });
        }

        public void setItem(PokeItem item){
            userName.setText(item.getUserName());
            meetupSuccessNum.setText("매칭 성공 "+item.getMeetupSuccessNum()+"회");
            meetupFailNum.setText("매칭 실패 "+item.getMeetupFailNum()+"회");
            oneLineReview.setText(item.getOneLineReview());

            String birthDate = item.getBirthDate();
            if(!birthDate.equals("null")){
                brithDate.setText(birthDate);
            }

            String sex = item.getUserSex();
            if(sex.isEmpty() || sex == null || sex.equals("")){

            } else if(sex.equals("male")){
                Glide.with(context)
                        .load(R.drawable.man_icon)
                        .into(userSex);
            } else if(sex.equals("female")){
                Glide.with(context)
                        .load(R.drawable.woman_icon)
                        .into(userSex);
            }

            String image_url = item.getImageResource();
            if(!(image_url == null || image_url.equals("null") || image_url.isEmpty() || image_url.equals(""))) {
                Glide.with(context)
                        .load(image_url)
                        .into(profilePhoto);
            }
        }
    }


}