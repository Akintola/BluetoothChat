package com.apk.franckadjibao.bluetoothtry;

/**
 * Created by Franck ADJIBAO on 06/05/2017.
 */
public class Message {
    private int idenvoye;
    private String content;
    private String horaire;

    public Message(int identifiant,String contenu,String time){
        this.idenvoye=identifiant;
        this.content=contenu;
        this.horaire=time;
    }

    public int getIdenvoye() {
        return idenvoye;
    }

    public String getContent() {
        return content;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setIdenvoye(int idenvoye) {
        this.idenvoye = idenvoye;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }
}
