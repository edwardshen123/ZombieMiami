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
	int boost = boosts[type];
	String boostLocation = boostTypes[type];
	if (type == 0) {
	    boost = 0;
	    boostLocation = "character";
	}
	return maskNames[type] + " adds " + boost + " to your " + boostLocation;
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
