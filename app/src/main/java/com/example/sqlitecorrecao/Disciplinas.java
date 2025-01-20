package com.example.sqlitecorrecao;

public class Disciplinas {
    String id, nome, professor, aulas;

    public Disciplinas(String id, String nome, String professor, String aulas) {
        this.id = id;
        this.nome = nome;
        this.professor = professor;
        this.aulas = aulas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getAulas() {
        return aulas;
    }

    public void setAulas(String aulas) {
        this.aulas = aulas;
    }
}
