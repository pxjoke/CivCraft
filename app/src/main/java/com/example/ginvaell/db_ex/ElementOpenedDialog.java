package com.example.ginvaell.db_ex;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by pxjoke on 08.06.2015.
 */
public class ElementOpenedDialog extends DialogFragment {
    private BitmapLoader bitmapLoader;
    private String resName;
    private String description;
    private ImageView imageView;


    static ElementOpenedDialog newInstance(String resName, String desc, BitmapLoader bitLoader) {
        ElementOpenedDialog f = new ElementOpenedDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("resName", resName);
        args.putString("description", desc);
        f.setArguments(args);
        f.setBitmapLoader(bitLoader);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resName = getArguments().getString("resName");
        description = getArguments().getString("description");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_el, container, false);
        TextView textView = (TextView) view.findViewById(R.id.el_description);
        textView.setText(description);
        imageView = (ImageView) view.findViewById(R.id.el_image);
        bitmapLoader.loadWithoutCaching(resName, imageView, 250, 250);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.new_el_wrapper);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }

        });
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        imageView = null;
        System.out.println("Dialog dismissed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Dialog destroyed");
    }

    private void setBitmapLoader(BitmapLoader bitLoader) {
        bitmapLoader = bitLoader;
    }
}