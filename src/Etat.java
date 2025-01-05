import java.util.ArrayList;
import java.util.List;

public class Etat {
    public int exp;
    boolean etatFinal = false;
    List<Transition> transitions = new ArrayList<>();

    Etat(int n, int base) {
        if (base == 10) this.exp = (int) Math.pow(2, n);
        if (base == 2) this.exp = n;
    }

    Etat(Etat e) {
        this.exp = e.exp;
        this.etatFinal = e.etatFinal;
        for (Transition t : e.transitions) {
            addTransition(t);
        }
    }

    void addTransition(Transition t) {
        transitions.add(t);
    }

    @Override
    public String toString() {
        return "Etat{exp=" + exp + ", transitions=" + transitions + '}';
    }
}
