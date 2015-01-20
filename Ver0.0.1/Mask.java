import java.awt.*;

public class Mask {
    
    //Mask Graphics
    private double x;
    private double y;
    private int r;

    //Mask Characteristics
    //numsOfMask: Number of Masks
    public static int numsOfMask = 4;
    private Color color;
    private int type;
    private boolean isSelect;

    //Final Variables
    private static final String[] maskNames = {
	"Rooster", "Tiger", "Rabbit", "Zamanasky"
    };
    private static final String[] boostTypes = {
	null,"Attack","Speed","Lives"
    };
    private static final int[] boosts = {
	0,2,2,3
    };
    private static final Color[] colors = {
	Color.YELLOW, Color.ORANGE, Color.WHITE, Color.RED
    };

    public Mask(double x, double y, int type) {
	this.x = x;
	this.y = y;
	this.type = type;
	color = colors[type];
	r = 4;
	isSelect = false;
    }

    public void setSelect(boolean b) {
	isSelect = b;
    }

    public String getBoostType() {
	return boostTypes[type];
    }
    
    public int getBoost() {
	return boosts[type];
    }

    public String toString() {
	return maskNames[type];
    }

    public boolean update() {
	return true;
    }
    
    public void draw(Graphics2D g) {
	g.setColor(color);
	g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);

	g.setStroke(new BasicStroke(3));
	g.setColor(color.darker());
	g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	g.setStroke(new BasicStroke(1));

	g.setColor(Color.WHITE);
	if (isSelect) {
	    g.setColor(Color.RED);
	}
	String s = maskNames[type];
	g.drawString(s, (int) x, (int) y + 10);
    }
}
