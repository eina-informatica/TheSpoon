package es.unizar.eina.thespoon.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Clase anotada como entidad que representa la relación entre pedidos y platos */
@Entity(tableName = "pedidoPlato", primaryKeys = {"pedidoId", "platoId"})
public class PedidoPlato {

    @ColumnInfo(name = "pedidoId")
    private int pedidoId;

    @ColumnInfo(name = "platoId")
    private int platoId;

    @NonNull
    @ColumnInfo(name = "cantidad")
    private int cantidad;


    public PedidoPlato(@NonNull int pedidoId, @NonNull int platoId, @NonNull int cantidad) {
        this.pedidoId = pedidoId;
        this.platoId = platoId;
        this.cantidad = cantidad;
    }

    /** Devuelve la parte de la clave primaria correspondiente al platoId */
    public int getPlatoId(){
        return this.platoId;
    }

    /** Devuelve la parte de la clave primaria correspondiente al pedidoId */
    public int getPedidoId(){
        return this.pedidoId;
    }

    /** Permite actualizar el identificador de la relación pedido-plato */
    public void setId(int pedidoId, int platoId) {
        this.pedidoId = pedidoId;
        this.platoId = platoId;
    }

    /** Devuelve la cantidad del plato "platoId" que hay en el pedido "pedidoId" */
    public int getCantidad(){
        return this.cantidad;
    }
}