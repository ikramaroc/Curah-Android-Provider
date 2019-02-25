package com.curahservice.netset;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Html;
import android.util.Log;


import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.base.IndexActivity;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.PrefStore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;


/**
 * Created by netset on 12/8/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private int currentNotificationID = 0;
    public NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private static final String TAG = "MyFirebaseMsgService";
    private Bitmap icon;
    public static notificationListner notification;
    public static String connectionId;
    public static String bookingId_Is="-1";
    public static String CHANNEL_ID = "com.curahservice.netset";
    String connection_id,receiver_id,senderId;
public static ratingInterface ratingInter;
    @Override
    public void onCreate() {
        super.onCreate();

    }


//{receiver_id=48, service_id=2, sender_id=25, label=Request-Notification, message=You Have New Request For Service}

//  {receiver_id=112, msg=Your profile  100 views in last week!, sender_id=, label=profile-NotificationPojo}
//    {receiver_id=112, msg=Your video has viewed by 100  times, label=view-video-notificationListner}
//

    // {receiver_id={"notification_type":"OFF","firstname":"gehe","appointment_from":"05:00:00","login_type":"O","city":"Sahibzada Ajit Singh Nagar","latitude":"30.7046486","facebook_link":"http:\/\/google.com","created_at":"2018-09-05 07:52:16","device_type":"A","type":"S","experience":"8 Year","profile_image":"","password":"25d55ad283aa400af464c76d713c07ad","user_type":"N","updated_at":"2018-09-24 15:56:05","yelp_link":"http:\/\/google.com","customerId":null,"instagram_link":"http:\/\/google.com","admin_aprove":null,"id":48,"identification_card":"","remember_token":"0yH2nQGhk","email":"barber12@yopmail.com","longitude":"76.71787259999999","address":"Shop No.19 Main Market Balongi, Sahibzada Ajit Singh Nagar, Punjab 140301, India","device_id":"dD6aUHFBQe4:APA91bEwYpjR1qTKih3h8Dhkoa49l2bio5Yx0ha0dmCI9e5Ua_g3Z9f6ZfQsAa9JgO0jC5ObkHz9KF308dNf2QWGc-wkPDbNKG-JO_HVELMaq_3B6fZlopnrcBEVgLDjTjW_Fkb-D9iN","appointment_to":"10:00:00","is_suspend":"OFF","lastname":"hehe","social_id":null,"driving_license":"","phone":"959599595995","dob":null,"password_token":"","customer_id":""}, sender_id=25, label=profile-Notification, message=You Have message from customer}

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("----Curah Provider----", remoteMessage.getData().toString());
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Map<String, String> data = remoteMessage.getData();
        icon = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_launcher);
        getMessaage(data);

    }

    //09-24 18:09:47.785 7377-9753/com.curahservice.netset E/----Curah Provider----: {receiver_id=48, booking_id=75, sender_id=25, label=Request-Notification, title=You have new Appoinment Request., message=You Have New Request For Service}
    //This method is only generating push notificationListner
    //It is same as we did in earlier posts
    private void getMessaage(Map<String, String> messageBody) {
        //Log.e("inside", "inside");
        JSONObject jsonObject = new JSONObject(messageBody);
        String message = jsonObject.optString("message");
        String title = jsonObject.optString("title");
        String label = jsonObject.optString("label");
        if (label.equals("Message-Notification")) {

            connection_id = jsonObject.optString("connection_id");
            receiver_id = jsonObject.optString("receiver_id");
            senderId=jsonObject.optString("sender_id");

            if (notification != null && connectionId != null) {
                if (connectionId.equals(connection_id)) {
                    notification.message(message, receiver_id, connection_id);
                } else {
                    notification.newConnection();
                    setDataForMaxPriorityNotification(getApplicationContext(), title, message, icon, label, connection_id,senderId);
                }
                if (!isAppForegoround(getApplicationContext())) {
                    setDataForMaxPriorityNotification(getApplicationContext(), title, message, icon, label, connection_id, senderId);
                }


            } else {
                setDataForMaxPriorityNotification(getApplicationContext(), title, message, icon, label, connection_id,senderId);
            }

        } else if (label.equals("Request-Notification") || label.equals("Cancel-Notification")) {
            String bookingId = jsonObject.optString("booking_id");
            if (notification != null) {
                notification.newAppointment(bookingId);
            }
            if (!bookingId_Is.equals(bookingId)){
                setDataForMaxPriorityNotification(getApplicationContext(), title, message, icon, label, bookingId,"");
            }else if (!isAppForegoround(getApplicationContext())){
                setDataForMaxPriorityNotification(getApplicationContext(), title, message, icon, label, bookingId,"");
            }
        } else if (label.equals("Rating-Notification")) {
            PrefStore store = new PrefStore(MyFirebaseMessagingService.this);
            createAccount.UserInfo info = store.getUserPojo(Const.USERDATA);
            int reviewCount = jsonObject.optInt("count");
            int rating = jsonObject.optInt("rating");
            info.setReviewCount(reviewCount);
            info.setRating(rating + "");
            store.saveUserPojo(Const.USERDATA, info);

            if (ratingInter!=null){
                ratingInter.rating();
            }
        }
    }




      public boolean isAppForegoround(final Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                Log.i("Foreground App", appProcess.processName);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }



    public static void unregisterNotification() {
        notification = null;
    }

    public static void registerChatNotification(notificationListner notificationIs) {
        notification = notificationIs;
    }


    public interface notificationListner {
        void message(String message, String recieverId, String connectionId);

        void newConnection();

        void newAppointment(String bookingid);

    }

    public interface ratingInterface{
        void rating();
    }

    public static  void  registerRating(ratingInterface ratingInterfaceIs){
        ratingInter=ratingInterfaceIs;
    }
    public static  void  unregisterRating(){
        ratingInter=null;
    }


    public void setDataForMaxPriorityNotification(Context context, String title, String content, Bitmap icon, String type, String id,String otherUserId) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = title;
            String description = content;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notificationListner behaviors after this
            notificationManager.createNotificationChannel(channel);
            Bundle data = new Bundle();
            data.putString("type", type);
            data.putString("id", id);
            Intent notificationIntent;
            notificationIntent = new Intent(context, DrawerActivity.class).putExtra("type", type).putExtra("id", id).putExtra("otherUserId", otherUserId).putExtra("from", "notificationListner").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(icon)
                    .setContentTitle(title)
                    .setContentText(Html.fromHtml(content))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            currentNotificationID++;
            int notificationId = currentNotificationID;
            if (notificationId == Integer.MAX_VALUE - 1) {
                notificationId = 0;
            }
            notificationManager.notify(notificationId, mBuilder.build());
        } else {

            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(icon)
                    .setContentTitle(title)
                    .setNumber(3)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content)).setNumber(2)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentText(Html.fromHtml(content));

            sendNotification(context, type, id,otherUserId);
        }


    }

    public void sendNotification(Context context, String type, String id,String otherUserId) {




        Intent notificationIntent;
        notificationIntent = new Intent(context, DrawerActivity.class).putExtra("type", type).putExtra("id", id).putExtra("otherUserId", otherUserId).putExtra("from", "notificationListner").
                setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        currentNotificationID++;
        int notificationId = currentNotificationID;
        if (notificationId == Integer.MAX_VALUE - 1)
            notificationId = 0;
        notificationManager.notify(notificationId, notification);
    }


}




