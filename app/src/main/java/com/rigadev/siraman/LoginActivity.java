package com.rigadev.siraman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.google.android.material.snackbar.Snackbar;
import com.rigadev.siraman.databinding.ActivityLoginBinding;
import com.rigadev.siraman.util.MyConfig;
import com.rigadev.siraman.util.NetworkState;
import com.rigadev.siraman.util.SessionLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        if (new SessionLogin(context).isLoggedin()){
            startActivity(new Intent(context, HomeActivity.class));
            finish();
        }

        ProgressLoadingJIGB.setupLoading = (setup) ->  {
            setup.srcLottieJson = R.raw.loader; // Tour Source JSON Lottie
            setup.message = "Mohon Menunggu";//  Center Message
            setup.timer = 0;   // Time of live for progress.
            setup.width = 200; // Optional
            setup.hight = 200; // Optional
        };

        if (MyConfig.isNetworkConnected(context)== false){
            MyConfig.showToast(context, "Tidak terkoneksi ke Internet");
        }

        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etUsername.getText().toString().isEmpty()){
                    binding.validationEmail.setError("Mohon mengisi username");
                }else if (binding.etPassword.getText().toString().isEmpty()){
                    binding.validationPassword.setError("Mohon mengisi password");
                }else {
                    ProgressLoadingJIGB.startLoading(context);
                    checkLogin(binding.etUsername.getText().toString(), binding.etPassword.getText().toString(), v);
                }
            }
        });
    }

    private void checkLogin(final String username, final String password, final View v) {
        String url = NetworkState.getIp()+"login.php";
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", "Login Response: " + response.toString());
                ProgressLoadingJIGB.finishLoadingJIGB(context);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int succes = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    if (succes == 1) {

                        String username = jsonObject.getString("username");
                        String id = jsonObject.getString("id");
                        String level = jsonObject.getString("level");
                        new SessionLogin(context).login(id,username,level);
                        Snackbar.make(v, "Login Berhasil, Anda Akan Dialihkan", Snackbar.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        MyConfig.showToast(context, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                ProgressLoadingJIGB.finishLoadingJIGB(context);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        mRequestQueue.add(stringRequest);

    }
}