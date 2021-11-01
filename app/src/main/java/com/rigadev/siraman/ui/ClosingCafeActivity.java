package com.rigadev.siraman.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.rigadev.siraman.Adapter.AdapterCartList;
import com.rigadev.siraman.LoginActivity;
import com.rigadev.siraman.Model.DataCart;
import com.rigadev.siraman.Model.DataValue2;
import com.rigadev.siraman.R;
import com.rigadev.siraman.databinding.ActivityClosingCafeBinding;
import com.rigadev.siraman.util.MyConfig;
import com.rigadev.siraman.util.SessionLogin;
import com.rigadev.siraman.util.async.AsyncBluetoothEscPosPrint;
import com.rigadev.siraman.util.async.AsyncEscPosPrinter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ClosingCafeActivity extends AppCompatActivity {

    Context context = this;
    ActivityClosingCafeBinding binding;

    AdapterCartList adapterCartList;
    List<DataValue2> listValue;
    List<DataCart> listCart;

    public BluetoothAdapter mBluetoothAdapter;
    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int REQUEST_ENABLE_BT = 2;
    private BluetoothConnection selectedDevice;

    String textPrint = "";

    String gerai="";
    String stores ="";
    int sumTotals2 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_closing_cafe);

        listCart = new ArrayList<>();

        gerai = "Siraman";
        stores = "Cafe & Car Wash";

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            listValue = (ArrayList<DataValue2>) getIntent().getSerializableExtra("listValue");
        }

        Log.d("listCart", listValue.toString());

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()){

        }else {
            dialogBluetoothPermission();
        }

        binding.btnPilihPerangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseBluetoothDevices();
            }
        });

        binding.btnCetakStruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printStrukBluetooth();
            }
        });

        binding.textUsername.setText(new SessionLogin(context).getUsername());
        binding.textGerai.setText(gerai);
        binding.textCurentDate.setText(MyConfig.getCurentDate());

        rcSetting();
    }

    private void rcSetting() {

        binding.rcListOrder.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcListOrder.setLayoutManager(linearLayoutManager);
        adapterCartList = new AdapterCartList(context,listCart);
        binding.rcListOrder.setAdapter(adapterCartList);


        for (int i=0; i<listValue.size();i++) {
            String barcode = listValue.get(i).getBarcode();
            String nama = listValue.get(i).getName();
            int harga = Integer.parseInt(listValue.get(i).getPrice());
            int qty = Integer.parseInt(listValue.get(i).getQty());
            int totalValue = qty * harga;
            sumTotals2 = sumTotals2 + Integer.parseInt(String.valueOf(Math.round(totalValue)));
            DataCart dataCart = new DataCart("0", barcode, nama,String.valueOf(qty), String.valueOf(harga), nama );
            listCart.add(dataCart);
        }

        adapterCartList.notifyDataSetChanged();

        binding.textTotal.setText(MyConfig.formatNumberComma(String.valueOf(sumTotals2)));
    }

    private void dialogBluetoothPermission() {
        activeBluetooth();
    }

    public void activeBluetooth() {
        if (mBluetoothAdapter == null) {

        }else{
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            }
        }
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PERMISSION_BLUETOOTH:
                    printStrukBluetooth();
                    break;
            }
        }
    }

    private void chooseBluetoothDevices() {
        final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

        if (bluetoothDevicesList != null) {
            final String[] items = new String[bluetoothDevicesList.length + 1];
            items[0] = "Default printer";
            int i = 0;
            for (BluetoothConnection device : bluetoothDevicesList) {
                items[++i] = device.getDevice().getName();
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Bluetooth printer selection");
            alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int index = i - 1;
                    if(index == -1) {
                        selectedDevice = null;
                    } else {
                        selectedDevice = bluetoothDevicesList[index];
                    }
                    binding.btnPilihPerangkat.setText(items[i]);
                }
            });

            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }
    }

    private void printStrukBluetooth() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH);
        }else {
            new AsyncBluetoothEscPosPrint(this).execute(this.getAsyncEscPosPrinter(selectedDevice));

            textPrint="";
        }
    }

    private AsyncEscPosPrinter getAsyncEscPosPrinter(BluetoothConnection printerConnection) {



        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);

        textPrint = textPrint + "[C]<font size='big'>Cafe</font>\n" +
                "[C]<font size='big'>Depo Bangunan</font>\n\n" +
                "[C]<font size='normal'>PT. Megadepo Indonesia</font>\n"+
                "[C]<font size='normal'>Closing Penjualan Cafe</font>\n\n\n"+
                "[L]<font size='normal'>Gerai : "+gerai+"</font>\n"+
                "[L]<font size='normal'>Kasir : "+new SessionLogin(context).getUsername()+"</font>\n"+
                "[L]<font size='normal'>Tanggal : "+MyConfig.getCurentDate()+"</font>\n"+

                "[C]================================\n" ;

        for (int i=0; i<listCart.size();i++){
            int harga = Integer.parseInt(listCart.get(i).getPrice());
            int qty = Integer.parseInt(listCart.get(i).getQty());
            int totalValue = qty * harga;

            textPrint = textPrint+"[L]<b>"+listCart.get(i).getAlias_name()+"</b>\n";
            textPrint = textPrint+"[L]"+qty+" x "+ MyConfig.formatNumberComma(String.valueOf(harga))+"[R]"+MyConfig.formatNumberComma(String.valueOf(totalValue))+"\n";

        }

        textPrint = textPrint + "[L]==============================\n "+
                "[L].[R]\n" +
                "[L]Total[R]"+MyConfig.formatNumberComma(String.valueOf(sumTotals2))+"\n" +
                "[L]<font size='normal'>Develop By : IT Depo Bangunan</font>\n" +
                "[L]\n" ;

        return printer.setTextToPrint(textPrint);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new SessionLogin(context).logout();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new SessionLogin(context).logout();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}