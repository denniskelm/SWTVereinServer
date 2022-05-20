package server.dienstleistungsmodul;

import java.util.ArrayList;

public class Anfragenliste {
    private String user_ID;

    public server.users.Mitglied nutzer;
    private ArrayList<GesuchAnfrage> gliste;
    private ArrayList<AngebotAnfrage> aliste;


    public Anfragenliste(String user_ID) {
        this.user_ID = user_ID;
        this.gliste = new ArrayList<GesuchAnfrage>();
        this.aliste = new ArrayList<AngebotAnfrage>();
    }

    public void addaAnfrage(server.users.Mitglied nutzer, Dienstleistungsangebot angebot, int stunden) throws Exception{
        AngebotAnfrage a =new AngebotAnfrage(nutzer, angebot,stunden);
        this.aliste.add(a);
    }

    public void addgAnfrage(server.users.Mitglied nutzer, Dienstleistungsgesuch gesuch, int stunden) throws Exception{
        GesuchAnfrage g = new GesuchAnfrage(nutzer, gesuch, stunden);
        this.gliste.add(g);
    }
    public void aremoveaAnfrage(){

    }

    public void aremovegAnfrage(){

    }

    public void gAnfrageAnnehmen(GesuchAnfrage g) throws Exception{
        /*int i=0;
        while (i<this.gliste.size()){
            if (this.gliste.get(i)==g){

            }

        }*/

        this.gliste.remove(g);
        this.nutzer.veraendereStundenkonto(g.stunden);
        g.nutzer.veraendereStundenkonto(-g.stunden);
    }

    public void aAnfrageAnnehmen(AngebotAnfrage a) throws Exception{
        this.gliste.remove(a);
        this.nutzer.veraendereStundenkonto(a.stunden);
        a.nutzer.veraendereStundenkonto(-a.stunden);
    }
}