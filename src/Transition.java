public class Transition {
    public int arrivEtat;
    public String symbole;

    Transition(int e, int base, String s) {
        this.symbole = s;
        if (base == 10) this.arrivEtat = (int) Math.pow(2, e);
        if (base == 2) this.arrivEtat = e;
    }

    @Override
    public String toString() {
        return "Transition{arrivEtat=" + arrivEtat + ", symbole='" + symbole + "'}";
    }
}