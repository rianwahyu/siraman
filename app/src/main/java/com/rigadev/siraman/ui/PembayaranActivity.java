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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.rigadev.siraman.Adapter.AdapterCartList;
import com.rigadev.siraman.HomeActivity;
import com.rigadev.siraman.Model.DataCart;
import com.rigadev.siraman.R;
import com.rigadev.siraman.databinding.ActivityPembayaranBinding;
import com.rigadev.siraman.util.MyConfig;
import com.rigadev.siraman.util.async.AsyncBluetoothEscPosPrint;
import com.rigadev.siraman.util.async.AsyncEscPosPrinter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PembayaranActivity extends AppCompatActivity {

    Context context = this;
    ActivityPembayaranBinding binding;
    String username, store, paidValue, salesValue, returnPays, invoice;


    AdapterCartList adapterCartList;
    List<DataCart> listCart;

    public BluetoothAdapter mBluetoothAdapter;
    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int REQUEST_ENABLE_BT = 2;
    private BluetoothConnection selectedDevice;

    String textPrint = "";
    int totalValue=0;
    int qtyValue=0;
    int hargaValue=0;
    int sumTotals=0;
    int sumTotals2=0;

    String gerai="";
    String stores ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pembayaran);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_pembayaran);

        username = getIntent().getStringExtra("username");
        store = getIntent().getStringExtra("store");
        paidValue = getIntent().getStringExtra("paidValue");
        salesValue = getIntent().getStringExtra("salesValue");
        returnPays = getIntent().getStringExtra("returnPays");
        invoice = getIntent().getStringExtra("invoice");

        gerai = "Siraman";
        stores = "Siraman";


        //binding.textGerai.setText(gerai);
        binding.textInvoice.setText(invoice);
        binding.textUsername.setText(username);
        binding.textTanggal.setText(MyConfig.getCurentDate());

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            listCart = (ArrayList<DataCart>) getIntent().getSerializableExtra("listCart");
        }

        Log.d("listCart", listCart.toString());


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

        rcSetting();
    }

    private void rcSetting() {

        for (int i=0; i<listCart.size();i++) {
            int harga = Integer.parseInt(listCart.get(i).getPrice());
            int qty = Integer.parseInt(listCart.get(i).getQty());
            int totalValue = qty * harga;

            sumTotals2 = sumTotals2 + Integer.parseInt(String.valueOf(Math.round(totalValue)));

        }

        binding.rcListOrder.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcListOrder.setLayoutManager(linearLayoutManager);
        adapterCartList = new AdapterCartList(context,listCart);
        binding.rcListOrder.setAdapter(adapterCartList);

        adapterCartList.notifyDataSetChanged();

        binding.textTotal.setText(MyConfig.formatNumberComma(salesValue));
        binding.textDibayar.setText(MyConfig.formatNumberComma(paidValue));
        binding.textKembalian.setText(MyConfig.formatNumberComma(returnPays));

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

        /*textPrint = textPrint + "[C]<font size='big'>Cafe</font>\n" +
                "[C]<font size='big'>Siraman</font>\n\n" +
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_hitam, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
                "[C]<font size='normal'>Car Wash & Caffee</font>\n"+
                "[C]<font size='normal'>Struk Pembayaran</font>\n\n\n"+
                "[L]<font size='normal'>Invoice : "+invoice+"</font>\n"+
                "[L]<font size='normal'>Kasir : "+username+"</font>\n"+
                "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                "[C]================================\n" ;*/

        textPrint = textPrint +
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_hitam, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
                "[C]<font size='normal'>Siraman</font>\n"+
                "[C]<font size='normal'>Car Wash & Caffee</font>\n"+
                "[C]<font size='normal'>Struk Pembayaran</font>\n\n\n"+
                "[L]<font size='normal'>Invoice : "+invoice+"</font>\n"+
                "[L]<font size='normal'>Kasir : "+username+"</font>\n"+
                "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
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
                "[L]Total[R]"+MyConfig.formatNumberComma(salesValue)+"\n" +
                "[L]Dibayarkan[R]"+MyConfig.formatNumberComma(paidValue)+"\n" +
                "[L]Kembalian[R]"+MyConfig.formatNumberComma(returnPays)+"\n\n" +

                "[C]<font size='normal'>Terima Kasih telah berkunjung</font>\n" +
                "[C]<font size='normal'>Jl AH Nasution Lampung Timur \n</font>\n" +
                "[C]<font size='normal'>Promo Desember : Kumpulkan 10 nota gratis (cuci/kopi).</font>\n\n\n" +
                "[L]<font size='normal'>Develop By : Rigadev</font>\n" +
                "[L]\n" ;

        return printer.setTextToPrint(textPrint);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(context, HomeActivity.class));
        finish();
    }
}