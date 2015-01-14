import java.util.Scanner;
import java.awt.*;

public class Jacket{

    private int Conversion = 1000000;

    //Graphics Variables
    private int x;
    private int y;
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

    //Weapons
    private int weaponType;
    private String weaponName;
    private boolean weaponRange;

    //Character Stats
    private int lives;
    private int attack;
    //public Mask m;

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
	firingDelay = 200;

	recovering = false;
	recoveryTimer = 0;

	score = 0;
	lives = 3;
	attack = 10;
	weaponType = 0;
	weaponName = "airsoft";
	weaponRange = true;
    }

    public double getX() {return x;}
    public double getY() {return y;}
    public double getR() {return r;}

    public String getWeaponName() {return weaponName;}

    public boolean isRecovering() { return recovering; }

    public void loseLife() {
	lives--;
	recovering = true;
	recoveryTimer = System.nanoTime();
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

    public void setFiring(boolean b) {
	firing = b;
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

		if (weaponType == 0) {
		    GamePanel.bullets.add(new Bullet(270, x, y));
		}
		else if (weaponType == 10) {
		    GamePanel.bullets.add(new Bullet(275, x + 5, y));
		    GamePanel.bullets.add(new Bullet(265, x - 5, y));
		    GamePanel.bullets.add(new Bullet(270, x, y));
		}
		else if (weaponType == 4 || weaponType == 8) {
		    GamePanel.rockets.add(new Rocket(270, x, y));
		}
		else {
		    GamePanel.bullets.add(new Bullet(270, x, y));
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
		g.fillOval(x - r, y - r, 2 * r, 2 * r);

		g.setStroke(new BasicStroke(3));
		g.setColor(color2.darker());
		g.drawOval(x - r, y - r, 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));

	}
	else {
	    g.setColor(color1);
	    g.fillOval(x - r, y - r, 2 * r, 2 * r);

	    g.setStroke(new BasicStroke(3));
	    g.setColor(color1.darker());
	    g.drawOval(x - r, y - r, 2 * r, 2 * r);
	    g.setStroke(new BasicStroke(1));
	}
    }

    public void setWeapon(Weapon w) {
	weaponType = w.getType();
	weaponName = w.getName();
	weaponRange = w.getRange();
    }

    public int getLives(){
	return lives;
    }

    public int getAttack(){
	return attack;
    }
    
    public void setAttack(int newAttack){
	attack=newAttack;
    }

    public int getSpeed(){
	return speed;
    }

    public void setSpeed(int newSpeed){
	speed=newSpeed;
    }

    public int getScore() {
	return score;
    }

    public void addScore(int i) {
	score += i;
    }

    public String toString(){
	return "Jacket";
    }

}
