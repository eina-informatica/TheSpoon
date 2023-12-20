package es.unizar.eina.thespoon.database;

import android.content.Context;
import android.util.Log;

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
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

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
            PedidoPlatoDao pedidoPlatoDao = INSTANCE.pedidoPlatoDao();
            pedidoPlatoDao.deleteAll();

            PlatoDao platoDao = INSTANCE.platoDao();
            platoDao.deleteAll();

            PedidoDao pedidoDao = INSTANCE.pedidoDao();
            pedidoDao.deleteAll();

            // Poblar base de datos con platos
            Plato plato = new Plato("Macarrones a la boloñesa", "- Macarrones\n- Salda boloñesa", CategoriaPlato.PRIMERO, 8);
            platoDao.insert(plato);
            plato = new Plato("Torrija", "Pan brioche infusionado con helado de guirlache y petazetas de chocolate", CategoriaPlato.POSTRE, 7);
            platoDao.insert(plato);
            plato = new Plato("Ternasco al horno", "- Ternasco\n- Patatas", CategoriaPlato.SEGUNDO, 13);
            platoDao.insert(plato);
            plato = new Plato("Tabla de jamón DO Teruel", "- Jamón DO Teruel", CategoriaPlato.PRIMERO, 12);
            platoDao.insert(plato);
            plato = new Plato("Canelón de pollo al chilindrón con bechamel ligera de azafrán", "- Pollo\n - Pimientos\n - Cebolla", CategoriaPlato.SEGUNDO, 11);
            platoDao.insert(plato);
            plato = new Plato("Arroz cremoso de boletus y longaniza de Graus", "- Arroz\n - Boletus\n - Longaniza de Graus", CategoriaPlato.PRIMERO, 10);
            platoDao.insert(plato);
            plato = new Plato("Tataki baturro", "- Carne de ternera del Pirineo macerada\n- Tomate\n- Crema de borraja", CategoriaPlato.SEGUNDO, 10);
            platoDao.insert(plato);

            // Poblar base de datos con pedidos
            Calendar date = Calendar.getInstance();
            date.set(2023, Calendar.NOVEMBER, 18, 9, 33);
            Pedido pedido = new Pedido("Pablo", "675123456", SDF.format(date.getTime()), EstadoPedido.SOLICITADO);
            pedidoDao.insert(pedido);
            Calendar date2 = Calendar.getInstance();
            date.set(2023, Calendar.FEBRUARY, 25, 17, 33);
            pedido = new Pedido("Abel", "581516684", SDF.format(date.getTime()), EstadoPedido.PREPARADO);
            pedidoDao.insert(pedido);
            Calendar date3 = Calendar.getInstance();
            date.set(2023, Calendar.APRIL, 1, 12, 33);
            pedido = new Pedido("Jorge", "123456789", SDF.format(date.getTime()), EstadoPedido.RECOGIDO);
            pedidoDao.insert(pedido);

            // Poblar base de datos con pedidoPlatos (platos que están asociados a un pedido)
            PedidoPlato pedidoplato= new PedidoPlato(1,2,2,7);
            pedidoPlatoDao.insert(pedidoplato);
            pedidoplato=new PedidoPlato(2,5,67,11);
            pedidoPlatoDao.insert(pedidoplato);

        });
        }
    };
}
