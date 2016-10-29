package com.xy.hashmaker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xy.hashmaker.R;
import com.xy.hashmaker.activities.HashtagResultActivity;
import com.xy.hashmaker.algorithm.Converter;
import com.xy.hashmaker.algorithm.HashMaker;
import com.xy.hashmaker.fragments.base.BaseFragment;

import java.util.ArrayList;

import static com.xy.hashmaker.constants.ExtraKey.EXTRA_KEY_CONTENT;
import static com.xy.hashmaker.constants.ExtraKey.EXTRA_KEY_HASHTAG_LIST;

/**
 * Created by Xavier Yin on 10/26/16.
 */

public class HashtagFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener {
    private Converter converter = new Converter();
    private HashMaker hashMaker = new HashMaker();

    private ViewGroup mainLayout;
    private ViewGroup progressBarLayout;

    private EditText contentEdit;
    private Button clearButton;
    private Button sendButton;

    private HashMakerHandler hashMakerHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hashtag;
    }

    @Override
    protected void findUI() {
        this.mainLayout = (ViewGroup) this.view.findViewById(R.id.content_main);
        this.progressBarLayout = (ViewGroup) this.view.findViewById(R.id.layout_progress_bar_parent);

        this.contentEdit = (EditText) this.view.findViewById(R.id.edit_content);
        this.clearButton = (Button) this.view.findViewById(R.id.button_clear);
        this.sendButton = (Button) this.view.findViewById(R.id.button_send);
    }

    @Override
    protected void registerEvent() {
        this.clearButton.setOnClickListener(this);
        this.sendButton.setOnClickListener(this);
        this.progressBarLayout.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_clear:
                this.clearContent();
                break;

            case R.id.button_send:
                this.sendContent();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    // Methods
    private void clearContent() {
        this.contentEdit.clearFocus();
        this.contentEdit.setText("");
        this.mainLayout.requestFocus();
    }

    private void sendContent() {
        String currentContent = this.contentEdit.getText().toString();

        if (TextUtils.isEmpty(currentContent)) {
            Toast.makeText(getActivity(), this.getString(R.string.custom_string_toast_empty_message), Toast.LENGTH_LONG).show();
        } else {
            this.progressBarLayout.setVisibility(View.VISIBLE);

            String convertedContent = this.converter.convertToSimplifiedChinese(currentContent);
            this.hashMakerHandler = new HashtagFragment.HashMakerHandler();
            HashtagFragment.HashMakerThread hashMakerThread = new HashtagFragment.HashMakerThread(convertedContent);
            hashMakerThread.start();
        }
    }

    // Inner Classes
    private class HashMakerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            progressBarLayout.setVisibility(View.GONE);

            Bundle bundle = msg.getData();
            String content = contentEdit.getText().toString();
            ArrayList<String> hashtagResult = bundle.getStringArrayList(EXTRA_KEY_HASHTAG_LIST);

            if (hashtagResult.size() > 0) {
                Intent intent = new Intent(getActivity(), HashtagResultActivity.class);
                intent.putExtra(EXTRA_KEY_CONTENT, content);
                intent.putExtra(EXTRA_KEY_HASHTAG_LIST, hashtagResult);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.custom_string_toast_short_message), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class HashMakerThread extends Thread {
        private String convertedContent;

        public HashMakerThread(String convertedContent) {
            this.convertedContent = convertedContent;
        }

        @Override
        public void run() {
            super.run();

            ArrayList<String> hashtagResultList = (ArrayList<String>) hashMaker.makeHashTag(this.convertedContent);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(EXTRA_KEY_HASHTAG_LIST, hashtagResultList);
            message.setData(bundle);

            HashtagFragment.this.hashMakerHandler.sendMessage(message);
        }
    }
}
