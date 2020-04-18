package learn.shendy.egdriodweek1challenge.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import learn.shendy.egdriodweek1challenge.R;
import learn.shendy.egdriodweek1challenge.databinding.ActivityMainBinding;
import learn.shendy.egdriodweek1challenge.enums.AnimationCommand;
import learn.shendy.egdriodweek1challenge.views.fragments.OrientationChangeListener;
import learn.shendy.egdriodweek1challenge.views.fragments.OrientationChangeListener.OrientationMode;
import learn.shendy.egdriodweek1challenge.views.fragments.EndFragment;
import learn.shendy.egdriodweek1challenge.views.fragments.IntroFragment;
import learn.shendy.egdriodweek1challenge.views.fragments.NoLandscapeFragment;
import learn.shendy.egdriodweek1challenge.views.fragments.PanelFragment;

import static learn.shendy.egdriodweek1challenge.views.fragments.OrientationChangeListener.OrientationMode.LANDSCAPE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String SHARED_PREFERENCE_NAME = "main";

    public static final PublishSubject<OrientationMode> sOrientationListenerSubject = PublishSubject.create();
    public static final PublishSubject<AnimationCommand> sAnimationListenerSubject = PublishSubject.create();

    private Disposable mNoLandscapeDisposable;
    private CompositeDisposable mDisposableContainer;

    private FragmentManager mFragmentManager;
    private ActivityMainBinding mActivityBinding;

    // MARK: Lifecycle Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);

        setupActivity();
        observeOrientationChange();
        observeAnimationStatus();

        if (savedInstanceState == null) {
            openDefaultFragment();
        }

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: starts");
        clearDisposableContainer();
        super.onDestroy();
        Log.d(TAG, "onDestroy: ends");
    }

    // MARK: Setup Methods

    private void setupActivity() {
        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(mActivityBinding.getRoot());

        mFragmentManager = getSupportFragmentManager();
        mDisposableContainer = new CompositeDisposable();

        OrientationChangeListener.enable(this);
    }

    @SuppressLint("CheckResult")
    private void observeOrientationChange() {
        mNoLandscapeDisposable = sOrientationListenerSubject
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .filter(orientation -> orientation == LANDSCAPE)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onLandscapeOrientation,
                        this::onObservationFailed
                );
    }

    @SuppressLint("CheckResult")
    private void observeAnimationStatus() {
        sAnimationListenerSubject
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mDisposableContainer::add)
                .subscribe(
                        this::onAnimationCommandTriggered,
                        this::onObservationFailed
                );
    }

    // MARK: Fragment Management Methods

    private void openDefaultFragment() {
        openIntroFragment();
    }

    private void openIntroFragment() {
        IntroFragment fragment = IntroFragment.newInstance();

        mFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    private void openControlFragment() {
        PanelFragment fragment = PanelFragment.newInstance();

        mFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void openEndFragment(AnimationCommand animationCommand) {
        Log.d(TAG, "openEndFragment: called");
        EndFragment fragment = EndFragment.newInstance(animationCommand);

        mFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // MARK: Animation Observer Handlers

    private void onLandscapeOrientation(OrientationMode newOrientation) {

        if (isNoLandscapeDialogDismissed()) {
            mNoLandscapeDisposable.dispose();
            return;
        }

        if (isNoLandscapeDialogOpen()) {
           return;
        }
        
        mFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, new NoLandscapeFragment(), NoLandscapeFragment.TAG)
                .addToBackStack(NoLandscapeFragment.TAG)
                .commit();
    }

    private void onAnimationCommandTriggered(AnimationCommand status) {
        switch (status) {
            case RUN_CONTROL_PANEL_ANIMATION:
                openControlFragment();
                break;

            case RUN_POWER_OFF_ANIMATION:
            case RUN_REBOOT_ANIMATION:
            case RUN_HIBERNATE_ANIMATION:
            case RUN_AIRPLANE_MODE_ANIMATION:
                openEndFragment(status);
                break;
        }
    }

    private void onObservationFailed(Throwable t) {
        Log.e(TAG, "observationFailed: " + t.getMessage(), t);
        Toast.makeText(this, "! ERROR !", Toast.LENGTH_SHORT).show();
    }

    // MARK: Helper Methods

    private void clearDisposableContainer() {
        mDisposableContainer.clear();
        Log.d(TAG, "clearDisposableContainer: cleared");
    }

    private boolean isNoLandscapeDialogOpen() {
        NoLandscapeFragment fragment = (NoLandscapeFragment) mFragmentManager
                .findFragmentByTag(NoLandscapeFragment.TAG);

        return fragment != null;
    }

    private boolean isNoLandscapeDialogDismissed() {
        return getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .getBoolean(NoLandscapeFragment.PREFERENCE_KEY, false);
    }
}
