package learn.shendy.egdriodweek1challenge.mediaplayer;

import android.content.Context;

import learn.shendy.egdriodweek1challenge.R;

public class WindowsMediaPlayer extends BaseMediaPlayer {

    public WindowsMediaPlayer(Context context, int audioResId) {
        super(context, audioResId);
    }

    public static WindowsMediaPlayer createShutdownPlayer(Context context) {
        return new WindowsMediaPlayer(context, R.raw.shutdown);
    }

    public static WindowsMediaPlayer createRebootPlayer(Context context) {
        return new WindowsMediaPlayer(context, R.raw.reboot);
    }

    public static WindowsMediaPlayer createHibernatePlayer(Context context) {
        return new WindowsMediaPlayer(context, R.raw.hibernate);
    }

    public static WindowsMediaPlayer createSwitchModeOn(Context context) {
        return new WindowsMediaPlayer(context, R.raw.switch_mode_on);
    }
}
