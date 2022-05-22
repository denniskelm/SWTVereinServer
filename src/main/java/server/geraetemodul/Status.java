package server.geraetemodul;

import java.rmi.NoSuchObjectException;

public enum Status {
    FREI("Frei"),
    BEANSPRUCHT("Beansprucht"),
    AUSGELIEHEN("Ausgeliehen");

    final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Status getStatusByName(String name) throws NoSuchObjectException {
        return switch (name) {
            case "Frei" -> FREI;
            case "FREI" -> FREI;
            case "Beansprucht" -> BEANSPRUCHT;
            case "BEANSPRUCHT" -> BEANSPRUCHT;
            case "Ausgeliehen" -> AUSGELIEHEN;
            case "AUSGELIEHEN" -> AUSGELIEHEN;
            default -> throw new NoSuchObjectException("Diesen Status gibt es nicht");
        };

    }

}
