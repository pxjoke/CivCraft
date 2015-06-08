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

    private ImageView imageView;
    private String description;
    private String img;
    private BitmapLoader bitmapLoader;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog builder = new Dialog(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_el, null);
        TextView textView = (TextView) view.findViewById(R.id.el_description);
        textView.setText(description);
        imageView = (ImageView) view.findViewById(R.id.el_image);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.new_el_wrapper);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               builder.dismiss();
            }

        });
        textView.setText(description);
        bitmapLoader.loadWithoutCaching(img, imageView, 250, 250);

        builder.setContentView(view);
        // Add action buttons
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //builder.setCanceledOnTouchOutside(true);

        return builder;

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        imageView = null;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public void setBitmapLoader(BitmapLoader bitmapLoader) {
        this.bitmapLoader = bitmapLoader;
    }
}
