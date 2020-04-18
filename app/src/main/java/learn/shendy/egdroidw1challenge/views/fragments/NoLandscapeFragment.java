package learn.shendy.egdroidw1challenge.views.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import learn.shendy.egdroidw1challenge.R;
import learn.shendy.egdroidw1challenge.utils.AnimUtils;
import learn.shendy.egdroidw1challenge.views.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoLandscapeFragment extends BaseFragment {
    public static final String TAG = "LandscapeNotSupportedFr";

    public static final String PREFERENCE_KEY = "dismissNoLandscapeDialog";

    private CardView mCardView;
    private Button mDismissBtn;
    private TextView mMessageTV;

    private AnimatorSet mCurrentAnimation;

    // MARK: Constructor Methods

    public NoLandscapeFragment() {
        // Required empty public constructor
    }

    // MARK: Lifecycle Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_no_landscape, container, false);

        mCardView = rootView.findViewById(R.id.no_landscape_card);
        mDismissBtn = rootView.findViewById(R.id.no_landscape_dismiss_btn);
        mMessageTV = rootView.findViewById(R.id.no_landscape_message_tv);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupFragment();
        setupEnterAnimation();
        startEnterAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseCurrentAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeCurrentAnimation();
    }

    // MARK: Setup Methods

    private void setupFragment() {
        mCurrentAnimation = new AnimatorSet();
        mDismissBtn.setOnClickListener(dismiss);
    }

    private void setupEnterAnimation() {
        mCurrentAnimation.play(attentionEnter());
    }

    private AnimatorSet attentionEnter() {
        ObjectAnimator attentionStep1 = AnimUtils.createAttentionEnterStep1Animator(mCardView);
        ObjectAnimator attentionStep2 = AnimUtils.createAttentionEnterStep2Animator(mCardView);
        ObjectAnimator attentionStep3 = AnimUtils.createAttentionEnterStep3Animator(mCardView);

        attentionStep1.setDuration(300);
        attentionStep2.setDuration(200);
        attentionStep3.setDuration(100);

        AnimatorSet attentionEnter = new AnimatorSet();

        attentionEnter.playSequentially(
                attentionStep1,
                attentionStep2,
                attentionStep3
        );

        return attentionEnter;
    }

    private void setupLeaveAnimation() {
        mCurrentAnimation.play(attentionLeave());

        mCurrentAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                closeFragment();
            }
        });
    }

    private AnimatorSet attentionLeave() {
        ObjectAnimator attentionStep1 = AnimUtils.createAttentionLeaveStep1Animator(mCardView);
        ObjectAnimator attentionStep2 = AnimUtils.createAttentionLeaveStep2Animator(mCardView);

        attentionStep1.setDuration(300);
        attentionStep2.setDuration(200);

        AnimatorSet attentionLeave = new AnimatorSet();

        attentionLeave.playSequentially(
                attentionStep1,
                attentionStep2
        );

        return attentionLeave;
    }
    
    // MARK: Start/Stop/Resume/Pause Animation Methods

    private void startEnterAnimation() {
        mCurrentAnimation.start();
    }
    
    private void startLeaveAnimation() {
        mCurrentAnimation.start();
    }

    private void resumeCurrentAnimation() {
        if (mCurrentAnimation.isPaused()) {
            mCurrentAnimation.resume();
        }
    }

    private void pauseCurrentAnimation() {
        if (mCurrentAnimation.isRunning()) {
            mCurrentAnimation.pause();
        }
    }

    private void resetCurrentAnimation() {
        mCurrentAnimation = new AnimatorSet();
    }

    // MARK: Listener Methods

    private OnClickListener dismiss = btn -> {
        updateDismissPreferenceKey();
        resetCurrentAnimation();
        setupLeaveAnimation();
        startLeaveAnimation();
    };

    private void updateDismissPreferenceKey() {
        getActivity()
                .getSharedPreferences(MainActivity.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(PREFERENCE_KEY, true)
                .apply();
    }

    private void closeFragment() {
        getActivity().onBackPressed();
    }
}
