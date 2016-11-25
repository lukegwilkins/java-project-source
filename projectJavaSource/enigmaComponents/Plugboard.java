package enigmaComponents;

import java.util.ArrayList;
import java.lang.Character;

public class Plugboard{
	private int[] swapMapping;
	private Scrambler scrambler;
	private Printer printer;
	private boolean printerEnabled;
	
	public Plugboard(){
		scrambler = new Scrambler();
		swapMapping = new int[26];
		for(int i=0; i<26;i++){
			swapMapping[i]=i;
		}
		printerEnabled = true;
	}
	
	public ArrayList<String> getSwapMapping(){
		ArrayList<String> returnList = new ArrayList<String>();
		for(int i=0; i<26;i++){
			if(swapMapping[i]!=i && swapMapping[i]>i){
				String swap = "" + ((char)(97+i))+"/"+((char)(97+swapMapping[i]));
				returnList.add(swap.toUpperCase());
			}
		}
		return returnList;
	}
	
	public void swap(char a, char b){
		int asciiA = (int)(Character.toLowerCase(a));
		int asciiB = (int)(Character.toLowerCase(b));
		
		if(asciiA<97 || asciiA>122 || asciiB<97 ||asciiB>122){
			throw new IllegalArgumentException("Can only swap 2 letters between a and z inclusive");
		}
		else if(swapMapping[asciiA-97] != asciiA-97 || swapMapping[asciiB-97] != asciiB-97){
			throw new IllegalArgumentException(""+ a +" or "+ b +" has already been swapped");
		}
		else{
			swapMapping[asciiA-97]=asciiB-97;
			swapMapping[asciiB-97]=asciiA-97;
		}
	}
	
	public void setScrambler(Scrambler scrambler){
		this.scrambler=scrambler;
	}
	
	public Scrambler getScrambler(){
		return scrambler;
	}
	
	public void resetMapping(){
		for(int i=0;i<26;i++){
			swapMapping[i]=i;
		}
	}
	
	public void setSwapMapping(ArrayList<String> mapping){
		for(String string : mapping){
			if(string.length()>3 || string.indexOf('/')!=1){
				throw new IllegalArgumentException("A swap is a string of length exactly 3 in the form A//B, where A and B are the two swapped letters");
			}
		}
		
		resetMapping();
		
		for(String string : mapping){
			String[] letters = string.split("/");
			swap(letters[0].charAt(0), letters[1].charAt(0));
		}
	}
	
	public void unswap(char a, char b){
		int asciiA = (int) a;
		int asciiB = (int) b;
		
		if(swapMapping[asciiA-97] != asciiB-97){
			throw new IllegalArgumentException(""+a+" and "+b+" aren't swapped");
		}
		else{
			swapMapping[asciiA-97] = asciiA-97;
			swapMapping[asciiB-97] = asciiB-97;
		}
	}
	
	//add output
	public char encrypt(char letter){
		int wire = (int) Character.toLowerCase(letter) -97;
		
		if(wire<0 || wire>25){
			throw new IllegalArgumentException("Can only encrypt the letters between a-z inclusive");
		}
		else{
			char swappedLetter = (char)(swapMapping[wire] + 97);
			
			if(printerEnabled){
				printer.print(""+letter+" is swapped with "+swappedLetter);
				printer.addTab();
			}
			
			char encryptedLetter = scrambler.encrypt(swappedLetter);		
			wire = (int) encryptedLetter - 97;
			
			if(printerEnabled){
				printer.removeTab();
				printer.print(""+encryptedLetter+" is swapped with "+((char)(swapMapping[wire]+97)));
			}
		}
		
		return (char)(swapMapping[wire]+97);
	}
	
	public void setPrinter(Printer printer){
		this.printer = printer;
		scrambler.setPrinter(printer);
	}
	
	public void enablePrinter(){
		printerEnabled = true;
		scrambler.enablePrinter();
	}
	
	public void disablePrinter(){
		printerEnabled = false;
		scrambler.disablePrinter();
	}
	
	public boolean printerEnabled(){
		return printerEnabled;
	}
}