package astar;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Random;

public class Panel extends JPanel implements ActionListener{
    int size, counter, animation, no_solution, counterb, game, rounds_per_size;
    boolean repaint, automatic_repaint;
    Image player, wall;
    HashMap<String, Node[]> actions;
    HashMap<String, Double> exp_value;
    int s_posx, s_posy, e_posx, e_posy;
    int[][] current_lab;
    Timer timer;

    Panel(int game, int size){
        this.game = game;
        this.size = size;
        rounds_per_size = 10;
        repaint = automatic_repaint = false;
        actions = new HashMap<String, Node[]>();
        exp_value = new HashMap<String, Double>();
        this.addKeyListener(new MyKeyAdapter());
        counter = animation = no_solution = counterb = 0;
        exp_value.put("astar", 0.0);
        exp_value.put("dijkstra", 0.0);
        exp_value.put("bruteforce", 0.0);
        timer = new Timer(20, this);
        timer.start();
    }

    public Stato onegame(int size){
        Random rand = new Random();
        int[][] ones = new int[size][size];
        int x = rand.nextInt(size), y = rand.nextInt(size);
        for (int i = 0; i < size; i++)for (int j = 0; j < size; j++)ones[i][j] = rand.nextInt(2);
        return new OneState(ones, y, x);
    }

    public Stato labirinthgame(int size){
        MazeGenerator mg = new MazeGenerator(size);
        Random rand = new Random();
        mg.generateMaze();
        int x = rand.nextInt(size), y = rand.nextInt(size), goalx = rand.nextInt(size), goaly = rand.nextInt(size);
        while(mg.maze[goaly][goalx] == 1){
            goalx = rand.nextInt(size);
            goaly = rand.nextInt(size);
        }
        while(mg.maze[y][x] == 1 || Math.sqrt((y-goaly)*(y-goaly)+(x-goalx)*(x-goalx)) < size/5 + 1){
            x = rand.nextInt(size);
            y = rand.nextInt(size);
        }
        return new LabirinthState(mg.maze, y, x, goalx, goaly);
    }

    private void alg_time(Stato stato, String alg){
        long t = System.currentTimeMillis();
        actions.put(alg, Main.search(stato, alg));
        exp_value.put(alg, System.currentTimeMillis() - t + exp_value.get(alg));
    }

    public void paintComponent(Graphics g) {
        if(repaint || automatic_repaint) {
            super.paintComponent(g);
            repaint = false;
            Image crossable = new ImageIcon(this.getClass().getResource("images/texture.png")).getImage();
            Image goal = new ImageIcon(this.getClass().getResource("images/goal.png")).getImage();
            int tile_width = getWidth() / (current_lab[0].length);
            int tile_height = getHeight() / (current_lab.length);

            current_lab[s_posy][s_posx] = 0;
            for (int i = 0; i < current_lab.length; i++)
                for (int j = 0; j < current_lab[i].length; j++) {
                    g.drawImage(crossable, j * tile_width, i * tile_height, tile_width, tile_height, this);
                    if (current_lab[i][j] == 1)g.drawImage(wall, j * tile_width, i * tile_height, tile_width, tile_height, this);
                }
            if(e_posy >= 0)
                g.drawImage(goal, e_posx * tile_width, e_posy * tile_height, tile_width, tile_height, this);
            g.drawImage(player, s_posx * tile_width, s_posy * tile_height, tile_width, tile_height, this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(repaint || automatic_repaint) {
            animation = automatic_repaint ? 0 : animation;
            if (animation > 0) {
                String a = actions.get("astar")[animation - 1].action;
                s_posy = a == "up" ? s_posy - 1 : a == "down" ? s_posy + 1 : s_posy;
                s_posx = a == "left" ? s_posx - 1 : a == "right" ? s_posx + 1 : s_posx;
                repaint = true;
                repaint();
            }else{
                Stato stato = onegame(size);
                this.current_lab = ((OneState) stato).ones;
                this.s_posx = ((OneState) stato).j;
                this.s_posy = ((OneState) stato).i;
                this.e_posx = this.e_posy = -1;
                wall = new ImageIcon(this.getClass().getResource("images/goal.png")).getImage();
                if(game == 1) {
                    stato = labirinthgame(size);
                    this.current_lab = ((LabirinthState) stato).labirinth;
                    this.s_posx = ((LabirinthState) stato).j;
                    this.s_posy = ((LabirinthState) stato).i;
                    this.e_posx = ((LabirinthState) stato).goalx;
                    this.e_posy = ((LabirinthState) stato).goaly;
                    wall = new ImageIcon(this.getClass().getResource("images/wall.png")).getImage();
                }

                alg_time(stato, "astar");
                alg_time(stato, "dijkstra");
                counter++;

                if (actions.get("astar") != null){// && actions.get("astar").length > size/2) {
                    counterb++;
                    if(game == 1) {
                        alg_time(stato, "bruteforce");
                        if (actions.get("bruteforce") == null) no_solution++;
                    }
                    size = counter%rounds_per_size == 0 ? size + 1: size;
                    int p = new Random().nextInt(10);
                    player = new ImageIcon(this.getClass().getResource("images/player"+ p +".png")).getImage();
                    repaint = true;
                    animation = 0;
                    repaint();
                }
            }
        }
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

            if(e.getKeyCode() == KeyEvent.VK_N){
                repaint = actions.get("astar") != null && (animation < actions.get("astar").length);
                animation = repaint ? animation + 1 : animation;
            }

            else if(e.getKeyCode() == KeyEvent.VK_ENTER){
                animation = 0;
                repaint = true;
            }

            else if(e.getKeyCode() == KeyEvent.VK_V) {
                System.out.println("----------------------------------------------------------------------------------");
                System.out.println("Dimensione attuale labirinto: "+size+"; numero di stati computati: " + counter + ", di cui " + counterb + " esiste una sol");
                System.out.println("Astar, media di esecuzione : " + exp_value.get("astar")/(1000*counter) + " secondi");
                System.out.println("Dijkstra, media di esecuzione : " + exp_value.get("dijkstra")/(1000*counter) + " secondi");
                System.out.println("Bruteforce, media di esecuzione : " + exp_value.get("bruteforce")/(1000*counterb) + " secondi, con " + no_solution + " problemi non risolti depth=64");
                System.out.println("----------------------------------------------------------------------------------\n\n\n");
            }

            else if(e.getKeyCode() == KeyEvent.VK_A){
                automatic_repaint = !automatic_repaint;
            }
        }
    }
}
