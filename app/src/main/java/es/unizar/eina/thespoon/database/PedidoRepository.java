package es.unizar.eina.thespoon.database;


import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import es.unizar.eina.thespoon.ui.Pedidos;

public class PedidoRepository {

    private PedidoDao mPedidoDao;
    private LiveData<List<Pedido>> mAllPedidos;
    public LiveData<List<Pedido>> mAllPedidosPorEstado;
    public LiveData<List<Pedido>> mAllPedidosPorNombreYEstado;

    // Pedido that in order to unit test the PedidoRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public PedidoRepository(Application application) {
        TheSpoonRoomDatabase db = TheSpoonRoomDatabase.getDatabase(application);
        mPedidoDao = db.pedidoDao();
        mAllPedidos = mPedidoDao.getOrderedPlatos();
        mAllPedidosPorEstado=mPedidoDao.getOrderedPedidosPorEstado();
        mAllPedidosPorNombreYEstado=mPedidoDao.getOrderedPedidosPorNombreYEstado();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Pedido>> getAllPedidos() {
        return mAllPedidos;
    }
    public LiveData<List<Pedido>> getAllPedidosPorEstado() { return mAllPedidosPorEstado; }
    public LiveData<List<Pedido>> getAllPedidosPorNombreYEstado() { return mAllPedidosPorNombreYEstado; }
    public LiveData<List<Pedido>> getPedidosPorEstado(EstadoPedido estadoSeleccionado) {
        return mPedidoDao.getPedidosPorEstado(estadoSeleccionado);
    }

    /** Inserta un pedido
     * @param pedido
     * @return un valor entero largo con el identificador del plato que se ha creado.
     */
    public long insert(Pedido pedido) {
        final long[] result = {0};
        CountDownLatch latch = new CountDownLatch(1);
        // You must call this on a non-UI thread or your app will throw an exception. Room ensures
        // that you're not doing any long running operations on the main thread, blocking the UI.
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            if (pedidoCorrecto(pedido)) {
                result[0] = mPedidoDao.insert(pedido);
                if (mPedidoDao.getPedidoCount() > 2000) {
                    Log.i("Inserción de pedido", "Se ha superado el límite de 2000 pedidos");
                }
            }
            latch.countDown(); // Signal that the operation is complete
        });

        try {
            latch.await(); // Wait until the count reaches zero
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];
    }

    /** Modifica un pedido
     * @param pedido
     * @return un valor entero con el número de filas modificadas.
     */
    public int update(Pedido pedido) {
        final int[] result = {0};
        CountDownLatch latch = new CountDownLatch(1);
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            if (pedidoCorrecto(pedido)) {
                result[0] = mPedidoDao.update(pedido);
            }
            latch.countDown(); // Signal that the operation is complete
        });

        try {
            latch.await(); // Wait until the count reaches zero
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];
    }

    /** Elimina un pedido
     * @param pedido
     * @return un valor entero con el número de filas eliminadas.
     */
    public int delete(Pedido pedido) {
        final int[] result = {0};
        CountDownLatch latch = new CountDownLatch(1);
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mPedidoDao.delete(pedido);
            latch.countDown(); // Signal that the operation is complete
        });

        try {
            latch.await(); // Wait until the count reaches zero
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];
    }

    /** Elimina todos los pedidos */
    public void deleteAll() {
        CountDownLatch latch = new CountDownLatch(1);
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPedidoDao.deleteAll();
            latch.countDown(); // Signal that the operation is complete
        });

        try {
            latch.await(); // Wait until the count reaches zero
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean pedidoCorrecto(Pedido pedido) {
        if (TextUtils.isEmpty(pedido.getNombreCliente())) {
            Log.e("Pedido", "El nombre de un cliente no puede estar vacío");
            return false;
        } else if (TextUtils.isEmpty(pedido.getTelefonoCliente())) {
            Log.e("Pedido", "El teléfono de un cliente no puede estar vacío");
            return false;
        }
        return true;
    }

    private boolean fechaCorrecta(String fechaHora) {
        Date dateActual = Calendar.getInstance().getTime();
        Date dateIntroducido = SDF.parse(fechaHora);

        // La fecha y hora de recogida deben de ser posteriores a la fecha y hora actual
        if (dateIntroducido.compareTo(dateActual) <= 0) {
            Log.e("Pedido", "La fecha y hora de recogida deben de ser posteriores a la fecha y hora actual");
            return false;
        }

        // Convertir Date a Calendar
        Calendar date = Calendar.getInstance();
        date.setTime(dateIntroducido);

        // Comprobar que el día de la semana está entre el martes y el domingo
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Log.e("Pedido", "La tienda solo está abierta de martes a domingo");
            return false;
        };

        // Comprobar que el pedido se va a recoger entre las 19:30 y las 23:00pm
        if (!isTimeBetween(date, 19, 30, 23, 0)) {
            Log.e("Pedido", "El pedido se ha de recoger entre las 19:30 y las 23.00pm");
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

        return targetCal.after(startCal) && targetCal.before(endCal);
    }
}
