package learn.shendy.egdriodweek1challenge.views.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import learn.shendy.egdriodweek1challenge.R;
import learn.shendy.egdriodweek1challenge.databinding.FragmentEndBinding;
import learn.shendy.egdriodweek1challenge.enums.AnimationCommand;
import learn.shendy.egdriodweek1challenge.mediaplayer.WindowsMediaPlayer;
import learn.shendy.egdriodweek1challenge.utils.AnimUtils;

import static learn.shendy.egdriodweek1challenge.utils.AnimUtils.END_LEAVE_DELAY;
import static learn.shendy.egdriodweek1challenge.utils.AnimUtils.END_LEAVE_DURATION;
import static learn.shendy.egdriodweek1challenge.utils.AnimUtils.THANK_YOU_ENTER_DELAY;

public class EndFragment extends BaseFragment {
    public static final String TAG = "EndFragment";

    private static final String END_TYPE_ARG = "ANIMATION_STATUS";

    private AnimationCommand mAnimationCommand;

    private ProgressBar mSpinner;
    private TextView mEndTitleTv;
    private TextView mEndSubtitleTv;
    private TextView mEndThankYouTv;
    private TextView mEndFlowerEmojiTv;

    private AnimatorSet mCurrentAnimation;
    private FragmentEndBinding mFragmentBinding;

    private WindowsMediaPlayer mMediaPlayer;

    // MARK: Constructor Methods

    public EndFragment() {
        // Required empty public constructor
    }

    public static EndFragment newInstance(AnimationCommand status) {
        Bundle args = new Bundle();
        args.putSerializable(END_TYPE_ARG, status);

        EndFragment fragment = new EndFragment();
        fragment.setArguments(args);

        return fragment;
    }

    // MARK: Lifecycle Methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mAnimationCommand = (AnimationCommand) getArguments().getSerializable(END_TYPE_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentBinding = FragmentEndBinding.inflate(inflater, container, false);
        
        mSpinner = mFragmentBinding.endFragmentSpinner;
        mEndTitleTv = mFragmentBinding.endFragmentTitleTv;
        mEndSubtitleTv = mFragmentBinding.endFragmentSubtitleTv;
        mEndThankYouTv = mFragmentBinding.endThankYouTv;
        mEndFlowerEmojiTv = mFragmentBinding.endFlowerEmojiTv;

        return mFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupFragment();
        setupFragmentEnterAnimation();
        startCurrentAnimation();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopCurrentAnimation();
    }

    // MARK: Setup Methods

    private void setupFragment() {
        mCurrentAnimation = new AnimatorSet();

        switch (mAnimationCommand) {
            case RUN_POWER_OFF_ANIMATION:
                mMediaPlayer = WindowsMediaPlayer.createShutdownPlayer(getContext());
                setViews(
                        R.string.power_off,
                        R.string.shutting_down,
                        R.color.powerOff
                );
                break;

            case RUN_REBOOT_ANIMATION:
                mMediaPlayer = WindowsMediaPlayer.createRebootPlayer(getContext());
                setViews(
                        R.string.reboot,
                        R.string.restarting,
                        R.color.reboot
                );
                break;

            case RUN_HIBERNATE_ANIMATION:
                mMediaPlayer = WindowsMediaPlayer.createHibernatePlayer(getContext());
                setViews(
                        R.string.hibernate,
                        R.string.hibernating,
                        R.color.hibernate
                );
                break;

            case RUN_AIRPLANE_MODE_ANIMATION:
                mMediaPlayer = WindowsMediaPlayer.createSwitchModeOn(getContext());
                setViews(
                        R.string.airplane,
                        R.string.switching_to_airplane,
                        R.color.airplane
                );
                break;

            default:
                String errorMessage = String
                        .format("setupFragment: Animation command %s is invalid", mAnimationCommand);

                Toast.makeText(getActivity().getApplicationContext(), "UNKNOWN ERROR", Toast.LENGTH_LONG).show();
                Log.e(TAG, errorMessage, new Throwable(errorMessage));
        }
    }

    private void setViews(int titleResId, int subTitleResId, int colorResId) {
        mFragmentBinding.setEndTitle(getString(titleResId));
        mFragmentBinding.setEndSubtitle(getString(subTitleResId));
        mFragmentBinding.setEndBackground(getResources().getColor(colorResId));
    }
    
    private void setupFragmentEnterAnimation() {
           mCurrentAnimation.playSequentially(
                   endEnterAnimation(),
                   endLeaveAnimator(),
                   endThankYouAnimator()
        );

        mCurrentAnimation.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                mMediaPlayer.start();
            }

            @Override
            public void onAnimationPause(Animator animation) {
                mMediaPlayer.pause();
            }

            @Override
            public void onAnimationResume(Animator animation) {
                mMediaPlayer.resume();
            }
        });
    }

    private void startCurrentAnimation() {
        mCurrentAnimation.start();
    }

    private void pauseCurrentAnimation() {
        mCurrentAnimation.pause();
    }

    private void resumeCurrentAnimation() {
        mCurrentAnimation.resume();
    }

    private void stopCurrentAnimation() {
        mCurrentAnimation.end();
        mCurrentAnimation.cancel();
        mCurrentAnimation.removeAllListeners();
    }

    private AnimatorSet endEnterAnimation() {
        Animator spinnerAnimator = AnimUtils.createPanelButtonEnterAnimator(mSpinner);
        Animator endTitleAnimator = AnimUtils.createPanelTitleEnterAnimator(mEndTitleTv);
        Animator endSubtitleAnimator = AnimUtils.createPanelTitleEnterAnimator(mEndSubtitleTv);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                spinnerAnimator,
                endTitleAnimator,
                endSubtitleAnimator
        );

        animatorSet.setDuration(500);

        return animatorSet;
    }

    private AnimatorSet endLeaveAnimator() {
        Animator spinnerAnimator = AnimUtils.createPanelButtonHideAnimator(mSpinner);
        Animator endTitleAnimator = AnimUtils.createPanelTitleHideAnimator(mEndTitleTv);
        Animator endSubtitleAnimator = AnimUtils.createPanelTitleHideAnimator(mEndSubtitleTv);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                spinnerAnimator,
                endTitleAnimator,
                endSubtitleAnimator
        );
        animatorSet.setDuration(END_LEAVE_DURATION);
        animatorSet.setStartDelay(END_LEAVE_DELAY);

        return animatorSet;
    }

    private AnimatorSet endThankYouAnimator() {
        Animator thankYouAnimator = AnimUtils.createPanelTitleEnterAnimator(mEndThankYouTv);
        Animator flowerEmojiAnimator = AnimUtils.createPanelTitleEnterAnimator(mEndFlowerEmojiTv);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                thankYouAnimator,
                flowerEmojiAnimator
        );
        animatorSet.setDuration(THANK_YOU_ENTER_DELAY);

        return animatorSet;
    }
}
