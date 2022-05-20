package server.geraetemodul;

public enum Status {
    FREI("Frei"),
    BEANSPRUCHT("Beansprucht"),
    AUSGELIEHEN("Ausgeliehen");

    String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
