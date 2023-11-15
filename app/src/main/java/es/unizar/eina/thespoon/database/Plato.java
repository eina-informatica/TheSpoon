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

    public Plato(@NonNull String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /** Devuelve el identificador de la nota */
    public int getId(){
        return this.id;
    }

    /** Permite actualizar el identificador de una nota */
    public void setId(int id) {
        this.id = id;
    }

    /** Devuelve el título de la nota */
    public String getNombre(){
        return this.nombre;
    }

    /** Devuelve el cuerpo de la nota */
    public String getDescripcion(){
        return this.descripcion;
    }

}
