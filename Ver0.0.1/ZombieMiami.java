import javax.swing.JFrame;

public class ZombieMiami {
    public static void main(String[] args) {
        
	JFrame window = new JFrame("Zombie Miami");
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	window.setContentPane(new GamePanel());

	window.pack();
	window.setVisible(true);

    }
}
