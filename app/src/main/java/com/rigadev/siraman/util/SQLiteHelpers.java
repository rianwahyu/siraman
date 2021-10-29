package com.rigadev.siraman.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHelpers extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "siraman";
    private static final String TABLE_DATA_CART ="cart";
    /*private static final String TABLE_DATA_TEMP_ITEM ="item_temp";
    private static final String TABLE_DATA_RECEIVE ="receive";
    private static final String TABLE_DATA_SR ="salesReturn";*/

    private static final String ID_ITEM = "id";
    private static final String BARCODE_ITEM = "barcode";
    private static final String NAME_ITEM = "name";
    private static final String NAME_ALIAS = "alias_name";
    private static final String QTY_ITEM = "qty";
    private static final String PRICE_ITEM = "price";

    private static final String TEMP_ID = "temp_id";
    private static final String TEMP_BARCODE = "temp_barcode";
    private static final String TEMP_STOCK = "temp_stock";
    private static final String TEMP_SUB_CATEGORY = "temp_subCat";

    private static final String ID_ITEM_RECEIVE = "id";
    private static final String BARCODE_ITEM_RECEIVE = "barcode";
    private static final String NAME_ITEM_RECEIVE = "name";
    private static final String NAME_ALIAS_RECEIVE = "alias_name";
    private static final String QTY_ITEM_RECEIVE = "qty";

    private static final String CREATE_TABLE_CART = " CREATE TABLE "
            + TABLE_DATA_CART + "(" +ID_ITEM + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BARCODE_ITEM + " TEXT, " + NAME_ITEM + " TEXT, " + NAME_ALIAS + " TEXT, " +QTY_ITEM+ " TEXT," + PRICE_ITEM + " TEXT "+")";

   /* private static final String CREATE_TABLE_TEMP_ITEM = " CREATE TABLE "
            + TABLE_DATA_TEMP_ITEM + "(" +TEMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TEMP_BARCODE + " TEXT, " +TEMP_STOCK+ " TEXT, " +TEMP_SUB_CATEGORY+" TEXT" + ")";

    private static final String CREATE_TABLE_RECEIVE = " CREATE TABLE "
            + TABLE_DATA_RECEIVE + "(" +ID_ITEM_RECEIVE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BARCODE_ITEM_RECEIVE + " TEXT, " + NAME_ITEM_RECEIVE + " TEXT," +QTY_ITEM+ " TEXT " +")";

    private static final String CREATE_TABLE_SR= " CREATE TABLE "
            + TABLE_DATA_SR + "(" +ID_ITEM + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BARCODE_ITEM + " TEXT, " + NAME_ITEM + " TEXT, " + NAME_ALIAS + " TEXT, " +QTY_ITEM+ " TEXT," + PRICE_ITEM + " TEXT "+")";*/

    Context context;
    public SQLiteHelpers(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CART);
        /*db.execSQL(CREATE_TABLE_TEMP_ITEM);
        db.execSQL(CREATE_TABLE_RECEIVE);
        db.execSQL(CREATE_TABLE_SR);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_DATA_CART);
       /* db.execSQL(" DROP TABLE IF EXISTS " + TABLE_DATA_TEMP_ITEM);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_DATA_RECEIVE);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_DATA_SR);*/
        onCreate(db);
    }

    public ArrayList<HashMap<String,String>> getDataItem(){
        ArrayList<HashMap<String,String>> dataList;
        dataList = new ArrayList<HashMap<String, String>>();
        String query = " SELECT * FROM " + TABLE_DATA_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(ID_ITEM, cursor.getString(0));
                map.put(BARCODE_ITEM, cursor.getString(1));
                map.put(NAME_ITEM, cursor.getString(2));
                map.put(NAME_ALIAS, cursor.getString(3));
                map.put(QTY_ITEM, cursor.getString(4));
                map.put(PRICE_ITEM, cursor.getString(5));
                dataList.add(map);
            } while (cursor.moveToNext());
        }
        Log.e("select sqlite", "" +dataList);

        db.close();
        return dataList;
    }

    public void insertData (String barcode, String name, String qty, String price, String alias_name ){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " INSERT INTO " + TABLE_DATA_CART + "(barcode, name, qty, price, alias_name )" +
                " VALUES ('"+barcode+"','"+name+"', '"+qty+"', '"+price+"', '"+alias_name+"')";

        Log.e("inset sqlite", "" + query);
        db.execSQL(query);
        db.close();
    }

    public void updateData(String id, String qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " UPDATE " + TABLE_DATA_CART + " SET "
                + QTY_ITEM + "='" + qty + "'"
                + " WHERE " + ID_ITEM + "=" + "'" + id + "'";
        Log.e("update sqlite", query);
        db.execSQL(query);
        db.close();
    }

    public void delete (int id){
        SQLiteDatabase db = this.getWritableDatabase();
//        sqlite_sequence where name='your_table'
        String query = " DELETE FROM " + TABLE_DATA_CART + " WHERE " + ID_ITEM +
                "=" + "'" + id + "'";
        Log.e("delete", query);
        db.execSQL(query);
        db.close();
    }

    public void deleteAllData (){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM cart" ;
        String query2 =" DELETE FROM sqlite_sequence WHERE name='cart'";
        Log.e("delete", query);
        db.execSQL(query);
        db.execSQL(query2);
        db.close();
    }


    /*public ArrayList<HashMap<String,String>> getDataItemSR(){
        ArrayList<HashMap<String,String>> dataList;
        dataList = new ArrayList<HashMap<String, String>>();
        String query = " SELECT * FROM " + TABLE_DATA_SR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(ID_ITEM, cursor.getString(0));
                map.put(BARCODE_ITEM, cursor.getString(1));
                map.put(NAME_ITEM, cursor.getString(2));
                map.put(NAME_ALIAS, cursor.getString(3));
                map.put(QTY_ITEM, cursor.getString(4));
                map.put(PRICE_ITEM, cursor.getString(5));
                dataList.add(map);
            } while (cursor.moveToNext());
        }
        Log.e("select sqlite", "" +dataList);

        db.close();
        return dataList;
    }*/

    /*public void insertDataSR (String barcode, String name, String qty, String price, String alias_name ){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " INSERT INTO " + TABLE_DATA_SR + "(barcode, name, qty, price, alias_name )" +
                " VALUES ('"+barcode+"','"+name+"', '"+qty+"', '"+price+"', '"+alias_name+"')";

        Log.e("inset sqlite", "" + query);
        db.execSQL(query);
        db.close();
    }*/

    /*public void insertTemp (String barcode, String stock, String subCat){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " INSERT INTO " + TABLE_DATA_TEMP_ITEM + "("+TEMP_BARCODE+", "+TEMP_STOCK+", "+TEMP_SUB_CATEGORY+")" +
                " VALUES ('"+barcode+"','"+stock+"', '"+subCat+"')";

        Log.e("inset sqlite", "" + query);
        db.execSQL(query);
        db.close();
    }*/

    /*String subCat="";
    public String getTempStock(String barcode){
        String nb = "";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + TEMP_STOCK+ ", "+TEMP_SUB_CATEGORY+ " FROM " +TABLE_DATA_TEMP_ITEM+ " " +
                "WHERE "+ TEMP_BARCODE +" = '"+ barcode +"'", null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        if (cursor.getCount() < 1){
            return "not";
        }
        nb = cursor.getString(0);
        subCat = cursor.getString(1);
        return nb;
    }

    public String getSubCat(String barcode) {
        return subCat;
    }*/



    /*public void updateDataSR(String id, String qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " UPDATE " + TABLE_DATA_SR + " SET "
                + QTY_ITEM + "='" + qty + "'"
                + " WHERE " + ID_ITEM + "=" + "'" + id + "'";
        Log.e("update sqlite", query);
        db.execSQL(query);
        db.close();
    }*/

    /*public void deleteSR (int id){
        SQLiteDatabase db = this.getWritableDatabase();
//        sqlite_sequence where name='your_table'
        String query = " DELETE FROM " + TABLE_DATA_SR + " WHERE " + ID_ITEM +
                "=" + "'" + id + "'";
        Log.e("delete", query);
        db.execSQL(query);
        db.close();
    }*/



    /*public void deleteAllDataSR (){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM salesReturn" ;
        String query2 =" DELETE FROM sqlite_sequence WHERE name='salesReturn'";
        Log.e("delete", query);
        db.execSQL(query);
        db.execSQL(query2);
        db.close();
    }*/

   /* public void deleteAllTempItem (){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM "+TABLE_DATA_TEMP_ITEM+" " ;
        String query2 =" DELETE FROM sqlite_sequence WHERE name='"+TABLE_DATA_TEMP_ITEM+"'";
        Log.e("delete", query);
        db.execSQL(query);
        db.execSQL(query2);
        db.close();
    }

    public ArrayList<HashMap<String,String>> getDataReceive(){
        ArrayList<HashMap<String,String>> dataList;
        dataList = new ArrayList<HashMap<String, String>>();
        String query = " SELECT * FROM " + TABLE_DATA_RECEIVE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(ID_ITEM_RECEIVE, cursor.getString(0));
                map.put(BARCODE_ITEM_RECEIVE, cursor.getString(1));
                map.put(NAME_ITEM_RECEIVE, cursor.getString(2));
                map.put(QTY_ITEM_RECEIVE, cursor.getString(3));
                dataList.add(map);
            } while (cursor.moveToNext());
        }
        Log.e("select sqlite", "" +dataList);

        db.close();
        return dataList;
    }

    public void insertReceive (String barcode, String name, String qty){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " INSERT INTO " + TABLE_DATA_RECEIVE + "(barcode, name, qty)" +
                " VALUES ('"+barcode+"','"+name+"', '"+qty+"')";

        Log.e("inset sqlite", "" + query);
        db.execSQL(query);
        db.close();
    }

    public void updateReceive(String id, String qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " UPDATE " + TABLE_DATA_RECEIVE + " SET "
                + QTY_ITEM_RECEIVE + "='" + qty + "'"
                + " WHERE " + ID_ITEM_RECEIVE + "=" + "'" + id + "'";
        Log.e("update sqlite", query);
        db.execSQL(query);
        db.close();
    }

    public void deleteReceive (int id){
        SQLiteDatabase db = this.getWritableDatabase();
//        sqlite_sequence where name='your_table'
        String query = " DELETE FROM " + TABLE_DATA_RECEIVE + " WHERE " + ID_ITEM_RECEIVE +
                "=" + "'" + id + "'";
        Log.e("delete", query);
        db.execSQL(query);
        db.close();
    }

    public void deleteAllReceive (){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM "+TABLE_DATA_RECEIVE+" " ;
        String query2 =" DELETE FROM sqlite_sequence WHERE name='"+TABLE_DATA_RECEIVE+"'";
        Log.e("delete", query);
        db.execSQL(query);
        db.execSQL(query2);
        db.close();
    }*/
}
