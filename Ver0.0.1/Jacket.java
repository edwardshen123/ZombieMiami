public class Jacket{
    private int health;
    private int attack;
    private int speed;
    public Mask m=new Mask();

    public int getHealth(){
	return health;
    }

    public void setHealth(int i){
	health=i + m.health;
    }

    public int getAttack(){
	return attack;
    }
    
    public void setAttack(Weapon w){
	attack= w.damage + m.attack;
    }

    public int getSpeed(){
	return speed;
    }

    public void setSpeed(int i){
	speed=i + m.speed;
    }

    

}
