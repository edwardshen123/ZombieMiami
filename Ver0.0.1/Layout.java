public class Layout{

    public boolean[][] Walls;


    public Layout(){
	Walls=new boolean[41][41];
	for (int i=0; i< Walls.length;i++){
	    for (int j=0; j< Walls[0].length;j++){
		if (i%4==0 || j%4==0){
		    Walls[i][j]=true;
		}
		if (i%4==2 || j%4==2){
		    if (Math.floor(Math.random()*100)%2==0){
			Walls[i][j]=false;
		    }
		}
	    }
	}
	String s="";
	for (int i=0; i< Walls.length;i++){
	    for (int j=0; j< Walls[0].length;j++){
		if (Walls[i][j]) s=s+"*";
		else s=s+" ";
	    }
	    s=s+"\n";
	}
	System.out.println(s);
    }

    public static void main(String[] args){
	Layout a=new Layout();
    }

}
