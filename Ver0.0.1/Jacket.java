import java.util.Scanner;

public class Jacket{
    private int health;
    private int attack;
    private int speed;
    public Mask m;
    public Weapon w;

    public Jacket(){
	setHealth(100);
	setAttack(10);
	setSpeed(2);
	m=selectMask();
    }

    public Mask selectMask() {
	System.out.println("Select Your Mask");
	Scanner sc = new Scanner();
	String sMask = sc.nextLine();
	Mask nMask;
	switch (sMask) {
	case "Rooster Mask":
	    nMask = new roosterMask();
	    break;
	default:
	    nMask = new roosterMask();
	    break;
	}
	return nMask;
    }

    public int getHealth(){
	return health;
    }

    public void setHealth(int newHealth){
	health=newHealth;;
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

    public String toString(){
	return m.getName()+" Jacket";
    }

}
