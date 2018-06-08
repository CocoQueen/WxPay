package com.coco.wxpay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coco.wxpay.MainActivity;
import com.coco.wxpay.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    MainActivity pmyid = new MainActivity();
    private RelativeLayout mLayout;
    private static final String TAG = "WXPayEntryActivity";
    private TextView tvOrderNo, tvOrderTime, tvMoney;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_results);
        api = WXAPIFactory.createWXAPI(this, pmyid.GetappId());//appid需换成商户自己开放平台appid
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        String openId = resp.openId;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            // resp.errCode == -1 原因：支付错误,可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
            // resp.errCode == -2 原因 用户取消,无需处理。发生场景：用户不支付了，点击取消，返回APP
            Log.e(TAG, "onResp: " + resp.errStr);
            if (resp.errCode == 0){
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onResp: " + resp.errStr);
            } else {
                Toast.makeText(this, getString(R.string.action_cancel) + resp.errCode + "test", Toast.LENGTH_SHORT)
                        .show();
                Log.e(TAG, "onResp: ==========" +openId+ resp.errStr);

                finish();
            }
        }
    }
}