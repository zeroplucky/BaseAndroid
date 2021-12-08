package com.huxwd.card.utils;

import android.content.Context;
import android.os.Parcelable;

import com.tencent.mmkv.MMKV;

import java.util.Set;

public class MMKVUtil {

    private static volatile MMKVUtil initialize;
    private final static String cryptKey = "mmvk_data_";

    public static MMKVUtil init(Context context) {
        synchronized (MMKVUtil.class) {
            if (initialize == null) {
                initialize = new MMKVUtil(context);
            }
            return initialize;
        }
    }

    private MMKVUtil(Context context) {
        MMKV.initialize(context);
    }

    private MMKV defaultMMKV() {
        return MMKV.defaultMMKV(1, cryptKey);
    }

    private MMKV mmkvWithID(String spName) {
        return MMKV.mmkvWithID(spName);
    }

    public void putObj(String key, Object value) {
        MMKV mmkv = defaultMMKV();
        if (value instanceof String) {
            mmkv.encode(key, (String) value);
        } else if (value instanceof Integer) {
            mmkv.encode(key, (Integer) value);
        } else if (value instanceof Boolean) {
            mmkv.encode(key, (Boolean) value);
        } else if (value instanceof Float) {
            mmkv.encode(key, (Float) value);
        } else if (value instanceof Long) {
            mmkv.encode(key, (Long) value);
        } else if (value instanceof Parcelable) {
            mmkv.encode(key, (Parcelable) value);
        } else {
            mmkv.encode(key, value.toString());
        }
    }

    public boolean getBool(String key) {
        MMKV mmkv = defaultMMKV();
        return mmkv.decodeBool(key, false);
    }

    public int getInt(String key) {
        MMKV mmkv = defaultMMKV();
        return mmkv.decodeInt(key, -1);
    }

    public long getLong(String key) {
        MMKV mmkv = defaultMMKV();
        return mmkv.decodeLong(key, -1L);
    }

    public float getFloat(String key) {
        MMKV mmkv = defaultMMKV();
        return mmkv.decodeFloat(key, -1f);
    }

    public double getDouble(String key) {
        MMKV mmkv = defaultMMKV();
        return mmkv.decodeDouble(key, -1.00);
    }

    public byte[] getBytes(String key) {
        MMKV mmkv = defaultMMKV();
        return mmkv.decodeBytes(key);
    }

    public String getString(String key) {
        MMKV mmkv = defaultMMKV();
        return mmkv.decodeString(key, "");
    }

    public String getString(String key, String value) {
        MMKV mmkv = defaultMMKV();
        return mmkv.decodeString(key, value);
    }

    public Set<String> getStringSet(String key) {
        MMKV mmkv = defaultMMKV();
        return mmkv.decodeStringSet(key);
    }

    public <T extends Parcelable> T getParcelable(String key, Class<T> clazz) {
        MMKV mmkv = defaultMMKV();
        return mmkv.decodeParcelable(key, clazz);
    }

    public static class KV {
        public static final String isLogin = "isLogin";
        public static final String cardNumber = "cardNumber";
        public static final String teacherName = "teacherName";
    }
}
