package com.allelink.wzyx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.pay.alipay.AliPay;
import com.allelink.wzyx.pay.alipay.IAliPayResultListener;
import com.allelink.wzyx.ui.TitleBar;
import com.allelink.wzyx.utils.log.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

/**
 * 订单支付
 * @author sjscode
 * @date 2017/12/11
 * @version 1.0
 * @email yangchendev@qq.com
 */

public class PayOrderActivity extends BaseActivity implements IAliPayResultListener{
    private static final String TAG = "PayOrderActivity";
    private static final String ACTIVITY_NAME = "activityName";
    private static final String ACTIVITY_COST = "cost";
    private static final String ORDER_ID = "orderId";
    /**
    * UI
    */
    //微信支付
    @BindView(R.id.rb_activity_pay_order_wechat_pay)
    RadioButton rbWechatPay = null;
    //支付宝支付
    @BindView(R.id.rb_iv_activity_pay_order_alipay)
    RadioButton rbAliPay = null;
    //确认支付按钮
    @BindView(R.id.btn_activity_pay_order_pay)
    Button btnPayOrder = null;
    @BindView(R.id.tv_activity_pay_order_cost)
    TextView tvCost = null;
    @BindView(R.id.tv_activity_pay_order_activity_name)
    TextView tvActivityName = null;
    @BindView(R.id.tb_activity_pay_order)
    TitleBar titleBar = null;

    /**
    * DATA
    */
    private String mActivityCost = null;
    private String mActivityName = null;
    private String mOrderId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置支付宝沙箱环境
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payorder);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white));
        //隐藏actionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //绑定ButterKnife
        ButterKnife.bind(this);
        initView(getIntent());
        initTitleBarEvent();
    }
    /**
    * titleBar点击事件
    */
    private void initTitleBarEvent() {
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                ((WzyxApplication)getApplication()).finishSingleActivity(PayOrderActivity.this);
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    private void initView(Intent intent) {
        //设置自定义button图标
        rbWechatPay.setButtonDrawable(R.drawable.btn_payorder_selector);
        rbAliPay.setButtonDrawable(R.drawable.btn_payorder_selector);
        mActivityCost = intent.getStringExtra(ACTIVITY_COST);
        mActivityName = intent.getStringExtra(ACTIVITY_NAME);
        mOrderId = intent.getStringExtra(ORDER_ID);
        tvCost.setText(getResources().getString(R.string.activity_cost,mActivityCost));
        tvActivityName.setText(mActivityName);
        //默认微信支付
        rbWechatPay.setChecked(true);
        rbAliPay.setChecked(false);
    }

    @OnClick(R.id.rb_activity_pay_order_wechat_pay)
    void selectWechatPay() {
        rbWechatPay.setChecked(true);
        rbAliPay.setChecked(false);
    }

    @OnClick(R.id.rb_iv_activity_pay_order_alipay)
    void selectAliPay() {
        rbWechatPay.setChecked(false);
        rbAliPay.setChecked(true);
    }

    @OnClick(R.id.btn_activity_pay_order_pay)
    void payOrder()
    {
        //微信支付
        if(rbWechatPay.isChecked()){
            Toast.makeText(this,"微信支付",Toast.LENGTH_SHORT).show();
        }
        //支付宝支付
        if(rbAliPay.isChecked())
        {
            Toast.makeText(this,"支付宝支付",Toast.LENGTH_SHORT).show();
            startAliPay();
        }
    }

    /**
    * 开始支付宝支付
    */
    private void startAliPay() {
        AliPay.create(PayOrderActivity.this)
                .setOrderId(mOrderId)
                .setSubject(mActivityName)
                .setTotalAmount(mActivityCost)
                .setPayResultListener(PayOrderActivity.this)
                .aliPay();
    }

    @Override
    public void onAliPaySuccess() {
        LogUtil.d(TAG,"支付成功");
    }

    @Override
    public void onAliPaying() {
        LogUtil.d(TAG,"支付中...");
    }

    @Override
    public void onAliPayFail() {
        LogUtil.d(TAG,"支付失败");
    }

    @Override
    public void onAliPayCancel() {
        LogUtil.d(TAG,"支付取消");
    }

    @Override
    public void onAliPayConnectError() {
        LogUtil.d(TAG,"网络连接错误");
    }
}
