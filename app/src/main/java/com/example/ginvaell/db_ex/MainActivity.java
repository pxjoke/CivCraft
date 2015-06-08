package com.example.ginvaell.db_ex;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    GridView mList;
    ImageButton right, left, center;
    DataBaseHelper sqlHelper;
    Cursor userCursor, ties, all;
    CustomCursorAdapter userAdapter;
    Context mContext;
    int pos, parent1, parent2, child;
    String[] tie, result;
    boolean isLeft;
    boolean isLeftEmpty;
    boolean isRightEmpty;
    NewElementDialog newElementDialog;
    int count = 0, rightElementId;
    Bitmap myBitmap1;
    private LruCache<String, Bitmap> mMemoryCache;
    private BitmapLoader bitmapLoader;
    private int resId;
    private DataHandler dataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (GridView) findViewById(R.id.list);
        newElementDialog = new NewElementDialog();
        mContext = getApplicationContext();
        left = (ImageButton) findViewById(R.id.leftButton);
        right = (ImageButton) findViewById(R.id.imageRight);
        center = (ImageButton) findViewById(R.id.imageCenter);
        left.setBackgroundResource(R.drawable.back_selected);
        right.setBackgroundResource(R.drawable.back);
        center.setBackgroundResource(R.drawable.back);

//        sqlHelper = new DataBaseHelper(getApplicationContext());
        isLeft = true;
        isLeftEmpty = true;
        isRightEmpty = true;
        bitmapLoader = new BitmapLoader(getApplicationContext());
        dataHandler = new DataHandler(mContext, mList,bitmapLoader);
        sqlHelper = dataHandler.getSqlHelper();
        // создаем базу данных
//        try {
//            sqlHelper.createDataBase();
//        } catch (IOException ioe) {
//            throw new Error("Unable to create database");
//        }


    }

    @Override
    public void onResume() {
        super.onResume();
        dataHandler.openDataBaseAndGetData();

        userCursor = dataHandler.getUserCursor();
        all = dataHandler.getAll();
        userAdapter = dataHandler.getUserAdapter();

        mList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                        count++;
                        dataHandler.getUserCursor().moveToPosition(position);
                        pos = dataHandler.getUserCursor().getInt(0);
                        if (isLeftEmpty) {
                            bitmapLoader.load(dataHandler.getUserCursor().getString(3), left, 100, 100);
                            left.setBackgroundResource(R.drawable.back);
                            right.setBackgroundResource(R.drawable.back_selected);
                           isLeftEmpty = false;
                            parent1 = pos;
                            if (!isRightEmpty) {
                                compare();
                            }
                            dataHandler.getUserCursor().moveToPosition(position);
                            tie = dataHandler.getUserCursor().getString(6).split("; ");
                            result = dataHandler.getUserCursor().getString(7).split("; ");
                        } else {
                           bitmapLoader.load(dataHandler.getUserCursor().getString(3), right, 100, 100);
                            isRightEmpty = false;
                            rightElementId = pos;
                            parent2 = pos;
                            compare();
                        }
                    }
                }
        );
    }

    private void compare() {
        //String newElement = tryElements(userCursor.getString(0));
        child = dataHandler.check(parent1, parent2);
        if (child == 0) {
            center.setImageResource(R.drawable.none);
        } else {
            all.moveToPosition(child - 1);
            if (all.getString(4).compareTo("1") == 0) {
                center.setImageResource(R.drawable.none);
            } else {
                bitmapLoader.load(all.getString(3), center, 100, 100);
                newElementDialog = new NewElementDialog();
                newElementDialog.setDescription("Вы открыли " + all.getString(2) + "!");
                newElementDialog.setImg(all.getString(3));
                newElementDialog.show(getSupportFragmentManager(), "new_el_tag");
                dataHandler.updateDataInDataBase(child);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем подключения
        sqlHelper.database.close();
        userCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public String tryElements(String element) {


        for (int i = 0; i < tie.length; i++) {
            if (tie[i].compareTo(element) == 0) {

                return result[i];
            }
        }

        return "0";
    }

    public void onBasicClick(View view) {
        userCursor = sqlHelper.database.query(DataBaseHelper.TABLE, null, "open=1 AND cat=1", null, null, null, null);

        userAdapter.notifyDataSetChanged();
        String[] headers = new String[]{DataBaseHelper.COLUMN_NAME, DataBaseHelper.COLUMN_YEAR};
        userAdapter.changeCursor(userCursor);

        mList.setAdapter(userAdapter);
    }

    public void onNatClick(View view) {
    }

    public void onAllClick(View view) {

    }

    public void onLeftClick(View view) {
        isLeft = true;
        isLeftEmpty = true;
        left.setImageResource(0);
        left.setBackgroundResource(R.drawable.back_selected);
        right.setBackgroundResource(R.drawable.back);
        all.moveToPosition(rightElementId - 1);
        tie = all.getString(6).split("; ");
        result = all.getString(7).split("; ");


    }

    public void onRightClick(View view) {
        isLeft = false;
        isRightEmpty = true;
        right.setImageResource(0);
    }

    public void onCenterClick(View view) {
        left.setBackgroundResource(R.drawable.back_selected);
        right.setBackgroundResource(R.drawable.back);
        center.setImageResource(0);
        left.setImageResource(0);
        right.setImageResource(0);
        isLeftEmpty = true;
        isRightEmpty = true;
    }

    public void onClearClick(View view) {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Вы действительно хотите начать игру с чистого листа?")
                .setTitle("Удалить всю статистику")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        clearAll();
                    }
                })
                .setNegativeButton("Нет, это ошибка", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });


// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void clearAll() {
        try {
            sqlHelper.copy();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        sqlHelper.openDataBase();
//            userCursor = sqlHelper.database.rawQuery("select * from "+DataBaseHelper.TABLE, null);
        userCursor = sqlHelper.database.query(DataBaseHelper.TABLE, null, "open=1", null, null, null, null);
        all = sqlHelper.database.query(DataBaseHelper.TABLE, null, null, null, null, null, null);
        userAdapter = new CustomCursorAdapter(this, userCursor, 1, bitmapLoader);
        mList.setAdapter(userAdapter);
        left.setBackgroundResource(R.drawable.back_selected);
        right.setBackgroundResource(R.drawable.back);
        center.setImageResource(0);
        left.setImageResource(0);
        right.setImageResource(0);
        isLeftEmpty = true;
        isRightEmpty = true;
        CharSequence text = "All data cleared!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(mContext, text, duration);
        toast.show();
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void onCatClick(View view) {
        String catigory;
        switch (view.getId()) {
            case R.id.catNature:
                catigory = " AND cat=1";
                break;
            case R.id.catTools:
                catigory = " AND cat=2";
                break;
            case R.id.catMaterials:
                catigory = " AND cat=3";
                break;
            case R.id.catAnimals:
                catigory = " AND cat=4";
                break;
            case R.id.catComfort:
                catigory = " AND cat=5";
                break;
            case R.id.catAll:
                catigory = "";
                break;
            default:
                catigory = " AND cat=1";
                break;

        }

        userCursor = sqlHelper.database.query(DataBaseHelper.TABLE, null, "open=1  " + catigory, null, null, null, null);

        userAdapter.notifyDataSetChanged();
        String[] headers = new String[]{DataBaseHelper.COLUMN_NAME, DataBaseHelper.COLUMN_YEAR};
        userAdapter.changeCursor(userCursor);

        mList.setAdapter(userAdapter);
    }
}
