public class Layout{

    public boolean[][] Walls;

    public Layout(){
	Walls=new boolean[40][40];
	for (int i=0; i< Walls.length;i++){
	    for (int j=0; j< Walls[0];j++){
		if (i==0 || i==39 || j==0 ||j==39){
		    Walls[i][j]=true;
		}
		if (Walls[i-1][j] || Walls[i][j-1]){
		    if (Math.ceil(Math.Random()*100)%2 == 0) Walls[i][j]=true;
		    else Wall[i][j]=false;
		}
	    }
	}
	String s="";
	for (int i=0; i< Walls.length;i++){
	    for (int j=0; j< Walls[0].length;j++){
		if (Walls[i][j]) s=s+"#";
		else s=s+" ";
	    }
	    s=s+"/n";
	}
	System.out.println(s);
    }

    public static void main(String[] args){
	Layout a=new Layout();
    }

}
