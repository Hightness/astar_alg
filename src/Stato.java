package astar;
import java.util.ArrayList;

public interface Stato{

    ArrayList<String> actions();

    boolean equals(Stato stato);

    Stato result(String action);

    double path_cost(String action, Stato new_stato);

    boolean goal_test();

    double h();

    String toString();

}
