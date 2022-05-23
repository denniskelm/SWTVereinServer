package shared.communication;

import server.geraetemodul.Ausleiher;
import server.geraetemodul.Status;
import java.util.ArrayList;

public interface IGeraet {

    ArrayList<Ausleiher> getHistorie();
    void reservierungHinzufuegen(String personenID);
    void reservierungEntfernen(String personenID);
    void ausgeben();
    void updateFristen();
    void annehmen();
    String getGeraeteID();
    String getSpenderName();
    String getGeraetName();
    String getGeraetAbholort();
    String getKategorie();
    String getGeraetBeschreibung();
    int getLeihfrist();
    Status getLeihstatus();
    ArrayList<Ausleiher> getReservierungsliste();
    void setHistorie(ArrayList<Ausleiher> historie);
    void setName(String name);
    void setSpenderName(String spenderName);
    void setLeihfrist(int leihfrist);
    void setKategorie(String kategorie);
    void setBeschreibung(String beschreibung);
    void setAbholort(String abholort);
    void setReservierungsliste(ArrayList<Ausleiher> reservierungsliste);

}
