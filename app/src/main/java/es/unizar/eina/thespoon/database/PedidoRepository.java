package es.unizar.eina.thespoon.database;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PedidoRepository extends CMP {

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
            if (pedidoCorrecto(pedido,false,null)) {
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
            if (pedidoCorrecto(pedido,false,null)) {
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

}
