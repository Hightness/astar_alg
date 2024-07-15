package astar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {


    public static void main(String[] args) {
        System.out.print("Digita 1 per labirinthgame, 2 per onegame..: ");
        Scanner in = new Scanner(System.in);
        int game = in.nextInt();
        System.out.print("Inserisci dimensioni iniziali della matrice di stato: ");
        int size = in.nextInt();
        Panel pane = new Panel(game, size);
        Frame frame = new Frame();
        frame.add(pane);
        pane.requestFocus();
    }

    public static Node[] bruteforce(Node nodo, int limit){
        Node[] s = null;
        int max_depth = 1;
        Set<String> ap = new HashSet<>();
        ap.add(nodo.stato.toString());
        while(s == null && limit >= max_depth){
            s = bruteforce(0, max_depth, nodo, ap);
            max_depth*=2;
        }
        return s;
    }

    private static Node[] bruteforce(int depth, int max_depth, Node nodo, Set<String> already_pathed){
        if(depth == max_depth)return null;
        if (nodo.stato.goal_test())return nodo.path();
        already_pathed.add(nodo.stato.toString());
        Node[] s = null;
        for(Node child: nodo.expand()){
            if(!already_pathed.contains(child.stato.toString())){
                Node[] temp = bruteforce(depth+1, max_depth, child, already_pathed);
                s = temp != null && (s == null || temp.length < s.length) ? temp : s;
            }
        }
        already_pathed.remove(nodo.stato.toString());
        return s;
    }

    public static Node[] search(Stato start, String ftype) {
        Node nodo = new Node(start, null, null, 0);
        if(ftype == "bruteforce")bruteforce(nodo, 64);
        Frontiera frontier = new Frontiera(ftype);
        frontier.setRoot(nodo);
        Set<String> explored = new HashSet<>();
        Node already_found;
        while(!frontier.isEmpty()) {
            nodo = frontier.popMinRoot();
            if (nodo.stato.goal_test())return nodo.path();
            explored.add(nodo.stato.toString());
            for(Node child: nodo.expand()){
                already_found = frontier.search(child);
                if(!explored.contains(child.stato.toString()) && already_found != null && frontier.compare(child, already_found) < 0){
                    frontier.RBdelete(already_found);
                    frontier.RBinsert(child);
                }
                if(!explored.contains(child.stato.toString()) && already_found == null)frontier.RBinsert(child);
            }
        }
        return null;
    }
}
