package enigmaComponents;

public class Printer{
	private int noOfTabs;
	
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
	
	public void print(String string){
		String output="";
		for(int i=0;i<noOfTabs;i++){
			output+="    ";
		}
		
		output+=string;
		System.out.println(output);
	}
}