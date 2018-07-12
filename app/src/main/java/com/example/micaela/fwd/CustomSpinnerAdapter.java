/*This code makes a custom adapter for the drop-down menus on the first page of the app*/

package com.example.micaela.fwd;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private List<String> dropDownItems;

    public CustomSpinnerAdapter(Context context, String[] array) {
        this.dropDownItems = Arrays.asList(array);
        this.context = context;
    }

    public int getCount() {
        return dropDownItems.size();
    }

    public Object getItem(int i) {
        return dropDownItems.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = null;
//      //This is if we don't want this to show in the dropdown menu
//      TextView txt = new TextView(context);
//      txt.setHeight(0); //Set the height to zero because we don't want this to show up!
//      view = txt;
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setGravity(Gravity.CENTER_HORIZONTAL);
        txt.setText(dropDownItems.get(position));
        txt.setTextColor(Color.parseColor("#000000"));
        view = txt;

        if (position == 0) {
            txt.setTextColor(Color.parseColor("#D3D3D3"));
        }
        return view;
    }
    //This is the default view
    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(context);
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setText(dropDownItems.get(i));
        txt.setTextColor(Color.parseColor("#FFFFFF"));
        if (i!=0) {
            viewgroup.setBackground(context.getDrawable(R.drawable.spinner_background_dark));
        } else {
            viewgroup.setBackground(context.getDrawable(R.drawable.spinner_background));
        }
            return txt;
    }

}

