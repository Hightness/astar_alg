package astar;
import javax.swing.JFrame;

public class Frame extends JFrame {

    public Frame(){
        this.setTitle("Premi invio per generare nuovo lab, premi \"n\" per spostare il pokemon, premi \"a\" per scorrimento automatico");
        this.setSize(1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
