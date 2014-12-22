public class Jacket{
    private int health;
    private int attack;
    private int speed;
    public Mask m=new Mask();
    public Weapon w=new Weapon();

    public Jacket(){
	setHealth();
	setAttack();
	setSpeed();
    }

    public int getHealth(){
	return this.health;
    }

    public void setHealth(){
	health=m.health;
    }

    public int getAttack(){
	return this.attack;
    }
    
    public void setAttack(Weapon w){
	attack= w.damage + m.attack;
    }

    public int getSpeed(){
	return this.speed;
    }

    public void setSpeed(){
	speed=m.speed;
    }

    public String toString(){
	return m.getName()+" Jacket";
    }

}
