package es.unizar.eina.thespoon.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Plato.class, Pedido.class, PedidoPlato.class}, version = 1, exportSchema = false)
public abstract class TheSpoonRoomDatabase extends RoomDatabase {

    public abstract PlatoDao platoDao();
    public abstract PedidoDao pedidoDao();
    public abstract PedidoPlatoDao pedidoPlatoDao();

    private static volatile TheSpoonRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TheSpoonRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TheSpoonRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TheSpoonRoomDatabase.class, "thespoon_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more platos, just add them.
                PlatoDao platoDao = INSTANCE.platoDao();
                platoDao.deleteAll();

                Plato plato = new Plato("Plato 1's title", "Plato 1's body", CategoriaPlato.PRIMERO, 33);
                platoDao.insert(plato);
                plato = new Plato("Plato 2's title", "Plato 2's body", CategoriaPlato.SEGUNDO, 13);
                platoDao.insert(plato);

                // Populate database with pedidos
                PedidoDao pedidoDao = INSTANCE.pedidoDao();
                pedidoDao.deleteAll();

                /*Calendar date = Calendar.getInstance();
                date.set(2023, Calendar.NOVEMBER, 18, 9, 33);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Pedido pedido = new Pedido("Plato 1's title", "Plato 1's body", dateFormat.format(date), EstadoPedido.SOLICITADO);
                pedidoDao.insert(pedido);*/
            });
        }
    };
}
