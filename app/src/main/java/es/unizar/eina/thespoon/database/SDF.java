package es.unizar.eina.thespoon.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SDF {
    static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /** Devuelve una cadena de texto que representa una fecha y hora a partir de un Date */
    public static String format(Date date) {
        return sdf.format(date);
    }

    /** Devuelve un Date a partir de una cadena de texto que representa una fecha y hora */
    public static Date parse(String date) {
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Devuelve la fecha formateada a partir de una cadena de texto que representa fecha y hora */
    public static String getDate(String dateString) {
        Date date = parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.format(Locale.getDefault(),"%02d/%02d/%04d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));
    }

    /** Devuelve la hora formateada a partir de una cadena de texto que representa fecha y hora */
    public static String getTime(String dateString) {
        Date date = parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.format(Locale.getDefault(),"%02d:%02d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));
    }
}
