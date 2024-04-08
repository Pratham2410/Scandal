package com.example.scandal;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles sending Firebase Cloud Messaging (FCM) notifications.
 */
public class FcmNotificationsSender extends FirebaseMessagingService {

    String userFcmToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = "AAAA_ohTcSQ:APA91bHal-yK8lnVIXKtvM5jFI33nJiBIskrKmU3wScympLANVMEJ69IBMtKKc32GXudzyUvHtvB1NpphQ4yT4m3CO9JFhtI4xahbpb_q236Pxj2ZF5QeFoAOht4-09OpeCRzZkRXMQb"; // Replace with your actual FCM server key

    /**
     * Constructor for FcmNotificationsSender.
     *
     * @param userFcmToken Firebase Cloud Messaging token of the user
     * @param title        Title of the notification
     * @param body         Body text of the notification
     * @param mContext     Context reference
     * @param mActivity    Activity reference
     */

    public FcmNotificationsSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public void SendNotifications() {
        requestQueue = Volley.newRequestQueue(mContext);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", body);

            // If you want to send data along with notifications, you can also put a JSON object for data
            //JSONObject dataObj = new JSONObject();
            //dataObj.put("key1", "value1");
            //dataObj.put("key2", "value2");
            //mainObj.put("data", dataObj);

            mainObj.put("notification", notificationObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("MUR", "onResponse: " + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: " + error.networkResponse);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;
                }
            };

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
