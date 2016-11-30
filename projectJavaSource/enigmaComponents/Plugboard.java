package enigmaComponents;

import java.util.ArrayList;
import java.lang.Character;

public class Plugboard{
	//variables for use in the program
	private int[] swapMapping;
	private Scrambler scrambler;
	private Printer printer;
	private boolean printerEnabled;
	
	//constructor for Plugboard
	public Plugboard(){
		//we set the scrambler to a new Scrambler
		scrambler = new Scrambler();
		
		//we set up the inital swap map, where each letter is swapped with itself
		swapMapping = new int[26];
		for(int i=0; i<26;i++){
			swapMapping[i]=i;
		}
		//we enable the printer
		printerEnabled = true;
	}
	
	//returns the swap mapping
	public ArrayList<String> getSwapMapping(){
		//arraylist to store the swap mapping
		ArrayList<String> returnList = new ArrayList<String>();
		//for each swapping we add it to the list
		for(int i=0; i<26;i++){
			//if a letter is not swapped with itself, and it is swapped with a letter greater than itself
			//then we add the "letter/swappedLetter" to the list.
			//we have the greater than portion to stop a swap be output twice but in reverse, and to make sure
			//all the outputted swaps are in alphabetical order
			if(swapMapping[i]!=i && swapMapping[i]>i){
				String swap = "" + ((char)(97+i))+"/"+((char)(97+swapMapping[i]));
				returnList.add(swap.toUpperCase());
			}
		}
		return returnList;
	}
	
	//stores a letter swap in the swapMapping
	public void swap(char a, char b){
		int asciiA = (int)(Character.toLowerCase(a));
		int asciiB = (int)(Character.toLowerCase(b));
		
		//if either of the letters aren't in the alphabet throw an error
		if(asciiA<97 || asciiA>122 || asciiB<97 ||asciiB>122){
			throw new IllegalArgumentException("Can only swap 2 letters between a and z inclusive");
		}
		//if one of the letter has already been swapped with another letter throw an error
		else if(swapMapping[asciiA-97] != asciiA-97 || swapMapping[asciiB-97] != asciiB-97){
			throw new IllegalArgumentException(""+ a +" or "+ b +" has already been swapped");
		}
		else{
			swapMapping[asciiA-97]=asciiB-97;
			swapMapping[asciiB-97]=asciiA-97;
		}
	}
	
	//sets the scrambler
	public void setScrambler(Scrambler scrambler){
		this.scrambler=scrambler;
	}
	
	//returns the scrambler
	public Scrambler getScrambler(){
		return scrambler;
	}
	
	//reset the swapMapping
	public void resetMapping(){
		for(int i=0;i<26;i++){
			swapMapping[i]=i;
		}
	}
	
	//takes an arraylist of strings of swaps and sets the swapMapping using it
	public void setSwapMapping(ArrayList<String> mapping){
		//checks that each swap has '/' in the middle and is of length ex
		for(String string : mapping){
			if(string.length()!=3 || string.indexOf('/')!=1){
				throw new IllegalArgumentException("A swap is a string of length exactly 3 in the form A//B, where A and B are the two swapped letters");
			}
		}
		//we rest the mapping
		resetMapping();
		//goes through each swap and swaps the letters
		for(String string : mapping){
			String[] letters = string.split("/");
			swap(letters[0].charAt(0), letters[1].charAt(0));
		}
	}
	
	//unswaps 2 letters
	public void unswap(char a, char b){
		int asciiA = (int) a;
		int asciiB = (int) b;
		
		//if the letters aren't swapped then an error is thrown
		if(swapMapping[asciiA-97] != asciiB-97){
			throw new IllegalArgumentException(""+a+" and "+b+" aren't swapped");
		}
		else{
			swapMapping[asciiA-97] = asciiA-97;
			swapMapping[asciiB-97] = asciiB-97;
		}
	}
	
	//encrypts a letter using the swap mappings and the scrambler
	public char encrypt(char letter){
		int wire = (int) Character.toLowerCase(letter) -97;
		
		//checks that the letter ot encrypt is part of the alphabet
		if(wire<0 || wire>25){
			throw new IllegalArgumentException("Can only encrypt the letters between a-z inclusive");
		}
		else{
			//we get what letter the input letter is swapped with
			char swappedLetter = (char)(swapMapping[wire] + 97);
			//if the printer is enabled we print that the letter was swapped
			if(printerEnabled){
				printer.print(""+letter+" is swapped with "+swappedLetter);
				//we add a tab the scrambler output
				printer.addTab();
			}
			//we get the scrambler to encrypt the letter
			char encryptedLetter = scrambler.encrypt(swappedLetter);		
			wire = (int) encryptedLetter - 97;
			
			//we output the letter returned by the scrambler and what it is swapped with
			if(printerEnabled){
				//we remove the tab that we  added
				printer.removeTab();
				printer.print(""+encryptedLetter+" is swapped with "+((char)(swapMapping[wire]+97)));
			}
		}
		//returns the encrypted letter
		return (char)(swapMapping[wire]+97);
	}
	
	//sets the printer
	public void setPrinter(Printer printer){
		this.printer = printer;
		//we set the scrambler to use the same printer
		scrambler.setPrinter(printer);
	}
	
	//enables the printer
	public void enablePrinter(){
		printerEnabled = true;
		//we enable the printer for the scrambler
		scrambler.enablePrinter();
	}
	
	//disables the printer
	public void disablePrinter(){
		printerEnabled = false;
		//disables the printer for the scrambler
		scrambler.disablePrinter();
	}
	
	//returns if the printer is enabled or not
	public boolean printerEnabled(){
		return printerEnabled;
	}
}