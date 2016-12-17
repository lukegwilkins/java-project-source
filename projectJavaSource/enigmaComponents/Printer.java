package enigmaComponents;

//simple class that we use for printing encryption information out in a nice formatted manner
public class Printer{
	//number of tabs
	private int noOfTabs;
	
	//constructor
	public Printer(){
		noOfTabs=0;
	}
	
	public void addTab(){
		noOfTabs+=1;
	}
	
	public void removeTab(){
		if (!(noOfTabs==0)){
			noOfTabs-=1;
		}
	}
	
	//prints however manner tabs there are and then whattever was passed as input
	public void print(String string){
		String output="";
		for(int i=0;i<noOfTabs;i++){
			output+="    ";
		}
		
		output+=string;
		System.out.println(output);
	}
}