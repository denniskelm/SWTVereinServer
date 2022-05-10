package server;
/*
@author
Raphael Kleebaum
Jonny Schlutter
Gabriel Kleebaum
Mhd Esmail Kanaan
Gia Huy Hans Tran
Ole Björn Adelmann
Bastian Reichert
Dennis Kelm
*/

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Geraet {

    private String geraeteID;
    private String name;
    private String spenderName;
    private int leihfrist; // muss in Tagen angegeben werden
    private String kategorie;
    private String beschreibung;
    private String abholort;
    private ArrayList<Ausleiher> reservierungsliste, historie;
    // reservierdatum: LocalDateTime
    // fristbeginn: LocalDateTime
    // abgegeben: boolean
    // mitgliedsID: int
    private Status leihstatus; // frei / beansprucht / ausgeliehen

    public Geraet(String geraeteID, String name, String spenderName, int leihfrist, String kategorie, String beschreibung, String abholort) {
        this.geraeteID = geraeteID;
        this.name = name;
        this.spenderName = spenderName;
        this.leihfrist = leihfrist;
        this.kategorie = kategorie;
        this.beschreibung = beschreibung;
        this.abholort = abholort;
        reservierungsliste = new ArrayList<>();
        historie = new ArrayList<>();
        leihstatus = Status.FREI;
    }

    public void reservierungHinzufuegen(String personenID) {
        Ausleiher ausleiher = new Ausleiher(personenID);

        // ausrechnen, wann er das Gerät abholen kann
        if (leihstatus == Status.FREI) {
            ausleiher.setFristBeginn(LocalDateTime.now());
            leihstatus = Status.BEANSPRUCHT;
        } else {
            Ausleiher letzter = reservierungsliste.get(reservierungsliste.size() - 1);
            ausleiher.setFristBeginn(letzter.getFristBeginn().plusDays(leihfrist));
        }

        reservierungsliste.add(ausleiher);
    }
        

    // Entfernt eine Reservierung von der Reservierungsliste.
    // TODO: wie soll damit umgegangen werden, wenn die methode nicht erfolgreich ausgeführt werden kann? (z.B. wenn die Person das Gerät momentan ausgeliehen hat)
    public void reservierungEntfernen(String personenID) {
        for (int i = 1; i < reservierungsliste.size(); i++) {
            if (reservierungsliste.get(i).getMitlgiedsID() == personenID){
                if (reservierungsliste.get(i).getFristBeginn().isAfter(LocalDateTime.now()) || leihstatus == Status.BEANSPRUCHT)
                    reservierungsliste.remove(i);
            }
        }
    }

    public void ausgeben() {
        leihstatus = Status.AUSGELIEHEN;
    }

    public void annehmen() {
        leihstatus = Status.FREI;

        LocalDateTime altesAbgabeDatum = reservierungsliste.get(0).getFristBeginn().plusDays(leihfrist);
        long tageZuFrueh = LocalDateTime.now().until(altesAbgabeDatum, ChronoUnit.DAYS);

        //aktuellen Ausleiher zur Historie hinzufügen
        historie.add(reservierungsliste.get(0));

        // aktuellen Ausleiher aus Reservierungsliste entfernen
        reservierungsliste.remove(0);

        // Fristbeginn der anderen Ausleiher neu berechnen
        for (Ausleiher a : reservierungsliste) {
            a.setFristBeginn(a.getFristBeginn().minusDays(tageZuFrueh));
        }

    }

    public String getGeraeteID() {
        return geraeteID;
    }

    public void setHistorie(ArrayList<Ausleiher> historie) {
        this.historie = historie;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpenderName(String spenderName) {
        this.spenderName = spenderName;
    }

    public void setLeihfrist(int leihfrist) {
        this.leihfrist = leihfrist;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setAbholort(String abholort) {
        this.abholort = abholort;
    }

}
