package example.caiworld.caihao.lylgapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by caihao on 2017/5/14.
 */
public class MyApplication extends Application {
    // 上下文菜单
    private Context mContext;

    // 记录是否已经初始化
    private boolean isInit = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // 初始化环信SDK
        initEasemob();
        //初始化百度地图sdk
        SDKInitializer.initialize(getApplicationContext());
        // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        JPushInterface.init(getApplicationContext());
        //默认初始化bmob
        Bmob.initialize(this, "185d4c4109490b94a8fc951c9763375b");

//        //默认初始化bmob
//        Bmob.initialize(this, "185d4c4109490b94a8fc951c9763375b");
    }

    /**
     * 初始化环信SDK
     */
    private void initEasemob() {

        if (EaseUI.getInstance().init(mContext, initOptions())) {

            // 设置开启debug模式
            EMClient.getInstance().setDebugMode(true);

            // 设置初始化已经完成
            isInit = true;
        }


    }

    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
     */
    private EMOptions initOptions() {

        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
//        options.setRequireServerAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);

        return options;
    }

    private static Map<String, Activity> destroyMap = new HashMap<>();

    /**
     * 将某个activity添加到销毁队列
     *
     * @param activity
     * @param activityName
     */
    public static void addDestroyActivity(Activity activity, String activityName) {
        destroyMap.put(activityName, activity);
    }

    /**
     * 销毁指定的activity
     *
     * @param activityName
     */
    public static void destroyActivity(String activityName) {
        Set<String> keySet = destroyMap.keySet();
        for (String key : keySet) {
            destroyMap.get(key).finish();
        }
    }

}
