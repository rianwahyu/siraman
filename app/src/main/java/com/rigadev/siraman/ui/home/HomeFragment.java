package com.rigadev.siraman.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.rigadev.siraman.Adapter.AdapterCart;
import com.rigadev.siraman.Adapter.AdapterCategoryCoffee;
import com.rigadev.siraman.Adapter.AdapterCategoryCuci;
import com.rigadev.siraman.Adapter.AdapterCategoryPromo;
import com.rigadev.siraman.Adapter.AdapterItem;
import com.rigadev.siraman.Adapter.AdapterItem2;
import com.rigadev.siraman.Adapter.AdapterItem3;
import com.rigadev.siraman.Model.DataCart;
import com.rigadev.siraman.Model.DataCategory;
import com.rigadev.siraman.Model.DataCategory2;
import com.rigadev.siraman.Model.DataCategory3;
import com.rigadev.siraman.Model.DataItem;
import com.rigadev.siraman.Model.DataItem2;
import com.rigadev.siraman.Model.DataItem3;
import com.rigadev.siraman.R;
import com.rigadev.siraman.databinding.FragmentHomeBinding;
import com.rigadev.siraman.ui.KeranjangActivity;
import com.rigadev.siraman.ui.PembayaranActivity;
import com.rigadev.siraman.util.ItemCategoryCoffeeListener;
import com.rigadev.siraman.util.ItemCategoryListener;
import com.rigadev.siraman.util.ItemCategoryPromoListener;
import com.rigadev.siraman.util.ItemClickCoffeeListener;
import com.rigadev.siraman.util.ItemClickListener;
import com.rigadev.siraman.util.ItemClickPromoListener;
import com.rigadev.siraman.util.ItemDeleteCartListener;
import com.rigadev.siraman.util.ItemQtyCartListener;
import com.rigadev.siraman.util.MyConfig;
import com.rigadev.siraman.util.NetworkState;
import com.rigadev.siraman.util.SQLiteHelpers;
import com.rigadev.siraman.util.SessionLogin;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.ionbit.ionalert.IonAlert;

public class HomeFragment extends Fragment implements ItemClickListener, ItemDeleteCartListener,
        ItemQtyCartListener, ItemCategoryListener, ItemCategoryCoffeeListener, ItemClickCoffeeListener, ItemClickPromoListener, ItemCategoryPromoListener {

    FragmentHomeBinding binding;
    private List<DataItem> listItem = new ArrayList<DataItem>();
    private List<DataCart> listCart = new ArrayList<DataCart>();
    private List<DataCategory> listCategory = new ArrayList<DataCategory>();
    private List<DataCategory2> listCategory2 = new ArrayList<DataCategory2>();
    private List<DataCategory3> listCategory3 = new ArrayList<DataCategory3>();
    private List<DataItem2> listItem2 = new ArrayList<DataItem2>();
    private List<DataItem3> listItem3 = new ArrayList<DataItem3>();

    AdapterItem adapteritem;
    AdapterItem2 adapterItem2;
    AdapterItem3 adapterItem3;
    AdapterCart adapterCart;
    AdapterCategoryCuci adapterCategoryCuci;
    AdapterCategoryCoffee adapterCategoryCoffee;
    AdapterCategoryPromo adapterCategoryPromo;
    int numberOfColumns = 3;

    Integer sumTotals=0;
    Integer sumItem=0;
    Integer parseQty=0;
    Integer parsePrice=0;
    String sumTotalString="";
    String paid;
    Integer jumlahbayar=0;
    Integer returnPay=0;
    Integer dibayar=0;
    EditText temp;

    SQLiteHelpers sqLiteHelpers;
    String subCategoryCuci="";
    String subCategoryCaffee="";
    String subCategoryPromo="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        /*View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;*/
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sqLiteHelpers = new SQLiteHelpers(getContext());

        binding.shimmer1.setVisibility(View.VISIBLE);
        binding.shimmer1.startShimmer();
        binding.shimmer2.setVisibility(View.VISIBLE);
        binding.shimmer2.startShimmer();
        binding.shimmer3.setVisibility(View.VISIBLE);
        binding.shimmer3.startShimmer();

        binding.frameMain.setVisibility(View.GONE);
        binding.frameCoffee.setVisibility(View.GONE);
        binding.framePromo.setVisibility(View.GONE);

        binding.relaCart.setVisibility(View.GONE);

        initRC();

        callCategory();
        callItem(subCategoryCuci);
        callCategory2();
        callItem2(subCategoryCaffee);
        callCategory3();
        callItem3(subCategoryPromo);

        binding.frameNumPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.frameNumPad.setVisibility(View.GONE);
                binding.frameMain.setVisibility(View.VISIBLE);
            }
        });

        binding.etPaid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int inType = binding.etPaid.getInputType(); // backup the input type
                binding.etPaid.setInputType(InputType.TYPE_NULL); // disable soft input
                binding.etPaid.onTouchEvent(motionEvent); // call native handler
                binding.etPaid.setInputType(inType); // restore input type

                binding.frameMain.setVisibility(View.GONE);
                binding.frameNumPad.setVisibility(View.VISIBLE);

                return true; // cons
            }
        });

        binding.etPaid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 1 || keycode != 66) {
                    return false;
                }
                binding.etPaid.setText(binding.etPaid.getText().toString().trim());
                return true;
            }
        });

        binding.etPaid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hashfocus) {
                if (hashfocus) {temp = binding.etPaid;}
            }
        });

        binding.t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != null) {
                    temp.setText(temp.getText().toString() + "1");
                }
            }
        });

        binding.t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != null) {
                    temp.setText(temp.getText().toString() + "2");
                }
            }
        });

        binding.t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != null) {
                    temp.setText(temp.getText().toString() + "3");
                }
            }
        });

        binding.t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != null) {
                    temp.setText(temp.getText().toString() + "4");
                }
            }
        });

        binding.t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != null) {
                    temp.setText(temp.getText().toString() + "5");
                }
            }
        });

        binding.t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != null) {
                    temp.setText(temp.getText().toString() + "6");
                }
            }
        });

        binding.t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != null) {
                    temp.setText(temp.getText().toString() + "7");
                }
            }
        });

        binding.t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != null) {
                    temp.setText(temp.getText().toString() + "8");
                }
            }
        });

        binding.t9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != null) {
                    temp.setText(temp.getText().toString() + "9");
                }
            }
        });

        binding.t0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != null) {
                    temp.setText(temp.getText().toString() + "0");
                }
            }
        });

        binding.tDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != null && temp.getText().length() != 0) {
                    temp.setText(temp.getText().toString().substring(0, temp.getText().length() - 1));
                }
            }
        });

        binding.etPaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.etPaid.removeTextChangedListener(this);
                Integer d;
                NumberFormat format = NumberFormat.getInstance(Locale.US);
                Number number = null;
                try {
                    String originalString = s.toString();
                    String oalah = s.toString();
                    number = format.parse(oalah);
                    d = number.intValue();
                    paid = binding.etPaid.getText().toString();
                    String formattedString = MyConfig.formatNumberComma(originalString);
                    binding.etPaid.setText(formattedString);
                    binding.etPaid.setSelection(binding.etPaid.getText().length());
                    jumlahbayar = Integer.valueOf(sumTotals);
                    dibayar = Integer.valueOf(d);
                    returnPay = dibayar - jumlahbayar;
                    String ntb = String.valueOf(returnPay);
                    String formattedkembali = MyConfig.formatNumberComma(ntb);
                    binding.textReturn.setText(formattedkembali);

                }catch (NumberFormatException e){
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                binding.etPaid.addTextChangedListener(this);
            }
        });

    }

    private void initRC() {

        listCategory = new ArrayList<DataCategory>();
        binding.recycleCategoryCuci.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recycleCategoryCuci.setLayoutManager(layoutManager1);
        adapterCategoryCuci = new AdapterCategoryCuci(getActivity(), listCategory);
        binding.recycleCategoryCuci.setAdapter(adapterCategoryCuci);
        adapterCategoryCuci.setClickListener(this);

        listItem = new ArrayList<DataItem>();
        binding.recycleItem.setHasFixedSize(true);
        binding.recycleItem.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        adapteritem = new AdapterItem(getActivity(), listItem, HomeFragment.this);
        binding.recycleItem.setAdapter(adapteritem);
        adapteritem.setClickListener(this);

        listCategory2 = new ArrayList<DataCategory2>();
        binding.recycleCategoryCoffee.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recycleCategoryCoffee.setLayoutManager(layoutManager2);
        adapterCategoryCoffee = new AdapterCategoryCoffee(getActivity(), listCategory2);
        binding.recycleCategoryCoffee.setAdapter(adapterCategoryCoffee);
        adapterCategoryCoffee.setCategoryCoffeListener(this);

        listItem2 = new ArrayList<DataItem2>();
        binding.recycleItemCoffee.setHasFixedSize(true);
        binding.recycleItemCoffee.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        adapterItem2 = new AdapterItem2(getActivity(), listItem2, HomeFragment.this);
        binding.recycleItemCoffee.setAdapter(adapterItem2);
        adapterItem2.setClickCoffeListener(this);

        listCategory3 = new ArrayList<DataCategory3>();
        binding.recycleCategoryPromo.setHasFixedSize(true);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recycleCategoryPromo.setLayoutManager(layoutManager3);
        adapterCategoryPromo = new AdapterCategoryPromo(getActivity(), listCategory3);
        binding.recycleCategoryPromo.setAdapter(adapterCategoryPromo);
        adapterCategoryPromo.setCategoryPrommoListener(this);

        listItem3 = new ArrayList<DataItem3>();
        binding.recycleItemPromo.setHasFixedSize(true);
        binding.recycleItemPromo.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapterItem3 = new AdapterItem3(getActivity(), listItem3, HomeFragment.this);
        binding.recycleItemPromo.setAdapter(adapterItem3);
        adapterItem3.setClickPromoListener(this);



        /*listCart = new ArrayList<DataCart>();
        binding.recycleCart.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recycleCart.setLayoutManager(layoutManager);
        adapterCart = new AdapterCart(getActivity(), listCart);
        binding.recycleCart.setAdapter(adapterCart);
        adapterCart.setClickListener(this);
        adapterCart.setQtyCatClickListener(this);*/
    }

    public void callCategory(){
        listCategory.clear();
        String url = NetworkState.getIp()+"getCategory.php" ;
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("category", response);
                        if(!response.isEmpty()){
                            try {
                                JSONArray jsonarray=new JSONArray(response);
                                /*DataCategory dataItems = new DataCategory("All Category");
                                listCategory.add(dataItems);*/
                                for(int i=0;i<jsonarray.length();i++)
                                {
                                    JSONObject users = jsonarray.getJSONObject(i);
                                    String sub_category = users.getString("sub_category");

                                    DataCategory dataItem = new DataCategory(sub_category);
                                    listCategory.add(dataItem);

                                }
                                adapterCategoryCuci.notifyDataSetChanged();
                            } catch (JSONException e) {
                                //showSnackBar("Koneksi ke Server Bermasalah, Silahkan Coba Lagi");
                                Log.e("catchException",e.toString());
                                e.printStackTrace();
                            }
                        }
                        else {
                            Log.e("error","Empty Response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("store", new SessionLogin(getActivity()).getStore() );
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        mRequestQueue.add(stringRequest);
    }

    public void callItem(final String subCategoryCuci){
        binding.shimmer1.startShimmer();
        listItem.clear();
        String url = NetworkState.getIp()+"showItem.php" ;
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.isEmpty()){
                            binding.shimmer1.stopShimmer();
                            binding.shimmer1.setVisibility(View.GONE);
                            binding.frameMain.setVisibility(View.VISIBLE);
                            try {
                                JSONArray jsonarray=new JSONArray(response);

                                for(int i=0;i<jsonarray.length();i++)
                                {
                                    JSONObject users = jsonarray.getJSONObject(i);

                                    String barcode = users.getString("barcode");
                                    String name = users.getString("name");
                                    String alias_name = users.getString("alias_name");
                                    String category = users.getString("category");
                                    String subCategory = users.getString("sub_category");
                                    String gp1 = users.getString("price");
                                    String imagURL = users.getString("imageURL");
                                    String source = NetworkState.getIpImage()+imagURL;

                                    DataItem dataItem = new DataItem();
                                    dataItem.setBarcode(barcode);
                                    dataItem.setName(name);
                                    dataItem.setGp1(gp1);
                                    dataItem.setCategory(category);
                                    dataItem.setSubCategory(subCategory);
                                    dataItem.setAlias_name(alias_name);
                                    dataItem.setSource(source);
                                    listItem.add(dataItem);

                                }
                            } catch (JSONException e) {
                                Log.e("catchException",e.toString());
                                e.printStackTrace();
                            }

                            adapteritem.notifyDataSetChanged();
                        }
                        else {
                            MyConfig.showToast(getContext(),"Koneksi Error, mencoba konek ulang");
                            Log.e("error","Empty Response");
                            //callSpecialPrice(category);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                        MyConfig.showToast(getContext(),"Koneksi Error, mencoba konek ulang");
                        //callSpecialPrice(category);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //String store = new SessionLogin(getActivity()).getStore();
                Map<String, String> params = new HashMap<String, String>();
                //params.put("store",store );
                params.put("sub_category",subCategoryCuci );
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        mRequestQueue.add(stringRequest);
    }

    public void callCategory2(){
        listCategory2.clear();
        String url = NetworkState.getIp()+"getCategory2.php" ;
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("category", response);
                        if(!response.isEmpty()){
                            try {
                                JSONArray jsonarray=new JSONArray(response);
                                /*DataCategory dataItems = new DataCategory("All Category");
                                listCategory.add(dataItems);*/
                                for(int i=0;i<jsonarray.length();i++)
                                {
                                    JSONObject users = jsonarray.getJSONObject(i);
                                    String sub_category = users.getString("sub_category");

                                    DataCategory2 dataItem = new DataCategory2(sub_category);
                                    listCategory2.add(dataItem);

                                }
                                adapterCategoryCoffee.notifyDataSetChanged();
                            } catch (JSONException e) {
                                //showSnackBar("Koneksi ke Server Bermasalah, Silahkan Coba Lagi");
                                Log.e("catchException",e.toString());
                                e.printStackTrace();
                            }
                        }
                        else {
                            Log.e("error","Empty Response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("store", new SessionLogin(getActivity()).getStore() );
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        mRequestQueue.add(stringRequest);
    }

    public void callItem2(final String subCategoryCaffee){
        binding.shimmer2.startShimmer();
        listItem2.clear();
        String url = NetworkState.getIp()+"showItem2.php" ;
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.isEmpty()){
                            binding.shimmer2.stopShimmer();
                            binding.shimmer2.setVisibility(View.GONE);
                            binding.frameCoffee.setVisibility(View.VISIBLE);
                            try {
                                JSONArray jsonarray=new JSONArray(response);

                                for(int i=0;i<jsonarray.length();i++)
                                {
                                    JSONObject users = jsonarray.getJSONObject(i);

                                    String barcode = users.getString("barcode");
                                    String name = users.getString("name");
                                    String alias_name = users.getString("alias_name");
                                    String category = users.getString("category");
                                    String subCategory = users.getString("sub_category");
                                    String gp1 = users.getString("price");
                                    String imagURL = users.getString("imageURL");
                                    String source = NetworkState.getIpImage()+imagURL;

                                    DataItem2 dataItem = new DataItem2();
                                    dataItem.setBarcode(barcode);
                                    dataItem.setName(name);
                                    dataItem.setGp1(gp1);
                                    dataItem.setCategory(category);
                                    dataItem.setSubCategory(subCategory);
                                    dataItem.setAlias_name(alias_name);
                                    dataItem.setSource(source);
                                    listItem2.add(dataItem);

                                }
                                adapterItem2.notifyDataSetChanged();
                            } catch (JSONException e) {
                                Log.e("catchException",e.toString());
                                e.printStackTrace();
                            }


                        }
                        else {
                            MyConfig.showToast(getContext(),"Koneksi Error, mencoba konek ulang");
                            Log.e("error","Empty Response");
                            //callSpecialPrice(category);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                        MyConfig.showToast(getContext(),"Koneksi Error, mencoba konek ulang");
                        //callSpecialPrice(category);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //String store = new SessionLogin(getActivity()).getStore();
                Map<String, String> params = new HashMap<String, String>();
                //params.put("store",store );
                params.put("sub_category",subCategoryCaffee );
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        mRequestQueue.add(stringRequest);
    }

    public void callCategory3(){
        listCategory3.clear();
        String url = NetworkState.getIp()+"getCategory3.php" ;
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("category", response);
                        if(!response.isEmpty()){
                            try {
                                JSONArray jsonarray=new JSONArray(response);
                                /*DataCategory dataItems = new DataCategory("All Category");
                                listCategory.add(dataItems);*/
                                for(int i=0;i<jsonarray.length();i++)
                                {
                                    JSONObject users = jsonarray.getJSONObject(i);
                                    String sub_category = users.getString("sub_category");

                                    DataCategory3 dataItem = new DataCategory3(sub_category);
                                    listCategory3.add(dataItem);

                                }
                                adapterCategoryCoffee.notifyDataSetChanged();
                            } catch (JSONException e) {
                                //showSnackBar("Koneksi ke Server Bermasalah, Silahkan Coba Lagi");
                                Log.e("catchException",e.toString());
                                e.printStackTrace();
                            }
                        }
                        else {
                            Log.e("error","Empty Response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("store", new SessionLogin(getActivity()).getStore() );
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        mRequestQueue.add(stringRequest);
    }

    public void callItem3(final String subCategoryPromo){
        binding.shimmer3.startShimmer();
        listItem3.clear();
        String url = NetworkState.getIp()+"showItem3.php" ;
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.isEmpty()){
                            binding.shimmer3.stopShimmer();
                            binding.shimmer3.setVisibility(View.GONE);
                            binding.framePromo.setVisibility(View.VISIBLE);
                            try {
                                JSONArray jsonarray=new JSONArray(response);

                                for(int i=0;i<jsonarray.length();i++)
                                {
                                    JSONObject users = jsonarray.getJSONObject(i);

                                    String barcode = users.getString("barcode");
                                    String name = users.getString("name");
                                    String alias_name = users.getString("alias_name");
                                    String category = users.getString("category");
                                    String subCategory = users.getString("sub_category");
                                    String gp1 = users.getString("price");
                                    String imagURL = users.getString("imageURL");
                                    String source = NetworkState.getIpImage()+imagURL;

                                    DataItem3 dataItem = new DataItem3();
                                    dataItem.setBarcode(barcode);
                                    dataItem.setName(name);
                                    dataItem.setGp1(gp1);
                                    dataItem.setCategory(category);
                                    dataItem.setSubCategory(subCategory);
                                    dataItem.setAlias_name(alias_name);
                                    dataItem.setSource(source);
                                    listItem3.add(dataItem);

                                }
                                adapterItem3.notifyDataSetChanged();
                            } catch (JSONException e) {
                                Log.e("catchException",e.toString());
                                e.printStackTrace();
                            }


                        }
                        else {
                            MyConfig.showToast(getContext(),"Koneksi Error, mencoba konek ulang");
                            Log.e("error","Empty Response");
                            //callSpecialPrice(category);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                        MyConfig.showToast(getContext(),"Koneksi Error, mencoba konek ulang");
                        //callSpecialPrice(category);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //String store = new SessionLogin(getActivity()).getStore();
                Map<String, String> params = new HashMap<String, String>();
                //params.put("store",store );
                params.put("sub_category",subCategoryCaffee );
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
        DataItem dataItem = listItem.get(position);

        viewItem(dataItem.getBarcode(), dataItem.getName(), dataItem.getGp1(), view,
                dataItem.getSubCategory(), dataItem.getAlias_name());
    }

    String num="1";
    public void viewItem(final String barcode, final String name ,final String price, View v,
                         final String subCategory, final String aliasName) {

        TextView textName, textPrice;
        NumberPicker numberPicker;
        final Button btnCancel, btnSave;
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = li.inflate(R.layout.detail_item, null);

        textName = (TextView) v.findViewById(R.id.textNameItem);
        textPrice = (TextView) v.findViewById(R.id.textPriceItem);
        numberPicker = (NumberPicker) v.findViewById(R.id.numberPicker);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnSave = (Button) v.findViewById(R.id.btnAddCart);

        numberPicker.setMin(0);
        numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ? "incremented" : "decremented");
                String message = String.format("NumberPicker is %s to %d", actionText, value);
                Log.v("tes", message);
                num = String.valueOf(value);
            }
        });

        numberPicker.getValueChangedListener();

        textName.setText(name);
        textPrice.setText(MyConfig.formatNumberComma(price));

        alert.setView(v);
        alert.create();

        final AlertDialog dialog = alert.create();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sqLiteHelpers = new SQLiteHelpers(getActivity());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer intStockReq = Integer.valueOf(num);
                if (intStockReq==0){
                    MyConfig.showToast(getContext(),"Jumlah tidak boleh 0 ");
                }else{
                    sqLiteHelpers.insertData(barcode,name,num,price, aliasName);
                    onResume();
                    dialog.dismiss();
                    AestheticDialog.showToaster(getActivity(), "Sukses",
                            "Sukses menambahkan daftar keranjang",
                            AestheticDialog.SUCCESS);
                }
            }
        });
        dialog.show();
    }

    public void callCart() {
        listCart.clear();
        sumTotals=0;
        sqLiteHelpers =  new SQLiteHelpers(getActivity());
        ArrayList<HashMap<String,String>> row = sqLiteHelpers.getDataItem();
        String totCart;
        if(row.size()==0){
            totCart="0";
            binding.cardKeranjang.setVisibility(View.GONE);
        }else{
            totCart=  String.valueOf(row.size());
            binding.cardKeranjang.setVisibility(View.VISIBLE);
            binding.btnLihatKeranjang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), KeranjangActivity.class));
                }
            });
        }

        binding.textTotalKeranjang.setText("Total Keranjang ( "+totCart+ " )");
        /*for (int i=0; i<row.size();i++){
            String id = row.get(i).get("id");
            String barcode = row.get(i).get("barcode");
            String name = row.get(i).get("name");
            String qty = row.get(i).get("qty");
            String price = row.get(i).get("price");
            String aliasName = row.get(i).get("alias_name");
            DataCart dataCart = new DataCart(id,barcode,name,qty,price, aliasName);
            listCart.add(dataCart);

            parsePrice = Integer.valueOf(price);
            parseQty = Integer.valueOf(qty);
            parseQty = Integer.parseInt(qty);
            sumItem = parseQty * parsePrice;
            sumTotals +=sumItem;
        }

        String nampung = String.valueOf(sumTotals);
        binding.textSumTotal.setText(MyConfig.formatNumberComma(nampung));
        adapterCart.notifyDataSetChanged();
        sumTotalString = String.valueOf(sumTotals);
        int dt = row.size();

        if (dt==0){
            binding.linearTotal.setVisibility(View.GONE);
            binding.btnClear.setVisibility(View.GONE);
            binding.btnSave.setVisibility(View.GONE);
        }else{
            binding.linearTotal.setVisibility(View.VISIBLE);
            binding.btnClear.setVisibility(View.VISIBLE);
            binding.btnSave.setVisibility(View.VISIBLE);
        }

        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                MaterialDialog mDialog = new MaterialDialog.Builder(getActivity())
                        .setTitle("Hapus data keranjang")
                        .setMessage("Apakah anda ingin menghapus semua daftar keranjang/belanja ?")
                        .setCancelable(false)
                        .setPositiveButton("Hapus", R.drawable.ic_delete_white, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                sqLiteHelpers.deleteAllData();
                                onResume();
                                AestheticDialog.showToaster(getActivity(), "Hapus Keranjang", "Sukses menghapus semua data keranjang", AestheticDialog.ERROR);
                            }
                        })
                        .setNegativeButton("Batal", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        })
                        .build();
                mDialog.show();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etPaid.getText().toString().equals("")){
                    AestheticDialog.showToaster(getActivity(), "Pembayaran",
                            "Anda harus mengisi jumlah pembayaran terlebih dahulu",
                            AestheticDialog.WARNING);
                }else if (dibayar<jumlahbayar){
                    AestheticDialog.showToaster(getActivity(), "Pembayaran",
                            "Nominal pembayaran kurang dari total harga !",
                            AestheticDialog.ERROR);
                }else{
                    new IonAlert(getContext(), IonAlert.WARNING_TYPE)
                            .setTitleText("Proses Pembayaran")
                            .setContentText("Apakah anda ingin melanjutkan ke proses pembayaran ?")
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
                                    processSave();
                                }
                            })
                            .show();
                }
            }
        });*/

    }

    @Override
    public void onResume() {
        super.onResume();
        sumTotals=0;
        listCart.clear();
        callCart();
    }

    @Override
    public void onClick2(View view, int position) {
        DataCart dataCart = listCart.get(position);
        final String id = dataCart.getId();

        MaterialDialog mDialog = new MaterialDialog.Builder(getActivity())
                .setTitle("Hapus item ?")
                .setMessage("Apakah anda ingin menghapus item dari daftar keranjang/belanja ?")
                .setCancelable(false)
                .setPositiveButton("Hapus", R.drawable.ic_delete_white, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        sqLiteHelpers.delete(Integer.parseInt(id));
                        onResume();
                        AestheticDialog.showToaster(getActivity(), "Hapus Item", "Sukses hapus item dari daftar keranjang ", AestheticDialog.WARNING);
                    }
                })
                .setNegativeButton("Batal", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
        // Show Dialog
        mDialog.show();
    }

    @Override
    public void onClick4(View view, int position) {
        DataCart dataCart = listCart.get(position);
        String id = dataCart.getId();
        String name = dataCart.getName();
        String qty = dataCart.getQty();
        String barcode = dataCart.getBarcode();
        viewCart(id,name, qty, view, barcode);
    }

    String nums="1";
    private void viewCart(final String id, String name, String qty, View v, String barcode) {
        NumberPicker numberPicker;
        Button btnEditQty;
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Jumlah" + name);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = li.inflate(R.layout.dialog_qty_cart, null);

        numberPicker = v.findViewById(R.id.et_edit_qty);
        btnEditQty = (Button) v.findViewById(R.id.btnEditQty);

        numberPicker.setValue(Integer.parseInt(qty));
        nums  = qty;
        numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ? "incremented" : "decremented");
                String message = String.format("NumberPicker is %s to %d", actionText, value);
                Log.v("tes", message);
                nums = String.valueOf(value);
            }
        });
        numberPicker.getValueChangedListener();

        alert.setView(v);
        alert.create();

        final AlertDialog dialog = alert.create();

        sqLiteHelpers = new SQLiteHelpers(getActivity());
        btnEditQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer intNums = Integer.valueOf(nums);

                if (intNums==0){
                    MyConfig.showToast(getContext(),"Mohon maaf stok tidak boleh 0");
                }else{
                    sqLiteHelpers.updateData(id, nums);
                    dialog.dismiss();
                    AestheticDialog.showToaster(getActivity(), "Edit", "Sukses edit jumlah pembelian", AestheticDialog.SUCCESS);
                    //callCart();
                }
            }
        });
        dialog.getWindow().setLayout(400, 400);
        dialog.show();
    }

    private void processSave() {
        sqLiteHelpers =  new SQLiteHelpers(getActivity());
        ArrayList<HashMap<String,String>> row = sqLiteHelpers.getDataItem();
        JSONArray jsonArray = new JSONArray();

        for (int i=0; i<row.size();i++) {
            String id = row.get(i).get("id");
            String barcode = row.get(i).get("barcode");
            String name = row.get(i).get("name");
            String qty = row.get(i).get("qty");
            String price = row.get(i).get("price");

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("id", id);
                jsonObject.put("barcode", barcode);
                jsonObject.put("name", name);
                jsonObject.put("qty", qty);
                jsonObject.put("price", price);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final String datatemp = jsonArray.toString();
        Log.d("items", datatemp);

        String username = new SessionLogin(getActivity()).getUsername();
        String store = "201";
        String paidValue = String.valueOf(dibayar);
        String salesValue = String.valueOf(sumTotals);
        String returnPays = String.valueOf(returnPay);
        //uploadServer(username,store,paidValue,salesValue,returnPays, datatemp);
        uploadServer2(username,store,paidValue,salesValue,returnPays, datatemp);
    }

    private void uploadServer2(final String username, final String store, final String paidValue,
                               final String salesValue, final String returnPays, String datatemp) {
        ProgressLoadingJIGB.startLoading(getContext());
        String url = NetworkState.getIp()+"insertCart.php";
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sqLiteHelpers.deleteAllData();
                ProgressLoadingJIGB.finishLoadingJIGB(getContext());

                new IonAlert(getContext(), IonAlert.SUCCESS_TYPE)
                        .setTitleText("Pembayaran Berhasil")
                        .setContentText("Transaksi berhasi dengan nomor invoice : "+ response+".\n" +
                                "Apakah anda ingin mencetak struk ?")
                        .setCancelText("Tidak")
                        .setConfirmText("Ya")
                        .showCancelButton(true)
                        .setCancelClickListener(new IonAlert.ClickListener() {
                            @Override
                            public void onClick(IonAlert sDialog) {
                                sDialog.cancel();
                                sqLiteHelpers.deleteAllData();
                                binding.frameNumPad.setVisibility(View.GONE);
                                onResume();
                                callItem("");
                            }
                        })
                        .setConfirmClickListener(new IonAlert.ClickListener() {
                            @Override
                            public void onClick(IonAlert ionAlert) {
                                ionAlert.dismiss();
                                Intent intent = new Intent(getContext(), PembayaranActivity.class);
                                intent.putExtra("username", username);
                                intent.putExtra("store", store);
                                intent.putExtra("paidValue", paidValue);
                                intent.putExtra("salesValue", salesValue);
                                intent.putExtra("returnPays", returnPays);
                                intent.putExtra("invoice", response);
                                intent.putExtra("listCart", (Serializable) listCart);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Login Error: " + error.getMessage());
                ProgressLoadingJIGB.finishLoadingJIGB(getContext());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", username);
                params.put("store", store);
                params.put("paidValue", paidValue);
                params.put("salesValue", salesValue);
                params.put("returnValue", returnPays);
                params.put("item", datatemp);
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
    public void onClickCategoryCoffee(View view, int position) {
        //coffee
        DataCategory2 dataCategory2 = listCategory2.get(position);
        adapterCategoryCoffee.notifyDataSetChanged();
        adapterItem2.notifyDataSetChanged();
        binding.frameCoffee.setVisibility(View.GONE);
        binding.shimmer2.setVisibility(View.VISIBLE);
        binding.shimmer2.startShimmer();
        callItem2(dataCategory2.getNamaCategory());
    }

    @Override
    public void onClickCoffee(View view, int position) {
        DataItem2 dataItem = listItem2.get(position);
        viewItem(dataItem.getBarcode(), dataItem.getName(), dataItem.getGp1(), view,
                dataItem.getSubCategory(), dataItem.getAlias_name());
    }

    @Override
    public void onClick3(View view, int position) {
        //cuci
        DataCategory dataCategory = listCategory.get(position);
        adapterCategoryCuci.notifyDataSetChanged();
        adapteritem.notifyDataSetChanged();
        binding.frameMain.setVisibility(View.GONE);
        binding.shimmer1.setVisibility(View.VISIBLE);
        binding.shimmer1.startShimmer();
        callItem(dataCategory.getNamaCategory());

    }

    @Override
    public void onClickPromo(View view, int position) {
        DataItem3 dataItem = listItem3.get(position);
        viewItem(dataItem.getBarcode(), dataItem.getName(), dataItem.getGp1(), view,
                dataItem.getSubCategory(), dataItem.getAlias_name());
    }

    @Override
    public void onClickCategoryPromo(View view, int position) {
        DataCategory3 dataCategory2 = listCategory3.get(position);
        adapterCategoryPromo.notifyDataSetChanged();
        adapterItem3.notifyDataSetChanged();
        binding.framePromo.setVisibility(View.GONE);
        binding.shimmer3.setVisibility(View.VISIBLE);
        binding.shimmer3.startShimmer();
        callItem3(dataCategory2.getNamaCategory());
    }
}