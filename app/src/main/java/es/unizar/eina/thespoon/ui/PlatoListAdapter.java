package es.unizar.eina.thespoon.ui;

import static es.unizar.eina.thespoon.ui.Platos.ACTIVITY_EDIT;

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

import es.unizar.eina.thespoon.database.Plato;

public class PlatoListAdapter extends ListAdapter<Plato, PlatoViewHolder> {
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public PlatoListAdapter(@NonNull DiffUtil.ItemCallback<Plato> diffCallback, PlatoViewModel platoViewModel) {
        super(diffCallback);
        PlatoViewModel mPlatoViewModel = platoViewModel;
    }

    @Override
    public PlatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PlatoViewHolder.create(parent);
    }

    public Plato getCurrent() {
        return getItem(getPosition());
    }

    @Override
    public void onBindViewHolder(PlatoViewHolder holder, int position) {

        Plato current = getItem(position);
        holder.bind(current.getNombre());

        holder.mPlatoEditButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Retrieve the position of the clicked item
                int clickedPosition = holder.getAdapterPosition();

                // Get the item associated with the clicked position
                Plato platoToEdit = getItem(clickedPosition);

                Intent intent = new Intent(view.getContext(), PlatoEdit.class);
                intent.putExtra(PlatoEdit.PLATO_NOMBRE, platoToEdit.getNombre());
                intent.putExtra(PlatoEdit.PLATO_DESCRIPCION, platoToEdit.getDescripcion());
                intent.putExtra(PlatoEdit.PLATO_CATEGORIA, platoToEdit.getCategoria().ordinal());
                intent.putExtra(PlatoEdit.PLATO_PRECIO, platoToEdit.getPrecio());
                intent.putExtra(PlatoEdit.PLATO_ID, platoToEdit.getId());

                // Start the activity for result
                ((Activity) view.getContext()).startActivityForResult(intent, ACTIVITY_EDIT);

                Toast.makeText(view.getContext(), "Editar", Toast.LENGTH_SHORT).show();
            }
        });

        holder.mPlatoDeleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (view.getContext() instanceof ViewModelStoreOwner) {
                    PlatoViewModel mPlatoViewModel = new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(PlatoViewModel.class);
                    PedidoPlatoViewModel mPedidoPlatoViewModel = new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(PedidoPlatoViewModel.class);

                    // Retrieve the position of the clicked item
                    int clickedPosition = holder.getAdapterPosition();

                    // Get the item associated with the clicked position
                    Plato platoToDelete = getItem(clickedPosition);
                    int platoId = platoToDelete.getId();

                    // Eliminar todas las relaciones con las pedido en las que interviene el plato
                    mPedidoPlatoViewModel.deleteAllFromPlato(platoId);

                    // Eliminar plato
                    mPlatoViewModel.delete(platoToDelete);

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
        
        holder.mPlatoItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlatoViewModel mNoteViewModel = new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(PlatoViewModel.class);

                // Retrieve the position of the clicked item
                int clickedPosition = holder.getAdapterPosition();

                // Get the item associated with the clicked position
                Plato platoToView = getItem(clickedPosition);

                // Create and show the modal window with the plato details
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Detalles del plato");
                builder.setMessage("Nombre: " + platoToView.getNombre() + "\n" +
                        "Descripción: \n" + platoToView.getDescripcion() + "\n" +
                        "Categoría: " + platoToView.getCategoria() + "\n" +
                        "Precio: " + platoToView.getPrecio()+" €");

                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
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


    static class PlatoDiff extends DiffUtil.ItemCallback<Plato> {

        @Override
        public boolean areItemsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            //android.util.Log.d ( "PlatoDiff" , "areItemsTheSame " + oldItem.getId() + " vs " + newItem.getId() + " " +  (oldItem.getId() == newItem.getId()));
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            //android.util.Log.d ( "PlatoDiff" , "areContentsTheSame " + oldItem.getTitle() + " vs " + newItem.getTitle() + " " + oldItem.getTitle().equals(newItem.getTitle()));
            // We are just worried about differences in visual representation, i.e. changes in the title
            return oldItem.getNombre().equals(newItem.getNombre());
        }
    }
}
