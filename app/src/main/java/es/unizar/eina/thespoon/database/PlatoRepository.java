package es.unizar.eina.thespoon.database;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.thespoon.ui.Pedidos;

public class PlatoRepository {

    private PlatoDao mPlatoDao;
    private LiveData<List<Plato>> mAllPlatos;

    // Plato that in order to unit test the PlatoRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public PlatoRepository(Application application) {
        TheSpoonRoomDatabase db = TheSpoonRoomDatabase.getDatabase(application);
        mPlatoDao = db.platoDao();
        mAllPlatos = mPlatoDao.getOrderedPlatos();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Plato>> getAllPlatos() {
        return mAllPlatos;
    }

    /** Inserta un plato
     * @param plato
     * @return un valor entero largo con el identificador del plato que se ha creado.
     */
    public long insert(Plato plato) {
        final long[] result = {0};
        // You must call this on a non-UI thread or your app will throw an exception. Room ensures
        // that you're not doing any long running operations on the main thread, blocking the UI.
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mPlatoDao.insert(plato);
        });
        return result[0];
    }

    /** Modifica un plato
     * @param plato
     * @return un valor entero con el número de filas modificadas.
     */
    public int update(Plato plato) {
        final int[] result = {0};
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mPlatoDao.update(plato);
        });
        Log.d("Categoria editada", String.valueOf(plato.getCategoria()));
        Log.d("Precio editado", String.valueOf(plato.getPrecio()));
        return result[0];
    }

    /** Elimina un plato
     * @param plato
     * @return un valor entero con el número de filas eliminadas.
     */
    public int delete(Plato plato) {
        final int[] result = {0};
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mPlatoDao.delete(plato);
        });
        return result[0];
    }


}
