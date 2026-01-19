package com.espol.aplicacion_g8;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.espol.aplicacion_g8.modelo.actividad.Actividad;

import java.util.ArrayList;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder> {

    private ArrayList<Actividad> listaActividades;
    private OnActividadActionListener listener;

    public ActividadAdapter(ArrayList<Actividad> lista, OnActividadActionListener listener) {
        this.listaActividades = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_actividad, parent, false);
        return new ActividadViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        Actividad act = listaActividades.get(position);

        holder.txtId.setText("ID: " + act.getId());
        holder.txtNombre.setText("Nombre: " + act.getNombre());
        holder.txtFecha.setText("Fecha de vencimiento: " + act.getFechaLimite());
        holder.txtPrioridad.setText("Prioridad: " + act.getPrioridad());
        holder.txtAvance.setText("Avance: " + act.getAvance() + "%");
        holder.txtTipo.setText("Tipo: " + act.getTipo());

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) listener.onEliminar(act);
        });

        holder.btnDetalles.setOnClickListener(v -> {
            if (listener != null) listener.onDetalles(act);
        });

        holder.btnAvance.setOnClickListener(v -> {
            if (listener != null) listener.onRegistrarAvance(act);
        });

        holder.btnPomodoro.setOnClickListener(v -> {
            if (listener != null) listener.onPomodoro(act);
        });

        holder.btnDeepWork.setOnClickListener(v -> {
            if (listener != null) listener.onDeepWork(act);
        });
    }
    @Override
    public int getItemCount() {
        return (listaActividades != null) ? listaActividades.size() : 0;
    }

    public static class ActividadViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtNombre, txtFecha, txtPrioridad, txtAvance, txtTipo;
        Button btnEliminar, btnDetalles, btnAvance,btnPomodoro,btnDeepWork;

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtId);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtPrioridad = itemView.findViewById(R.id.txtPrioridad);
            txtAvance = itemView.findViewById(R.id.txtAvance);
            txtTipo = itemView.findViewById(R.id.txtTipo);

            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnDetalles = itemView.findViewById(R.id.btnDetalles);
            btnAvance = itemView.findViewById(R.id.btnAvance);
            btnPomodoro = itemView.findViewById(R.id.btnPomodoro);
            btnDeepWork = itemView.findViewById(R.id.btnDeepWork);
        }
    }
}