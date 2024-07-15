package astar;

import java.util.ArrayList;
import java.util.Arrays;

public class OneState implements Stato {
    public int[][] ones;
    public int i,j;

    public OneState(int[][] ones, int i, int j) {
        this.ones = new int[ones.length][ones[0].length];
        for(int k=0;k<ones.length;k++)for(int l=0;l<ones[0].length;l++)this.ones[k][l] = ones[k][l];
        this.i = i;
        this.j = j;
        this.ones[i][j] = 0;
    }

    public ArrayList<String> actions(){
        ArrayList<String> actions = new ArrayList<>();
        if(i != 0)actions.add("up");
        if(i < ones.length - 1)actions.add("down");
        if(j != 0)actions.add("left");
        if(j < ones[0].length - 1)actions.add("right");
        return actions;
    }

    public boolean equals(Stato s){
        OneState stato = (OneState) s;
        if(stato.i != this.i || stato.j != this.j)return false;
        for(int k = 0; k < this.ones.length; k++)
            for(int l = 0; l < this.ones[0].length; l++)if(this.ones[k][l] != stato.ones[k][l]) return false;
        return true;
    }

    public String toString(){
        String slab = "\n\n\n";
        for (int i = 0; i < ones.length; i++){
            for (int j = 0; j < ones[i].length; j++){
                if(i == this.i && j == this.j)slab += "T    ";
                else slab += ones[i][j] + "    ";
            }
            slab += "\n";
        }
        return slab;
    }

    public OneState result(String action){
        return action == "up" ? new OneState(ones,i-1,j) : action == "down" ? new OneState(ones,i+1,j) :
                action == "left" ? new OneState(ones,i,j-1) : new OneState(ones,i,j+1);
    }

    public double path_cost(String action, Stato new_stato){
        return 1;
    }

    public boolean goal_test(){
        for(int k = 0; k < this.ones.length; k++)if(Arrays.stream(ones[k]).sum() > 0) return false;
        return true;
    }

    public double h(){
        int counter = 0;
        for(int k = 0; k < ones.length; k++)counter += Arrays.stream(ones[k]).sum();
        return counter;
    }

}
