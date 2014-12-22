public class Zombie {
    
    private int health;
    private String type;

    public String claw(Jacket player) {
	int damage = 100;
	player.setHealth(player.getHealth - damage);
	return type + " lunge at " + player + " for " + damage + " damages";
    }

    public int getHealth() {
	return health;
    }
    
    public void setHealth(int newHealth) {
	health = newHealth;
    }

    public void setType(String newType) {
	type = newType;
    }

    public String toString() {
	return type;
    }
}
