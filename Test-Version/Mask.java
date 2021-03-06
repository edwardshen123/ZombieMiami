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
	"","Shotgun","Speed","Lives"
    };
    private static final int[] boosts = {
	-1,-1,2,3
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
	String s = maskNames[type];
	if (type == 0) {
	    s += " is just for decoration";
	}
	if (type == 1) {
	    s += " gives you a " + boostTypes[type];
	}
	if (type >= 2) {
	    s += " adds " + boosts[type] + " to " + boostTypes[type];
	}
	return s;
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
	g.setFont(new Font("Century Gothic", Font.PLAIN, 15));
	String s = maskNames[type];
	int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
	g.drawString(s, (int) (x - length / 2), (int) y + 20);
    }
}
