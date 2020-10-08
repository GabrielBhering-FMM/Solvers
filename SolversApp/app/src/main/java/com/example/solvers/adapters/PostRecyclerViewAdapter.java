package com.example.solvers.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solvers.R;
import com.example.solvers.models.Post;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.noties.markwon.Markwon;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>{
    public List<Post> postList;
    private OnItemClickListener mClickListener;
    private Context context;

    public PostRecyclerViewAdapter(List<Post> posts, Context context){
        this.postList = posts;
        this.context = context;
    }

    public interface OnItemClickListener{
        void onPostClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName,txtDesc;
        private ImageView imgProfile;
        private Button btAnswer;

        private CardView cardView;

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener){
            super(itemView);

            txtName = itemView.findViewById(R.id.tv_name_plus_date);
            txtDesc = itemView.findViewById(R.id.tv_description);
            imgProfile = itemView.findViewById(R.id.tv_profile);
            btAnswer = itemView.findViewById(R.id.bt_answer);

            cardView = itemView.findViewById(R.id.card_view);
            constraintLayout = itemView.findViewById(R.id.card_view_constraint_layout);

            setEvents(listener);
        }

        public void setEvents(final OnItemClickListener listener){
            constraintLayout.setOnClickListener(view -> {
                if(listener!=null){
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listener.onPostClick(position);
                    }
                }
            });

            btAnswer.setOnClickListener(view -> {
                if(listener!=null){
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listener.onPostClick(position);
                    }
                }
            });

            txtDesc.setOnClickListener(view -> {
                if(listener!=null){
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listener.onPostClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Markwon markwon = Markwon.create(context);

        Post post = postList.get(position);

        //Get the difference between now and post date
        Date now = new Date();
        long diff = now.getTime() - post.getCreatedAt().toDate().getTime();
        long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);

        //Stylizing the TextView in CardView
        if(diffMinutes > 60) {
            long diffHours = TimeUnit.MILLISECONDS.toHours(diff);
            if(diffHours>1) holder.txtName.setText(post.getSubject()+" • "+TimeUnit.MILLISECONDS.toHours(diff)+" hours ago");
            else holder.txtName.setText(post.getSubject()+" • "+TimeUnit.MILLISECONDS.toHours(diff)+" hour ago");
        }else{
            if(diffMinutes!=1) holder.txtName.setText(post.getSubject()+" • "+diffMinutes+" minutes ago");
            else holder.txtName.setText(post.getSubject()+" • "+diffMinutes+" minute ago");
        }

        //Set the Markdown interpreter in TextView
        markwon.setMarkdown(holder.txtDesc, post.getDescription());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
