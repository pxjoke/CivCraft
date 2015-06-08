package com.example.ginvaell.db_ex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.GridView;
import android.widget.ImageButton;

import java.io.IOException;

/**
 * Created by ginva_000 on 07.06.2015.
 */
public class DataHandler {
    private DataBaseHelper sqlHelper;
    private Cursor userCursor, all, result;
    private CustomCursorAdapter userAdapter;
    private GridView mList;
    ImageButton right, left, center;
    private Context mContext;
    private BitmapLoader bitmapLoader;
    private ContentValues cv;

    public DataHandler(Context mContext,GridView mList, BitmapLoader bitmapLoader) {
        this.mList = mList;
//        this.center = center;
        this.mContext = mContext;
        this.bitmapLoader = bitmapLoader;
//gitt
        sqlHelper = new DataBaseHelper(mContext);

        try {
            sqlHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        cv = new ContentValues();
        cv.put("open", "1");
    }

    public void openDataBaseAndGetData() {
        try {
            sqlHelper.openDataBase();
            getDataFromDataBase("open=1");
        } catch (SQLException ex) {
        }
    }

    public void getDataFromDataBase(String where) {
        queryFromDataBase(where);
        userCursor.moveToFirst();
        userAdapter = new CustomCursorAdapter(mContext, userCursor, 1, bitmapLoader);
        mList.setAdapter(userAdapter);
    }

    public void queryFromDataBase(String where) {
        userCursor = sqlHelper.database.query(DataBaseHelper.TABLE, null, where, null, null, null, null);
        all = sqlHelper.database.query(DataBaseHelper.TABLE, null, null, null, null, null, null);
    }

    public int check(int parent1, int parent2){
        if(parent1 > parent2) {
            int a = parent2;
            parent2 = parent1;
            parent1 = a;
        }

        result = sqlHelper.database.query(DataBaseHelper.TABLE, null, "parent1="+parent1+" AND parent2="+parent2, null, null, null, null);
        if(result.getCount() > 0) {
            result.moveToFirst();
            return result.getInt(0);
        }
        return 0;
    }

    public void updateDataInDataBase(int id){
        String where = DataBaseHelper.UID + "=" + id;
        sqlHelper.database.update(DataBaseHelper.TABLE, cv, where, null);
        queryFromDataBase("open==1");
        userAdapter.notifyDataSetChanged();
        userAdapter.changeCursor(userCursor);
        mList.setAdapter(userAdapter);
    }

    public DataBaseHelper getSqlHelper() {
        return sqlHelper;
    }

    public Cursor getUserCursor() {
        return userCursor;
    }

    public Cursor getAll() {
        return all;
    }

    public CustomCursorAdapter getUserAdapter() {
        return userAdapter;
    }

    public Cursor getResult() {
        return result;
    }
}
