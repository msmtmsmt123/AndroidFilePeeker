/*
 * Copyright (C) 2015 CyanFlxy <xyufeico@gmail.com>
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

package com.cyanflxy.peekerui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cyanflxy.filepeeker.FilePeeker;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 文件浏览界面
 * Created by CyanFlxy on 2015/6/3.
 */
public class FileBrowserActivity extends Activity implements AdapterView.OnItemClickListener {

    private String mCurrentPath;

    private FileListAdapter mAdapter;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_file_browser);
        mCurrentPath = "/";

        mAdapter = new FileListAdapter();
        mAdapter.setData(FilePeeker.listFiles(mCurrentPath));

        ListView mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = (File) mAdapter.getItem(position);
        if (file.isFile()) {
            // TODO 文件处理办法
        } else {
            enter(file.getName());
        }
    }

    private void enter(String subFolder) {
        mCurrentPath = mCurrentPath + "/" + subFolder;
        mAdapter.setData(FilePeeker.listFiles(mCurrentPath));
    }


    private class FileListAdapter extends BaseAdapter {

        private List<File> mFileList;

        public FileListAdapter() {

        }

        public void setData(File[] fileList) {
            mFileList = Arrays.asList(fileList);
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return mFileList.size();
        }

        @Override
        public Object getItem(int position) {
            return mFileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(FileBrowserActivity.this)
                        .inflate(R.layout.file_browser_list_item, parent, false);
                vh = new ViewHolder();
                vh.mFileName = (TextView) convertView.findViewById(R.id.file_name);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            File file = mFileList.get(position);
            vh.mFileName.setText(file.getName());

            return convertView;
        }
    }

    private class ViewHolder {
        public TextView mFileName;
    }
}
