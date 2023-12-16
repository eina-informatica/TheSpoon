package es.unizar.eina.thespoon.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unizar.eina.thespoon.ui.Pedidos;

/** Definición de un Data Access Object para los pedidos */
@Dao
public interface PedidoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Pedido pedido);

    @Update
    int update(Pedido pedido);

    @Delete
    int delete(Pedido pedido);

    @Query("DELETE FROM Pedido")
    void deleteAll();

    @Query("SELECT * FROM Pedido ORDER BY nombreCliente ASC")
    LiveData<List<Pedido>> getOrderedPlatos();

    @Query("SELECT * FROM Pedido ORDER BY " +
            "CASE WHEN estado = 'SOLICITADO' THEN 1 " +
            "WHEN estado = 'PREPARADO' THEN 2 " +
            "WHEN estado = 'RECOGIDO' THEN 3 ELSE 4 END")
    LiveData<List<Pedido>> getOrderedPedidosPorEstado();

    @Query("SELECT * FROM Pedido ORDER BY nombreCliente ASC, estado DESC")
    LiveData<List<Pedido>> getOrderedPedidosPorNombreYEstado();
    @Query("SELECT * FROM Pedido WHERE estado = :estadoSeleccionado")
    LiveData<List<Pedido>> getPedidosPorEstado(EstadoPedido estadoSeleccionado);

    @Query("SELECT COUNT(*) FROM Pedido")
    int getPedidoCount();
}