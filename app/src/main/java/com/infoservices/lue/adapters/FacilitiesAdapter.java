package com.infoservices.lue.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infoservices.lue.condomanagement.MainActivity;
import com.infoservices.lue.condomanagement.MenuOptionSelectedActivity;
import com.infoservices.lue.condomanagement.PayNowActivity;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.FacilitiesEntity;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lue on 18-07-2016.
 */
public class FacilitiesAdapter extends BaseAdapter {
    ArrayList<FacilitiesEntity> facilitiesEntities;
    Activity context;
    String from;
       private static LayoutInflater inflater=null;
    public FacilitiesAdapter(Activity mainActivity,  ArrayList<FacilitiesEntity> facilitiesEntities) {
        // TODO Auto-generated constructor stub
        this.facilitiesEntities=facilitiesEntities;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return facilitiesEntities.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
          Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.listitem, null);
        holder.tv=(TextView) rowView.findViewById(R.id.text_facility_item);
        holder.tv1=(TextView) rowView.findViewById(R.id.point1);
        holder.tv2=(TextView) rowView.findViewById(R.id.point2);
        holder.tv3=(TextView) rowView.findViewById(R.id.point3);
        holder.img=(ImageView) rowView.findViewById(R.id.image_facility_item);
        holder.tv.setText(facilitiesEntities.get(position).getTitle());
        holder.tv1.setText(facilitiesEntities.get(position).getPoint_1());
        holder.tv2.setText(facilitiesEntities.get(position).getPoint_2());
        holder.tv3.setText(facilitiesEntities.get(position).getPoint_3());
        Picasso.with(context)
                .load("http://api.condoassist2u.com/"+facilitiesEntities.get(position).getPhoto())
                .placeholder(R.drawable.lobo2)   // optional
                .error(R.drawable.lobo2)     // optional
                .into(holder.img);
        //holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent( context, MenuOptionSelectedActivity.class );
                intent.putExtra( "from","Multipurpose Hall" );
                intent.putExtra( "object",facilitiesEntities.get( position ) );
                context.startActivity( intent );
            }
        });
        return rowView;
    }

    public class Holder
    {
        TextView tv,tv1,tv2,tv3;
        ImageView img;
    }


}