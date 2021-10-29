package com.rigadev.siraman.ui.home;

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
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rigadev.siraman.Adapter.AdapterItem;
import com.rigadev.siraman.Model.DataItem;
import com.rigadev.siraman.R;
import com.rigadev.siraman.databinding.FragmentHomeBinding;
import com.rigadev.siraman.util.ItemClickListener;
import com.rigadev.siraman.util.MyConfig;
import com.rigadev.siraman.util.NetworkState;
import com.rigadev.siraman.util.SQLiteHelpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment implements ItemClickListener {

    FragmentHomeBinding binding;
    private List<DataItem> listItem = new ArrayList<DataItem>();

    AdapterItem adapteritem;
    int numberOfColumns = 4;

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
        binding.frameMain.setVisibility(View.GONE);
        binding.relaCart.setVisibility(View.GONE);

        initRC();

        callItem("");
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
        listItem = new ArrayList<DataItem>();
        binding.recycleItem.setHasFixedSize(true);
        binding.recycleItem.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        adapteritem = new AdapterItem(getActivity(), listItem, HomeFragment.this);
        binding.recycleItem.setAdapter(adapteritem);
        adapteritem.setClickListener(this);
    }

    public void callItem(final String category){
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
                            binding.frameNumPad.setVisibility(View.GONE);
                            binding.relaCart.setVisibility(View.VISIBLE);
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
                params.put("category",category );
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

    }
}