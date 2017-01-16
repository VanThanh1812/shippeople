package com.shipfindpeople.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.shipfindpeople.app.BaseActivity;
import com.shipfindpeople.app.R;
import com.shipfindpeople.app.ServiceAPI;
import com.shipfindpeople.app.model.requestbody.LoginParamsBody;
import com.shipfindpeople.app.model.response.CommonResponse;
import com.shipfindpeople.app.model.response.LoginResponse;
import com.shipfindpeople.app.model.response.LoginViaFacebookResponse;
import com.shipfindpeople.app.storage.AppPreference;
import com.shipfindpeople.app.storage.UserSaved;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity {

    private CallbackManager mCallbackManager;
    LoginButton mBtnFacebookLogin;
    TextView mButtonLogin;
    UserSaved userSaved;

    private Gson mGson;
    ServiceAPI stackOverflowAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mGson = new Gson();
        userSaved = new UserSaved(AppPreference.getInstance(this));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.shiptimnguoi.vn/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stackOverflowAPI = retrofit.create(ServiceAPI.class);
        if (userSaved.getId() > 0) {
            redirectToMain();
            stackOverflowAPI
                    .Accept(userSaved.getId())
                    .enqueue(new Callback<CommonResponse<LoginResponse>>() {
                        @Override
                        public void onResponse(Call<CommonResponse<LoginResponse>> call, Response<CommonResponse<LoginResponse>> response) {
                            LoginResponse result = response.body().getData();
                            userSaved.setName(result.getName());
                            userSaved.setEmail(result.getEmail());
                            userSaved.setGender(result.getGender());
                            userSaved.setId(result.getId());
                            userSaved.setAvatar(result.getUrlAvatar());
                            userSaved.setPhone(result.getPhone());
                            userSaved.setCoin(result.getCoin());
                            userSaved.setDateExpire(result.getDateUse());
                        }

                        @Override
                        public void onFailure(Call<CommonResponse<LoginResponse>> call, Throwable t) {

                        }
                    });
        }

        mCallbackManager = CallbackManager.Factory.create();
        mBtnFacebookLogin = (LoginButton) findViewById(R.id.sign_in_facebook);
        mButtonLogin = (TextView) findViewById(R.id.dummy_button);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViaFb();
            }
        });
    }

    public void loginViaFb() {
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mBtnFacebookLogin.performClick();
        mBtnFacebookLogin.setReadPermissions(Arrays.asList("email", "user_photos", "public_profile", "user_location"));
        mBtnFacebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (loginResult != null) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(final JSONObject user, GraphResponse response) {
                                    LoginViaFacebookResponse result = mGson.fromJson(user.toString(), LoginViaFacebookResponse.class);
                                    LoginParamsBody paramsBody = new LoginParamsBody();
                                    paramsBody.setEmail(result.getEmail());
                                    paramsBody.setName(result.getName());
                                    paramsBody.setSource("Facebook");
                                    paramsBody.setUsername(result.getId());
                                    paramsBody.setGender(result.getGender());

                                    onLogin(paramsBody);
                                    //Log.i(TAG, user.toString());
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                showToast("Đăng nhập bị hủy bỏ");
            }

            @Override
            public void onError(FacebookException e) {
                showToast("Lỗi kết nối");
            }
        });
    }

    public void onLogin(LoginParamsBody paramsBody) {

        stackOverflowAPI
                .Login(paramsBody)
                .enqueue(new Callback<CommonResponse<LoginResponse>>() {
                    @Override
                    public void onResponse(Call<CommonResponse<LoginResponse>> call, Response<CommonResponse<LoginResponse>> response) {
                        if (!response.body().isError()) {

                            LoginResponse result = response.body().getData();
                            userSaved.setName(result.getName());
                            userSaved.setEmail(result.getEmail());
                            userSaved.setGender(result.getGender());
                            userSaved.setId(result.getId());
                            userSaved.setAvatar(result.getUrlAvatar());
                            userSaved.setPhone(result.getPhone());
                            userSaved.setCoin(result.getCoin());
                            userSaved.setDateExpire(result.getDateUse());

                            showToast("Xin chào :" + result.getName());

                            redirectToMain();
                        } else {
                            showToast(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse<LoginResponse>> call, Throwable t) {
                        showToast("Đăng nhập thất bại");
                    }
                });
    }

    public void redirectToMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
