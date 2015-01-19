import java.awt.*;

public class Weapon {
    
    //Weapon Graphics
    private double x;
    private double y;
    private int r;

    //Weapons Characteristics
    private int type;
    private Color color1;

    //Final Variables
    private static final boolean[] weaponRanges = {true,false,false,false,true,
				  false,true,false,true,true,
				  true,true,true};
    private static final String[] weaponNames = {
	"pistol","bananabomb","bat","assault rifle","cannon",
	"golf club","grenade","katana","mortar","RPG",
	"shotgun","machine gun","The Holy Hand Grenade Of Antioch"
    };
    private static final long[] firingDelay = {
	500, 1000, 750, 400, 500,
	750, 1000, 750, 500, 500,
	500, 200, 1000
    };

    //Constructor
    public Weapon(int type, double x, double y) {
	
	this.type = type;
	this.x = x;
	this.y = y;
	r = 3;

	if (type == 0 || type == 11) {
	    color1 = Color.PINK;
	}
	if (type == 1 || type == 12) {
	    color1 = Color.YELLOW;
	}
	if (type == 4 || type == 6 || type == 8) {
	    color1 = Color.GREEN;
	}
	if (type == 3 || type == 10) {
	    color1 = Color.GRAY;
	}
	if (type == 2 || type == 5 || type == 7) {
	    color1 = Color.WHITE;
	}
	if (type == 9) {
	    color1 = Color.RED;
	}

    }

    public double getX() {return x;}
    public double getY() {return y;}
    public int getR() {return r;}

    public int getType() {return type;}
    public String getName() {return weaponNames[type];}
    public boolean getRange() {return weaponRanges[type];}
    public long getFiringDelay() {return firingDelay[type];}

    public boolean update() {
	
	y += 2;

	if (y > GamePanel.HEIGHT + r) {
	    return true;
	}

	return false;
    }

    public void draw(Graphics2D g) {
	g.setColor(color1);
	g.fillRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	
	g.setStroke(new BasicStroke(3));
	g.setColor(color1.darker());
	g.drawRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	g.setStroke(new BasicStroke(1));
	
    }
    /*
    public void setDamage(int i){
	Damage=weaponInfo[i][0];
    }

    public int getDamage(){
	return Damage;
    }
    */

}
