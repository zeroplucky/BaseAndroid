package com.huxwd.card.widget;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.huxwd.card.R;
import com.huxwd.card.utils.SizeUtils;

import java.io.Serializable;


/**
 * 仿IOS对话框
 * Created by cai.jia on 2017/10/20 0020.
 */

public class UniversalDialog extends DialogFragment {

    private static final String PARAMS = "params";
    TextView tvTitle;
    TextView tvContent;
    TextView tvSingleButton;
    TextView tvPositive;
    TextView tvNegative;
    LinearLayout llyDoubleAction;

    private Builder params;

    private static UniversalDialog newInstance(Builder builder) {
        UniversalDialog dialog = new UniversalDialog();
        Bundle args = new Bundle();
        args.putSerializable(PARAMS, builder);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_full);
        Bundle args = getArguments();
        if (args != null) {
            params = (Builder) args.getSerializable(PARAMS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_dialog_universal, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
        tvSingleButton = view.findViewById(R.id.tv_single_button);
        tvSingleButton.setOnClickListener(this::onViewClicked);
        tvPositive = view.findViewById(R.id.tv_positive_button);
        tvPositive.setOnClickListener(this::onViewClicked);
        tvNegative = view.findViewById(R.id.tv_negative_button);
        tvNegative.setOnClickListener(this::onViewClicked);
        llyDoubleAction = view.findViewById(R.id.lly_double_action);
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        return view;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setText(tvTitle, params.title);
        setTextSize(tvTitle, params.titleTextSize);
        setTextColor(tvTitle, params.titleTextColor);

        setText(tvContent, params.content);
        setTextSize(tvContent, params.contentTextSize);
        setTextColor(tvContent, params.contentTextColor);
        setTextViewGravity(tvContent, params.contentGravity);

        setText(tvSingleButton, params.positive);
        setTextSize(tvSingleButton, params.positiveTextSize);
        setTextColor(tvSingleButton, params.positiveTextColor);

        setText(tvPositive, params.positive);
        setTextSize(tvPositive, params.positiveTextSize);
        setTextColor(tvPositive, params.positiveTextColor);

        setText(tvNegative, params.negative);
        setTextSize(tvNegative, params.negativeTextSize);
        setTextColor(tvNegative, params.negativeTextColor);

        llyDoubleAction.setVisibility(params.isSingleButton ? View.GONE : View.VISIBLE);
        tvSingleButton.setVisibility(params.isSingleButton ? View.VISIBLE : View.GONE);
        tvTitle.setVisibility(params.hasTitle || !TextUtils.isEmpty(params.title) ? View.VISIBLE : View.GONE);
    }

    private void setText(TextView textView, CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }
    }

    private void setTextSize(TextView textView, int textSize) {
        if (textSize > 0) {
            textView.setTextSize(textSize);
        }
    }

    private void setTextColor(TextView textView, int textColor) {
        if (textColor > 0) {
            textView.setTextColor(textColor);
        }
    }

    private void setTextViewGravity(TextView textView, int gravity) {
        textView.setGravity(gravity);
    }

    public void onViewClicked(View view) {
        int action = -1;
        switch (view.getId()) {
            case R.id.tv_single_button:
            case R.id.tv_positive_button:
                action = ACTION_POSITIVE;
                break;
            case R.id.tv_negative_button:
                action = ACTION_NEGATIVE;
                break;
        }
        if (actionClickListener != null && action != -1) {
            actionClickListener.onDialogActionClick(this, action);
        }
    }

    public static class Builder implements Serializable {

        private CharSequence title;
        private int titleTextSize;
        private int titleTextColor;

        private CharSequence content;
        private int contentTextSize;
        private int contentTextColor;

        private CharSequence negative;
        private int negativeTextSize;
        private int negativeTextColor;

        private CharSequence positive;
        private int positiveTextSize;
        private int positiveTextColor;

        private boolean isSingleButton;
        private boolean hasTitle;
        private int contentGravity = Gravity.CENTER;

        public Builder title(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder titleTextSize(int titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        public Builder titleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder content(CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder contentTextSize(int contentTextSize) {
            this.contentTextSize = contentTextSize;
            return this;
        }

        public Builder contentTextColor(int contentTextColor) {
            this.contentTextColor = contentTextColor;
            return this;
        }

        public Builder negative(CharSequence negative) {
            this.negative = negative;
            return this;
        }

        public Builder negativeTextSize(int negativeTextSize) {
            this.negativeTextSize = negativeTextSize;
            return this;
        }

        public Builder negativeTextColor(int negativeTextColor) {
            this.negativeTextColor = negativeTextColor;
            return this;
        }

        public Builder positive(CharSequence positive) {
            this.positive = positive;
            return this;
        }

        public Builder contentGravity(int gravity) {
            contentGravity = gravity;
            return this;
        }

        public Builder positiveTextSize(int positiveTextSize) {
            this.positiveTextSize = positiveTextSize;
            return this;
        }

        public Builder positiveTextColor(int positiveTextColor) {
            this.positiveTextColor = positiveTextColor;
            return this;
        }

        public Builder isSingleButton(boolean isSingleButton) {
            this.isSingleButton = isSingleButton;
            return this;
        }

        public Builder hasTitle(boolean hasTitle) {
            this.hasTitle = hasTitle;
            return this;
        }

        public UniversalDialog build() {
            return UniversalDialog.newInstance(this);
        }
    }

    public static final int ACTION_POSITIVE = 1;
    public static final int ACTION_NEGATIVE = 2;

    public interface OnDialogActionClickListener {

        void onDialogActionClick(UniversalDialog dialog, int action);

    }

    private OnDialogActionClickListener actionClickListener;

    public void setOnDialogActionClickListener(OnDialogActionClickListener listener) {
        this.actionClickListener = listener;
    }

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
