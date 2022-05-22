package shared.communication;

import java.time.LocalDateTime;

public interface IAusleiher {

    LocalDateTime getFristBeginn();
    void setFristBeginn(LocalDateTime fristBeginn);
    String getMitgliedsID();
    LocalDateTime getReservierdatum();
    void setReservierdatum(LocalDateTime reservierdatum);
    boolean isAbgegeben();
    void setAbgegeben(boolean abgegeben);

}
