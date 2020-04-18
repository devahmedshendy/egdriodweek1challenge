package learn.shendy.egdriodweek1challenge.views.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    private int mWindowHeight;
    private int mWindowWidth;

    // MARK: Lifecycle Methods

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupFragment();
    }

    // MARK: Setup Methods

    private void setupFragment() {
        DisplayMetrics screenMetrics = getDisplayMetrics();

        mWindowHeight = screenMetrics.heightPixels;
        mWindowWidth = screenMetrics.widthPixels;
    }

    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity()
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(metrics);

        return metrics;
    }

    // MARK: Getters/Setters Methods

    int getWindowHeight() {
        return mWindowHeight;
    }

    int getWindowWidth() {
        return mWindowWidth;
    }
}
