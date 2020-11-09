package botLabirinto;

public enum ComandoGioco {
    SU("SU"), DESTRA("DESTRA"), GIU("GIÙ"), SINISTRA("SINISTRA"), TERMINA("TERMINA"), ERRORE("ERRORE");

    String comando;

    private ComandoGioco(String comando) {
        this.comando = comando;
    }

    public static ComandoGioco fromString(String text) {
        if (text != null) {
            for (ComandoGioco c : ComandoGioco.values()) {
                if (text.equals(c.comando)) {
                    return c;
                }
            }
        }
        return ComandoGioco.ERRORE;
    }
}
