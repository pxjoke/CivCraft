package com.example.ginvaell.db_ex;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
    public CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.cellgrid, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView text = (TextView)view.findViewById(R.id.textpart);
        text.setText(cursor.getString(2));
        String c = cursor.getString(5);
        String col = colorByCat(cursor.getInt(5));
        text.setBackgroundColor(Color.parseColor(col));
        text.setTextColor(Color.parseColor("#ffffff"));
        ImageView img = (ImageView)view.findViewById(R.id.imagepart);
        //img.setImageURI(URI.parse("file://mnt/sdcard/cat.jpg"));
        //String imgName = cursor.getString(3); cursor.getString(3)
        Bitmap myBitmap1 = decodeSampledBitmapFromResource(mContext.getResources(), mContext.getResources().getIdentifier( cursor.getString(3), "drawable", "com.example.ginvaell.db_ex"), 100, 100);
        //imageView.setImageBitmap(myBitmap1);

        img.setImageBitmap(myBitmap1);

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

    public String colorByCat(int cat)
    {
        if (cat == 1) return "#388e3c";
        if (cat == 2) return "#009688";
        if (cat == 3) return "#5d4037";
        if (cat == 4) return "#d32f2f";
        if (cat == 5) return "#455a64";
        return "#0288d1";
    }


}
