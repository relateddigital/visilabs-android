package com.visilabs.notificationbell;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.visilabs.notificationbell.model.NotificationBell;

/**
 * NotificationBell'in {@code FragmentActivity} host'lar için sarmalayıcısı.
 * Görünüm ve etkileşim mantığının tamamı {@link NotificationBellController}
 * içindedir; {@code FragmentActivity} olmayan host'lar (ör. Flutter'ın
 * {@code FlutterActivity}'si) için {@link NotificationBellOverlay} kullanılır.
 */
public class NotificationBellFragment extends Fragment {

    private static final String LOG_TAG = "NotificationBell";
    private static final String ARG_PARAM1 = "dataKey";

    private NotificationBellController controller;

    public static NotificationBellFragment newInstance(NotificationBell model) {
        NotificationBellFragment fragment = new NotificationBellFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NotificationBell notificationBell = null;
        if (getArguments() != null) {
            notificationBell = (NotificationBell) getArguments().getSerializable(ARG_PARAM1);
        }

        if (notificationBell == null) {
            Log.e(LOG_TAG, "NotificationBell data is null. Closing fragment.");
            endFragment();
            return null;
        }

        controller = new NotificationBellController(getActivity(), inflater, container, notificationBell,
                new Runnable() {
                    @Override
                    public void run() {
                        endFragment();
                    }
                });
        return controller.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        controller = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null && !getActivity().isChangingConfigurations()) {
            endFragment();
        }
    }

    private void endFragment() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }
}
