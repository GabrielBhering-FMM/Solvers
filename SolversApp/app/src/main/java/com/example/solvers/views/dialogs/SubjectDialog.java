package com.example.solvers.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.solvers.R;
import com.google.android.material.textfield.TextInputLayout;

public class SubjectDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity activity;
    private OnSendClickListener mSendListener;

    private TextInputLayout txtSubject;
    private Button btSubmit, btCancel;

    public SubjectDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public interface OnSendClickListener{
        void onSendClick(String subject);
    }

    public void setOnSendListener(OnSendClickListener listener){
        this.mSendListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_subject_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txtSubject = findViewById(R.id.textInputLayout);
        btSubmit = findViewById(R.id.bt_send);
        btCancel = findViewById(R.id.bt_cancel);

        btSubmit.setOnClickListener(this);
        btCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_send:
                sendAction();
                break;
            case R.id.bt_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void sendAction(){
        if(txtSubject.getEditText().getText().toString()!=null && !txtSubject.getEditText().getText().toString().equals("")){
            mSendListener.onSendClick(txtSubject.getEditText().getText().toString());
        }else{
            txtSubject.setError("Type a subject");
        }
    }
}