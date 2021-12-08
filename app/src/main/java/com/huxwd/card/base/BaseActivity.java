package com.huxwd.card.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.huxwd.card.MainActivity;
import com.huxwd.card.utils.BindingReflex;

public class BaseActivity<VM extends ViewModel> extends AppCompatActivity {

    public BaseActivity mContext;

    protected VM viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityManager2.addActivity(this);
        mContext = BaseActivity.this;
        viewModel = BindingReflex.reflexViewModel(this.getClass(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager2.removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {
            moveTaskToBack(false);
            return;
        }
        super.onBackPressed();
    }

    /*  Activity 跳转 */
    public void skipActivity(Context context, Class<?> goal) {
        Intent intent = new Intent(context, goal);
        context.startActivity(intent);

    }

    /* Activity 跳转*/
    public void skipActivity(Context context, Class<?> goal, Bundle bundle) {
        Intent intent = new Intent(context, goal);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}