package com.infoservices.lue.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.PreResitrationEntity;

import java.util.ArrayList;

/**
 * Created by lue on 29-03-2017.
 */

public class PreResitrationAdapter extends BaseAdapter {
    ArrayList<PreResitrationEntity> preResitrationEntities;
    Activity context;
    String from;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Name = "name";
    public static final String Unitno = "unit_no";
    private static LayoutInflater inflater=null;
    public PreResitrationAdapter(Activity mainActivity,  ArrayList<PreResitrationEntity> preResitrationEntities) {
        // TODO Auto-generated constructor stub
        this.preResitrationEntities=preResitrationEntities;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return preResitrationEntities.size();
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
        PreResitrationAdapter.Holder holder=new PreResitrationAdapter.Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.pre_res_item, null);
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        holder.tv=(TextView) rowView.findViewById(R.id.heading_pre_res);
        holder.tv1=(TextView) rowView.findViewById(R.id.time_pre_res);
        holder.tv2=(TextView) rowView.findViewById(R.id.date_pre_res);
        holder.tv3=(TextView) rowView.findViewById(R.id.nifc);
        holder.tv4=(TextView) rowView.findViewById(R.id.car_no);
        holder.tv5=(TextView) rowView.findViewById(R.id.status_pre_res);
        holder.text_passport_no=(TextView) rowView.findViewById(R.id.text_passport_no);
        holder.text_name_with_unit_no=(TextView) rowView.findViewById(R.id.text_name_with_unit_no);
       // holder.img=(ImageView) rowView.findViewById(R.id.image_facility_item);
        holder.tv.setText(preResitrationEntities.get(position).getMember_name());
       // holder.tv1.setText(preResitrationEntities.get(position).getFrom_date()+" to "+preResitrationEntities.get(position).getTo_date());
        holder.tv1.setText(preResitrationEntities.get(position).getFrom_date());
        holder.tv2.setText(preResitrationEntities.get(position).getReg_date());
        holder.tv3.setText("NRIC NO : "+preResitrationEntities.get(position).getNric_number());
        holder.tv4.setText("CAR NO : "+preResitrationEntities.get(position).getCar_number());
        holder.text_passport_no.setText("Passport No : "+preResitrationEntities.get(position).getPassport_no());
        holder.text_name_with_unit_no.setText(sharedPreferences.getString(Name,"")+"   Unit Number:- "+sharedPreferences.getString(Unitno,""));
        if (preResitrationEntities.get(position).getStatus().equals("0")) {
            holder.tv5.setText("Status:-Pending");
        }else{
            holder.tv5.setText("Status:-Confirmed");
        }
        /*Picasso.with(context)
                .load("http://demo.lueinfoservices.com/"+facilitiesEntities.get(position).getPhoto())
                .placeholder(R.drawable.hotel)   // optional
                .error(R.drawable.error)     // optional
                .into(holder.img);*/
        //holder.img.setImageResource(imageId[position]);
       /* rowView.setOnClickListener(new View.OnClickListener() {
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
        TextView tv,tv1,tv2,tv3,tv4,tv5,text_passport_no,text_name_with_unit_no;
        ImageView img;
    }


}