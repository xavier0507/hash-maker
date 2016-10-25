package com.xy.hashmaker.utils;

import android.util.Log;

import com.xy.hashmaker.BuildConfig;

/**
 * Created by Xavier on 2015/2/10.
 */
public class Logger {
    private static boolean enable = true;

    private String tag;

    private Logger(String tag) {
        this.tag = tag;
    }

    public static synchronized Logger getInstance(Class<?> clazz) {
        String tag = ">> " + clazz.getName();

        return new Logger(tag);
    }

    public String getTag() {
        return tag;
    }

    public void v(String message) {
        if (BuildConfig.DEBUG) {
            Log.v(this.tag, message);
        }
    }

    public void v(String message, Throwable t) {
        if (BuildConfig.DEBUG) {
            Log.v(this.tag, message, t);
        }
    }

    public void d(String message) {
        if (BuildConfig.DEBUG) {
            if (enable) {
                if (message.length() > 4000) {
                    Log.d(this.tag, message.substring(0, 4000));
                    d(message.substring(4000));
                } else {
                    Log.d(this.tag, message);
                }
            }
        }
    }

    public void d(String message, Throwable t) {
        if (BuildConfig.DEBUG) {
            if (enable) {
                Log.d(this.tag, message, t);
            }
        }
    }

    public void i(String message) {
        if (BuildConfig.DEBUG) {
            if (enable) {
                Log.i(this.tag, message);
            }
        }
    }

    public void i(String message, Throwable t) {
        if (BuildConfig.DEBUG) {
            if (enable) {
                Log.i(this.tag, message, t);
            }
        }
    }

    public void w(String message) {
        if (BuildConfig.DEBUG) {
            if (enable) {
                Log.w(this.tag, message);
            }
        }
    }

    public void w(String message, Throwable t) {
        if (BuildConfig.DEBUG) {
            if (enable) {
                Log.w(this.tag, message, t);
            }
        }
    }

    public void e(String message) {
        if (BuildConfig.DEBUG) {
            if (enable) {
                Log.e(this.tag, message);
            }
        }
    }

    public void e(String message, Throwable t) {
        if (BuildConfig.DEBUG) {
            if (enable) {
                Log.e(this.tag, message, t);
            }
        }
    }
}
