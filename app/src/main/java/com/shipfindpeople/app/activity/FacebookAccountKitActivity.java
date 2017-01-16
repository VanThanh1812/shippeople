package com.shipfindpeople.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;
import com.shipfindpeople.app.BaseActivity;
import com.shipfindpeople.app.storage.IntentBundleKey;

/**
 * Created by sonnd on 10/4/2016.
 */

public class FacebookAccountKitActivity extends BaseActivity {

    private static final String TAG = FacebookAccountKitActivity.class.getSimpleName();

    public static final int APP_REQUEST_CODE = 99;
    public static final int REQUEST_PERMISSION_CODE = 98;
    private String phoneNumberString = "";
    private String defaultPhoneNumber = "";
    private boolean isActivityCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        AccountKit.initialize(this);

        defaultPhoneNumber = getIntent().getStringExtra(IntentBundleKey.PHONE_NUMBER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                    REQUEST_PERMISSION_CODE);
        } else {
            onLoginPhone();
        }
    }


    public void onLoginPhone() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);

        if (defaultPhoneNumber != null && defaultPhoneNumber.isEmpty()) {
            configurationBuilder.setInitialPhoneNumber(new PhoneNumber("+84", defaultPhoneNumber));
        }
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityCreated) {
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    // Get phone number
                    PhoneNumber phoneNumber = account.getPhoneNumber();
                    phoneNumberString = phoneNumber.toString();
                    if (phoneNumberString != null && !phoneNumberString.isEmpty()) {
                        Intent data = new Intent();
                        data.putExtra(IntentBundleKey.PHONE_NUMBER, phoneNumberString);
                        data.putExtra(IntentBundleKey.IS_SUCCESS, true);
                        setResult(RESULT_OK, data);
                        finish();
                    } else {

                        Intent data = new Intent();
                        data.putExtra(IntentBundleKey.IS_SUCCESS, false);
                        setResult(RESULT_OK, data);
                        finish();
                    }

                    AccountKit.logOut();
                }

                @Override
                public void onError(final AccountKitError error) {
                    Intent data = new Intent();
                    data.putExtra(IntentBundleKey.IS_SUCCESS, false);
                    setResult(RESULT_OK, data);

                    finish();
                }
            });
        }

        isActivityCreated = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case APP_REQUEST_CODE: // confirm that this response matches your request
                    AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
                    String logMessage;
                    if (loginResult.getError() != null) {
                        logMessage = loginResult.getError().getErrorType().getMessage();
                        //showErrorActivity(loginResult.getError());
                    } else if (loginResult.wasCancelled()) {
                        logMessage = "Login Cancelled";
                    } else {
                        if (loginResult.getAccessToken() != null) {
                            logMessage = "Success: " + loginResult.getAccessToken().getAccountId();
                        } else {
                            logMessage = String.format(
                                    "Success: %s...",
                                    loginResult.getAuthorizationCode().substring(0, 10));
                        }
                    }
                    Log.v(TAG, logMessage);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
            onLoginPhone();
        }
    }
}
