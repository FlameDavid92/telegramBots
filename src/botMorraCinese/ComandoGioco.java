package botMorraCinese;

public enum ComandoGioco {
    AVVIA("Avvia Gioco"), AVVIASPECIAL("Avvia Special"), AVVIAEVIL("Avvia Evil"), BENVENUTO("Benvenuto");

    String comando;
    private ComandoGioco(String comando){
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
        return ComandoGioco.BENVENUTO;
    }
}
