public class Mask {
    
    private String type;
    private String boostType;
    private int boost;

    public void setBoostType(String insertType) {
	boostType = insertType;
    }

    public String getBoostType() {
	return boostType;
    }

    public void setBoost(int boostValue) {
	boost = boostValue;
    }
    
    public int getBoost() {
	return boost;
    }

    public void setType(String initType) {
	type = initType;
    }

    public String toString() {
	return type;
    }
}
