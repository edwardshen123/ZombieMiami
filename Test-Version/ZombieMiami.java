import javax.swing.JFrame;

public class ZombieMiami {
    public static void main(String[] args) {
        
	//Starts the GUI
	JFrame window = new JFrame("Zombie Miami");
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	//Runs GamePanel as Container
	window.setContentPane(new GamePanel());

	window.pack();
	window.setLocationRelativeTo(null);
	window.setVisible(true);

    }
}
