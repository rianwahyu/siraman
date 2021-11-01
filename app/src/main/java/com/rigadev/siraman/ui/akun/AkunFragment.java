package com.rigadev.siraman.ui.akun;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.rigadev.siraman.Adapter.AdapterValue3;
import com.rigadev.siraman.LoginActivity;
import com.rigadev.siraman.Model.DataValue2;
import com.rigadev.siraman.R;
import com.rigadev.siraman.databinding.FragmentAkunBinding;
import com.rigadev.siraman.ui.ClosingCafeActivity;
import com.rigadev.siraman.util.MyConfig;
import com.rigadev.siraman.util.NetworkState;
import com.rigadev.siraman.util.SessionLogin;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.ionbit.ionalert.IonAlert;

public class AkunFragment extends Fragment {

    FragmentAkunBinding binding;

    private List<DataValue2> listValue2 = new ArrayList<DataValue2>();
    AdapterValue3 adapterValue2;

    int sumTotals=0;
    int parseTotal=0;

    int sumTotals2=0;
    int parseTotal2=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_akun, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressLoadingJIGB.setupLoading = (setup) ->  {
            setup.srcLottieJson = R.raw.loader; // Tour Source JSON Lottie
            setup.message = "Memuat Data";//  Center Message
            setup.timer = 0;   // Time of live for progress.
            setup.width = 200; // Optional
            setup.hight = 200; // Optional
        };

        listValue2 = new ArrayList<DataValue2>();
        binding.recycleValueClosing.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        binding.recycleValueClosing.setLayoutManager(layoutManager2);
        adapterValue2 = new AdapterValue3(getActivity(), listValue2);
        binding.recycleValueClosing.setAdapter(adapterValue2);


        binding.cardClosing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                callClosing();
                binding.linearClosing.setVisibility(View.VISIBLE);
                /*binding.linearCard1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.linearCard2.setBackgroundColor(Color.parseColor("#FFFFFF"));*/
                binding.linearCard3.setBackgroundColor(Color.parseColor("#FFEA00"));
            }
        });

        binding.cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IonAlert(getContext(), IonAlert.ERROR_TYPE)
                        .setTitleText("Logout")
                        .setContentText("Apakah anda logout dari akun anda saat ini ?")
                        .setCancelText("Tidak")
                        .setConfirmText("Ya")
                        .showCancelButton(true)
                        .setCancelClickListener(new IonAlert.ClickListener() {
                            @Override
                            public void onClick(IonAlert sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new IonAlert.ClickListener() {
                            @Override
                            public void onClick(IonAlert ionAlert) {
                                ionAlert.dismiss();
                                new SessionLogin(getContext()).logout();
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .show();
            }
        });

    }


    void callClosing(){
        ProgressLoadingJIGB.startLoading(getContext());
        listValue2.clear();
        sumTotals2=0;
        parseTotal2=0;
        String url = NetworkState.getIp()+"getClosing.php" ;
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ProgressLoadingJIGB.finishLoadingJIGB(getContext());
                if(!response.isEmpty()){
                    try {
                        JSONArray jsonarray=new JSONArray(response);

                        for(int i=0;i<jsonarray.length();i++)
                        {
                            JSONObject users = jsonarray.getJSONObject(i);

                            String barcode = users.getString("barcode");
                            String qty = users.getString("totQty");
                            String price = users.getString("price");
                            String name = users.getString("name");

                            String total = users.getString("totalan");

                            DataValue2 dataValue = new DataValue2("", barcode,"", name,
                                    qty, price, total);
                            listValue2.add(dataValue);
                            parseTotal2 = Integer.parseInt(total);

                            sumTotals2 +=parseTotal2;

                        }
                    } catch (JSONException e) {
                        Log.e("catchException",e.toString());
                        e.printStackTrace();
                    }

                    adapterValue2.notifyDataSetChanged();

                    String totalSales = String.valueOf(sumTotals2);

                    binding.textTotalSalesClosing.setText("Rp. " + MyConfig.formatNumberComma(totalSales));

                    binding.btnReprintClosing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //dialogPrintClosing();
                            new IonAlert(getContext(), IonAlert.WARNING_TYPE)
                                    .setTitleText("Closing Cafe")
                                    .setContentText("Apakah anda ingin melakukan closing penjualan cafe ")
                                    .setCancelText("Tidak")
                                    .setConfirmText("Ya")
                                    .showCancelButton(true)
                                    .setCancelClickListener(new IonAlert.ClickListener() {
                                        @Override
                                        public void onClick(IonAlert sDialog) {
                                            sDialog.cancel();
                                        }
                                    })
                                    .setConfirmClickListener(new IonAlert.ClickListener() {
                                        @Override
                                        public void onClick(IonAlert ionAlert) {
                                            ionAlert.dismiss();
                                            updateCLosing();
                                        }
                                    })
                                    .show();
                        }
                    });

                }else{

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressLoadingJIGB.finishLoadingJIGB(getContext());
                Log.e("error",error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user",new SessionLogin(getActivity()).getUsername());
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        mRequestQueue.add(stringRequest);
    }


    int success;
    String message="";
    void updateCLosing(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        ProgressLoadingJIGB.setupLoading = (setup) ->  {
            setup.srcLottieJson = R.raw.loader; // Tour Source JSON Lottie
            setup.message = "Proses Ganti Shift";//  Center Message
            setup.timer = 0;   // Time of live for progress.
            setup.width = 200; // Optional
            setup.hight = 200; // Optional
        };
        ProgressLoadingJIGB.startLoading(getContext());

        String url = NetworkState.getIp()+"updateClosing.php" ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ProgressLoadingJIGB.finishLoadingJIGB(getContext());
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    success = jObj.getInt("success");
                    if (success==1){
                        message = jObj.getString("message");
                        new IonAlert(getContext(), IonAlert.SUCCESS_TYPE)
                                .setTitleText("Sukses Closing Cafe")
                                .setContentText("Apakah anda ingin melanjutkan cetak struk closing penjualan cafe ?")
                                .setCancelText("Tidak")
                                .setConfirmText("Ya")
                                .showCancelButton(true)
                                .setCancelClickListener(new IonAlert.ClickListener() {
                                    @Override
                                    public void onClick(IonAlert sDialog) {
                                        sDialog.cancel();
                                        new SessionLogin(getActivity()).logout();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                })
                                .setConfirmClickListener(new IonAlert.ClickListener() {
                                    @Override
                                    public void onClick(IonAlert ionAlert) {
                                        ionAlert.dismiss();
                                        Intent intent = new Intent(getContext(), ClosingCafeActivity.class);
                                        intent.putExtra("username", new SessionLogin(getContext()).getUsername());
                                        intent.putExtra("store", new SessionLogin(getContext()).getStore());
                                        intent.putExtra("listValue", (Serializable) listValue2);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                })
                                .show();
//                        showToast(message);
//                        printBillsClosing();
                    }else{
                        message = jObj.getString("message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressLoadingJIGB.finishLoadingJIGB(getContext());
                Log.e("error",error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user",new SessionLogin(getActivity()).getUsername());
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