package com.magally.michiganmain;

/**
 * Created by Magally on 16-09-2015.
 */
public class Respuesta {
    private int preguntaID;
    private String foto, username, respuesta;

    public Respuesta (int preguntaID, String respuesta, String foto, String username) {
        this.preguntaID = preguntaID;
        this.respuesta = respuesta;
        this.foto = foto;
        this.username = username;
    }

    public int getPreguntaID() {
        return preguntaID;
    }

    public void setPreguntaID(int preguntaID) {
        this.preguntaID = preguntaID;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String enunciado) {
        this.respuesta = respuesta;
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

}
