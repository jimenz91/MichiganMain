package com.magally.michiganmain;

/**
 * Created by Magally on 16-09-2015.
 */
public class Respuesta {
    private int respuestaID;
    private String foto, username, respuesta;
    private long reputacion;

    public Respuesta (int respuestaID, String respuesta, String foto, String username, long reputacion) {
        this.respuestaID = respuestaID;
        this.respuesta = respuesta;
        this.foto = foto;
        this.username = username;
        this.reputacion = reputacion;
    }

    public long getReputacion() {
        return reputacion;
    }

    public void setReputacion(long reputacion) {
        this.reputacion = reputacion;
    }

    public int getRespuestaID() {
        return respuestaID;
    }

    public void setRespuestaID(int respuestaID) {
        this.respuestaID = respuestaID;
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
