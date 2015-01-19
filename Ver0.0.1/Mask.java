import java.awt.*;

public class Mask {
    
    //Mask Graphics
    private double x;
    private double y;
    private int r;

    //Mask Characteristics
    private Color color;
    private int type;

    //Final Variables
    private static final String[] maskNames = {
	"Rooster", "Tiger", "Rabbit", "Zamanasky"
    };
    private static final String[] boostType = {
	null,"Attack","Speed","Lives"
    };
    private static final int[] boost = {
	0,2,2,3
    };

    public Mask(double x, double y, int type) {
	this.x = x;
	this.y = y;
	this.type = type;
	r = 4;

	
    }

    public void setBoostType(String insertType) {
	boostType = insertType;
    }

    public String getBoostType() {
	return boostType;
    }

    public void setBoost(int boostValue) {
	boost = boostValue;
    }
    
    public int getBoost() {
	return boost;
    }

    public void setType(String initType) {
	type = initType;
    }

    public String toString() {
	return type;
    }

    public boolean update() {
    }
    
    public void draw(Graphics2D g) {
	g.setColor(color1);
	g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);

	g.setStroke(new BasicStroke(3));
	g.setColor(color1.darker());
	g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	g.setStroke(new BasicStroke(1));
    }
}
