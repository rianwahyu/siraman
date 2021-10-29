package com.rigadev.siraman.ui.riwayat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.rigadev.siraman.Adapter.AdapterHeader;
import com.rigadev.siraman.Adapter.AdapterValue;
import com.rigadev.siraman.Model.DataHeader;
import com.rigadev.siraman.Model.DataValue;
import com.rigadev.siraman.R;
import com.rigadev.siraman.databinding.FragmentRiwayatBinding;
import com.rigadev.siraman.ui.HistoryActivity;
import com.rigadev.siraman.util.HeaderManager;
import com.rigadev.siraman.util.ItemClickListener;
import com.rigadev.siraman.util.KCal;
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

public class RiwayatFragment extends Fragment implements ItemClickListener {

    FragmentRiwayatBinding binding;

    private List<DataHeader> listHeader = new ArrayList<DataHeader>();
    List<DataHeader> listHeaderSearch = new ArrayList<DataHeader>();

    private List<DataValue> listValue = new ArrayList<DataValue>();

    AdapterHeader adapterHeader, adapterHeaderSearch;
    AdapterValue adapterValue;

    int sumTotals=0;
    int parseTotal=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_riwayat, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRC();

        binding.textSearch.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        binding.textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listHeaderSearch = new ArrayList<DataHeader>();

                for (int i=0; i<listHeader.size(); i++){
                    DataHeader item = listHeader.get(i);
                    if (item.getInvoice().toUpperCase().contains(s.toString()) ||
                            item.getPaidValue().toUpperCase().contains(s.toString()) ||
                            item.getSalesValue().toUpperCase().contains(s.toString()) ||
                            item.getReturnValue().toUpperCase().contains(s.toString())){
                        listHeaderSearch.add(item);
                    }
                }

                if (s.length() < 1){
                    binding.recylceHeader.swapAdapter(adapterHeader, false);
                    //textResult.setVisibility(View.GONE);
                }else {
                    adapterHeaderSearch = new AdapterHeader(getActivity(), listHeaderSearch, RiwayatFragment.this);
                    adapterHeaderSearch.notifyDataSetChanged();
                    binding.recylceHeader.swapAdapter(adapterHeaderSearch, false);
                    binding.recylceHeader.setAdapter(adapterHeaderSearch);
                    //adapteritem.notifyDataSetChanged();
                }
            }
        });
    }

    void initRC(){
        listHeader = new ArrayList<DataHeader>();
        binding.recylceHeader.setHasFixedSize(true);
        binding.recylceHeader.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recylceHeader.setLayoutManager(layoutManager);
        adapterHeader = new AdapterHeader(getActivity(), listHeader, RiwayatFragment.this);
        binding.recylceHeader.setAdapter(adapterHeader);
        adapterHeader.setClickListener(this);
        String username = new SessionLogin(getActivity()).getUsername();

        ProgressLoadingJIGB.setupLoading = (setup) ->  {
            setup.srcLottieJson = R.raw.loader; // Tour Source JSON Lottie
            setup.message = "Memuat Data";//  Center Message
            setup.timer = 0;   // Time of live for progress.
            setup.width = 200; // Optional
            setup.hight = 200; // Optional
        };

        callHistoryHeader(username);

        listValue = new ArrayList<DataValue>();
        binding.recycleValue.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        binding.recycleValue.setLayoutManager(layoutManager1);
        adapterValue = new AdapterValue(getActivity(), listValue);
        binding.recycleValue.setAdapter(adapterValue);
    }

    public void callHistoryHeader(final String username){
        ProgressLoadingJIGB.startLoading(getContext());
        String url = NetworkState.getIp()+"getHistoryHeader.php" ;
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ProgressLoadingJIGB.finishLoadingJIGB(getContext());
                        if(!response.isEmpty()){
                            try {
                                JSONArray jsonarray=new JSONArray(response);

                                for(int i=0;i<jsonarray.length();i++)
                                {
                                    JSONObject users = jsonarray.getJSONObject(i);

                                    String invoice = users.getString("invoice");
                                    String store = users.getString("store");
                                    String user = users.getString("user");
                                    String time = users.getString("time");
                                    String salesValue = users.getString("salesValue");
                                    String paidValue = users.getString("paidValue");
                                    String returnValue = users.getString("returnValue");
                                    String year = users.getString("year");

                                    KCal kcal = new KCal();
                                    String tglKonvert = kcal.konvertdate(time,
                                            "yyyy-MM-dd" , "dd-MM-yyyy");
                                    DataHeader dataHeader = new DataHeader(invoice, store, user, tglKonvert,
                                            salesValue,paidValue,returnValue,year);
                                    listHeader.add(dataHeader);
                                }
                            } catch (JSONException e) {
                                Log.e("catchException",e.toString());
                                e.printStackTrace();
                            }


                            adapterHeader.notifyDataSetChanged();
                        }
                        else {
                            Log.e("error","Empty Response");
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ProgressLoadingJIGB.finishLoadingJIGB(getContext());
                        Log.e("error",error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",username );
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view, int position) {
        DataHeader dataBarang = listHeader.get(position);
        final String invoice = dataBarang.getInvoice().toString();
        final String store = dataBarang.getStore();
        final String user = dataBarang.getUser();
        final String time = dataBarang.getTime();
        final String sales = dataBarang.getSalesValue();
        final String paid = dataBarang.getPaidValue();
        final String returnValue = dataBarang.getReturnValue();
        final String year = dataBarang.getYear();
        callHistoryValue(invoice);
        new HeaderManager(getActivity()).header(invoice,store,user,time,sales,paid,returnValue,year);
    }

    public void callHistoryValue(final String invoice){
        listValue.clear();
        sumTotals=0;
        parseTotal=0;
        ProgressLoadingJIGB.startLoading(getContext());
        String url = NetworkState.getIp()+"getHistoryValue.php" ;
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ProgressLoadingJIGB.finishLoadingJIGB(getContext());
                        if(!response.isEmpty()){
                            try {
                                JSONArray jsonarray=new JSONArray(response);

                                for(int i=0;i<jsonarray.length();i++)
                                {
                                    JSONObject users = jsonarray.getJSONObject(i);

                                    String invoice = users.getString("invoice");
                                    String barcode = users.getString("barcode");
                                    String qty = users.getString("qty");
                                    String price = users.getString("price");
                                    String name = users.getString("name");
                                    String store = users.getString("store");
                                    String total = users.getString("total");
                                    String aliasName = users.getString("alias_name");

                                    DataValue dataValue = new DataValue(invoice, barcode,store, name,
                                            qty, price, total, aliasName);
                                    listValue.add(dataValue);
                                    parseTotal = Integer.parseInt(total);

                                    sumTotals +=parseTotal;

                                }
                            } catch (JSONException e) {
                                Log.e("catchException",e.toString());
                                e.printStackTrace();
                            }


                            adapterValue.notifyDataSetChanged();

                            String totalSales = String.valueOf(sumTotals);

                            binding.textTotalSales.setText("Rp. " + MyConfig.formatNumberComma(totalSales));

                            binding.btnReprint.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogReprint();
                                }
                            });

                        }
                        else {
                            Log.e("error","Empty Response");
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ProgressLoadingJIGB.finishLoadingJIGB(getContext());
                        Log.e("error",error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("invoice",invoice );
                return params;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        mRequestQueue.add(stringRequest);
    }

    private void dialogReprint() {
        new IonAlert(getContext(), IonAlert.WARNING_TYPE)
                .setTitleText("Cetak Struk")
                .setContentText("Apakah anda ingin mencetak ulang struk ?")
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
                        Intent intent = new Intent(getContext(), HistoryActivity.class);
                        intent.putExtra("username", new HeaderManager(getContext()).getUser());
                        intent.putExtra("store", new HeaderManager(getContext()).getStore());
                        intent.putExtra("paidValue", new HeaderManager(getContext()).getPaid());
                        intent.putExtra("salesValue",new HeaderManager(getContext()).getSales());
                        intent.putExtra("returnPays", new HeaderManager(getContext()).getReturn());
                        intent.putExtra("invoice", new HeaderManager(getContext()).getInvoice());
                        intent.putExtra("listValue", (Serializable) listValue);
                        startActivity(intent);
                        getActivity().finish();

                    }
                })
                .show();
    }
}