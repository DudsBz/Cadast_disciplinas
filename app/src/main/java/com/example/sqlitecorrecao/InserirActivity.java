package com.example.sqlitecorrecao;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InserirActivity extends AppCompatActivity {

    SQLiteDatabase database;

    String strID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir);
        abrirBanco();
        EditText edtDisciplina = findViewById(R.id.edt_disciplina);
        EditText edtProfessor = findViewById(R.id.edt_professor);
        EditText edtAulas = findViewById(R.id.edt_aulas);
        Button btnInserir = findViewById(R.id.btn_inserir_disciplina);
        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strDisciplina = edtDisciplina.getText().toString();
                String strProfessor = edtProfessor.getText().toString();
                String strAulas = edtAulas.getText().toString();
                if(strID.equals("")){
                    //insere um disciplina
                    database.execSQL("INSERT INTO disciplinas (nome, prefessor, aulas) " +
                            "VALUES ('"+ strDisciplina +"', " +
                            "'" + strProfessor + "'," +
                            " '" + strAulas + "')");
                    Toast.makeText(InserirActivity.this,
                            "Disciplina inserida com sucesso!",
                            Toast.LENGTH_SHORT).show();
                }else {
                    //altera disciplina
                    database.execSQL("UPDATE disciplinas SET nome = '"+strDisciplina+"', " +
                            " prefessor= '"+strProfessor+"', " +
                            " aulas= '"+strAulas+"' WHERE ID = '"+strID+"'");

                    Toast.makeText(InserirActivity.this, "Alterado", Toast.LENGTH_SHORT).show();
                }
                edtDisciplina.setText("");
                edtProfessor.setText("");
                edtAulas.setText("");
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            //edita a disciplina
            strID = bundle.getString("id");
            btnInserir.setText("Alterar");
            Cursor cursor = database.rawQuery(
                   "SELECT * FROM disciplinas WHERE ID = '" + strID + "'" ,
                    null);
            int indiceColunNome = cursor.getColumnIndex("nome");
            int indiceColunAula = cursor.getColumnIndex("aulas");
            int indiceColunProf = cursor.getColumnIndex("prefessor");
            try {
                cursor.moveToFirst();
                while (cursor != null){
                    String strDisciplina, strAula, strProf;
                    strDisciplina = cursor.getString(indiceColunNome);
                    strAula = cursor.getString(indiceColunAula);
                    strProf = cursor.getString(indiceColunProf);
                    edtAulas.setText(strAula);
                    edtDisciplina.setText(strDisciplina);
                    edtProfessor.setText(strProf);
                    cursor.moveToNext();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            //insere uma disciplina
        }
    }

    private void abrirBanco() {
        database = openOrCreateDatabase(
                        "sqlite",
                        MODE_PRIVATE,
                        null
                );
    }
}