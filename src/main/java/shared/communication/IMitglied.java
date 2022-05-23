package shared.communication;


import server.dienstleistungsmodul.Anfragenliste;
import server.users.Personendaten;

import java.time.LocalDateTime;

public interface IMitglied extends IGast {

    void reservierungenErhöhen();
    void reservierungenVerringern();
    int getReservierungen();
    void veraendereStundenkonto(int change);
    boolean isGesperrt();
    Anfragenliste getAnfragenliste();
    LocalDateTime getMitgliedSeit();
    void datenVerwalten(Personendaten attr, String wert);

}
