package es.unizar.eina.thespoon.database;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;
import java.util.Date;

/** Clase con métodos para comprobar que un pedido es correcto */
public class CMP {
    public static boolean pedidoCorrecto(Pedido pedido, boolean showToast, Context context) {
        if (TextUtils.isEmpty(pedido.getNombreCliente())) {
            String errorMessage = "El nombre de un cliente no puede estar vacío";
            Log.e("Pedido", errorMessage);
            if (showToast) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
            return false;
        } else if (TextUtils.isEmpty(pedido.getTelefonoCliente())) {
            String errorMessage = "El teléfono de un cliente no puede estar vacío";
            Log.e("Pedido", errorMessage);
            if (showToast) {
                Toast.makeText( context, errorMessage, Toast.LENGTH_SHORT).show();
            }
            return false;
        } else if (!fechaCorrecta(pedido.getFechaHoraRecogida(),showToast,context)) {
            return false;
        } else if (pedido.getEstado() == null) {
            String errorMessage = "El pedido no puede tener estado nulo";
            Log.e("Pedido", errorMessage);
            if (showToast) {
                Toast.makeText( context, errorMessage, Toast.LENGTH_SHORT).show();
            }
            return false;
        } else if(!isPhoneNumber(pedido.getTelefonoCliente())) {
            String errorMessage = "El teléfono no es correcto";
            Log.e("Pedido", errorMessage);
            if (showToast) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }
    private static boolean fechaCorrecta(String fechaHora, boolean showToast, Context context) {
        Date dateActual = Calendar.getInstance().getTime();
        Date dateIntroducido = SDF.parse(fechaHora);

        // La fecha y hora de recogida deben de ser posteriores a la fecha y hora actual
        if (dateIntroducido.compareTo(dateActual) <= 0) {
            String errorMessage="La fecha y hora de recogida deben de ser posteriores a la fecha y hora actual";
            if (showToast) {
                Toast.makeText( context, errorMessage, Toast.LENGTH_SHORT).show();
            }
            Log.e("Pedido", errorMessage);
            return false;
        }

        // Convertir Date a Calendar
        Calendar date = Calendar.getInstance();
        date.setTime(dateIntroducido);

        // Comprobar que el día de la semana está entre el martes y el domingo
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            String errorMessage="La tienda solo está abierta de martes a domingo";
            if (showToast) {
                Toast.makeText( context, errorMessage, Toast.LENGTH_SHORT).show();
            }
            Log.e("Pedido", errorMessage);
            return false;
        };

        // Comprobar que el pedido se va a recoger entre las 19:30 y las 23:00pm
        if (!isTimeBetween(date, 19, 30, 23, 0)) {
            String errorMessage="El pedido se ha de recoger entre las 19:30 y las 23.00pm";
            if (showToast) {
                Toast.makeText( context, errorMessage, Toast.LENGTH_SHORT).show();
            }
            Log.e("Pedido", errorMessage);
            return false;
        }

        return true;
    }

    private static boolean isTimeBetween(Calendar targetCal, int startHour, int startMinute, int endHour, int endMinute) {
        Calendar startCal = (Calendar) targetCal.clone();
        startCal.set(Calendar.HOUR_OF_DAY, startHour);
        startCal.set(Calendar.MINUTE, startMinute);

        Calendar endCal = (Calendar) targetCal.clone();
        endCal.set(Calendar.HOUR_OF_DAY, endHour);
        endCal.set(Calendar.MINUTE, endMinute);

        return (targetCal.after(startCal) || targetCal.equals(startCal)) &&
                (targetCal.before(endCal) || targetCal.equals(endCal));
    }

    public static boolean isPhoneNumber(String input) {
        // Define the pattern for a typical US phone number
        //String phoneNumberRegex = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        String phoneNumberRegex = "^\\(?(\\d+)\\)?[- ]?(\\d+)[- ]?(\\d+)$";
        // Create a Pattern object
        Pattern pattern = Pattern.compile(phoneNumberRegex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(input);

        // Check if the input string matches the pattern
        return matcher.matches();
    }
}
