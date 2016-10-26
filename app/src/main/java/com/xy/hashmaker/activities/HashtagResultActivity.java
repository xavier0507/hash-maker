package com.xy.hashmaker.activities;

import android.content.ClipboardManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xy.hashmaker.BuildConfig;
import com.xy.hashmaker.R;
import com.xy.hashmaker.activities.base.BaseActivity;
import com.xy.hashmaker.algorithm.Filter;
import com.xy.hashmaker.constants.ExtraKey;

import java.util.ArrayList;

/**
 * Created by Xavier Yin on 10/20/16.
 */

public class HashtagResultActivity extends BaseActivity implements View.OnClickListener {
    private Filter filter;

    private Toolbar toolbar;

    private ViewGroup selectingHashTagLayout;
    private TextView hashtagResultText;

    private Button clearButton;
    private Button copyButton;

    private ArrayList<String> hashtagResultList;
    private String currentSelectedResult;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    protected void receiveIntent() {
        this.hashtagResultList = this.getIntent().getExtras().getStringArrayList(ExtraKey.EXTRA_KEY_CONTENT);
        this.filter = new Filter(this.hashtagResultList);
    }

    @Override
    protected void findUI() {
        this.toolbar = (Toolbar) this.findViewById(R.id.toolbar);

        this.selectingHashTagLayout = (ViewGroup) this.findViewById(R.id.layout_selecting_hashtag);
        this.hashtagResultText = (TextView) this.findViewById(R.id.text_hashtag_result);

        this.clearButton = (Button) this.findViewById(R.id.button_clear);
        this.copyButton = (Button) this.findViewById(R.id.button_copy);
    }

    @Override
    protected void initUI() {
        this.toolbar.setLogo(R.drawable.pic_sharp);
        this.toolbar.setTitle(this.getTitle() + " " + "v" + BuildConfig.VERSION_NAME);
        this.toolbar.setTitleTextColor(this.getResources().getColor(R.color.color_general_white_FFFFFF));
        this.setSupportActionBar(toolbar);
    }

    @Override
    protected void registerEvent() {
        this.clearButton.setOnClickListener(this);
        this.copyButton.setOnClickListener(this);
    }

    @Override
    protected void postProcess() {
        super.postProcess();
        this.addHashtagButtons();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_clear:
                hashtagResultText.setText("");
                break;

            case R.id.button_copy:
                String stringYouExtracted = hashtagResultText.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", stringYouExtracted);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(this, "已將hashtag複製！", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void addHashtagButtons() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.selectingHashTagLayout.removeAllViews();

        for (int i = 0; i < this.filter.getTop5Hashtags().size(); i++) {
            TextView hashtagButton = (TextView) layoutInflater.inflate(R.layout.component_hashtag_text, null);
            String hashtag = "#" + this.filter.getTop5Hashtags().get(i);

            hashtagButton.setTextColor(this.getResources().getColor(R.color.custom_color_hashtag_top5));
            hashtagButton.setText(hashtag);
            hashtagButton.setTag(hashtag);
            hashtagButton.setOnClickListener(this.hashtagTextOnClickListener);

            this.selectingHashTagLayout.addView(hashtagButton, layoutParams);
        }

        if (this.filter.getOtherHashtags() != null || this.filter.getOtherHashtags().size() > 0) {
            for (int i = 0; i < this.filter.getOtherHashtags().size(); i++) {
                TextView hashtagButton = (TextView) layoutInflater.inflate(R.layout.component_hashtag_text, null);
                String hashtag = "#" + this.filter.getOtherHashtags().get(i);

                hashtagButton.setTextColor(this.getResources().getColor(R.color.custom_color_hashtag_other));
                hashtagButton.setText(hashtag);
                hashtagButton.setTag(hashtag);
                hashtagButton.setOnClickListener(this.hashtagTextOnClickListener);

                this.selectingHashTagLayout.addView(hashtagButton, layoutParams);
            }
        }
    }

    // Events
    private View.OnClickListener hashtagTextOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String content = (String) view.getTag();
            currentSelectedResult = hashtagResultText.getText().toString();

            logger.d("selectedResult: " + currentSelectedResult);

            if (TextUtils.isEmpty(currentSelectedResult)) {
                currentSelectedResult = currentSelectedResult + content;
            } else {
                if (!currentSelectedResult.contains(content)) {
                    currentSelectedResult = currentSelectedResult + " " + content;
                }
            }

            logger.d("currentSelectedResult: " + currentSelectedResult);

            hashtagResultText.setText(currentSelectedResult);
        }
    };
}
