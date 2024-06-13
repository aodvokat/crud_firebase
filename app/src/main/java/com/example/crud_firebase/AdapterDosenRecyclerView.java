package com.example.crud_firebase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class AdapterDosenRecyclerView extends RecyclerView.Adapter<AdapterDosenRecyclerView.ViewHolder> {
    private ArrayList<Dosen> daftarDosen;
    private Context context;
    private DatabaseReference database;

    public AdapterDosenRecyclerView(ArrayList<Dosen> dosens, Context ctx){
        daftarDosen = dosens;
        context = ctx;
        database = FirebaseDatabase.getInstance().getReference();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_namadosen);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dosen, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final String name = daftarDosen.get(position).getNik();
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read detail data
            }
        });
        holder.tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_view);
                dialog.show();
                Button editButton = (Button) dialog.findViewById(R.id.bt_edit_data);
                Button delButton = (Button) dialog.findViewById(R.id.bt_delete_data);

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        context.startActivity(DBCreateActivity.getActIntent((Activity) context).putExtra("data", daftarDosen.get(position)));
                    }
                });

                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteDosen(daftarDosen.get(position));
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });
        holder.tvTitle.setText(name);
    }

    private void deleteDosen(Dosen dosen) {
        if (database != null) {
            database.child("dosen").child(dosen.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    daftarDosen.remove(dosen);
                    notifyDataSetChanged();
                    Snackbar.make(((Activity) context).findViewById(R.id.rv_main), "Data berhasil dihapus", Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return daftarDosen.size();
    }
}
