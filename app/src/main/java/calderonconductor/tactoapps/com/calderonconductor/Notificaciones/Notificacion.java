package calderonconductor.tactoapps.com.calderonconductor.Notificaciones;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RemoteViews;
import static android.os.SystemClock.sleep;

import calderonconductor.tactoapps.com.calderonconductor.MainActivity;
import calderonconductor.tactoapps.com.calderonconductor.R;

public class Notificacion {
    private NotificationManager notifManager;

    private void ActivarSonido(Context activity){
        AudioManager audioManager = (AudioManager) activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            float count=10*.01f;
            int origionalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

            MediaPlayer mp = MediaPlayer.create(activity, R.raw.pyxis);
            mp.setLooping(false);
            // mp = MediaPlayer.create(getApplicationContext(), notification1);
            count=10*.01f;
            mp.setVolume(count,count);
            mp.start();
            sleep(3000);
            count=10*.01f;
            mp = MediaPlayer.create(activity, R.raw.pyxis);
            mp.start();
            sleep(3000);
            mp = MediaPlayer.create(activity, R.raw.pyxis);
            mp.start();
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            //audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            //To increase media player volume
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            //To decrease media player volume
            //audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

            float count = 0;
            MediaPlayer mp = MediaPlayer.create(activity, R.raw.pyxis1);
            mp.setLooping(false);
            // mp = MediaPlayer.create(getApplicationContext(), notification1);
            count=10*.01f;
            mp.start();
            sleep(10000);
            count=10*.01f;
            mp.stop();
        }

    }


    public void Notificacion(Context activity, String titulo, String mensaje, String estado, int id, int codigo, boolean notificacion_servicio, boolean subirvolumen){
        RemoteViews notificationView = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ){
            notificationView = new RemoteViews(activity.getPackageName(), R.layout.notificacion);

            notificationView.setTextViewText(R.id.TextBox_Notif_Mensaje, mensaje);
            notificationView.setTextViewText(R.id.TextBox_Notif_Titulo, titulo);
            notificationView.setTextViewText(R.id.TextBox_Notif_Tipo, estado);
            switch(codigo){
                case 1:
                    notificationView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_rojo);
                    break;
                case 2:
                    notificationView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_amarillo);
                    break;
                case 3:
                    notificationView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_azul);
                    break;
                case 4:
                    notificationView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_blanco);
                    break;
            }

        } else {
            notificationView = new RemoteViews(activity.getPackageName(), R.layout.notificacion_a5_normal);

            switch(codigo){
                case 1:
                    notificationView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_rojo);
                    break;
                case 2:
                    notificationView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_amarillo);
                    break;
                case 3:
                    notificationView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_azul);
                    break;
                case 4:
                    notificationView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_blanco);
                    break;
            }

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1 ){
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(ns);
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder(activity)
                    .setContentTitle(titulo)
                    .setSmallIcon(R.mipmap.ic_launcher);

            Notification notification = new Notification(R.mipmap.ic_launcher,  "Servicio", System.currentTimeMillis());

            Intent notificationIntent = new Intent(activity, MainActivity.class);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(activity, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (notificacion_servicio == true){
                notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
            }



            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notification1.setSmallIcon(R.mipmap.ic_launcher);
            notification.icon = R.mipmap.ic_launcher;
            notification.color = R.color.colorPrimary;
            notification.contentView = notificationView;
            notification.contentIntent = pendingNotificationIntent;
            notification.priority = Notification.PRIORITY_MAX;
            notification.sound =soundUri;
            notificationManager.notify(id, notification);

            if(subirvolumen == true){
                ActivarSonido(activity);
            }


        } else{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O  ){

                Intent intent;
                PendingIntent pendingIntent;
                NotificationCompat.Builder builder;
                if (notifManager == null) {
                    notifManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
                }
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = notifManager.getNotificationChannel(String.valueOf(id));
                if (mChannel == null) {
                    mChannel = new NotificationChannel(String.valueOf(id),titulo, importance);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notifManager.createNotificationChannel(mChannel);
                }
                builder = new NotificationCompat.Builder(activity, String.valueOf(id));
                intent = new Intent(activity, MainActivity.class);

                if (notificacion_servicio == true){
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }


                pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
                builder.setContentTitle(titulo)                            // required
                        .setSmallIcon(R.mipmap.ic_launcher)   // required
                        .setContentText(activity.getString(R.string.app_name)) // required
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker(titulo)
                        .setContent(notificationView)
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});


                Notification notification = builder.build();


                if (notificacion_servicio == true){
                    notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
                }


                notifManager.notify(id, notification);

                if(subirvolumen == true){
                    ActivarSonido(activity);
                }

            } else {

                String ns = Context.NOTIFICATION_SERVICE;
                NotificationManager notificationManager = (NotificationManager) activity.getSystemService(ns);

                Notification notification = new Notification(R.mipmap.ic_launcher,  "Servicio", System.currentTimeMillis());

                Intent notificationIntent = new Intent(activity, MainActivity.class);
                PendingIntent pendingNotificationIntent = PendingIntent.getActivity(activity, 0, notificationIntent, 0);
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                RemoteViews bigView = new RemoteViews(activity.getApplicationContext().getPackageName(),
                        R.layout.notificacion_a5);

                bigView.setTextViewText(R.id.TextBox_Notif_Mensaje, mensaje);
                bigView.setTextViewText(R.id.TextBox_Notif_Titulo, titulo);
                bigView.setTextViewText(R.id.TextBox_Notif_Tipo, estado);
                switch(codigo){
                    case 1:
                        bigView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_rojo);
                        break;
                    case 2:
                        bigView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_amarillo);
                        break;
                    case 3:
                        bigView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_azul);
                        break;
                    case 4:
                        bigView.setImageViewResource(R.id.Imagen_Codigo, R.drawable.circulo_blanco);
                        break;
                }

                notification.bigContentView = bigView;

                notification.contentView = notificationView;
                notification.contentIntent = pendingNotificationIntent;
                notification.priority = Notification.PRIORITY_HIGH;
                notification.sound =soundUri;

                if (notificacion_servicio == true){
                    notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
                }


                notificationManager.notify(id, notification);


                if(subirvolumen == true){
                    ActivarSonido(activity);
                }



            }

        }

    }

}
