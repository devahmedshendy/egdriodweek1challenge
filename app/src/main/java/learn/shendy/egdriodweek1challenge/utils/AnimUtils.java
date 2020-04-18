package learn.shendy.egdriodweek1challenge.utils;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Property;
import android.view.View;

public class AnimUtils {
    private static final String TAG = "AnimUtils";


    // MARK: IntroFragment Animation DELAY/DURATION

    public static final int INTRO_ENTER_DELAY = 500;
    public static final int DIVIDERS_DURATION = 500;

    public static final int NUMBER_ZERO_ENTER_DELAY = INTRO_ENTER_DELAY + (DIVIDERS_DURATION / 2);
    public static final int NUMBER_ZERO_SHOW_DURATION = 500;
    public static final int NUMBER_ZERO_HIDE_DURATION = 0;
    public static final int NUMBER_ZERO_HIDE_DELAY = 500;

    public static final int NEXT_NUMBERS_ENTER_DELAY = 200;
    public static final int NEXT_NUMBER_ENTER_DELAY = 500;
    public static final int NUMBER_SHOW_DURATION = 300;
    public static final int NUMBER_HIDE_DURATION = 200;
    public static final int NUMBER_HIDE_DELAY = 200;

    public static final int NUMBER_THREE_ENTER_DELAY = 500;
    public static final int NUMBER_THREE_HIDE_DELAY = DIVIDERS_DURATION / 2;

    // MARK: ControlFragment Animation DELAY/DURATION

    public static final int PANEL_ENTER_DELAY = 700;
    public static final int PANEL_ENTER_DURATION = 500;
    public static final int MAIN_BUTTONS_SHOW_DELAY = PANEL_ENTER_DURATION + 100;
    public static final int VOLUME_BUTTONS_ENTER_DELAY = MAIN_BUTTONS_SHOW_DELAY + 100;
    public static final int PANEL_CONTENT_SHOW_DURATION = 700;

    public static final int CLICKED_CONTROL_TITLE_LEAVE_DELAY = 1000;
    public static final int CLICKED_CONTROL_BUTTON_LEAVE_DELAY = 1100;
    public static final int CLICKED_CONTROL_BG_LEAVE_DELAY = 1300;
    public static final int CLICKED_CONTROL_SELECT_BG_HIDE_DELAY = 100;
    public static final int UnCLICKED_CONTROL_LEAVE_DELAY = 250;

    // MARK: EndFragment Animation Delay/Duration

    public static final int END_LEAVE_DURATION = 200;
    public static final int END_LEAVE_DELAY = 3000;
    public static final int THANK_YOU_ENTER_DELAY = 500;

    // MARK: Object Animator Static Methods DELAY/DURATION

    public static ObjectAnimator createPanelButtonEnterAnimator(View target) {
        return ObjectAnimator.ofPropertyValuesHolder(
                target,
                scaleXPropertyValueHolder(0f, 1f),
                scaleYPropertyValueHolder(0f, 1f),
                alphaPropertyValueHolder(0.5f, 1f)
        );
    }

    public static ObjectAnimator createPanelImageLeaveAnimator(View target) {
        return ObjectAnimator.ofPropertyValuesHolder(
                target,
                scaleXPropertyValueHolder(1f, 0f),
                scaleYPropertyValueHolder(1f, 0f),
                alphaPropertyValueHolder(1f, .5f)
        );
    }

    public static ObjectAnimator createPanelBgLeaveAnimator(View target) {
        return ObjectAnimator.ofPropertyValuesHolder(
                target,
                scaleXPropertyValueHolder(0f, 50f),
                scaleYPropertyValueHolder(0f, 50f)
        );
    }

    public static ObjectAnimator createPanelButtonHideAnimator(View target) {
        return ObjectAnimator.ofPropertyValuesHolder(
                target,
                scaleXPropertyValueHolder(1f, 0f),
                scaleYPropertyValueHolder(1f, 0f)
        );
    }

    public static ObjectAnimator createPanelSelectBgShowAnimator(View target) {
        return ObjectAnimator.ofPropertyValuesHolder(
                target,
                scaleXPropertyValueHolder(0f, 1f),
                alphaPropertyValueHolder(0f, .5f)
        );
    }

    public static ObjectAnimator createPanelSelectBgHideAnimator(View target) {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, .5f, 0f);

        return ObjectAnimator.ofPropertyValuesHolder(
                target,
                alphaPropertyValueHolder(.5f, 0f)
        );
    }

    public static ObjectAnimator createPanelTitleHideAnimator(View target) {
        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, -100f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f);

        return ObjectAnimator.ofPropertyValuesHolder(
                target,
                translationXPropertyValueHolder(0f, -100f),
                alphaPropertyValueHolder(1f, 0f)
        );
    }

    public static ObjectAnimator createPanelTitleEnterAnimator(View target) {
        PropertyValuesHolder transitionXHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 50f, 0f);
        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f);

        return ObjectAnimator.ofPropertyValuesHolder(
                target,
                translationXPropertyValueHolder(50f, 0f),
                alphaPropertyValueHolder(0f, 1f)
        );
    }

    public static ObjectAnimator createObjectAnimator(View target, Property property, float from, float to) {
        return ObjectAnimator.ofFloat(target, property, from, to);
    }

    public static ObjectAnimator createObjectAnimator(View target, String propertyName, float from, float to) {
        return ObjectAnimator.ofFloat(target, propertyName, from, to);
    }

    // MARK:

    private static PropertyValuesHolder scaleXPropertyValueHolder(float from, float to) {
        return PropertyValuesHolder.ofFloat(View.SCALE_X, from, to);
    }

    private static PropertyValuesHolder scaleYPropertyValueHolder(float from, float to) {
        return PropertyValuesHolder.ofFloat(View.SCALE_Y, from, to);
    }

    private static PropertyValuesHolder alphaPropertyValueHolder(float from, float to) {
        return PropertyValuesHolder.ofFloat(View.ALPHA, from, to);
    }

    private static PropertyValuesHolder translationXPropertyValueHolder(float from, float to) {
        return PropertyValuesHolder.ofFloat(View.TRANSLATION_X, from, to);
    }
}
