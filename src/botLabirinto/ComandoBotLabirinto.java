package botLabirinto;

public enum ComandoBotLabirinto {
    LABIRINTO1("Labirinto 1"),LABIRINTO2("Labirinto 2"),START("Start");

    String comando;
    private ComandoBotLabirinto(String comando){
        this.comando = comando;
    }

    public static ComandoBotLabirinto fromString(String text) {
        if (text != null) {
            for (ComandoBotLabirinto c : ComandoBotLabirinto.values()) {
                if (text.equals(c.comando)) {
                    return c;
                }
            }
        }
        return ComandoBotLabirinto.START;
    }
}
