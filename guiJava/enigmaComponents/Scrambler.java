package enigmaComponents;
import java.lang.Character;
//class file for the scrambler
public class Scrambler{
	private int amountOfRotors;
	private BasicRotor firstRotor;
	private boolean crackingMode;
	private boolean printerEnabled;
	private Printer printer;
	//constructor for scrambler
	public Scrambler(){
		//set crackingMode to false
		crackingMode = false;
		
		//create a new reflector for the scrambler
		BasicRotor reflector = new NonRotReflector();
		
		//create a set of rotors for the scrambler to use
		Rotor thirdRotor = new Rotor();
		Rotor secondRotor = new Rotor();
		Rotor firstRotor = new Rotor();
		
		int[] circuitry = {25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
		
		//sets the circuitry for the scrambler and the rotors
		reflector.setCircuitry(circuitry);
		
		thirdRotor.setCircuitry(circuitry);
		secondRotor.setCircuitry(circuitry);
		firstRotor.setCircuitry(circuitry);
		
		//sets it up so the rotors are in other with the second next to the first, the third next
		//to the second and the reflector next to the third, the rotors are like a linked list of 
		//rotors terminated by an instance of the Reflector class
		thirdRotor.setNextRotor(reflector);
		secondRotor.setNextRotor(thirdRotor);
		firstRotor.setNextRotor(secondRotor);
		
		//stores the amount of Rotors we have, 3 normal Rotors and a reflector
		amountOfRotors = 4;		
		
		//set the firstRotor variable for the class to be the firstRotor created in the constructor
		this.firstRotor=firstRotor;
		
		printerEnabled = true;
	}
	
	
	//sets what the first rotor is, note this method is allowed to completely change the rotor chain
	//e.g. you could go from 3 rotors and a reflector to just a reflector, it will not attempt to preserve
	//the rotor chain setup.
	public void setFirstRotor(BasicRotor rotor){
		//sets firstRotor to the rotor
		firstRotor = rotor;
		
		//we need to recalculate the number of rotors
		amountOfRotors = 1;
		while(rotor instanceof Rotor){
			//cast rotor to an instance of Rotor
			rotor = ((Rotor) rotor).getNextRotor();
			amountOfRotors += 1;
		}
	}
	
	//returns the firstRotor
	public BasicRotor getFirstRotor(){
		return firstRotor;
	}
	
	//returns the amount of Rotors used for testing
	public int getAmountOfRotors(){
		return amountOfRotors;
	}
	
	//get the rotor at the given position
	public BasicRotor getRotorAtPos(int position) throws IllegalArgumentException{
		//throw an exception if a bad position is given
		if(position<0 || position>(amountOfRotors-1)){
			throw new IllegalArgumentException("Positions must be between 0 or the amount of rotors -1 inclusive");
		}
		else{
			//else we go through the linked list of rotors and find the rotor at the given
			//position
			BasicRotor returnRotor = firstRotor;
			while(position>0){
				returnRotor = ((Rotor)returnRotor).getNextRotor();
				position -=1;
			}
			return returnRotor;
		}
	}
	
	//sets the rotor at the given position
	//fix so a specific rotor object only occurs once
	public void setRotorAtPos(BasicRotor rotor, int position){
		//check if we are replacing the first rotor
		if(position == 0){
			//check if rotor is and Instance of Rotor
			if(rotor instanceof Rotor){
				if(amountOfRotors == 1){
					firstRotor = rotor;
					((Rotor) rotor).setNextRotor(new NonRotReflector());
					amountOfRotors=2;
				}
				else{
					Rotor nextRotor = (Rotor) ((Rotor)firstRotor).getNextRotor();
					((Rotor) rotor).setNextRotor(nextRotor);
					firstRotor = rotor;
				}
			}
			else{
				firstRotor=rotor;
				amountOfRotors = 1;
			}
		}
		//check if we are replacing the last rotor
		else if(position == (amountOfRotors-1)){
			Rotor prevRotor = (Rotor) getRotorAtPos(position-1);
			if(rotor instanceof Rotor){
				prevRotor.setNextRotor(rotor);
				((Rotor) rotor).setNextRotor(new NonRotReflector());
				amountOfRotors +=1;
			}
			else{
				prevRotor.setNextRotor(rotor);
			}
		}
		else{
			Rotor prevRotor = (Rotor) getRotorAtPos(position-1);
			Rotor tempRotor = (Rotor) prevRotor.getNextRotor();
			prevRotor.setNextRotor(rotor);
			((Rotor) rotor).setNextRotor(tempRotor.getNextRotor());
		}
	}
	
	//encrypts a character
	public char encrypt(char a){
		//converts character to lowercase and ascii
		char letter = Character.toLowerCase(a);
		if((int)a <97 || (int)a >122){
			throw new IllegalArgumentException("Can only encrypt the letters between a-z inclusive");
		}
		else{
			if(printerEnabled){
				printer.print("The char is " + letter);
			}
			
			//if crackingmode is not enabled we rotate the first rotor then encrypt
			if(!crackingMode){
				
				if(printerEnabled){
					printer.addTab();
				}
				
				firstRotor.rotate();
				char encryptedLetter =firstRotor.encrypt(letter);
				
				if(printerEnabled){
					printer.removeTab();			
					printer.print("This encrypts as "+encryptedLetter);
				}
				
				return encryptedLetter;
				
			}
			
			//else we just encrypt
			else{
				if(printerEnabled){
					printer.addTab();
				}
				
				char returnChar= firstRotor.encrypt(letter);
				
				if(printerEnabled){
					printer.removeTab();			
					printer.print("This encrypts as "+returnChar);
				}
				return returnChar;
			}
		}
	}
	
	//returns the encryption for the entire alphabet 
	public char[] encryptAlphabet(){
		
		boolean temp = crackingMode;
		crackingMode = true;
		char[] returnList = new char[26];
		
		for(int i =0; i<26;i++){
			returnList[i]=encrypt((char)(i+97));
		}
		
		crackingMode= temp;
		return returnList;
	}
	
	//gets whether cracking mode is on or off
	public boolean getCrackingMode(){
		return crackingMode;
	}
	
	//set the cracking mode
	public void setCrackingMode(boolean mode){
		crackingMode=mode;
	}
	public void setPrinter(Printer printer){
		this.printer = printer;
		firstRotor.setPrinter(printer);
	}
	
	public Printer getPrinter(){
		return printer;
	}
	
	public void enablePrinter(){
		printerEnabled = true;
		firstRotor.enablePrinter();
	}
	
	public void disablePrinter(){
		printerEnabled = false;
		firstRotor.disablePrinter();
	}
	
	public boolean printerEnabled(){
		return printerEnabled;
	}
}