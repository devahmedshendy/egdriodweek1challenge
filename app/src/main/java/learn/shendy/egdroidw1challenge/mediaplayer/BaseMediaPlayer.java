package learn.shendy.egdroidw1challenge.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public abstract class BaseMediaPlayer implements OnCompletionListener {
    private static final String TAG = "BaseMediaPlayer";

    private MediaPlayer mMediaPlayer;

    BaseMediaPlayer(Context context, int mediaResId) {
        mMediaPlayer = MediaPlayer.create(context, mediaResId);
        mMediaPlayer.setOnCompletionListener(this);
    }

    public void start() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            Log.d(TAG, "start: done");
        }
    }

    public void pause() {
        Log.d(TAG, "pause: here");
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            Log.d(TAG, "pause: done");
        }
    }

    public void resume() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            Log.d(TAG, "resume: done");
        }
    }

    public void release() {
        mMediaPlayer.reset();
        mMediaPlayer.release();
        Log.d(TAG, "release: done");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        release();
    }
}
