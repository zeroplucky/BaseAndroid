package com.sunfusheng;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sunfusheng.firupdater.R;

/**
 * 创建日期：2021/4/2 13:06
 *
 * @author zhang_
 * @version 1.0
 */
public class UpdateDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private TextView update;
    private NumberProgressBar progressBar;
    FirAppInfo.AppInfo appInfo;
    private View ibClose;

    public UpdateDialog(@NonNull Context context, FirAppInfo.AppInfo appInfo) {
        super(context, R.style.UpdateDialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init(context, appInfo);
    }

    /**
     * 初始化布局
     */
    private void init(Context context, FirAppInfo.AppInfo appInfo) {
        this.context = context;
        this.appInfo = appInfo;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);
        setContentView(view);
        setWindowSize(context);
        initView(view);
    }

    private void setWindowSize(Context context) {
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenUtil.getWith(context) * 0.7f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);

        if (context instanceof AppCompatActivity) {
            AppCompatActivity a = (AppCompatActivity) context;
            WindowManager.LayoutParams attributes = a.getWindow().getAttributes();
            attributes.dimAmount = 0.2f;
            attributes.alpha = 0.7f;
            a.getWindow().setAttributes(attributes);
        }
    }

    @Override
    public void dismiss() {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity a = (AppCompatActivity) context;
            WindowManager.LayoutParams attributes = a.getWindow().getAttributes();
            attributes.dimAmount = 1f;
            attributes.alpha = 1f;
            a.getWindow().setAttributes(attributes);
        }
        super.dismiss();
    }

    private void initView(View view) {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        ibClose = view.findViewById(R.id.text_close);
        TextView description = view.findViewById(R.id.tv_description);
        progressBar = view.findViewById(R.id.np_bar);
        progressBar.setVisibility(View.INVISIBLE);
        update = view.findViewById(R.id.btn_update);
        update.setOnClickListener(this);
        ibClose.setOnClickListener(this);
        //强制升级
        if (appInfo.forceUpDater) {
            ibClose.setVisibility(View.GONE);
        }
        //设置更新内容
        if (!TextUtils.isEmpty(appInfo.updateDesc)) {
            description.setText(appInfo.updateDesc);
        } else {
            description.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_update) { // 升级
            if (update.getText().toString().equals("升级")) {
                if (onClickDownloadDialogListener != null) {
                    onClickDownloadDialogListener.onClickDownload(this);
                    onClickDownloadDialogListener.onClickBackgroundDownload(this);
                }
                if (progressBar.getVisibility() == View.INVISIBLE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (appInfo.forceUpDater) {
                    update.setEnabled(false);
                    update.setBackground(getContext().getResources().getDrawable(R.drawable.background_gradient_btn_hui));
                }
                update.setText("后台升级");
                ibClose.setVisibility(View.GONE);
            } else if (update.getText().toString().equals("后台升级")) {
                dismiss();
            }
        } else if (id == R.id.text_close) {
            if (onClickDownloadDialogListener != null) {
                onClickDownloadDialogListener.onClickCancelDownload(this);
            }
        }
    }

    public void downloading(int max, int progress) {


        if (max != -1 && progressBar.getVisibility() == View.VISIBLE) {
            int curr = (int) (progress / (double) max * 100.0);
            progressBar.setProgress(curr);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private OnClickDownloadDialogListener onClickDownloadDialogListener;

    public void setOnClickDownloadDialogListener(OnClickDownloadDialogListener onClickDownloadDialogListener) {
        this.onClickDownloadDialogListener = onClickDownloadDialogListener;
    }

    public interface OnClickDownloadDialogListener {

        void onClickDownload(DialogInterface dialog);

        void onClickBackgroundDownload(DialogInterface dialog);

        void onClickCancelDownload(DialogInterface dialog);
    }
}
