package com.huxwd.card.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huxwd.card.utils.BindingReflex;


abstract class BaseFragment<VM extends ViewModel> extends Fragment {

    protected VM viewModel;

    protected View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(setLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BindingReflex.reflexViewModel(this.getClass(), this);
        initViewAndData(view);
    }

    protected abstract int setLayoutId();

    protected abstract void initViewAndData(View view);


}