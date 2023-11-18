package es.unizar.eina.thespoon.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/** Clase anotada como entidad que representa un pedido y que consta de nombre del cliente,
 * teléfono móvil, fecha y hora de recogida y número de raciones para cada uno de los platos */
@Entity(tableName = "pedido")
public class Pedido {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "nombreCliente")
    private String nombreCliente;

    @NonNull
    @ColumnInfo(name = "telefonoCliente")
    private String telefonoCliente;

    @NonNull
    @ColumnInfo(name = "fechaHoraRecogida")
    private String fechaHoraRecogida;

    @NonNull
    @ColumnInfo(name = "estado")
    private EstadoPedido estado;

    public Pedido(@NonNull String nombreCliente,
                  @NonNull String telefonoCliente,
                  @NonNull String fechaHoraRecogida,
                  @NonNull EstadoPedido estado) {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.nombreCliente = nombreCliente;
        this.telefonoCliente = telefonoCliente;
        this.fechaHoraRecogida = fechaHoraRecogida /*dateFormat.format(fechaHoraRecogida)*/;
        this.estado = estado;
    }

    /** Devuelve el identificador del pedido */
    public int getId(){
        return this.id;
    }

    /** Permite actualizar el identificador de un pedido */
    public void setId(int id) {
        this.id = id;
    }

    /** Devuelve el nombre del cliente asociado al pedido */
    public String getNombreCliente(){
        return this.nombreCliente;
    }

    /** Devuelve el número de teléfono del cliente asociaado al pedido */
    public String getTelefonoCliente(){
        return this.telefonoCliente;
    }

    /** Devuelve la fecha y hora de recogida del pedido como una cadena de carácteres */
    public String getFechaHoraRecogida() {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //return dateFormat.parse(this.fechaHoraRecogida);
        return this.fechaHoraRecogida;
    }

    /** Devuelve el estado del pedido */
    public EstadoPedido getEstado() { return this.estado; }
}