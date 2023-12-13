package es.unizar.eina.thespoon.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/** Definición de un Data Access Object para la relación pedido-plato */
@Dao
public interface PedidoPlatoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(PedidoPlato pedidoPlato);

    @Update
    int update(PedidoPlato pedidoPlato);

    @Delete
    int delete(PedidoPlato pedidoPlato);

    @Query("DELETE FROM PedidoPlato")
    void deleteAll();

    /*@Query("SELECT * FROM PedidoPlato ORDER BY cantidad ASC")
    LiveData<List<PedidoPlato>> getOrderedPedidosPlatos();*/
    @Query("SELECT * FROM Plato INNER JOIN PedidoPlato ON Plato.id = PedidoPlato.platoId WHERE PedidoPlato.pedidoId = :pedidoId")
    LiveData<List<PlatoPedido>> getPlatosPorPedidoId(int pedidoId);
}