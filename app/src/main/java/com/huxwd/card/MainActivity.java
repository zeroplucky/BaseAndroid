package com.huxwd.card;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huxwd.card.base.BaseActivity;
import com.huxwd.card.db.RoomListDb;
import com.huxwd.card.module.statusbar.StatusBarUtil;
import com.huxwd.card.ui.vm.MainViewModel;
import com.huxwd.card.utils.MMKVUtil;
import com.huxwd.card.utils.SizeUtils;
import com.huxwd.card.widget.FullyGridLayoutManager;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity<MainViewModel> {

    private TextView titleName;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private int spanCount = 2;
    private TextView wel;
    private String Wel = "当前登录人：";
    private TextView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addListenerAndObserver();
        checkNewVersion();
        viewModel.getClassroomList();
    }

    /*
     * 添加观察者，监听数据回调
     * */
    private void addListenerAndObserver() {
        // 返回教室
        viewModel.roomListInfo.observe(this, (Observer<List<RoomListDb>>) o -> {
            List<RoomListDb> list = o;
            if (list == null) {
                list = RoomListDb.selectAll();
            }
            if (list == null) return;
        });
    }

    private void initView() {
        StatusBarUtil.translucentStatusBar(this, false);
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext);
        findViewById(R.id.status_bar).setLayoutParams(new LinearLayout.LayoutParams(-1, statusBarHeight + 10));
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setTextColor(Color.WHITE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new FullyGridLayoutManager(mContext, spanCount);
        ((FullyGridLayoutManager) layoutManager).setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MainAdapter());
        wel = (TextView) findViewById(R.id.wel);
        btnBack = (TextView) findViewById(R.id.btn_back);

        titleName.setText("新系统");
        String cardNumber = MMKVUtil.init(mContext).getString(MMKVUtil.KV.cardNumber);
        String teacherName = MMKVUtil.init(mContext).getString(MMKVUtil.KV.teacherName);
        wel.setText(getSpan(Wel + teacherName + "(" + cardNumber + ")", Wel.length()));

        btnBack.setOnClickListener(v -> {
            MMKVUtil.init(mContext).putObj(MMKVUtil.KV.isLogin, false);
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            finish();
        });
    }

    class MainAdapter extends BaseQuickAdapter<Type, BaseViewHolder> {

        int resId1 = R.drawable.main1_icon;
        int resId2 = R.drawable.main2_icon;
        int resId3 = R.drawable.main3_icon;
        int resId4 = R.drawable.card_icon;

        public MainAdapter() {
            super(R.layout.adapter_mian_type_item);
            Type[] data = new Type[]{new Type(resId1), new Type(resId2), new Type(resId3), new Type(resId4)};
            setNewInstance(Arrays.asList(data));
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, Type item) {
            ImageView imageView = helper.getView(R.id.image);
            imageView.setImageResource(item.resId);
            int adapterPosition = helper.getAdapterPosition();
            if (adapterPosition >= 2) {
                RecyclerView.LayoutParams itemViewLayoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
                itemViewLayoutParams.topMargin = SizeUtils.dp2px(18);
            }
        }
    }

    public static class Type {

        int resId;

        public Type(int resId) {
            this.resId = resId;
        }
    }

    //设置文字
    private SpannableString getSpan(String content, int length) {
        SpannableString spanStr = new SpannableString(content);
        spanStr.setSpan(new ForegroundColorSpan(Color.RED), length, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    /*
     * 检测新版本
     * */
    public void checkNewVersion() {
        viewModel.checkNewVersion(mContext);
    }

}