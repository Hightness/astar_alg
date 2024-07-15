package astar;
import java.util.ArrayList;

public class Node{
    Node parent, left, right, fparent;//nodo dal quale proviene questo nodo
    public Stato stato;
    String action, color;
    double path_cost;
    int depth;

    public Node(Stato stato, Node parent, String action, double path_cost) {
        left = right = fparent = null;
        this.stato = stato;
        this.action = action;
        this.path_cost = path_cost;
        this.depth = 0;
        this.parent = parent;
        if(parent != null)this.depth = parent.depth + 1;
    }

    public Node[] path() {
        Node[] nodes = new Node[depth];
        Node n = this;
        while (n.parent != null) {
            nodes[n.depth - 1] = n;
            n = n.parent;
        }
        return nodes;
    }

    public Node[] expand(){
        ArrayList<String> actions = stato.actions();
        Node[] expanded = new Node[actions.size()];
        for(int i = 0; i < actions.size(); i++){
            Stato ns = stato.result(actions.get(i));
            expanded[i] = new Node(ns, this, actions.get(i), path_cost + stato.path_cost(actions.get(i), ns));
        }
        return expanded;
    }
}
