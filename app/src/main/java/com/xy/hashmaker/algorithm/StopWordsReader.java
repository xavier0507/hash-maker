package com.xy.hashmaker.algorithm;

import android.content.res.Resources;

import com.xy.hashmaker.R;
import com.xy.hashmaker.activities.application.HashMakerApplication;
import com.xy.hashmaker.utils.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Xavier Yin on 10/19/16.
 */

public class StopWordsReader {
    private Logger logger = Logger.getInstance(this.getClass());

    public Set<String> getStopWords() {
        Set<String> stopWordSet = new HashSet<>();
        String stopWordsContent = this.readStopWordsFile();
        String[] stopWords = stopWordsContent.split("\\n");

        for (String word : stopWords) {
            stopWordSet.add(word);
        }

        return stopWordSet;
    }

    public String readStopWordsFile() {
        String content = "";
        Resources resources = HashMakerApplication.getAppResources();
        InputStream is = null;

        try {
            is = resources.openRawResource(R.raw.stop_words);
            byte buffer[] = new byte[is.available()];

            is.read(buffer);
            content = new String(buffer);
        } catch (IOException e) {
            logger.e("write file: ", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.e("close file: ", e);
                }
            }
        }

        return content;
    }
}
