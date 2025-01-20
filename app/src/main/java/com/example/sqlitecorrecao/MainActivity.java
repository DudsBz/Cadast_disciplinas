package com.example.sqlitecorrecao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rcwDisciplinas;
    SQLiteDatabase database;
    ArrayList<Disciplinas> arrayDisciplinas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        criaBanco();
        Button btnInserir = findViewById(R.id.btn_inserir);
        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,
                        InserirActivity.class);
                startActivity(i);
            }
        });
        atualizaRcw();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaRcw();
    }

    private void atualizaRcw() {
        //recria o array vazio
        arrayDisciplinas = new ArrayList<Disciplinas>();
        //Popula o recyclerview
        buscarDisciplinas();
        //setar as disciplinas no RW
        rcwDisciplinas = findViewById(R.id.rcw_disciplinas);
        rcwDisciplinas.setAdapter(new
                AdapterDisciplinas(arrayDisciplinas));
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false);
        rcwDisciplinas.setLayoutManager(layoutManager);
    }

    private void buscarDisciplinas() {
        Cursor cursor = database.rawQuery(
                "SELECT * FROM disciplinas",
                null);
        int indiceColunaID =
                cursor.getColumnIndex("ID");
        int indiceColunaNome =
                cursor.getColumnIndex("nome");
        int indiceColunaProfessor =
                cursor.getColumnIndex("prefessor");
        int indiceColunaAulas =
                cursor.getColumnIndex("aulas");
        try{
            cursor.moveToFirst();
            while (cursor != null){
                String strID =
                        cursor.getString(indiceColunaID);
                String strDisciplina =
                        cursor.getString(indiceColunaNome);
                String strProfessor =
                        cursor.getString(indiceColunaProfessor);
                String strAulas =
                        cursor.getString(indiceColunaAulas);
                Disciplinas disciplinas = new
                        Disciplinas(strID, strDisciplina, strProfessor, strAulas);

                arrayDisciplinas.add(disciplinas);
                cursor.moveToNext();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void criaBanco() {
        database = openOrCreateDatabase(
                        "sqlite",
                        MODE_PRIVATE,
                        null
                );
        database.execSQL("" +
                "CREATE TABLE IF NOT EXISTS " +
                "disciplinas ( ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "nome VARCHAR, " +
                                "prefessor VARCHAR, " +
                                "aulas INT(1))");
       /*try{
           Cursor cursor = database.rawQuery(
                   "SELECT * FROM disciplinas",
                   null);
           int indiceColunaNome =
                   cursor.getColumnIndex("nome");
           cursor.moveToFirst();
           while (cursor != null){
               Toast.makeText(this,
                       "Disciplina: " +
                               cursor.getString(indiceColunaNome)
                       , Toast.LENGTH_SHORT).show();
               cursor.moveToNext();
           }
       }catch (Exception e){

       }*/

    }

    private void excluirDisciplina(String id) {
        database.execSQL("DELETE FROM disciplinas WHERE ID= '" + id + "'");
        Toast.makeText(this, "Excluído", Toast.LENGTH_SHORT).show();
        atualizaRcw();
    }

    private class AdapterDisciplinas extends RecyclerView.Adapter {
        ArrayList<Disciplinas> arrayDisciplinas;
        public AdapterDisciplinas(ArrayList<Disciplinas> arrayDisciplinas) {
            this.arrayDisciplinas = arrayDisciplinas;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(
                    getApplicationContext()).inflate(
                            R.layout.row_disciplinas,
                    parent,false);
            ViewHolderDisciplinas viewHolderDisciplinas =
                    new ViewHolderDisciplinas(view);

            return viewHolderDisciplinas;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((ViewHolderDisciplinas)holder).tvwDisciplina.setText(
                    arrayDisciplinas.get(holder.getAdapterPosition()).getNome());

            ((ViewHolderDisciplinas)holder).tvwProfessor.setText(
                    arrayDisciplinas.get(holder.getAdapterPosition()).getProfessor());

            ((ViewHolderDisciplinas)holder).tvwAulas.setText(
                    arrayDisciplinas.get(holder.getAdapterPosition()).getAulas());

            ((ViewHolderDisciplinas)holder).btnExcluir.
                    setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            excluirDisciplina(arrayDisciplinas.get(holder.getAdapterPosition()).getId());
                            arrayDisciplinas.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                    });

            ((ViewHolderDisciplinas)holder).btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this,
                            InserirActivity.class);
                    i.putExtra("id",
                            arrayDisciplinas.get(holder.getAdapterPosition()).getId());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayDisciplinas.size();
        }
    }

    private class ViewHolderDisciplinas extends
            RecyclerView.ViewHolder {
        TextView tvwDisciplina;
        TextView tvwProfessor;
        TextView tvwAulas;
        Button btnEditar;
        Button btnExcluir;
        public ViewHolderDisciplinas(@NonNull View itemView) {
            super(itemView);
            tvwDisciplina = itemView.findViewById(
                    R.id.tvw_disciplina);

            tvwProfessor = itemView.findViewById(
                    R.id.tvw_professor);

            tvwAulas = itemView.findViewById(
                    R.id.tvw_aulas);

            btnEditar = itemView.findViewById(
                    R.id.btn_editar);

            btnExcluir = itemView.findViewById(
                    R.id.btn_excluir);
        }
    }
}