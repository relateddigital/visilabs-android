package com.relateddigital.visilabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class VisilabsBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static VisilabsBottomSheetDialogFragment newInstance() {
        return new VisilabsBottomSheetDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        int a = R.style.AlertDialogStyle;

        View view = inflater.inflate(R.layout.layout_bottom_sheet, container, false);
        return view;

    }
}
