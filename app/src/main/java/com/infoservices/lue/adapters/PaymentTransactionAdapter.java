package com.infoservices.lue.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infoservices.lue.condomanagement.MenuOptionSelectedActivity;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.FacilitiesEntity;
import com.infoservices.lue.entity.PaymentEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lue on 24-03-2017.
 */

public class PaymentTransactionAdapter extends BaseAdapter {
    ArrayList<PaymentEntity> paymentEntities;
    Activity context;
    String from;
    private static LayoutInflater inflater=null;
    public PaymentTransactionAdapter(Activity mainActivity,  ArrayList<PaymentEntity> paymentEntities) {
        // TODO Auto-generated constructor stub
        this.paymentEntities=paymentEntities;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return paymentEntities.size();
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
        PaymentTransactionAdapter.Holder holder=new PaymentTransactionAdapter.Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.listitem, null);
        holder.tv=(TextView) rowView.findViewById(R.id.text_facility_item);
        holder.tv1=(TextView) rowView.findViewById(R.id.point1);
        holder.tv2=(TextView) rowView.findViewById(R.id.point2);
        holder.tv3=(TextView) rowView.findViewById(R.id.point3);
        holder.img=(ImageView) rowView.findViewById(R.id.image_facility_item);
        holder.tv.setText("Grand Total : "+paymentEntities.get(position).getGrand_total());
        holder.tv1.setText("Unit Price : "+paymentEntities.get(position).getUnit_price());
        holder.tv2.setText("Date :-"+paymentEntities.get(position).getDate ());
        if (paymentEntities.get(position).getStatus().equals("1")) {
            holder.tv3.setText("Status :- " +"Success");
        }else{
            holder.tv3.setText("Status :- " + "Failed");
        }
        /*Picasso.with(context)
                .load("http://demo.lueinfoservices.com/"+facilitiesEntities.get(position).getPhoto())
                .placeholder(R.drawable.hotel)   // optional
                .error(R.drawable.error)     // optional
                .into(holder.img);*/
        //holder.img.setImageResource(imageId[position]);
        /*rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent( context, MenuOptionSelectedActivity.class );
                intent.putExtra( "from","Multipurpose Hall" );
                intent.putExtra( "object",facilitiesEntities.get( position ) );
                context.startActivity( intent );
            }
        });*/
        return rowView;
    }

    public class Holder
    {
        TextView tv,tv1,tv2,tv3;
        ImageView img;
    }


}