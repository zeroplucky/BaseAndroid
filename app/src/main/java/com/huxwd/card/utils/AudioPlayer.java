/*
 * Copyright (c) 20-6-22 下午2:21
 *               2020
 *               Administrator
 */

package com.huxwd.card.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.camera.core.Logger;

import java.io.IOException;

/*
*

AudioPlayer audioPlayer = new AudioPlayer(getActivity(), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case AudioPlayer.HANDLER_CUR_TIME://更新的时间
                        curPosition = (int) msg.obj;
                        playText.setText(toTime(curPosition) + " / " + toTime(duration));
                        break;
                    case AudioPlayer.HANDLER_COMPLETE://播放结束
                        playText.setText(" ");
                        mIsPlay = false;
                        break;
                    case AudioPlayer.HANDLER_PREPARED://播放开始
                        duration = (int) msg.obj;
                        playText.setText(toTime(curPosition) + " / " + toTime(duration));
                        break;
                    case AudioPlayer.HANDLER_ERROR://播放错误
                        resolveResetPlay();
                        break;
                }

            }
        });


* */

public class AudioPlayer implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    public final static int HANDLER_PREPARED = 2;//装备好了
    public final static int HANDLER_COMPLETE = 0;//完成
    public final static int HANDLER_ERROR = -28;//错误

    private static AudioPlayer instanse;
    private static MediaPlayer mMediaPlayer;
    private Context mContext;

    private Handler mRemoteHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AudioPlayer.HANDLER_COMPLETE://播放结束
                    if (audioPlayerListener != null) {
                        audioPlayerListener.playerComplete();
                    }
                    break;
                case AudioPlayer.HANDLER_PREPARED://播放开始
                    if (audioPlayerListener != null) {
                        audioPlayerListener.playerPrepared();
                    }
                    break;
                case AudioPlayer.HANDLER_ERROR://播放错误
                    if (audioPlayerListener != null) {
                        audioPlayerListener.playerError();
                    }
                    break;
            }
        }
    };


    public static AudioPlayer getInstanse(Context context) {
        if (instanse == null) {
            instanse = new AudioPlayer(context, null);
        }
        return instanse;
    }

    /**
     * 音频播放器
     *
     * @param context 上下文
     */
    private AudioPlayer(Context context, AudioPlayerListener audioPlayerListener) {
        this.audioPlayerListener = audioPlayerListener;
        this.mContext = context;

    }

    public AudioPlayer addlistener(AudioPlayerListener audioPlayerListener) {
        this.audioPlayerListener = audioPlayerListener;
        return this;
    }

    /**
     * @param url url地址
     */
    public int playUrl(String url) {
        try {
            if (mMediaPlayer == null) {
                try {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
                    mMediaPlayer.setOnBufferingUpdateListener(this);
                    mMediaPlayer.setOnPreparedListener(this);
                    mMediaPlayer.setOnCompletionListener(this);
                    mMediaPlayer.setOnErrorListener(this);
                } catch (Exception e) {
                    Log.e("app", Log.getStackTraceString(e));
                }
            }
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(url); // 设置数据源
            mMediaPlayer.prepareAsync(); // prepare自动播放

        } catch (IllegalArgumentException e) {
            Log.e("app", Log.getStackTraceString(e));
            e.printStackTrace();
            return -1;
        } catch (SecurityException e) {
            Log.e("app", Log.getStackTraceString(e));
            e.printStackTrace();
            return -1;
        } catch (IllegalStateException e) {
            Log.e("app", Log.getStackTraceString(e));
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            Log.e("app", Log.getStackTraceString(e));
            e.printStackTrace();
            return -1;
        }
        return 100;
    }

    /**
     * @param audioAsset
     */
    public int playUrl2(String audioAsset) {
        try {

            if (mMediaPlayer == null) {
                try {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
                    mMediaPlayer.setOnBufferingUpdateListener(this);
                    mMediaPlayer.setOnPreparedListener(this);
                    mMediaPlayer.setOnCompletionListener(this);
                    mMediaPlayer.setOnErrorListener(this);
                } catch (Exception e) {
                    Log.e("app", Log.getStackTraceString(e));
                }
            }

            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AssetFileDescriptor assetFileDescriptor = mContext.getAssets().openFd(audioAsset);
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength()); // 设置数据源
            mMediaPlayer.prepareAsync(); // prepare自动播放

        } catch (IllegalArgumentException e) {
            Log.e("app", Log.getStackTraceString(e));
            e.printStackTrace();
            return -1;
        } catch (SecurityException e) {
            Log.e("app", Log.getStackTraceString(e));
            e.printStackTrace();
            return -1;
        } catch (IllegalStateException e) {
            Log.e("app", Log.getStackTraceString(e));
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            Log.e("app", Log.getStackTraceString(e));
            e.printStackTrace();
            return -1;
        }
        return 100;
    }

    public void rePlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    // 暂停
    public void pause() {
        if (mMediaPlayer != null)
            mMediaPlayer.pause();
    }

    // 停止
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void seekTo(int time) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(time);
            mMediaPlayer.start();
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    // 播放准备
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (mRemoteHandler != null) {
            Message msg = new Message();
            msg.obj = mMediaPlayer.getDuration();
            msg.what = 2;
            mRemoteHandler.sendMessageAtTime(msg, 0);
        }
        Log.e("appX", "-------------------------------开始播放");
    }

    // 播放完成
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mRemoteHandler != null) {
            Message msg = new Message();
            msg.what = 0;
            mRemoteHandler.sendMessageAtTime(msg, 0);
        }
    }

    /**
     * 缓冲更新
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        if (mRemoteHandler != null) {
//            Message msg = new Message();
//            msg.what = 0;
//            mRemoteHandler.sendMessageAtTime(msg, 0);
//        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mRemoteHandler != null) {
            Message msg = new Message();
            msg.what = -28;
            mRemoteHandler.sendMessageAtTime(msg, 0);
        }
        Toast.makeText(mContext, "播放失败", Toast.LENGTH_SHORT).show();
        return false;
    }

    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    private AudioPlayerListener audioPlayerListener;

    public interface AudioPlayerListener {

        void playerPrepared();

        void playerComplete();

        void playerError();
    }


    public static class AudioPlayerListenerImpl implements AudioPlayerListener {

        @Override
        public void playerPrepared() {

        }

        @Override
        public void playerComplete() {

        }

        @Override
        public void playerError() {

        }
    }

}