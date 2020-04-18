package learn.shendy.egdroidw1challenge.views.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import learn.shendy.egdroidw1challenge.databinding.FragmentIntroBinding;
import learn.shendy.egdroidw1challenge.mediaplayer.TimerTickMediaPlayer;
import learn.shendy.egdroidw1challenge.utils.AnimUtils;
import learn.shendy.egdroidw1challenge.views.MainActivity;

import static learn.shendy.egdroidw1challenge.enums.AnimationCommand.RUN_CONTROL_PANEL_ANIMATION;
import static learn.shendy.egdroidw1challenge.utils.AnimUtils.*;

public class IntroFragment extends BaseFragment {
    public static final String TAG = "IntroFragment";

    private final int NUMBER_DEFAULT_SIZE = 80;

    private AnimatorSet mCurrentAnimator;
    private FragmentIntroBinding mFragmentBinding;
    private TextView mNumberTv;
    private View mTopDivider;
    private View mBottomDivider;

    private Map<Integer, TimerTickMediaPlayer> mTickMediaPlayerMap = new HashMap<>();

    // MARK: Instance Construction Methods

    public IntroFragment() {
        // Required empty public constructor
    }

    public static IntroFragment newInstance() {
        return new IntroFragment();
    }

    // MARK: Lifecycle Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: starts");
        mFragmentBinding = FragmentIntroBinding.inflate(inflater, container, false);

        mNumberTv = mFragmentBinding.numberTv;
        mTopDivider = mFragmentBinding.introFragmentTopDivider;
        mBottomDivider = mFragmentBinding.introFragmentBottomDivider;

        return mFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: starts");
        super.onActivityCreated(savedInstanceState);

        setupFragment();
        setupEnterAnimation();
        startFragmentAnimation();
        Log.d(TAG, "onActivityCreated: ends");
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeCurrentAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseCurrentAnimation();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: starts");
        super.onDestroyView();
        stopCurrentAnimation();
        Log.d(TAG, "onDestroyView: ends");
    }

    // MARK: Setup Methods

    private void setupFragment() {
        mCurrentAnimator = new AnimatorSet();

        // Set View To Initial State for Animation
        resetDividersAnimationState();
        resetNumberTVAnimationState();
    }

    private void resetDividersAnimationState() {
        mTopDivider.setTranslationX(getWindowWidth());
        mBottomDivider.setTranslationX(-getWindowWidth());
    }

    private void resetNumberTVAnimationState() {
        mNumberTv.setTextSize(0);
        mNumberTv.setAlpha(0f);
    }

    private void setupEnterAnimation() {
        mCurrentAnimator.addListener(enterAnimationListener);
        mCurrentAnimator
                .playSequentially(
                        introEnterAnimator(),
                        nextNumbersEnterAnimator(),
                        introLeaveAnimator()
                );
    }

    private AnimatorListenerAdapter enterAnimationListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
                MainActivity.sAnimationListenerSubject
                        .onNext(RUN_CONTROL_PANEL_ANIMATION);
        }
    };

    private void startFragmentAnimation() {
        mCurrentAnimator.start();
    }

    private void resumeCurrentAnimation() {
        if (mCurrentAnimator.isPaused()) {
            mCurrentAnimator.resume();
        }
    }

    private void pauseCurrentAnimation() {
        if (mCurrentAnimator.isRunning()) {
            mCurrentAnimator.pause();
        }
    }

    private void stopCurrentAnimation() {
        mCurrentAnimator.end();
        mCurrentAnimator.cancel();
        mCurrentAnimator.removeAllListeners();
    }

    // MARK: Animators

    private AnimatorSet introEnterAnimator() {
        AnimatorSet enterAnimator = new AnimatorSet();

        enterAnimator.playTogether(
                allDividersEnterAnimator(),
                numberZeroEnterAnimator()
        );

        enterAnimator.setStartDelay(INTRO_ENTER_DELAY);

        return enterAnimator;
    }
//
    private AnimatorSet introLeaveAnimator() {
        AnimatorSet leaveAnimator = new AnimatorSet();

        leaveAnimator.playTogether(
                allDividersLeaveAnimator(),
                numberThreeAnimator()
        );

        return leaveAnimator;
    }

    private AnimatorSet allDividersEnterAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                topDividerEnterAnimator(),
                bottomDividerEnterAnimator()
        );
        animatorSet.setDuration(DIVIDERS_DURATION);
        animatorSet.setStartDelay(INTRO_ENTER_DELAY);

        return animatorSet;
    }

    private Animator topDividerEnterAnimator() {
        ObjectAnimator animator = AnimUtils.createObjectAnimator(
                mTopDivider,
                View.TRANSLATION_X,
                getWindowWidth(), 0
        );

        return animator;
    }

    private Animator bottomDividerEnterAnimator() {
        ObjectAnimator animator = AnimUtils.createObjectAnimator(
                mBottomDivider,
                View.TRANSLATION_X,
                -getWindowWidth(), 0
        );

        return animator;
    }

    private AnimatorSet numberZeroEnterAnimator() {
        ObjectAnimator showAnimator = AnimUtils.createObjectAnimator(
                mNumberTv,
                View.ALPHA,
                0f, 1f
        );

        ObjectAnimator hideAnimator = AnimUtils.createObjectAnimator(
                mNumberTv,
                View.ALPHA,
                1f, 0f
        );

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mNumberTv.setText("0");
                mNumberTv.setTextSize(NUMBER_DEFAULT_SIZE);
            }
        });

        animatorSet.playSequentially(
                showAnimator,
                hideAnimator
        );

        showAnimator.setDuration(NUMBER_ZERO_SHOW_DURATION);
        hideAnimator.setDuration(NUMBER_ZERO_HIDE_DURATION);
        hideAnimator.setStartDelay(NUMBER_ZERO_HIDE_DELAY);

        animatorSet.setStartDelay(NUMBER_ZERO_ENTER_DELAY);

        return animatorSet;
    }

    private AnimatorSet nextNumbersEnterAnimator() {
        AnimatorSet nextNumbersAnimator = new AnimatorSet();

        Animator numberOneAnimator = nextNumberEnterAnimator(1);
        Animator numberTwoAnimator = nextNumberEnterAnimator(2);

        nextNumbersAnimator.playSequentially(
                numberOneAnimator,
                numberTwoAnimator
        );

        nextNumbersAnimator.setStartDelay(NEXT_NUMBERS_ENTER_DELAY);

        return nextNumbersAnimator;
    }

    private AnimatorSet nextNumberEnterAnimator(Integer number) {
        Animator showAnimator = showNumberAnimator(number);
        Animator hideAnimator = hideNumberAnimator();

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playSequentially(
                showAnimator,
                hideAnimator
        );
        animatorSet.setStartDelay(NEXT_NUMBER_ENTER_DELAY);

        return animatorSet;
    }

    private AnimatorSet allDividersLeaveAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                topDividerLeaveAnimator(),
                bottomDividerLeaveAnimator()
        );
        animatorSet.setDuration(DIVIDERS_DURATION);
        animatorSet.setStartDelay(INTRO_ENTER_DELAY);

        return animatorSet;
    }

    private Animator topDividerLeaveAnimator() {
        ObjectAnimator animator = AnimUtils.createObjectAnimator(
                mTopDivider,
                View.TRANSLATION_X,
                0, -getWindowWidth()
        );

        return animator;
    }

    private Animator bottomDividerLeaveAnimator() {
        ObjectAnimator animator = AnimUtils.createObjectAnimator(
                mBottomDivider,
                View.TRANSLATION_X,
                0, getWindowWidth()
        );

        return animator;
    }

    private AnimatorSet numberThreeAnimator() {
        Animator showAnimator = showNumberAnimator(3);

        ObjectAnimator hideAnimator = AnimUtils.createObjectAnimator(
                mNumberTv,
                View.ALPHA,
                1f, 0f
        );

        hideAnimator.setDuration(NUMBER_HIDE_DURATION);
        hideAnimator.setStartDelay(NUMBER_THREE_HIDE_DELAY);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playSequentially(
                showAnimator,
                hideAnimator
        );
        animatorSet.setStartDelay(NUMBER_THREE_ENTER_DELAY);

        return animatorSet;
    }

    private AnimatorSet showNumberAnimator(Integer number) {
        ObjectAnimator alphaAnimator = AnimUtils.createObjectAnimator(
                mNumberTv,
                View.ALPHA,
                0f, 1f
        );

        ObjectAnimator textSizeAnimator = AnimUtils.createObjectAnimator(
                mNumberTv,
                "textSize",
                0, NUMBER_DEFAULT_SIZE
        );

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                alphaAnimator,
                textSizeAnimator
        );

        animatorSet.setDuration(NUMBER_SHOW_DURATION);

        mTickMediaPlayerMap.put(number, new TimerTickMediaPlayer(getContext()));

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                resetNumberTVAnimationState();
                mNumberTv.setText(number.toString());
                mTickMediaPlayerMap.get(number).start();
            }

            @Override
            public void onAnimationPause(Animator animation) {
                mTickMediaPlayerMap.get(number).pause();
            }

            @Override
            public void onAnimationResume(Animator animation) {
                mTickMediaPlayerMap.get(number).resume();
            }
        });

        return animatorSet;
    }

    private AnimatorSet hideNumberAnimator() {

        ObjectAnimator alphaAnimator = AnimUtils.createObjectAnimator(
                mNumberTv,
                View.ALPHA,
                1f, 0f
        );

        ObjectAnimator textSizeAnimator = AnimUtils.createObjectAnimator(
                mNumberTv, 
                "textSize",
                NUMBER_DEFAULT_SIZE, 50
        );

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                alphaAnimator,
                textSizeAnimator
        );

        animatorSet.setDuration(NUMBER_HIDE_DURATION);
        animatorSet.setStartDelay(NUMBER_HIDE_DELAY);

        return animatorSet;
    }
}
