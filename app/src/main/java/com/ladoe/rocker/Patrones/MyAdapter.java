package com.ladoe.rocker.Patrones;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ladoe.rocker.Constantes.CLAVES;
import com.ladoe.rocker.Entidades.Publicacion;
import com.ladoe.rocker.Entidades.TipoPublicacion;
import com.ladoe.rocker.ListActivity;
import com.ladoe.rocker.R;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<? extends Publicacion> mDataset;
    private List<TipoPublicacion> tipoPublicacionList;
    private boolean flagBackGround=false;
    private ListActivity listActivity;
    private Intent intent;

    public List<? extends Publicacion> getmDataset() {
        return mDataset;
    }

    public void setmDataset(List<? extends Publicacion> mDataset) {
        this.mDataset = mDataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNombre;
        public TextView textViewTipo;
        public TextView textViewDistancia;
        public TextView textViewDireccion;
        public TextView textViewLocalidad;
        public TextView textViewTelefono;
        public ImageView imageViewTipo;
        public ImageView imageViewLLamar;
        public ImageView imageViewDetalles;
        public ConstraintLayout constraintLayout;

        public ViewHolder(View ItemView) {
            super(ItemView);
            textViewNombre=itemView.findViewById(R.id.textViewNombre);
            textViewTipo=itemView.findViewById(R.id.textViewTipo);
            textViewDistancia=itemView.findViewById(R.id.textViewDistancia);
            textViewDireccion=itemView.findViewById(R.id.textViewDireccion);
            textViewLocalidad=itemView.findViewById(R.id.textViewLocalidad);
            textViewTelefono=itemView.findViewById(R.id.textViewTelefono);
            imageViewTipo=itemView.findViewById(R.id.imageViewTipo);
            imageViewLLamar=itemView.findViewById(R.id.imageViewLLamar);
            imageViewDetalles=itemView.findViewById(R.id.imageViewDetalles);
            constraintLayout=itemView.findViewById(R.id.constraintLayout);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<? extends Publicacion> myDataset, List<TipoPublicacion> tipoPublicacionList, ListActivity listActivity, Intent intent) {
        this.mDataset = myDataset;
        this.tipoPublicacionList=tipoPublicacionList;
        this.listActivity=listActivity;
        this.intent=intent;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (flagBackGround){
            holder.constraintLayout.setBackgroundColor(Color.parseColor("#f2f2f2"));
        }
        flagBackGround=!flagBackGround;

        Publicacion publicacion=mDataset.get(position);
        TipoPublicacion tipoPublicacion=tipoPublicacionList.get(publicacion.getDatosBasicos().getTipoPub()-1);
        holder.textViewNombre.setText(publicacion.getDatosBasicos().getNombre());
        holder.textViewTipo.setText(tipoPublicacion.getDescripcion());
        if(publicacion.getDistancia()==null)
            holder.textViewDistancia.setText("Obteniendo ubicación...");
        else
            holder.textViewDistancia.setText("Estas a "+String.format("%.2f", publicacion.getDistancia())+" km.");
        if(publicacion.getDireccion().getLocalidad().equals(""))
            holder.textViewLocalidad.setText(publicacion.getDireccion().getProvincia());
        else
            holder.textViewLocalidad.setText(publicacion.getDireccion().getLocalidad()+" "+publicacion.getDireccion().getProvincia());

        holder.textViewDireccion.setText(publicacion.getDireccion().getCalle()+" "+publicacion.getDireccion().getAltura());
        holder.textViewTelefono.setText(publicacion.getTelefono().getCodArea()+" "+publicacion.getTelefono().getNumero());

        holder.imageViewDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(CLAVES.ID,mDataset.get(position).getDatosBasicos().getId());
                listActivity.startActivity(intent);
            }
        });

        holder.imageViewLLamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listActivity.llamar(mDataset.get(position));

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();

    }
}
