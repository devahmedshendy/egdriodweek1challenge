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
        }
    }

    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void resume() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    public void release() {
        mMediaPlayer.reset();
        mMediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        release();
    }
}
