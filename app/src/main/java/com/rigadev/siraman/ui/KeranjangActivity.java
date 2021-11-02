package com.rigadev.siraman.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import android.widget.Button;
import android.widget.EditText;

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
import com.rigadev.siraman.Model.DataCart;
import com.rigadev.siraman.R;
import com.rigadev.siraman.databinding.ActivityKeranjangBinding;
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

public class KeranjangActivity extends AppCompatActivity implements ItemDeleteCartListener, ItemQtyCartListener {

    Context context = this;
    ActivityKeranjangBinding binding;
    private List<DataCart> listCart = new ArrayList<DataCart>();
    AdapterCart adapterCart;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_keranjang);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_keranjang);

        ProgressLoadingJIGB.setupLoading = (setup) ->  {
            setup.srcLottieJson = R.raw.loader; // Tour Source JSON Lottie
            setup.message = "Proses Pembayaran";//  Center Message
            setup.timer = 0;   // Time of live for progress.
            setup.width = 200; // Optional
            setup.hight = 200; // Optional
        };

        sqLiteHelpers = new SQLiteHelpers(context);

        binding.frameNumPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*binding.frameNumPad.setVisibility(View.GONE);
                binding.frameMain.setVisibility(View.VISIBLE);*/
            }
        });

        binding.etPaid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int inType = binding.etPaid.getInputType(); // backup the input type
                binding.etPaid.setInputType(InputType.TYPE_NULL); // disable soft input
                binding.etPaid.onTouchEvent(motionEvent); // call native handler
                binding.etPaid.setInputType(inType); // restore input type

                /*binding.frameMain.setVisibility(View.GONE);
                binding.frameNumPad.setVisibility(View.VISIBLE);*/

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

        listCart = new ArrayList<DataCart>();
        binding.recycleCart.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.recycleCart.setLayoutManager(layoutManager);
        adapterCart = new AdapterCart(context, listCart);
        binding.recycleCart.setAdapter(adapterCart);
        adapterCart.setClickListener(this);
        adapterCart.setQtyCatClickListener(this);

    }

    public void callCart() {
        listCart.clear();
        sumTotals=0;
        ArrayList<HashMap<String,String>> row = sqLiteHelpers.getDataItem();
        String totCart;
        if(row.size()==0){
            totCart="0";

        }else{
            totCart=  String.valueOf(row.size());
        }

        binding.textTotalCart.setText("Total Keranjang ( "+totCart+ " )");
        for (int i=0; i<row.size();i++){
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
                MaterialDialog mDialog = new MaterialDialog.Builder(KeranjangActivity.this)
                        .setTitle("Hapus data keranjang")
                        .setMessage("Apakah anda ingin menghapus semua daftar keranjang/belanja ?")
                        .setCancelable(false)
                        .setPositiveButton("Hapus", R.drawable.ic_delete_white, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                sqLiteHelpers.deleteAllData();
                                onResume();
                                AestheticDialog.showToaster(KeranjangActivity.this, "Hapus Keranjang", "Sukses menghapus semua data keranjang", AestheticDialog.ERROR);
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
                    AestheticDialog.showToaster(KeranjangActivity.this, "Pembayaran",
                            "Anda harus mengisi jumlah pembayaran terlebih dahulu",
                            AestheticDialog.WARNING);
                }else if (dibayar<jumlahbayar){
                    AestheticDialog.showToaster(KeranjangActivity.this, "Pembayaran",
                            "Nominal pembayaran kurang dari total harga !",
                            AestheticDialog.ERROR);
                }else{
                    new IonAlert(context, IonAlert.WARNING_TYPE)
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
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        sumTotals=0;
        listCart.clear();
        callCart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick2(View view, int position) {
        DataCart dataCart = listCart.get(position);
        final String id = dataCart.getId();

        MaterialDialog mDialog = new MaterialDialog.Builder(KeranjangActivity.this)
                .setTitle("Hapus item ?")
                .setMessage("Apakah anda ingin menghapus item dari daftar keranjang/belanja ?")
                .setCancelable(false)
                .setPositiveButton("Hapus", R.drawable.ic_delete_white, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        sqLiteHelpers.delete(Integer.parseInt(id));
                        onResume();
                        AestheticDialog.showToaster(KeranjangActivity.this, "Hapus Item", "Sukses hapus item dari daftar keranjang ", AestheticDialog.WARNING);
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
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Jumlah" + name);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        btnEditQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer intNums = Integer.valueOf(nums);

                if (intNums==0){
                    MyConfig.showToast(context,"Mohon maaf stok tidak boleh 0");
                }else{
                    sqLiteHelpers.updateData(id, nums);
                    dialog.dismiss();
                    AestheticDialog.showToaster(KeranjangActivity.this, "Edit", "Sukses edit jumlah pembelian", AestheticDialog.SUCCESS);
                    //callCart();
                }
            }
        });
        dialog.getWindow().setLayout(400, 400);
        dialog.show();
    }

    private void processSave() {
        sqLiteHelpers =  new SQLiteHelpers(context);
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

        String username = new SessionLogin(context).getUsername();
        String store = "201";
        String paidValue = String.valueOf(dibayar);
        String salesValue = String.valueOf(sumTotals);
        String returnPays = String.valueOf(returnPay);
        //uploadServer(username,store,paidValue,salesValue,returnPays, datatemp);
        uploadServer2(username,store,paidValue,salesValue,returnPays, datatemp);
    }

    private void uploadServer2(final String username, final String store, final String paidValue,
                               final String salesValue, final String returnPays, String datatemp) {
        ProgressLoadingJIGB.startLoading(context);
        String url = NetworkState.getIp()+"insertCart.php";
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sqLiteHelpers.deleteAllData();
                ProgressLoadingJIGB.finishLoadingJIGB(context);

                new IonAlert(context, IonAlert.SUCCESS_TYPE)
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
                                onBackPressed();
//                                callItem("");
                            }
                        })
                        .setConfirmClickListener(new IonAlert.ClickListener() {
                            @Override
                            public void onClick(IonAlert ionAlert) {
                                ionAlert.dismiss();
                                Intent intent = new Intent(context, PembayaranActivity.class);
                                intent.putExtra("username", username);
                                intent.putExtra("store", store);
                                intent.putExtra("paidValue", paidValue);
                                intent.putExtra("salesValue", salesValue);
                                intent.putExtra("returnPays", returnPays);
                                intent.putExtra("invoice", response);
                                intent.putExtra("listCart", (Serializable) listCart);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Login Error: " + error.getMessage());
                ProgressLoadingJIGB.finishLoadingJIGB(context);
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
}