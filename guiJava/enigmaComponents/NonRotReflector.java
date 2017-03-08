package enigmaComponents;

//class file for a Non rotating Reflector
public class NonRotReflector extends Reflector{
	
	//constructor for a NonRotReflector
	public NonRotReflector(){
		//initializes the array list storing the circuitry to map each input wire to a corresponding output wire
		int[] initialCircuitry = new int[26];		
		for(int i=0; i<26; i++){
			initialCircuitry[i]=(25-i);
		}
		setCircuitry(initialCircuitry);
		//sets position to be 1
		setPosition(1);
		//enables the printer
		enablePrinter();
	}
	
	//implements the rotate method inherited from BasicRotor, since this rotor doesn't rotate it doesn't do anything
	public void rotate(){
		
	}
	
	
}