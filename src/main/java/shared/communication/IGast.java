package shared.communication;

import server.users.Personendaten;

import java.time.LocalDateTime;

public interface IGast {

    String getPersonenID();
    String getNachname();
    String getAnschrift();
    String getVorname();
    String getEmail();
    int getPassword();
    String getMitgliedsNr();
    int getTelefonNr();
    boolean getSpenderStatus();
    void datenVerwalten(Personendaten attr, String wert);

}
