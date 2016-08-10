package com.example.administrator.shareproject;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class MainActivity extends Activity implements PlatformActionListener{

    private static final int SINAWEIBO = 1;
    private static final int QQSHARE = 2;
    private static final int WECHAT = 3;
    private static final int WECHAT_MOMENT = 4;
    private static final int CANCEL = 5;
    private static final int ERROR = 6;

    private Button shareButton;
    private ShareDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shareButton = (Button) findViewById(R.id.button1);
        shareButton.setOnClickListener(new sharebuttonClickListener());
        //在所有ShareSDK操作前必须初始化SDK,否则会报空指针异常
        ShareSDK.initSDK(MainActivity.this);
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

        String name = platform.getName();
        if(name.equals(SinaWeibo.NAME)){
            handler.sendEmptyMessage(SINAWEIBO);
        }else if (name.equals(QQ.NAME)){
            handler.sendEmptyMessage(QQSHARE);
        }else if (name.equals(Wechat.NAME)){
            handler.sendEmptyMessage(WECHAT);
        }else if (name.equals(WechatMoments.NAME)){
            handler.sendEmptyMessage(WECHAT_MOMENT);
        }

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

        throwable.printStackTrace();
        Message msg = new Message();
        msg.what = ERROR;
        msg.obj = throwable.getMessage();
        handler.sendMessage(msg);

    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(CANCEL);

    }

    class sharebuttonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            showShare();
        }
    }

    private void showShare() {
        dialog = new ShareDialog(this);
        dialog.setCancelButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setGridViewListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,Object> item = (HashMap<String, Object>) adapterView.getItemAtPosition(i);
                String text = (String) item.get("Text");
                switch (text){
                    case "新浪微博":
                        Platform.ShareParams sp1 = new Platform.ShareParams();
                        sp1.setText("我是分享文本。。。");
                        sp1.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");

                        Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                        sina.setPlatformActionListener(MainActivity.this);
                        sina.share(sp1);
                        break;
                    case "QQ":
                        Platform.ShareParams sp2 = new Platform.ShareParams();
                        sp2.setTitle("我是分享标题~~");
                        sp2.setText("我是分享文本。。。");
                        sp2.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");
                        sp2.setTitleUrl("http://www.baidu.com");

                        Platform tencent = ShareSDK.getPlatform(QQ.NAME);
                        tencent.setPlatformActionListener(MainActivity.this);
                        tencent.share(sp2);

                        break;
                    case "微信朋友":
                        Platform.ShareParams sp3 = new Platform.ShareParams();
                        sp3.setTitle("我是分享标题~~");
                        sp3.setText("我是分享文本。。。");
                        sp3.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");

                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(MainActivity.this);
                        wechat.share(sp3);
                        break;
                    case "微信朋友圈":
                        Platform.ShareParams sp4 = new Platform.ShareParams();
                        sp4.setTitle("我是分享标题~~");
                        sp4.setText("我是分享文本。。。");
                        sp4.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");

                        Platform wechatMoment = ShareSDK.getPlatform(WechatMoments.NAME);
                        wechatMoment.setPlatformActionListener(MainActivity.this);
                        wechatMoment.share(sp4);
                        break;
                    default: break;
                }
                dialog.dismiss();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SINAWEIBO:
                    Toast.makeText(getApplicationContext(),"新浪微博分享成功",
                            Toast.LENGTH_LONG).show();
                    break;
                case QQSHARE:
                    Toast.makeText(getApplicationContext(),"QQ分享成功",
                            Toast.LENGTH_LONG).show();
                    break;
                case WECHAT:
                    Toast.makeText(getApplicationContext(),"微信分享成功",
                            Toast.LENGTH_LONG).show();
                    break;
                case WECHAT_MOMENT:
                    Toast.makeText(getApplicationContext(),"微信朋友圈分享成功",
                            Toast.LENGTH_LONG).show();
                    break;
                case CANCEL:
                    Toast.makeText(getApplicationContext(),"取消分享",
                            Toast.LENGTH_LONG).show();
                    break;
                case ERROR:
                    Toast.makeText(getApplicationContext(),"分享失败",
                            Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };





}
