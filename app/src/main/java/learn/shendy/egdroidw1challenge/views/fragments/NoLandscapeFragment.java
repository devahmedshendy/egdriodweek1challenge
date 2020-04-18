package learn.shendy.egdroidw1challenge.views.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import learn.shendy.egdroidw1challenge.R;
import learn.shendy.egdroidw1challenge.views.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoLandscapeFragment extends BaseFragment {
    public static final String TAG = "LandscapeNotSupportedFr";

    public static final String PREFERENCE_KEY = "dismissNoLandscapeDialog";

    private Button mDismissBtn;

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

        mDismissBtn = rootView.findViewById(R.id.no_landscape_dismiss_btn);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupFragment();
    }

    // MARK: Setup Methods

    private void setupFragment() {
        mDismissBtn.setOnClickListener(dismiss);
    }

    // MARK: Listener Methods

    private OnClickListener dismiss = btn -> {
        updateDismissPreferenceKey();
        closeFragment();
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
