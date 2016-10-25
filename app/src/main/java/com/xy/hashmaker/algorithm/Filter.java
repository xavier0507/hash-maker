package com.xy.hashmaker.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier Yin on 10/18/16.
 */

public class Filter {
    private Converter converter;


    private List<String> hashtagResultList;
    private int hashtagResultListSize;

    public Filter(List<String> hashtagResultList) {
        this.converter = new Converter();
        this.hashtagResultList = this.convertedTranditionalChineseHashtagList(hashtagResultList);
        this.hashtagResultListSize = this.hashtagResultList.size();
    }

    public List<String> getAllHashtags() {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < hashtagResultListSize; i++) {
            result.add(this.hashtagResultList.get(i));
        }

        return result;
    }

    public List<String> getTop5Hashtags() {
        List<String> result = new ArrayList<>();

        if (this.hashtagResultListSize <= 5) {
            result.addAll(this.hashtagResultList);
        } else {
            for (int i = 0; i < 5; i++) {
                result.add(this.hashtagResultList.get(i));
            }
        }

        return result;
    }

    public List<String> getOtherHashtags() {
        List<String> result = new ArrayList<>();

        if (this.hashtagResultListSize >= 6 && this.hashtagResultListSize <= 20) {
            for (int i = 5; i < this.hashtagResultListSize; i++) {
                result.add(this.hashtagResultList.get(i));
            }
        } else if (this.hashtagResultListSize >= 21) {
            for (int i = 5; i < 21; i++) {
                result.add(this.hashtagResultList.get(i));
            }
        }

        return result;
    }

    private List<String> convertedTranditionalChineseHashtagList(List<String> hashtagResultList) {
        List<String> result = new ArrayList<>();

        for (String simplifiedChineseHashtag : hashtagResultList) {
            result.add(this.converter.convertToTraditionalChinese(simplifiedChineseHashtag));
        }

        return result;
    }
}
