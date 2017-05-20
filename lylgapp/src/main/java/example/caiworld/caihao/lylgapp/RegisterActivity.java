package example.caiworld.caihao.lylgapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import example.caiworld.caihao.lylgapp.bean.LYLGBmobUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;//用户名，密码，确认密码
    private ProgressDialog dialog;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
    }

    /**
     * 注册按钮调用
     *
     * @param viwe
     */
    public void register(View viwe) {
        dialog = new ProgressDialog(this);
        dialog.setTitle("正在注册...");
        username = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
            //将密码框和确认框清空
            etPassword.setText("");
            etConfirmPassword.setText("");
            //让密码框获取焦点
            etPassword.requestFocus();
            return;
        }

        dialog.show();

        new Thread() {
            @Override
            public void run() {
                //TODO 这块逻辑
                /*
                 注册bmob
                     成功--
                         注册环信
                            成功--完成注册
                            失败--删除掉注册成功的bmob用户
                     失败--重新注册
                */
                LYLGBmobUser bmobUser = new LYLGBmobUser();
                bmobUser.setUsername(username);
                bmobUser.setPassword(password);
                bmobUser.setFriends("");
                bmobUser.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, final BmobException e) {
                        if (e == null) {//注册成功
                            //开始环信注册,在这块注册报错，可能是不能在这个方法中执行那段代码
//                            huanXinRegister();
                        } else {//注册失败
                            dialog.dismiss();
                            Log.e("Bmob失败：", e.getMessage());//Bmob失败：: unique index cannot has duplicate value: caihao
                            Log.e("BmobError:", e.getErrorCode() + "");//BmobError:: 401
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (e.getErrorCode() == 401 && e.getMessage().contains("unique index")) {
                                        Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "注册失败，请重新注册", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //注册失败，让其返回
                            return;
                        }

                    }
                });
                //Bmob注册成功后才开始环信注册
                huanXinRegister();
            }
        }.start();
    }

    /**
     * 环信注册
     */
    public void huanXinRegister() {
        try {
            EMClient.getInstance().createAccount(username, password);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!RegisterActivity.this.isFinishing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    //TODO 做本地化存储，把username和password存起来下次直接进入主页面，还有应该需要将username同时注册到bmob后端云
                    //注册成功，帮助用户登录，登录成功直接跳转到主页面
                    signIn();
                }
            });
        } catch (final HyphenateException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!RegisterActivity.this.isFinishing()) {
                        dialog.dismiss();
                    }
                    /**
                     * 关于错误码可以参考官方api详细说明
                     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                     */
                    int errorCode = e.getErrorCode();
                    String message = e.getMessage();
                    Log.d("lzan13", String.format("sign up - errorCode:%d, errorMsg:%s", errorCode, e.getMessage()));
                    switch (errorCode) {
                        // 网络错误
                        case EMError.NETWORK_ERROR:
                            Toast.makeText(RegisterActivity.this, "网络错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                            break;
                        // 用户已存在
                        case EMError.USER_ALREADY_EXIST:
                            Toast.makeText(RegisterActivity.this, "用户已存在 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                            break;
                        // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                        case EMError.USER_ILLEGAL_ARGUMENT:
                            Toast.makeText(RegisterActivity.this, "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                            break;
                        // 服务器未知错误
                        case EMError.SERVER_UNKNOWN_ERROR:
                            Toast.makeText(RegisterActivity.this, "服务器未知错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                            break;
                        case EMError.USER_REG_FAILED:
                            Toast.makeText(RegisterActivity.this, "账户注册失败 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(RegisterActivity.this, "ml_sign_up_failed code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                            break;
                    }

                    //删除掉注册的bmob用户
                    deleteBmobUser(username);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 环信注册失败时，删除掉bmob的注册的user
     *
     * @param user
     */
    public void deleteBmobUser(String user) {
        BmobQuery<LYLGBmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username", user);
        query.findObjects(new FindListener<LYLGBmobUser>() {
            @Override
            public void done(List<LYLGBmobUser> list, BmobException e) {
                if (e == null || list.size() != 0) {//查询成功，有此人
                    //需要删除掉user
                    LYLGBmobUser bmobUser = list.get(0);
                    bmobUser.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {//删除成功
                                Log.e("删除成功", "Success");
                            } else {//删除失败
                                Log.e("删除失败:", e.getMessage());
                            }
                        }
                    });
                } else {//查询失败，没有此人

                }
            }
        });
    }

    /**
     * 注册成功后直接登录，登录失败跳转至LoginActivity页面让其自己登录
     */
    public void signIn() {
        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                //登录成功的回调
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存，如果使用了群组的话
                        // EMClient.getInstance().groupManager().loadAllGroups();
                        //TODO 做本地化存储，将username和password保存到本地，下次直接进入主页面不用重新登录
                        // 登录成功跳转到主界面
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                        MyApplication.destroyActivity("LoginActivity");//同时将LoginActivity给finish掉
                    }
                });
            }

            @Override
            public void onError(final int i, final String s) {
                //登录错误，跳转到登录页面
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

}
