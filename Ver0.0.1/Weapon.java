public class Weapon{
    private int Damage;
    private boolean Ranged;
    private Object[][] weaponInfo;
    

    public Weapon(String name){
	setupInfo();
	i=findWeapon(name);
	if (i>0){
	    setDamage(i);
	    setRanged(i);
	}
    }

    public int findWeapon(String name){
	for (int i=0; i<weaponInfo.length; i++){
	    if (weaponInfo[i][0].equals(name)) return i;
	}
	return -1;
    }

    public void setDamage(int i){
	Damage=weaponInfo[i][1];
    }

    public int getDamage(){
	return Damage;
    }

    public void setRanged(int i){
	Ranged=weaponInfo[i][2];
    }

    public boolean getRanged(){
	return Ranged;
    }

    public void setupInfo(){
	weaponInfo={
	    {"airsoft", 3, true},
	    {"bananabomb", 10, false},
	    {"bat", 2, false},
	    {"cat", 4, false},
	    {"cannon", 5, true},
	    {"HolyHandGrenade", 10000000000000000000, true},
	    {"mortar", 7, true},
	    {"shotgun", 5, true},
	    {"teeshirtgun", 2, true},
	}
    }

    public static void main(String[] args){
	Weapon w= new Weapon("HolyHandGrenade");
	System.out.println(getDamage());
	System.out.println(getRanged());
    }
}
