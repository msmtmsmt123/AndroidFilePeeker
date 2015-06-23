package com.cyanflxy.peekerui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 输入对话框
 * Created by XiaYuqiang on 2015/6/23.
 */
public class InputDialog extends Dialog {
    public interface OnTextResultListener {
        void onTextResult(String result);
    }


    private String mTitleString;
    private TextView mTitleView;

    private OnTextResultListener mTextResultListener;

    public InputDialog(Context context) {
        super(context, R.style.dialog_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_dialog_layout);

        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(mTitleString);

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mTextResultListener != null) {
                    EditText editText = (EditText) findViewById(R.id.edit_text);
                    mTextResultListener.onTextResult(editText.getText().toString());
                }
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setTitle(int titleId) {
        mTitleString = getContext().getString(titleId);
    }

    public void setTitle(String title) {
        mTitleString = title;
    }

    public void setOnTextResultListener(OnTextResultListener l) {
        mTextResultListener = l;
    }
}
