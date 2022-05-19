package server.users;

public enum Rolle {

    GAST(Gast.class),
    MITGLIED(Mitglied.class),
    MITARBEITER(Mitarbeiter.class),
    VORSITZ(Vorsitz.class);

    private Class<?> klasse;


    Rolle(Class<?> klasse) {
        this.klasse = klasse;
    }

    public Class<?> getKlasse() {
        return klasse;
    }

}
