package com.shipfindpeople.app.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shipfindpeople.app.GPSTracker;
import com.shipfindpeople.app.R;
import com.shipfindpeople.app.RegionCodeEnum;
import com.shipfindpeople.app.adapter.NotificationAdapter;
import com.shipfindpeople.app.model.pojo.Notification;
import com.shipfindpeople.app.storage.AppPreference;
import com.shipfindpeople.app.storage.UserSaved;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;

public class NotificationTabFragment extends AffiliateBaseFragment {

    private int mTabPosition;
    GPSTracker mGPS;
    public static final int LATITUDE_OF_BOUNDARY = 17;

    private static final String TAG = "FShipService";
    private static final String TOPIC_NOTIFY = "/5ship/shipper/facebookOrder";
    public static final String TOPIC_CONNECTED = "/5ship/connected";
    public static final String TOPIC_WILL = "/5ship/will";
    public static final String SERVER_MQTT = "tcp://mqtt.5ship.vn:1883";
    public static final String USERNAME_MQTT = "5ship";
    public static final String PASSWORD_MQTT = "bvn5ship@2016";

    private final int KEEP_ALIVE_TIME_INTERVAL = 30 * 1000;
    private final int RETRY_TIME_INTERVAL = 15 * 1000;
    private final int MAX_DATA_SIZE = 100;
    private boolean mKeepAlive;
    private boolean mRetryConnect;

    private String mClientMQTTId;
    private MqttAndroidClient client;
    private IMqttToken token;
    private Runnable reconnect;
    private Runnable keepAlive;
    private boolean mIsViewDestroyed;
    private Gson mGson;
    private UserSaved mUser;

    @BindView(R.id.rv_data_binding)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar loadingBar;
    ArrayList<Notification> data;
    /*
    * Thành code
    * */
    @BindView(R.id.bt_reload)
    Button btn_reload;
    @BindView(R.id.tv_notify)
    TextView tv_notify;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;

    NotificationAdapter adapter;

    public NotificationTabFragment() {
        // Required empty public constructor
    }

    public static NotificationTabFragment newInstance(int position) {
        NotificationTabFragment fragment = new NotificationTabFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabPosition = getArguments().getInt("position", 0);
        mGson = new Gson();
        mUser = new UserSaved(AppPreference.getInstance(mContext));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    98);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mGPS = new GPSTracker(mContext);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_notification_tab;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkInternet();// có thì load data ko thì nghỉ
    }

    private void checkInternet() {
        loadingBar.setVisibility(View.VISIBLE);
        try {
            if (isInternet()){
                relativeLayout.setVisibility(View.GONE);
                loadData();
            }else {
                loadingBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                btn_reload.setVisibility(View.VISIBLE);
                btn_reload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkInternet();
                    }
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        data = new ArrayList<>();
        adapter = new NotificationAdapter(mContext);
        mGPS = new GPSTracker(mContext);
        if (mTabPosition != 0) {
            if (!mUser.getOrder().isEmpty())
                data = mGson.fromJson(mUser.getOrder(), new TypeToken<ArrayList<Notification>>() {
                }.getType());

            BroadcastReceiver bReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("com.shipfindpeople.app.NOTIFICATION")) {
                        Notification notification = (Notification) intent.getSerializableExtra("notification");
                        data.add(0, notification);
                        adapter.notifyItemInserted(0);
                        if (data.size() > 20) {
                            data.remove(20);
                            adapter.notifyItemRemoved(20);
                        }
                    }
                }
            };

            LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(mContext);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.shipfindpeople.app.NOTIFICATION");
            bManager.registerReceiver(bReceiver, intentFilter);

            loadingBar.setVisibility(View.GONE);

        } else {
            reconnect();
        }

        adapter.setArrayList(data);
        mRecyclerView.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public boolean isInternet() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }

    private void connect() {

        mClientMQTTId = getMQTTClientId();

        MqttConnectOptions options;
        options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(USERNAME_MQTT);
        options.setPassword(PASSWORD_MQTT.toCharArray());

        try {
            options.setWill(TOPIC_WILL, mClientMQTTId.getBytes("UTF-8"), 1, false);
        } catch (Exception e) {
            //
        }
        //options.setConnectionTimeout(5000);
        //options.setKeepAliveInterval(1000 * 60 * 90);

        client = new MqttAndroidClient(mContext, SERVER_MQTT, mClientMQTTId);

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                if (!mRetryConnect && !mIsViewDestroyed) {

                    enableRetryConnect();
                    stopKeepAlive();

                    reconnectIfNecessary();
                }
            }

            @Override
            public void messageArrived(String topic, final MqttMessage message) {

                Notification notification = new Notification();

                try {
                    JSONObject jObj = new JSONObject(new String(message.getPayload()));
                    if (mGPS.getLatitude() != 0) {
                        if (jObj.has("RegionCode")
                                && jObj.getInt("RegionCode") == RegionCodeEnum.Hochiminh.getRegionCode()
                                && Double.compare(mGPS.getLatitude(), LATITUDE_OF_BOUNDARY) >= 0) {
                            return;
                        }

                        if ((!jObj.has("RegionCode") || jObj.getInt("RegionCode") == RegionCodeEnum.Hanoi.getRegionCode())
                                && Double.compare(mGPS.getLatitude(), LATITUDE_OF_BOUNDARY) < 0) {
                            return;
                        }
                    }

                    if(!jObj.getString("PostId").isEmpty()) {
                        notification.setContent(jObj.getString("Details"));
                        notification.setCreatorId(jObj.getString("Summary"));
                        notification.setPostId(jObj.getString("PostId"));
                        notification.setCreatorName(jObj.getString("Name"));
                        notification.setPhone(jObj.getString("Phones"));
                        notification.setDateCreated(jObj.has("DateCreated") ? jObj.getString("DateCreated") : "");

                        data.add(0, notification);
                        adapter.notifyItemInserted(0);
                        if (data.size() > MAX_DATA_SIZE) {
                            data.remove(data.size() - 1);
                            adapter.notifyItemRemoved(data.size() - 1);
                        }

                        if (mRecyclerView.computeVerticalScrollOffset() == 0) {
                            mRecyclerView.scrollToPosition(0);
                        }
                        loadingBar.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    //
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }

        });

        try {
            token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    if (!mKeepAlive && !mIsViewDestroyed) {

                        stopRetryConnect();
                        enableKeepAlive();
                        subscribeNotify();
                        publishConnected();
                        startKeepAlive();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    if (!mRetryConnect && !mIsViewDestroyed) {
                        enableRetryConnect();
                        stopKeepAlive();
                        reconnectIfNecessary();
                    }
                }

            });
        } catch (Exception e) {
            //
        }

    }

    private void publishConnected() {
        try {
            client.publish(TOPIC_CONNECTED, mClientMQTTId.getBytes("UTF-8"), 1, false);
        } catch (Exception e) {
            //
        }
    }

    private void subscribeNotify() {
        try {
            String[] arrTopic = new String[]{TOPIC_NOTIFY};
            int[] arrQos = new int[]{0};
            client.subscribe(arrTopic, arrQos);
        } catch (Exception e) {
            //
        }
    }

    // reconnect to mqtt server
    private synchronized void reconnect() {
        if (client == null || !client.isConnected()) {
            if (client != null) stop();
            connect();
        }
    }

    // keeping connection alive method
    private void startKeepAlive() {

        final Handler handler = new Handler();

        keepAlive = new Runnable() {
            @Override
            public void run() {

                if (mKeepAlive) {
                    reconnect();
                    handler.postDelayed(keepAlive, KEEP_ALIVE_TIME_INTERVAL);
                }
            }
        };

        handler.postDelayed(keepAlive, KEEP_ALIVE_TIME_INTERVAL);

    }

    //try to reconnect to server every specific second
    private void reconnectIfNecessary() {

        final Handler handler = new Handler();

        reconnect = new Runnable() {
            @Override
            public void run() {

                if (mRetryConnect) {
                    //Toast.makeText(mContext, "Đang thử kết nối lại...", Toast.LENGTH_SHORT).show();
                    reconnect();
                    handler.postDelayed(reconnect, RETRY_TIME_INTERVAL);
                }
            }
        };

        handler.postDelayed(reconnect, RETRY_TIME_INTERVAL);

    }

    private void stopKeepAlive() {
        mKeepAlive = false;
    }

    private void enableKeepAlive() {
        mKeepAlive = true;
    }

    private void stopRetryConnect() {
        mRetryConnect = false;
    }

    private void enableRetryConnect() {
        mRetryConnect = true;
    }


    //disconnect to mqtt server
    private synchronized void stop() {
        // Disconnect the MQTT connection if there is one
        if (client != null) {
            try {
                client.disconnect();
                client = null;
                token = null;
            } catch (Exception e) {
            }
        }
    }
    //endregion

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            // Destroy the MQTT connection if there is one
            if (client != null) {
                client.unregisterResources();
                client.close();
            }
        } catch (Exception e) {
        }

        stop();
        stopKeepAlive();
        stopRetryConnect();
        mIsViewDestroyed = true;
    }

    public String getMQTTClientId() {

        try {
            return "5ship_shipper_27_" + getUuid() + "_android_facebook";
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return MqttClient.generateClientId();
    }

    public String getUuid() {
        try {
            return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
