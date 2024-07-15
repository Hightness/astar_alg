package astar;
import java.util.ArrayList;

public class LabirinthState implements Stato {
    public int[][] labirinth;
    public int i,j, goalx, goaly;

    public LabirinthState(int[][] labirinth, int i, int j, int goalx, int goaly) {
        this.labirinth = new int[labirinth.length][labirinth[0].length];
        for(int k=0;k<labirinth.length;k++)for(int l=0;l<labirinth[0].length;l++)this.labirinth[k][l] = labirinth[k][l];

        this.goalx = goalx;
        this.goaly = goaly;
        this.i = i;
        this.j = j;
    }

    public ArrayList<String> actions(){
        ArrayList<String> actions = new ArrayList<>();
        if(i != 0 && labirinth[i-1][j] != 1)actions.add("up");
        if(i < labirinth.length - 1 && labirinth[i+1][j] != 1)actions.add("down");
        if(j != 0 && labirinth[i][j - 1] != 1)actions.add("left");
        if(j < labirinth[0].length - 1 && labirinth[i][j+1] != 1)actions.add("right");
        return actions;
    }

    public boolean equals(Stato s){
        LabirinthState stato = (LabirinthState) s;
        return stato.i == this.i && stato.j == this.j;
    }

    public String toString(){
        String slab = "\n\n\n";
        for (int i = 0; i < labirinth.length; i++){
            for (int j = 0; j < labirinth.length; j++){
                if(i == this.i && j == this.j)slab += "T  ";
                else if(i == goaly && j == goalx)slab += "G  ";
                else slab += labirinth[i][j] + "  ";
            }
            slab += "\n";
        }
        return slab;
    }

    public LabirinthState result(String action){
        switch(action){
            case "up":
                return new LabirinthState(labirinth,i-1,j, goalx, goaly);
            case "down":
                return new LabirinthState(labirinth,i+1,j, goalx, goaly);
            case "left":
                return new LabirinthState(labirinth,i,j-1, goalx, goaly);
            case "right":
                return new LabirinthState(labirinth,i,j+1, goalx, goaly);
        }
        return null;
    }

    public double path_cost(String action, Stato new_stato){
        return 1;
    }

    public boolean goal_test(){
        return i == goaly && j == goalx;
    }

    public double h(){
        return Math.sqrt((i-goaly)*(i-goaly) + (j-goalx)*(j-goalx));
    }

}
