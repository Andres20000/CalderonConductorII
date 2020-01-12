package calderonconductor.tactoapps.com.calderonconductor.Clases;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by tacto on 4/05/17.
 */

public class Utility {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CallPhone= 0;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static String convertToMoney(String valor) {

        String temp = "$";

        try {
            Locale colombia = new Locale("es", "CO");
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(colombia);
            defaultFormat.setMaximumFractionDigits(0);
            temp = defaultFormat.format(Integer.valueOf(valor));
            temp = temp.replace(',', '.');


        } catch (RuntimeException r) {
            Log.i("MONEY", r.getLocalizedMessage());
            Log.i("MONEY", r.getMessage());
            return "$";


        }
        return temp;

    }


    public static Date convertStringConHoraToDate(String fecha) {
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.ENGLISH);
        try {
            Date f = sdfr.parse(fecha);
            return f;
        } catch (Exception e) {
            return null;

        }
    }


    public static String convertToDistancia(float distancia) {

        int metros = (int) distancia;

        if (metros < 1000) {
            return metros + " m";
        } else {
            String res = String.format("%.1f kms", distancia / 1000);
            return res;
        }

    }


    public static String convertToMoney(int valor) {

        String temp = "$";
        try {
            Locale colombia = new Locale("es", "CO");
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(colombia);
            defaultFormat.setMaximumFractionDigits(0);
            temp = defaultFormat.format(valor);
            temp = temp.replace(',', '.');


        } catch (RuntimeException r) {
            Log.i("MONEY", r.getLocalizedMessage());
            Log.i("MONEY", r.getMessage());
            return "$";

        }
        return temp;

    }


    public static Date convertStringToDate(String fecha) {
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date f = sdfr.parse(fecha);
            return f;
        } catch (Exception e) {

        }
        return new Date();
    }


    public static String getFechaHora() {
        DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy h:mm a");

        String fecha = hourdateFormat.format(new Date());
        fecha = fecha.replace("a. m.", "AM");
        fecha = fecha.replace("p. m.", "PM");
        fecha = fecha.replace("a.m.", "AM");
        fecha = fecha.replace("p.m.", "PM");
        fecha = fecha.replace("am", "AM");
        fecha = fecha.replace("pm", "PM");

        return fecha;

    }



    public static String getHora() {
        DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy h:mm a");

        String fecha = hourdateFormat.format(new Date());
        fecha = fecha.replace("a. m.", "AM");
        fecha = fecha.replace("p. m.", "PM");
        fecha = fecha.replace("a.m.", "AM");
        fecha = fecha.replace("p.m.", "PM");
        fecha = fecha.replace("am", "AM");
        fecha = fecha.replace("pm", "PM");

        String[] parts = fecha.split(" ");

        if (parts.length < 3) {
            return "00:00 AM";
        }

        return parts[1] + " " + parts[2];


    }

    public static String convertDateToFechayHora(Date indate) {
        String fecha = null;
        DateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy h:mm a");

        try {
            fecha = sdfr.format(indate);
            fecha = fecha.replace("a. m.", "AM");
            fecha = fecha.replace("p. m.", "PM");
            fecha = fecha.replace("a.m.", "AM");
            fecha = fecha.replace("p.m.", "PM");
            fecha = fecha.replace("am", "AM");
            fecha = fecha.replace("pm", "PM");

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return fecha;
    }


    public static String convertDateToString(Date indate) {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");

        try {
            dateString = sdfr.format(indate);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return dateString;
    }


    public static Calendar convertDateToCalendar(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return cal;
    }

    public static Date addHorasAFechaActual(int horas) {
        Calendar calendar = Calendar.getInstance();
        System.out.println("Original = " + calendar.getTime());

        // Substract 2 hour from the current time
        calendar.add(Calendar.HOUR, horas);

        System.out.println("Updated  = " + calendar.getTime());
        return calendar.getTime();
    }


    public static Date convetirToDate(String fecha, String hora) {
        Calendar cal = Calendar.getInstance();
        String parts[] = fecha.split("\\/");
        int year = Integer.parseInt(parts[2]);
        int mes = Integer.parseInt(parts[1]) - 1;
        int dia = Integer.parseInt(parts[0]);
        parts = hora.split(" ");
        String nhora = parts[0];
        String ama = hora.substring(hora.length() - 2);
        parts = nhora.split(":");
        int thoras = Integer.valueOf(parts[0]);
        int minutos = Integer.parseInt(parts[1]);

        int kkk = thoras;


        if (ama.equals("PM") && kkk != 12) {
            cal.set(year, mes, dia, kkk + 12, minutos);

            System.out.println("Updated  = " + cal.getTime());
            return cal.getTime();
        }

        cal.set(year, mes, dia, kkk, minutos);

        System.out.println("Updated  = " + cal.getTime());
        return cal.getTime();


    }


    public static String llenarCeros(String inicial, int numero) {

        String snum = numero + "";
        while (snum.length() < 6) {
            snum = "0" + snum;
        }

        return inicial + snum;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }


    public static boolean LunCheck(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = 0;
            try {
                n = Integer.parseInt(ccNumber.substring(i, i + 1));
            } catch (Exception e) {
                return false;
            }
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }


    public static int calcularMesesEntresFechas(Date fechaInicio, Date fechaFin) {
        try {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(fechaInicio);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(fechaFin);
            int startMes = (startCalendar.get(Calendar.YEAR) * 12) + startCalendar.get(Calendar.MONTH);
            int endMes = (endCalendar.get(Calendar.YEAR) * 12) + endCalendar.get(Calendar.MONTH);
            int diffMonth = endMes - startMes;
            return diffMonth;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getEdad(Calendar newDate) {
        long diff = new Date().getTime() - newDate.getTime().getTime();
        long dias = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        int anios = (int) Math.floor(dias / 365);
        long resto = dias % 365;
        int meses = (int) Math.floor(resto / 30);

        if (anios == 1) {
            return "1 año " + meses + " meses";
        }

        if (anios > 0) {
            return anios + " Años " + meses + " meses";
        } else {
            return meses + " meses";
        }


    }

    public static String getVersionParaUsuario(Context context) {
        PackageInfo pInfo;

        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return "Versión " + pInfo.versionName + " (" + pInfo.versionCode + ")";
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }

        return "";


    }


    public static String getFotmatoFechaDestino(Date fecha) {

        SimpleDateFormat dfsimple = new SimpleDateFormat("dd/MM/yyyy");

        if (formatearFecha(fecha).equals("Hoy") || formatearFecha(fecha).equals("Mañana") || formatearFecha(fecha).equals("Pasado Mañana")) {

            return formatearFecha(fecha);
        }

        Calendar calendar = Calendar.getInstance();
        final int year1 = calendar.get(Calendar.YEAR);
        final int month1 = calendar.get(Calendar.MONTH) + 1;
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        final String fecha_actual = day1 + "/" + month1 + "/" + year1;


        try {
            //convertimos a tipo fecha
            Date date_1 = dfsimple.parse(fecha_actual);
            Date date_2 = fecha;

            //convertimos de date a DAtetime
            DateTime fechanow = new DateTime(date_1);
            DateTime fechaOrden = new DateTime(date_2);

            Days.daysBetween(fechanow, fechaOrden).getDays();

            int f = fechanow.compareTo(fechaOrden);
            int f2 = fechaOrden.compareTo(fechanow);


            if (f2 < 0) {
                return dfsimple.format(date_2);
            } else if (Days.daysBetween(fechanow, fechaOrden).getDays() < 0) {
                return formatearFecha(fecha);
            } else {
                return Days.daysBetween(fechanow, fechaOrden).getDays() + " Dias";

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "";

    }

    /**
     * MEtodo que dada una fecha la convierte al formato hoy , mañana y pasado mañana o EEE MMMM dd
     *
     * @return
     */
    public static String formatearFecha(Date f) {

        SimpleDateFormat dfsimple = new SimpleDateFormat("dd/MM/yyyy");


        if (dfsimple.format(f).equals(dfsimple.format(new Date()))) {
            return "Hoy";
        }

        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 1);

        if (dfsimple.format(f).equals(dfsimple.format(gc.getTime()))) {
            return "Mañana";
        }

        gc.add(Calendar.DATE, 1);

        if (dfsimple.format(f).equals(dfsimple.format(gc.getTime()))) {
            return "Pasado Mañana";
        }

        return dfsimple.format(f);

    }

    public static int calcularMinutosEntresFechas(Date fechaInicio, Date fechaFin) {

        long fin = fechaFin.getTime();
        long ini = fechaInicio.getTime();


        int  diff  = (int) Math.abs((( fin -  ini) / 1000 ) / 60 );

        return diff;

    }



    //permiso para hacer llamadas
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermissionCall(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Call phone permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CallPhone);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();


                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CallPhone);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }




}