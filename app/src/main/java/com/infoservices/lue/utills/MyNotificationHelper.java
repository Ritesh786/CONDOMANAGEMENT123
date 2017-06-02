package com.infoservices.lue.utills;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.infoservices.lue.entity.NotificationEntity;



public class MyNotificationHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "MyNotification.db";
	public static final String NOTIFICATION_TABLE_NAME = "notifications";
	public static final String NOTIFICATION_ID = "notification_id";
	public static final String MANAGEMENT_ID = "mgnt_id";
	public static final String TITAL = "title";
	public static final String IMAGE = "image";
	public static final String DESCRIPTION = "description";
	public static final String STATUS = "status";
	public static final String CREATED = "created";

	SQLiteDatabase db2;
	Cursor res;

	public MyNotificationHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table notifications "
				+ "(id integer primary key,notification_id text,mgnt_id text,title text,image text,description text,status text,created text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS notifications");
		onCreate(db);
	}
    
	public boolean insertnotification(String notification_id, String management_id,
			String titel,String img,String description,String status,String created) {
		try {
		db2 = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("notification_id", notification_id);
		contentValues.put("mgnt_id", management_id);
		contentValues.put("title", titel);
		contentValues.put("image", img);
		contentValues.put("description", description);
		contentValues.put("status", status);
		contentValues.put("created", created);
		db2.insert(NOTIFICATION_TABLE_NAME, null, contentValues);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			db2.close();
		}
		return true;
	}
	public boolean deleteAll(){
		int r=numberOfRows();
		try {
			for (int i=0;i<=r;i++){
				deleteNotification( i );
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return true;
	}
	public boolean updateNotification(Integer id,String notification_id, String management_id,
									  String titel,String img,String description,String status,String created) {
		try {
			db2 = this.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put( "notification_id", notification_id );
			contentValues.put( "mgnt_id", management_id );
			contentValues.put( "title", titel );
			contentValues.put( "image", img );
			contentValues.put( "description", description );
			contentValues.put( "status", status );
			contentValues.put( "created", created );
			db2.update( NOTIFICATION_TABLE_NAME, contentValues, " id = ? ",
					new String[]{Integer.toString( id )} );
			//String.valueOf(Integer.toString( id ))
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			db2.close();
		}
		return true;
	}

	public Cursor getData(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from notifications where id=" + id + "",
				null);
		return res;
	}

	public int numberOfRows() {
		int numRows=0;
		try {
			db2 = this.getReadableDatabase();
		numRows = (int) DatabaseUtils.queryNumEntries(db2,
				NOTIFICATION_TABLE_NAME);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			db2.close();
		}
		return numRows;
	}


	public Integer deleteNotification(Integer id) {

		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(NOTIFICATION_TABLE_NAME, "id = ? ",
				new String[] { Integer.toString(id) });
	}

	public ArrayList<NotificationEntity> getAllNotifications() {
		ArrayList<NotificationEntity> array_list = new ArrayList<NotificationEntity>();
		try {
		db2 = this.getReadableDatabase();
		res = db2.rawQuery("select * from notifications", null);
		res.moveToLast();
		while (res. isBeforeFirst() == false) {
			NotificationEntity notificationEntity = new NotificationEntity();
			notificationEntity.setId(res.getString(res
					.getColumnIndex("id")));
			notificationEntity.setNotification_id(res.getString(res
					.getColumnIndex(NOTIFICATION_ID)));
			notificationEntity.setMgnt_id(res.getString(res
					.getColumnIndex(MANAGEMENT_ID)));
			notificationEntity.setTitle(res.getString(res
					.getColumnIndex(TITAL)));
			notificationEntity.setImage(res.getString(res
					.getColumnIndex(IMAGE)));
			notificationEntity.setDescription(res.getString(res
					.getColumnIndex(DESCRIPTION)));
			notificationEntity.setStatus( res.getString( res
					.getColumnIndex( STATUS ) ) );
			notificationEntity.setCreated(res.getString(res
					.getColumnIndex(CREATED)));
			array_list.add(notificationEntity);
			res.moveToPrevious();
		}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			res.close();
			db2.close();
		}
		return array_list;
	}
	
}
