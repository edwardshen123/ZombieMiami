import java.util.Scanner;
import java.awt.*;

public class Jacket{

    private int Conversion = 1000000;

    //Graphics Variables
    private double x;
    private double y;
    private int r;

    private int dx;
    private int dy;
    private int speed;

    //Input variables
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    
    //Shooting Variables
    private boolean firing;
    private boolean firingGrenade;
    private double firingX;
    private double firingY;
    private long firingTimer;
    private long firingDelay;

    //Death Variables
    private boolean recovering;
    private long recoveryTimer;

    //Graphics Color
    private Color color1;
    private Color color2;

    //Character Stat
    private int score;
    private int lives;
    public String[] grenadeNames = {
	"Grenade", "Banana bomb", "HHG of A"
    };
    private int[] bombNum = {
	0, 0, 0
    };
    /*
      [grenadeNum, bananabombNum, holyHandGrenadeNum]
     */

    //Weapons
    private int weaponType;
    private String weaponName;
    private int ammo;
    private int maxAmmo;

    //Constructor
    public Jacket(){

	x = GamePanel.WIDTH / 2;
	y = GamePanel.HEIGHT / 2;
	r = 5;

	dx = 0;
	dy = 0;
	speed = 5;

	color1 = Color.WHITE;
	color2 = Color.RED;

	firing = false;
	firingTimer = System.nanoTime();
	firingDelay = 500;

	recovering = false;
	recoveryTimer = 0;

	score = 0;
	lives = 3;

	weaponType = 0;
	weaponName = "pistol";
	ammo = 1000000;
	maxAmmo = 1000000;
    }

    public double getX() {return x;}
    public double getY() {return y;}
    public double getR() {return r;}

    public String getWeaponName() {return weaponName;}

    public boolean isRecovering() { return recovering; }

    public int getLives(){return lives;}

    public int getScore() {return score;}

    public int getGrenadeNum(int index) {return bombNum[index];}

    public int getAmmo() {return ammo;}
    public int getMaxAmmo() {return maxAmmo;}

    public void loseLife() {
	lives--;
	recovering = true;
	recoveryTimer = System.nanoTime();
    }

    public void addScore(int i) {
	score += i;
    }

    public void setLeft(boolean b) {
	left = b;
    }
    public void setRight(boolean b) {
	right = b;
    }
    public void setUp(boolean b) {
	up = b;
    }
    public void setDown(boolean b) {
	down = b;
    }

    public void setFiring(boolean b, double firingX, double firingY, boolean isGrenade) {
	firing = b;
	this.firingX = firingX;
	this.firingY = firingY;
	firingGrenade = isGrenade;
    }

    public void setWeapon(Weapon w) {
	if (w.getType() == 1) {
	    bombNum[1]++;
	    return;
	}
	if (w.getType() == 6) {
	    bombNum[0]++;
	    return;
	}
	if (w.getType() == 12) {
	    bombNum[2]++;
	    return;
	}
	weaponType = w.getType();
	weaponName = w.getName();
	ammo = w.getAmmo();
	maxAmmo = w.getAmmo();
	firingDelay = w.getFiringDelay();
    }

    public void setMaskBoost(String maskBoostType, int maskBoost) {
	switch (maskBoostType) {
	case "":
	    break;
	case "Shotgun":
	    weaponType = 10;
	    weaponName = "shotgun";
	    ammo = 15;
	    maxAmmo = 15;
	    break;
	case "Speed":
	    speed += maskBoost;
	    break;
	case "Lives":
	    lives += maskBoost;
	    break;
	}
    }

    public String toString(){
	return "Jacket";
    }

    public void update() {
	if (left) {
	    dx = -speed;
	}
	if (right) {
	    dx = speed;
	}
	if (up) {
	    dy = -speed;
	}
	if (down) {
	    dy = speed;
	}
	
	x += dx;
	y += dy;

	if (x < r) {
	    x = r;
	}
	if (y < r) {
	    y = r;
	}
	if (x > GamePanel.WIDTH - r) {
	    x = GamePanel.WIDTH - r;
	}
	if (y > GamePanel.HEIGHT - r) {
	    y = GamePanel.HEIGHT - r;
	}

	dx = 0;
	dy = 0;

	if (firing) {
	    //pause between firing weapons
	    long elapsed = (System.nanoTime() - firingTimer) / Conversion;
	    if (elapsed > firingDelay) {
		firingTimer = System.nanoTime();
		if (firingX == x) {
		    firingX++;
		}
	        double slope = (firingY - y)/(firingX - x);
		//angleRad : angle in Radians
		double angleRad = Math.atan(slope);
		if (firingX < x) {
		    angleRad += Math.PI;
		}
		if (firingGrenade) {
		    int type = GamePanel.grenadeType;
		    if (bombNum[type] > 0) {
			if (type == 0 || type == 1) {
			    GamePanel.grenades.add(new Grenade(angleRad, x, y, false));
			} else {
			    Grenade gr = new Grenade(angleRad, x, y, true);
			    GamePanel.grenades.add(gr);
			    if (!GamePanel.hhgOnScreen) {
				GamePanel.hhgOnScreen = true;
				gr.isFirstHolyHandGrenade = true;
			    }
			}
			bombNum[type]--;
		    }
		} else {
		    if (ammo > 0) {
			if (weaponType == 0 || weaponType == 3 || weaponType == 4) {
			    GamePanel.bullets.add(new Bullet(angleRad, x, y));
			} else if (weaponType == 10) {
			    int rightAngle = 5;
			    int leftAngle = -5;
			    //Displacement Angles for left and right angle
			    double rightDA = angleRad + Math.PI/2;
			    double leftDA = angleRad - Math.PI/2;
			    //X and Y Displacement coordinates of the right bullet
			    double rightDX = Math.cos(rightDA) * 5.0;
			    double rightDY = Math.sin(rightDA) * 5.0;
			    //X and Y Displacement coordinates of the left bullet
			    double leftDX = Math.cos(leftDA) * 5.0;
			    double leftDY = Math.sin(leftDA) * 5.0;
			    GamePanel.bullets.add(new Bullet(angleRad + Math.toRadians(rightAngle), x + rightDX, y + rightDY));
			    GamePanel.bullets.add(new Bullet(angleRad + Math.toRadians(leftAngle), x + leftDX, y + leftDY));
			    GamePanel.bullets.add(new Bullet(angleRad, x, y));
			} else if (weaponType == 4 || weaponType == 8 || weaponType == 9) {
			    GamePanel.rockets.add(new Rocket(angleRad, x, y));
			} else {
			    GamePanel.bullets.add(new Bullet(angleRad, x, y));
			}
			ammo--;
		    } else {
			weaponType = 0;
			weaponName = "pistol";
			ammo = 1000000;
			maxAmmo = 1000000;
		    }
		}
	    }
	}
	if (recovering) {
	    long elapsed = (System.nanoTime() - recoveryTimer) / Conversion;
	    if (elapsed > 2000) {
		recovering = false;
		recoveryTimer = 0;
	    }
	}
    }

    public void draw(Graphics2D g) {

	if (recovering) {
	    	g.setColor(color2);
		g.fillOval((int) x - r,(int) y - r, 2 * r, 2 * r);

		g.setStroke(new BasicStroke(3));
		g.setColor(color2.darker());
		g.drawOval((int) x - r,(int) y - r, 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));

	}
	else {
	    g.setColor(color1);
	    g.fillOval((int) x - r,(int) y - r, 2 * r, 2 * r);

	    g.setStroke(new BasicStroke(3));
	    g.setColor(color1.darker());
	    g.drawOval((int) x - r,(int) y - r, 2 * r, 2 * r);
	    g.setStroke(new BasicStroke(1));
	}
    }

}
