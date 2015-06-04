package com.example.ginvaell.db_ex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by pxjoke on 27.05.15.
 */
public class NewElementDialog extends DialogFragment {

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog builder = new Dialog(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.new_el, null);
        TextView textView = (TextView) view.findViewById(R.id.el_description);
        textView.setText(description);
        imageView = (ImageView) view.findViewById(R.id.el_image);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.new_el_wrapper);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Do something
                imageView = null;

                if(myBitmap1 != null && !myBitmap1.isRecycled()) {
                    myBitmap1.recycle();
                    myBitmap1 = null; // null reference
                }
                System.gc();
                builder.dismiss();
            }

        });
        textView.setText(description);
        myBitmap1 = decodeSampledBitmapFromResource(getResources(), getResources().getIdentifier(img, "drawable", "com.example.ginvaell.db_ex"), 250, 250);
        imageView.setImageBitmap(myBitmap1);

        //imageView.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(img, "drawable", "com.example.ginvaell.db_ex")));
        //imageView.destroyDrawingCache();
        //imageView.setImageResource(getResources().getIdentifier("stone", "drawable", "com.example.ginvaell.db_ex"));
       // myBitmap1.recycle();
        System.gc();
        builder.setContentView(view);
        // Add action buttons
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //builder.setCanceledOnTouchOutside(true);

        return builder;

    }
    public void onDestroy() {
        super.onDestroy();
        imageView = null;

        if(myBitmap1 != null && !myBitmap1.isRecycled()) {
            myBitmap1.recycle();
            myBitmap1 = null; // null reference
        }
    }
    private ImageView imageView;
    private String description;
    private String img;
    private Bitmap myBitmap1;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImg(String img) {
        this.img = img;
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
}
