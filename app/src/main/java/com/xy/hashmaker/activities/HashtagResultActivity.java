package com.xy.hashmaker.activities;

import android.content.ClipboardManager;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.xy.hashmaker.R;
import com.xy.hashmaker.activities.base.BaseWithToolBarActivity;
import com.xy.hashmaker.algorithm.Filter;
import com.xy.hashmaker.constants.ExtraKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier Yin on 10/20/16.
 */

public class HashtagResultActivity extends BaseWithToolBarActivity implements View.OnClickListener {
    private Filter filter;

    private ViewGroup selectingHashTagLayout;
    private TextView hashtagResultText;

    private Button clearButton;
    private Button copyButton;

    private String content;
    private ArrayList<String> hashtagResultList;
    private String currentSelectedResult;

//    private String stringYouExtracted;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_hastag_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy_hashtag:
                this.copyText(R.id.action_copy_hashtag);
                return true;

            case android.R.id.home:
                this.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void preProcess() {
        super.preProcess();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this.getApplication());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    protected void receiveIntent() {
        this.content = this.getIntent().getExtras().getString(ExtraKey.EXTRA_KEY_CONTENT);
        this.hashtagResultList = this.getIntent().getExtras().getStringArrayList(ExtraKey.EXTRA_KEY_HASHTAG_LIST);
        this.filter = new Filter(this.hashtagResultList);
    }

    @Override
    protected void findUI() {
        super.findUI();

        this.selectingHashTagLayout = (ViewGroup) this.findViewById(R.id.layout_selecting_hashtag);
        this.hashtagResultText = (TextView) this.findViewById(R.id.text_hashtag_result);

        this.clearButton = (Button) this.findViewById(R.id.button_clear);
        this.copyButton = (Button) this.findViewById(R.id.button_post);
    }

    @Override
    protected void initUI() {
        super.initUI();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                this.clear();
                break;

            case R.id.button_post:
                this.copyText(R.id.button_post);
                break;
        }
    }

    // Methods
    private void addHashtagButtons() {
        this.selectingHashTagLayout.removeAllViews();
        this.showHashtag(true);

        if (this.filter.getOtherHashtags() != null || this.filter.getOtherHashtags().size() > 0) {
            this.showHashtag(false);
        }
    }

    private void showHashtag(boolean isTop5) {
        List<String> results;
        int color;

        if (isTop5) {
            results = this.filter.getTop5Hashtags();
            color = R.color.custom_color_hashtag_top5;
        } else {
            results = this.filter.getOtherHashtags();
            color = R.color.custom_color_hashtag_other;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < results.size(); i++) {
            TextView hashtagButton = (TextView) layoutInflater.inflate(R.layout.component_hashtag_text, null);
            String hashtag = this.getString(R.string.custom_string_hash) + results.get(i);

            hashtagButton.setTextColor(this.getResources().getColor(color));
            hashtagButton.setText(hashtag);
            hashtagButton.setTag(hashtag);
            hashtagButton.setOnClickListener(this.hashtagTextOnClickListener);

            this.selectingHashTagLayout.addView(hashtagButton, layoutParams);
        }
    }

    private void clear() {
        this.hashtagResultText.setText("");
    }

    private void showEmptyToast() {
        Toast.makeText(this, this.getString(R.string.custom_string_hashtag_empty), Toast.LENGTH_LONG).show();
    }

    private void copyText(int viewId) {
        if (TextUtils.isEmpty(this.hashtagResultText.getText().toString())) {
            this.showEmptyToast();
        } else {
            String results;

            switch (viewId) {
                case R.id.action_copy_hashtag:
                    results = hashtagResultText.getText().toString();

                    this.copyToClipboard(results);

                    Toast.makeText(this, this.getString(R.string.custom_string_copy_hashtag), Toast.LENGTH_LONG).show();
                    break;

                case R.id.button_post:
                    StringBuilder stringBuilder = new StringBuilder();
                    results = stringBuilder.append(content).append("\n\n").append(hashtagResultText.getText().toString()).toString();

                    this.copyToClipboard(results);

                    Toast.makeText(this, this.getString(R.string.custom_string_copy_all_to_facebook), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, results);
                    intent.setType("text/plain");
                    startActivity(intent);
                    break;
            }
        }
    }

    private void copyToClipboard(String results) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", results);
        clipboard.setPrimaryClip(clip);
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
