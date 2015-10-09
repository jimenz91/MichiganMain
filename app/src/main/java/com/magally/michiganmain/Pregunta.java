package com.magally.michiganmain;

/**
 * Created by Magally on 13-09-2015.
 */
public class Pregunta {
    private int preguntaID;
    private String enunciado;
    private String foto;
    private String username;
    private String reputacion;



    private String respuestaCount;

    public Pregunta(int preguntaID, String enunciado, String foto, String username, String reputacion, String respuestCount) {
        this.preguntaID = preguntaID;
        this.enunciado = enunciado;
        this.foto = foto;
        this.username = username;
        this.reputacion = reputacion;
        this.respuestaCount = respuestCount;
    }
    public String getRespuestaCount() {
        return respuestaCount;
    }
    public int getPreguntaID() {
        return preguntaID;
    }

    public void setPreguntaID(int preguntaID) {
        this.preguntaID = preguntaID;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReputacion(){
        return reputacion;
    }

    public void setReputacion(String reputacion){
        this.reputacion = reputacion;
    }


}
