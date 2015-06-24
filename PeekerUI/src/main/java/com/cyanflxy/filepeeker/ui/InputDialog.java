/*
 * Copyright (C) 2015 CyanFlxy <cyanflxy@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanflxy.filepeeker.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cyanflxy.peekerui.R;

/**
 * 输入对话框
 * Created by CyanFlxy on 2015/6/23.
 */
public class InputDialog extends Dialog {
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

    public interface OnTextResultListener {
        void onTextResult(String result);
    }
}
