package es.unizar.eina.thespoon.database;


import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import es.unizar.eina.thespoon.ui.Pedidos;

public class PlatoRepository {

    private PlatoDao mPlatoDao;
    private LiveData<List<Plato>> mAllPlatos;

    private LiveData<List<Plato>> mAllPlatosPorCategoria;

    private LiveData<List<Plato>> mAllPlatosPorNombreYCategoria;

    // Plato that in order to unit test the PlatoRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public PlatoRepository(Application application) {
        TheSpoonRoomDatabase db = TheSpoonRoomDatabase.getDatabase(application);
        mPlatoDao = db.platoDao();
        mAllPlatos = mPlatoDao.getOrderedPlatos();
        mAllPlatosPorCategoria = mPlatoDao.getOrderedPlatosPorCategoria();
        mAllPlatosPorNombreYCategoria = mPlatoDao.getOrderedPlatosPorNombreYCategoria();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Plato>> getAllPlatos() {
        return mAllPlatos;
    }

    public LiveData<List<Plato>> getAllPlatosPorCategoria() { return mAllPlatosPorCategoria; }

    public LiveData<List<Plato>> getAllPlatosPorNombreYCategoria() { return mAllPlatosPorNombreYCategoria; }

    /** Inserta un plato
     * @param plato
     * @return un valor entero largo con el identificador del plato que se ha creado.
     */
    public long insert(Plato plato) {
        final long[] result = {0};
        CountDownLatch latch = new CountDownLatch(1);
        // You must call this on a non-UI thread or your app will throw an exception. Room ensures
        // that you're not doing any long running operations on the main thread, blocking the UI.
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            result[0] = mPlatoDao.insert(plato);
            latch.countDown(); // Signal that the operation is complete
        });

        try {
            latch.await(); // Wait until the count reaches zero
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    /** Elimina todos los platos*/
    public void deleteAll() {
        CountDownLatch latch = new CountDownLatch(1);
        TheSpoonRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPlatoDao.deleteAll();
            latch.countDown(); // Signal that the operation is complete
        });

        try {
            latch.await(); // Wait until the count reaches zero
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
