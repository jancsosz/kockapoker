package jatek;

/**
 * Alap játékos struktúráját megvalósító osztály.
 */
public abstract class Jatekos {
    private String nev;
    private String nem;
    private int nyertJatekokSzama;

    public Jatekos(){}

    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setNem(String nem) {
        this.nem = nem;
    }

    public String getNev() {
        return nev;
    }

    public String getNem() {
        return nem;
    }

    public int getNyertJatekokSzama() {
        return nyertJatekokSzama;
    }

    /**
     * Egy kör megnyerése esetén használt metódus.
     */
    public void nyert(){
        this.nyertJatekokSzama++;
    }
}
