public class Transition {
    public Etat arrivEtat;
    public String symbole;

    Transition(Etat e, String s) {
        this.arrivEtat = e;
        this.symbole = s;
    }
}
