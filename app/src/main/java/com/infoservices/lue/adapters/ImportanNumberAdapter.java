package com.infoservices.lue.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infoservices.lue.condomanagement.Manifest;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.ImportantContactEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by lue on 21-07-2016.
 */
public class ImportanNumberAdapter extends BaseAdapter {
    ArrayList<ImportantContactEntity> importantContactEntities;
    Activity context;
    private  static  LayoutInflater inflater=null;
    public ImportanNumberAdapter(Activity context,ArrayList<ImportantContactEntity> importantContactEntities){
        this.context=context;
        this.importantContactEntities=importantContactEntities;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return importantContactEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate( R.layout.important_number_item, null);
        holder.text_contact_item=(TextView) rowView.findViewById(R.id.text_contact_item);
        holder.text_designation=(TextView) rowView.findViewById(R.id.text_designation);
        holder.img=(ImageView) rowView.findViewById(R.id.call_button);
        holder.text_contact_item.setText(importantContactEntities.get(position).getName());
        holder.text_designation.setText(importantContactEntities.get(position).getDesignation());
       /* Picasso.with(context)
                .load("http://192.168.1.185/condo/"+facilitiesEntities.get(position).getPhoto())
                .placeholder(R.drawable.user_profile_icon2_84)   // optional
                .error(R.drawable.loginusericon)     // optional
                .into(holder.img);*/
        //holder.img.setImageResource(imageId[position]);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               //  Toast.makeText(context, "You Clicked "+importantContactEntities.get(position).getNumber(), Toast.LENGTH_LONG).show();
                try {
                 Intent intent = new Intent("android.intent.action.CALL");
                 Uri data = Uri.parse("tel:"+ importantContactEntities.get(position).getNumber());
                 intent.setData(data);
                 context.startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return rowView;
    }
    public class Holder
    {
        TextView text_contact_item,text_designation;
        ImageView img;
    }

}
