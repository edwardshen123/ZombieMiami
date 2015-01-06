public class Weapon{
    private int Damage;
    private int Ranged;
    private int[][] weaponInfo;
    private String[] weaponNames;
    

    public Weapon(String name){
	setupInfo();
	i=findWeapon(name);
	if (i>0){
	    setDamage(i);
	    setRanged(i);
	}
    }

    public int findWeapon(String name){
	for (int i=0; i<weaponNames.length; i++){
	    if (weaponNames[i][0].equals(name)) return i;
	}
	return -1;
    }

    public void setDamage(int i){
	Damage=weaponInfo[i][0];
    }

    public int getDamage(){
	return Damage;
    }

    public void setRanged(int i){
	Ranged=weaponInfo[i][1];
    }

    public int getRanged(){
        return Ranged;
    }

    public void setupInfo(){
	weaponInfo= new int[9][2];
	//info for weapon with corresponding index in weaponName
	//{Damage (just a number), Ranged (0=false and 1=true)}
	weaponInfo={
	    {3, 1},
	    {10, 0},
	    {2, 0},
	    {4, 0},
	    {5, 1},
	    {7, 1},
	    {5, 1},
	    {2, 1},
	    {10000000, 1}
	};

	weaponNames=new String[9];
	weaponNames = {
	    "airsoft",
	     "bananabomb",
	     "bat",
	     "cat",
	     "cannon",
	     "mortar",
	     "shotgun",
	     "teeshirtgun",
	     "TheHolyHandGrenadeOfAntioch"
	};
    }

    public static void main(String[] args){
	Weapon w= new Weapon("TheHolyHandGrenadeOfAntioch");
	System.out.println(getDamage());
	System.out.println(getRanged());
    }
}
