package seatback.com.seatback.services;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import seatback.com.seatback.R;
import seatback.com.seatback.activities.MainActivity;
import seatback.com.seatback.application.SeatBackApplication;
import seatback.com.seatback.fragments.HomeFragment;
import seatback.com.seatback.utils.Utils;

/**
 * Created by naji on 9/18/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    SeatBackApplication helper = SeatBackApplication.getInstance();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        String endpointURL = Utils.getServerURL() + "/updatePushToken";
        // updating the server with the data record
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id", Utils.getDeviceID(this.getBaseContext()));
            jsonObject.put("token", token);
            jsonObject.put("lang", Locale.getDefault().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Instantiate the RequestQueue.

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(endpointURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                }
            }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_key",Utils.getAPITokenId());
                //..add other headers
                return params;
            }
        };
        // Add the request to the RequestQueue.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        helper.add(jsonObjectRequest);
    }
}
