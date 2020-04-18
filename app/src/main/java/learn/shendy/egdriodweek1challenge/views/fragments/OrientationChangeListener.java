package learn.shendy.egdriodweek1challenge.views.fragments;

import android.content.Context;
import android.view.OrientationEventListener;

import learn.shendy.egdriodweek1challenge.views.MainActivity;

import static learn.shendy.egdriodweek1challenge.views.fragments.OrientationChangeListener.OrientationMode.LANDSCAPE;
import static learn.shendy.egdriodweek1challenge.views.fragments.OrientationChangeListener.OrientationMode.PORTRAIT;
import static learn.shendy.egdriodweek1challenge.views.fragments.OrientationChangeListener.OrientationMode.UNKNOWN;

public class OrientationChangeListener extends OrientationEventListener {
    private static final String TAG = "OrientationChangeListen";

    private OrientationMode mCurrentOrientation;

    private static OrientationChangeListener INSTANCE;

    public static OrientationChangeListener singletonInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new OrientationChangeListener(context);
        }

        return INSTANCE;
    }

    public static void enable(Context context) {
        if (INSTANCE == null) {
            singletonInstance(context);
        }

        INSTANCE.enable();
    }

    private OrientationChangeListener(Context context) {
        super(context);
    }

    @Override
    public void onOrientationChanged(int orientationDegree) {
        OrientationMode newOrientationMode = mapToOrientationMode(orientationDegree);

        if (mCurrentOrientation == null) {
            updateCurrentOrientation(newOrientationMode);
            return;
        }

        if (isOrientationChanged(newOrientationMode)) {
            updateCurrentOrientation(newOrientationMode);
        }
    }

    private OrientationMode mapToOrientationMode(int orientation) {

        if (
                orientation >= 301
                || (orientation >= 0 && orientation <= 50)
                || (orientation >= 131 && orientation <= 190)
        ) {
            return PORTRAIT;
        }

        if (
                (orientation >= 51 && orientation <= 130)
                || orientation >= 191
        ) {
            return LANDSCAPE;
        }

        return UNKNOWN;
    }

    private void updateCurrentOrientation(OrientationMode changedOrientation) {
        mCurrentOrientation = changedOrientation;

        MainActivity
                .sOrientationListenerSubject
                .onNext(mCurrentOrientation);
    }

    private boolean isOrientationChanged(OrientationMode newOrientationMode) {
        return (mCurrentOrientation == PORTRAIT && newOrientationMode == LANDSCAPE)
                || (mCurrentOrientation == LANDSCAPE && newOrientationMode == PORTRAIT);
    }

    private boolean isPortrait(OrientationMode mode) {
        return mode == PORTRAIT;
    }

    public enum OrientationMode {
        UNKNOWN,
        PORTRAIT,
        LANDSCAPE
    }
}
