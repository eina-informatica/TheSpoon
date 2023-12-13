package es.unizar.eina.thespoon.database;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PedidoPlatoRepository {

    private PedidoPlatoDao mPedidoPlatoDao;
    //private LiveData<List<PedidoPlato>> mAllPedidoPlatos;

    // Plato that in order to unit test the PlatoRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public PedidoPlatoRepository(Application application) {
        TheSpoonRoomDatabase db = TheSpoonRoomDatabase.getDatabase(application);
        mPedidoPlatoDao = db.pedidoPlatoDao();
        //mAllPedidoPlatos = mPedidoPlatoDao.getOrderedPedidosPlatos();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    /*public LiveData<List<PedidoPlato>> getAllPedidoPlatos() {
        return mAllPedidoPlatos;
    }*/
    public LiveData<List<PlatoPedido>> getPlatosPorPedidoId(int pedidoId) {
        return mPedidoPlatoDao.getPlatosPorPedidoId(pedidoId);
    }

    /** Inserta un plato
     * @param pedidoPlato
     * @return un valor entero largo con el identificador del plato que se ha creado.
     */
    public long insert(PedidoPlato pedidoPlato) {
        final long[] result = {0};
        // You must call this on a non-UI thread or your app will throw an exception. Room ensures
        // that you're not doing any long running operations on the main thread, blocking the UI.
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mPedidoPlatoDao.insert(pedidoPlato);
        });
        return result[0];
    }

    /** Modifica un plato
     * @param pedidoPlato
     * @return un valor entero con el número de filas modificadas.
     */
    public int update(PedidoPlato pedidoPlato) {
        final int[] result = {0};
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mPedidoPlatoDao.update(pedidoPlato);
        });
        return result[0];
    }

    /** Elimina un plato
     * @param pedidoPlato
     * @return un valor entero con el número de filas eliminadas.
     */
    public int delete(PedidoPlato pedidoPlato) {
        final int[] result = {0};
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mPedidoPlatoDao.delete(pedidoPlato);
        });
        return result[0];
    }
}
