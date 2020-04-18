package learn.shendy.egdroidw1challenge.views.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import learn.shendy.egdroidw1challenge.databinding.FragmentPanelBinding;
import learn.shendy.egdroidw1challenge.views.MainActivity;
import learn.shendy.egdroidw1challenge.views.controls.AbstractControlButton;
import learn.shendy.egdroidw1challenge.views.controls.BasicControlButton;
import learn.shendy.egdroidw1challenge.views.controls.ControlButton;
import learn.shendy.egdroidw1challenge.R;
import learn.shendy.egdroidw1challenge.views.controls.VolumeControlButton;
import learn.shendy.egdroidw1challenge.mediaplayer.ControlClickMediaPlayer;
import learn.shendy.egdroidw1challenge.utils.AnimUtils;

import static learn.shendy.egdroidw1challenge.enums.AnimationCommand.*;
import static learn.shendy.egdroidw1challenge.utils.AnimUtils.*;

public class PanelFragment extends BaseFragment {
    public static final String TAG = "PanelFragment";


    private View mPanelView;
    private View mPanelSeparatorV;
    private List<AbstractControlButton> mMainControlList = new ArrayList<>();
    private List<AbstractControlButton> mVolumeControlList = new ArrayList<>();

    private FragmentPanelBinding mFragmentBinding;
    private AnimatorSet mCurrentAnimation;
    private ControlClickMediaPlayer mControlClickMediaPlayer;

    // MARK: Constructor Methods

    public PanelFragment() {
        // Required empty public constructor
    }

    public static PanelFragment newInstance() {
        return new PanelFragment();
    }

    // MARK: Lifecycle Methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentBinding = FragmentPanelBinding.inflate(inflater, container, false);

        AbstractControlButton mPowerOffControl = createBasicMainControl(
                mFragmentBinding.panelFragmentPoweroffBg,
                mFragmentBinding.panelFragmentPoweroffBtn,
                mFragmentBinding.panelFragmentPoweroffTitle,
                mFragmentBinding.panelFragmentPoweroffSelectBg
        );

        AbstractControlButton mRebootControl = createBasicMainControl(
                mFragmentBinding.panelFragmentRebootBg,
                mFragmentBinding.panelFragmentRebootBtn,
                mFragmentBinding.panelFragmentRebootTitle,
                mFragmentBinding.panelFragmentRebootSelectBg
        );

        AbstractControlButton mHibernateControl = createBasicMainControl(
                mFragmentBinding.panelFragmentHibernateBg,
                mFragmentBinding.panelFragmentHibernateBtn,
                mFragmentBinding.panelFragmentHibernateTitle,
                mFragmentBinding.panelFragmentHibernateSelectBg
        );

        AbstractControlButton mAirplaneControl = createMainControl(
                mFragmentBinding.panelFragmentAirplaneBg,
                mFragmentBinding.panelFragmentAirplaneBtn,
                mFragmentBinding.panelFragmentAirplaneTitle,
                mFragmentBinding.panelFragmentAirplaneSubtitle,
                mFragmentBinding.panelFragmentAirplaneSelectBg
        );

        mMainControlList.add(mPowerOffControl);
        mMainControlList.add(mRebootControl);
        mMainControlList.add(mHibernateControl);
        mMainControlList.add(mAirplaneControl);

        AbstractControlButton mVolumeOffControl = createVolumeControl(
                mFragmentBinding.panelFragmentVolumeOffBtn
        );

        AbstractControlButton mVolumeOnControl = createVolumeControl(
                mFragmentBinding.panelFragmentVolumeOnBtn
        );

        AbstractControlButton mVolumeVibrateControl = createVolumeControl(
                mFragmentBinding.panelFragmentVibrateBtn
        );

        mVolumeControlList.add(mVolumeOffControl);
        mVolumeControlList.add(mVolumeOnControl);
        mVolumeControlList.add(mVolumeVibrateControl);


        mPanelView = mFragmentBinding.panelFragmentPanelView;
        mPanelSeparatorV = mFragmentBinding.panelFragmentVolumesDivider;

        return mFragmentBinding.getRoot();
    }

    private BasicControlButton createBasicMainControl(View background, ImageView button, TextView title, View selectBg) {
        return  new BasicControlButton(background, button, title, selectBg);
    }

    private ControlButton createMainControl(View background, ImageView button, TextView title, TextView subtitle, View selectBg) {
        return  new ControlButton(background, button, title, subtitle, selectBg);
    }

    private VolumeControlButton createVolumeControl(ImageView button) {
        return new VolumeControlButton(button);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopCurrentAnimation();
    }

    // MARK: Setup Methods

    private void setupFragment() {
        mControlClickMediaPlayer = new ControlClickMediaPlayer(getContext());

        resetCurrentAnimator();
        setViewsToInitialAnimationState();
    }

    private void setViewsToInitialAnimationState() {
        for (AbstractControlButton control : mMainControlList) {
            control.resetAnimationState();
        }

        for (AbstractControlButton control : mVolumeControlList) {
            control.resetAnimationState();
        }

        mPanelSeparatorV.setScaleX(0);
        mPanelView.setAlpha(0f);
    }

    private void setupEnterAnimation() {
        mCurrentAnimation.addListener(enterAnimationListener);

        mCurrentAnimation.playTogether(
                panelEnterAnimator(),
                controlButtonsShowAnimator(),
                volumeButtonsEnterAnimator(),
                panelSeparatorEnterAnimator()
        );
    }

    private AnimatorListenerAdapter enterAnimationListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            resetCurrentAnimator();
            setControlOnClickListeners();
        }
    };

    private void setupFragmentLeaveAnimation(View clickedBtn) {
        mCurrentAnimation
                .playTogether(
                        mainControlsLeaveAnimator(clickedBtn),
                        volumeControlsLeaveAnimator(),
                        controlsDividerLeaveAnimator()
                );

        mCurrentAnimation
                .addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mControlClickMediaPlayer.start();
                    }

                    @Override
                    public void onAnimationPause(Animator animation) {
                        mControlClickMediaPlayer.pause();
                    }

                    @Override
                    public void onAnimationResume(Animator animation) {
                        mControlClickMediaPlayer.resume();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        resetCurrentAnimator();

                        switch (clickedBtn.getId()) {
                            case R.id.panel_fragment_poweroff_btn:
                                MainActivity
                                        .sAnimationListenerSubject
                                        .onNext(RUN_POWER_OFF_ANIMATION);
                                break;

                            case R.id.panel_fragment_reboot_btn:
                                MainActivity
                                        .sAnimationListenerSubject
                                        .onNext(RUN_REBOOT_ANIMATION);
                                break;

                            case R.id.panel_fragment_hibernate_btn:
                                MainActivity
                                        .sAnimationListenerSubject
                                        .onNext(RUN_HIBERNATE_ANIMATION);
                                break;

                            case R.id.panel_fragment_airplane_btn:
                                MainActivity
                                        .sAnimationListenerSubject
                                        .onNext(RUN_AIRPLANE_MODE_ANIMATION);
                                break;
                        }
                    }
                });
    }

    // MARK: Start/Stop/Resume/Pause Animation Methods

    private void startEnterAnimation() {
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

    private void stopCurrentAnimation() {
        mCurrentAnimation.end();
        mCurrentAnimation.cancel();
        mCurrentAnimation.removeAllListeners();
    }

    private void resetCurrentAnimator() {
        mCurrentAnimation = new AnimatorSet();
    }

    // MARK: Enter Animators

    private Animator panelEnterAnimator() {
        Animator panelAnimator = AnimUtils.createObjectAnimator(
                mPanelView, View.ALPHA,
                0f, 1f
        );

        panelAnimator.setDuration(PANEL_ENTER_DURATION);
        panelAnimator.setStartDelay(PANEL_ENTER_DELAY);

        return panelAnimator;
    }

    private AnimatorSet controlButtonsShowAnimator() {
        List<Animator> animators = new ArrayList<>();

        for (AbstractControlButton control : mMainControlList) {
            animators.add(control.showAnimator());
        }

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(animators);
        animatorSet.setDuration(PANEL_CONTENT_SHOW_DURATION);
        animatorSet.setStartDelay(MAIN_BUTTONS_SHOW_DELAY);

        return animatorSet;
    }

    private AnimatorSet volumeButtonsEnterAnimator() {
        List<Animator> animators = new ArrayList<>();

        Animator controlsDividerAnimator = AnimUtils.createObjectAnimator(
                mPanelSeparatorV, View.SCALE_X,
                0f, 1f
        );

        animators.add(controlsDividerAnimator);

        for (AbstractControlButton control : mVolumeControlList) {
            animators.add(control.showAnimator());
        }

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(animators);
        animatorSet.setDuration(PANEL_CONTENT_SHOW_DURATION);
        animatorSet.setStartDelay(VOLUME_BUTTONS_ENTER_DELAY);

        return animatorSet;
    }

    private Animator panelSeparatorEnterAnimator() {
        Animator animator = AnimUtils.createObjectAnimator(
                mPanelSeparatorV,
                View.SCALE_X,
                0f, 1f
        );

        animator.setDuration(PANEL_CONTENT_SHOW_DURATION);
        animator.setStartDelay(VOLUME_BUTTONS_ENTER_DELAY);

        return animator;
    }

    // MARK: Leave Animators

    private AnimatorSet mainControlsLeaveAnimator(View clickedBtn) {
        AnimatorSet animatorSet = new AnimatorSet();

        List<Animator> animators = new ArrayList<>();

        for (AbstractControlButton control : mMainControlList) {
            if (control.mButton.getId() == clickedBtn.getId()) {
                animators.add(control.clickedLeaveAnimator());
                continue;
            }

            animators.add(control.unClickedLeaveAnimator());
        }

        animatorSet.playTogether(animators);


        return animatorSet;
    }

    private AnimatorSet volumeControlsLeaveAnimator() {
        List<Animator> animators = new ArrayList<>();
        AnimatorSet animatorSet = new AnimatorSet();

        for (AbstractControlButton control : mVolumeControlList) {
            animators.add(control.unClickedLeaveAnimator());
        }

        animatorSet.playTogether(animators);

        return animatorSet;
    }

    private Animator controlsDividerLeaveAnimator() {
        Animator animator = AnimUtils.createObjectAnimator(
                mPanelSeparatorV, View.SCALE_X,
                1f, 0f
        );

        return animator;
    }

    // MARK: Listener Methods

    private void setControlOnClickListeners() {
        for (AbstractControlButton control : mMainControlList) {
            control.mButton.setOnClickListener(mainControlOnClickListener);
        }

        for (AbstractControlButton control : mVolumeControlList) {
            control.mButton.setOnClickListener(volumeControlOnClickListener);
        }
    }

    private OnClickListener mainControlOnClickListener = btn -> {

        if (mCurrentAnimation.isStarted() || mCurrentAnimation.isRunning()) {
            return;
        }

        setupFragmentLeaveAnimation(btn);
        startEnterAnimation();
    };

    private OnClickListener volumeControlOnClickListener = btn -> {

        // Show message to user
        Toast.makeText(getContext(), R.string.not_implemented, Toast.LENGTH_LONG).show();
    };
}
