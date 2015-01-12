public class Layout{

    public boolean[][] Walls;
    public int[][]doorways=
    {{;


    public Layout(){
	Walls=new boolean[81][81];
	for (int i=0; i< Walls.length;i++){
	    for (int j=0; j< Walls[0].length;j++){
		if (i%5==0 || j%5==0){
		    Walls[i][j]=true;
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
