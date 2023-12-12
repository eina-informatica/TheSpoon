package es.unizar.eina.thespoon.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.thespoon.database.CategoriaPlato;
import es.unizar.eina.thespoon.database.Plato;

public class AddPlatoSerializer {
    static String serialize(List<Pair<Plato, Integer>> platoList) {
        String platos = "";
        for (Pair<Plato, Integer> pair : platoList) {
            if (pair.second > 0) {
                try {
                    platos += String.valueOf(pair.first.getId()) + ":"
                            + URLEncoder.encode(pair.first.getNombre(), StandardCharsets.UTF_8.toString()) + ":"
                            + String.valueOf(pair.first.getPrecio()) + ":"
                            + String.valueOf(pair.second) + ",";
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // Eliminar la coma del final
        if (!platos.isEmpty()) {
            platos = platos.substring(0, platos.length() - 1);
        }
        return platos;
    }

    static List<Pair<Plato, Integer>> deserialize(String platoString) {
        List<Pair<Plato, Integer>> platoList = new ArrayList<>();
        String[] platoArray = platoString.split(",");
        for (String platoStr : platoArray) {
            try {
                String[] atributos = platoStr.split(":");
                int id = Integer.parseInt(atributos[0]);
                String nombre = URLDecoder.decode(atributos[1], StandardCharsets.UTF_8.toString());
                double precio = Double.parseDouble(atributos[2]);
                int cantidad = Integer.parseInt(atributos[3]);
                Plato plato = new Plato(nombre, "", CategoriaPlato.PRIMERO, precio);
                plato.setId(id);
                Pair<Plato, Integer> pair = new Pair<>(plato, cantidad);
                platoList.add(pair);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return platoList;
    }

    static double calcularPrecio(List<Pair<Plato, Integer>> platoList) {
        double precioTotal = 0;
        for (Pair<Plato, Integer> pair : platoList) {
            precioTotal += pair.first.getPrecio() * pair.second;
        }
        return precioTotal;
    }
}
