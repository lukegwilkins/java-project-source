package enigmaComponents;

//class file for a Non rotating Reflector
public class NonRotReflector extends Reflector{
	
	public NonRotReflector(){
		//initializes the array list storing the circuitry to map each input wire to a corresponding output wire
		int[] initialCircuitry = new int[26];		
		for(int i=0; i<26; i++){
			initialCircuitry[i]=(25-i);
		}
		setCircuitry(initialCircuitry);
		setPosition(1);
		enablePrinter();
	}
	
	public void rotate(){
		
	}
	
	
}