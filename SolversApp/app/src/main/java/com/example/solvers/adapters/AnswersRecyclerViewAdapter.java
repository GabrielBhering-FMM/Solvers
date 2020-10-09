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
import com.example.solvers.models.Answer;
import com.example.solvers.models.Post;
import com.example.solvers.models.User;
import com.example.solvers.utils.MarkwonBuilder;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import io.noties.markwon.Markwon;
import io.noties.markwon.ext.latex.JLatexMathPlugin;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin;

public class AnswersRecyclerViewAdapter extends RecyclerView.Adapter<AnswersRecyclerViewAdapter.ViewHolder> {
    public List<Answer> answerList;
    private AnswersRecyclerViewAdapter.OnItemClickListener mClickListener;
    private Context context;

    private User author;

    public AnswersRecyclerViewAdapter(List<Answer> list,Context context){
        this.answerList = list;
        this.context = context;
    }

    public interface OnItemClickListener{
        void onPostClick(int position);
    }

    public void setOnItemClickListener(AnswersRecyclerViewAdapter.OnItemClickListener listener){
        mClickListener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private AvatarView anwserAuthorImg;
        private TextView answerText,answerTime;

        public ViewHolder(@NonNull View itemView, final AnswersRecyclerViewAdapter.OnItemClickListener listener){
            super(itemView);

            answerText = itemView.findViewById(R.id.answer_text);
            answerTime = itemView.findViewById(R.id.answer_time);
            anwserAuthorImg = itemView.findViewById(R.id.answer_author);
        }
    }

    @NonNull
    @Override
    public AnswersRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item, parent, false);
        AnswersRecyclerViewAdapter.ViewHolder viewHolder = new AnswersRecyclerViewAdapter.ViewHolder(view, mClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersRecyclerViewAdapter.ViewHolder holder, int position) {
        Answer answer = answerList.get(position);
        Log.d("answer", String.valueOf(position));

        answer.getAuthorRef().get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                Map<String,Object> author = doc.getData();
                Log.d("answer_author",author.get("displayName").toString());

                if(author.get("imageUrl")!=null&&!author.get("imageUrl").equals("")){
                    IImageLoader imgLoader = new PicassoLoader();
                    imgLoader.loadImage(holder.anwserAuthorImg, author.get("imageUrl").toString(),author.get("displayName").toString());
                }

                if(doc.exists()){
                    //Get the difference between now and post date
                    Date now = new Date();
                    long diff = now.getTime() - answer.getCreatedAt().toDate().getTime();
                    long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);

                    //Stylizing the TextView in CardView
                    if(diffMinutes > 60) {
                        long diffHours = TimeUnit.MILLISECONDS.toHours(diff);
                        String time = diffHours+"h";
                        Log.d("time",time);
                        holder.answerTime.setText(time);
                    }else{
                        String time = diffMinutes+"min";
                        if(diffMinutes==0) holder.answerTime.setText("now");
                        else holder.answerTime.setText(time);
                    }

                    String text = "**"+doc.getData().get("displayName").toString()+"** "+answer.getText();

                    final Markwon markwon = MarkwonBuilder.build(context, holder.answerText.getTextSize());
                    markwon.setMarkdown(holder.answerText, text);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }
}
