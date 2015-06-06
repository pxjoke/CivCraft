package com.example.ginvaell.db_ex;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ginvaell.db_ex.R;

import java.net.URI;

/**
 * Created by ginva_000 on 24.05.2015.
 */
public class CustomCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private BitmapLoader bitmapLoader;
    public CustomCursorAdapter(Context context, Cursor c, int flags, BitmapLoader bit) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        bitmapLoader = bit;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.cellgrid, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView text = (TextView) view.findViewById(R.id.textpart);
        text.setText(cursor.getString(2));
        String c = cursor.getString(5);
        String col = colorByCat(cursor.getInt(5));
        text.setBackgroundColor(Color.parseColor(col));
        text.setTextColor(Color.parseColor("#ffffff"));
        ImageView img = (ImageView) view.findViewById(R.id.imagepart);


        //img.setImageBitmap(myBitmap1);
        bitmapLoader.load(cursor.getString(3), img, 100, 100);

    }


    public String colorByCat(int cat) {
        if (cat == 1) return "#388e3c";
        if (cat == 2) return "#009688";
        if (cat == 3) return "#5d4037";
        if (cat == 4) return "#d32f2f";
        if (cat == 5) return "#455a64";
        return "#0288d1";
    }


}
