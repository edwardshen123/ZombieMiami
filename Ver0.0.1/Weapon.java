public class Weapon{
    //private int Damage;
    private boolean Ranged;
    private boolean[] weaponInfo={true,false,false,false,true,
				  false,true,false,true,true,
				  true,true,true};
    private String[] weaponNames={
	"airsoft","bananabomb","bat","cat","cannon",
	"golfclub","grenade","katana","mortar","POKEBALL",
	"shotgun","teeshirtgun","TheHolyHandGrenadeOfAntioch"
    };
    

    public Weapon(String n){
	int a=findWeapon(n);
	if (a>=0){
	    //setDamage(a);
	    setRanged(a);
	}
	else System.out.println("weapons not found");
    }

    public int findWeapon(String name){
	for (int i=0; i<weaponNames.length; i++){
	    if (weaponNames[i].equals(name)) return i;
	}
	return -1;
    }

    /*
    public void setDamage(int i){
	Damage=weaponInfo[i][0];
    }

    public int getDamage(){
	return Damage;
    }
    */

    public void setRanged(int i){
	Ranged=weaponInfo[i];
    }

    public boolean getRanged(){
        return Ranged;
    }

    public static void main(String[] args){
	Weapon w= new Weapon("bananabomb");
	//System.out.println(w.getDamage());
	System.out.println(w.getRanged());
    }
}
