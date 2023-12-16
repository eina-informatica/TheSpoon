package es.unizar.eina.thespoon.ui;

import static es.unizar.eina.thespoon.ui.Pedidos.ACTIVITY_EDIT;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.thespoon.database.Pedido;
import es.unizar.eina.send.SendAbstraction;
import es.unizar.eina.send.SendAbstractionImpl;

public class PedidoListAdapter extends ListAdapter<Pedido, PedidoViewHolder> {
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public PedidoListAdapter(@NonNull DiffUtil.ItemCallback<Pedido> diffCallback, PedidoViewModel noteViewModel) {
        super(diffCallback);
        PedidoViewModel mNoteViewModel = noteViewModel;
    }

    @Override
    public PedidoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PedidoViewHolder.create(parent);
    }

    public Pedido getCurrent() {
        return getItem(getPosition());
    }

    @Override
    public void onBindViewHolder(PedidoViewHolder holder, int position) {

        Pedido current = getItem(position);
        holder.bind(current.getTitle());

        holder.mPedidoEditButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Retrieve the position of the clicked item
                int clickedPosition = holder.getAdapterPosition();

                // Get the item associated with the clicked position
                Pedido pedidoToEdit = getItem(clickedPosition);

                Intent intent = new Intent(view.getContext(), PedidoEdit.class);
                intent.putExtra(PedidoEdit.PEDIDO_ID, pedidoToEdit.getId());
                intent.putExtra(PedidoEdit.PEDIDO_CLIENTE, pedidoToEdit.getNombreCliente());
                intent.putExtra(PedidoEdit.PEDIDO_TELEFONO, pedidoToEdit.getTelefonoCliente());
                intent.putExtra(PedidoEdit.PEDIDO_FECHA_HORA, pedidoToEdit.getFechaHoraRecogida());
                intent.putExtra(PedidoEdit.PEDIDO_ESTADO, pedidoToEdit.getEstado().ordinal());

                // Start the activity for result
                ((Activity) view.getContext()).startActivityForResult(intent, ACTIVITY_EDIT);

                Toast.makeText(view.getContext(), "Editar", Toast.LENGTH_SHORT).show();
            }
        });

        holder.mPedidoDeleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (view.getContext() instanceof ViewModelStoreOwner) {
                    PedidoViewModel mPedidoViewModel = new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(PedidoViewModel.class);
                    PedidoPlatoViewModel mPedidoPlatoViewModel = new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(PedidoPlatoViewModel.class);

                    // Retrieve the position of the clicked item
                    int clickedPosition = holder.getAdapterPosition();

                    // Get the item associated with the clicked position
                    Pedido pedidoToDelete = getItem(clickedPosition);
                    int pedidoId = pedidoToDelete.getId();

                    // Eliminar platos asociados a ese pedido
                    mPedidoPlatoViewModel.deleteAllFromPedido(pedidoId);

                    // Eliminar pedido
                    mPedidoViewModel.delete(pedidoToDelete);

                    // Notify the adapter that an item has been removed
                    notifyItemRemoved(clickedPosition);

                    Toast.makeText(view.getContext(), "Borrar", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the case where the context is not a ViewModelStoreOwner
                    // This might happen if you are using the adapter in a non-Activity, non-Fragment context
                    Toast.makeText(view.getContext(), "Unable to delete item", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.mPedidoItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PedidoViewModel mPedidoViewModel = new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(PedidoViewModel.class);

                // Retrieve the position of the clicked item
                int clickedPosition = holder.getAdapterPosition();

                // Get the item associated with the clicked position
                Pedido pedidoToView = getItem(clickedPosition);

                // Create and show the modal window with the pedido details
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Detalles del pedido");
                String whatsappMessage ="Cliente: " + pedidoToView.getNombreCliente() + "\n" +
                        "Teléfono: " + pedidoToView.getTelefonoCliente() + "\n" +
                        "Fecha y Hora de Recogida: " + pedidoToView.getFechaHoraRecogida() + "\n" +
                        "Estado: " + pedidoToView.getEstado();
                /*builder.setMessage("Cliente: " + pedidoToView.getNombreCliente() + "\n" +
                        "Teléfono: " + pedidoToView.getTelefonoCliente() + "\n" +
                        "Fecha y Hora de Recogida: " + pedidoToView.getFechaHoraRecogida() + "\n" +
                        "Estado: " + pedidoToView.getEstado());*/
                builder.setMessage(whatsappMessage);


                // Añadir botones para enviar SMS y WhatsApp
                builder.setPositiveButton("Enviar SMS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Aquí deberías llamar a la lógica de envío de SMS
                        SendAbstraction sendAbstraction = new SendAbstractionImpl((Activity) view.getContext(), "SMS");
                        sendAbstraction.send(pedidoToView.getTelefonoCliente(), "Detalles del pedido:\n"+whatsappMessage);
                    }
                });

                builder.setNegativeButton("Enviar WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Aquí deberías llamar a la lógica de envío de WhatsApp
                        SendAbstraction sendAbstraction = new SendAbstractionImpl((Activity) view.getContext(), "WhatsApp");
                        sendAbstraction.send(pedidoToView.getTelefonoCliente(), "Detalles del pedido:\n"+whatsappMessage);
                    }
                });
                builder.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Close the dialog
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    static class PedidoDiff extends DiffUtil.ItemCallback<Pedido> {
        @Override
        public boolean areItemsTheSame(@NonNull Pedido oldItem, @NonNull Pedido newItem) {
            //android.util.Log.d ( "PedidoDiff" , "areItemsTheSame " + oldItem.getId() + " vs " + newItem.getId() + " " +  (oldItem.getId() == newItem.getId()));
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Pedido oldItem, @NonNull Pedido newItem) {
            //android.util.Log.d ( "PedidoDiff" , "areContentsTheSame " + oldItem.getTitle() + " vs " + newItem.getTitle() + " " + oldItem.getTitle().equals(newItem.getTitle()));
            // We are just worried about differences in visual representation, i.e. changes in the title
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}
