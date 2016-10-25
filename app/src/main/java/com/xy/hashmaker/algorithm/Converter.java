package com.xy.hashmaker.algorithm;

import com.spreada.utils.chinese.ZHConverter;

/**
 * Created by Xavier Yin on 10/18/16.
 */

public class Converter {
    private ZHConverter zhConverter;

    public String convertToSimplifiedChinese(String content) {
        this.zhConverter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
        return this.zhConverter.convert(content);
    }

    public String convertToTraditionalChinese(String content) {
        this.zhConverter = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
        return this.zhConverter.convert(content);
    }
}
