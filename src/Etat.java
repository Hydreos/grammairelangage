import java.util.ArrayList;
import java.util.List;

public class Etat {
    public int exp;
    List<Transition> transitions = new ArrayList<>();

    Etat(int n, int base) {
        if (base == 10) this.exp = (int) Math.pow(2, n);
        if (base == 2) this.exp = n;
    }

    void addTransition(Transition t) {
        transitions.add(t);
    }

    @Override
    public String toString() {
        return "Etat{exp=" + exp + ", transitions=" + transitions + '}';
    }
}
