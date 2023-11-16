package es.unizar.eina.thespoon.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Clase anotada como entidad que representa una nota y que consta de título y cuerpo */
@Entity(tableName = "plato")
public class Plato {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "categoria")
    private Categoria categoria;

    public Plato(@NonNull String nombre, String descripcion, Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    /** Devuelve el identificador del plato */
    public int getId(){
        return this.id;
    }

    /** Permite actualizar el identificador de un plato */
    public void setId(int id) {
        this.id = id;
    }

    /** Devuelve el título del plato */
    public String getNombre(){
        return this.nombre;
    }

    /** Devuelve el cuerpo del plato */
    public String getDescripcion(){
        return this.descripcion;
    }

    /** Devuelve la categoría del plato */
    public Categoria getCategoria() { return this.categoria; }
}
