package es.unizar.eina.thespoon.database;

import androidx.room.Embedded;

public class PlatoPedido {
    @Embedded
    public Plato plato;

    @Embedded
    public PedidoPlato pedidoPlato;
}
