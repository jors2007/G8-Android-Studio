package com.espol.aplicacion_g8;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;

import java.util.ArrayList;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder> {
    private ArrayList<Actividad> listaActividades;

    public ActividadAdapter(ArrayList<Actividad> lista) {
        this.listaActividades = lista;
    }

    @NonNull
    @Override

    public ActividadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad,parent,false);
        return new ActividadViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        Actividad act = listaActividades.get(position);
        holder.txtId.setText("ID: "+ act.getId());
        holder.txtNombre.setText("nombre: "+ act.getNombre());
    holder.txtFecha.setText("Fecha de vencimiento: "+ act.getFechaLimite());
        holder.txtPrioridad.setText("Prioridad: "+ act.getPrioridad());
        holder.txtAvance.setText("Avance: "+ act.getAvance());
        holder.txtTipo.setText("Tipo: "+ act.getTipo());
    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }


    public static class ActividadViewHolder extends RecyclerView.ViewHolder {
        TextView txtId,txtNombre,txtFecha,txtPrioridad,txtAvance,txtTipo;
        public ActividadViewHolder(View itemView) {

            super(itemView);
            txtId = itemView.findViewById(R.id.txtId);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtPrioridad = itemView.findViewById(R.id.txtPrioridad);
            txtAvance = itemView.findViewById(R.id.txtAvance);
            txtTipo = itemView.findViewById(R.id.txtTipo);


        }
    }

    public void agregarActividad(Actividad nuevaActividad){
        listaActividades.add(nuevaActividad);
        notifyItemInserted(listaActividades.size() - 1);
    }



}
