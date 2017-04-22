import enigmaComponents.*;

import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

public class EnigmaMachine{
	//variables for the EnigmaMachine to use
	private Rotor[] rotors; 
	private Printer printer;
	private Reflector[] reflectors;
	
	private Scanner scanner;
	private Scrambler scrambler;
	private Plugboard plugboard;
	
	private String permutation;
	//we set up the rotors for use
	public EnigmaMachine(){
		//creates a new printer that is used by the enigma components
		printer = new Printer();
		
		//stores each of the 5 original rotors that came with the enigma machine
		rotors = new Rotor[5];
		//a 2d char array that stores each of the rotors circuits
		char[][] rotorCircuits = {
			{'E','K','M','F','L','G','D','Q','V','Z','N','T','O','W','Y','H','X','U','S','P','A','I','B','R','C','J'},
			{'A','J','D','K','S','I','R','U','X','B','L','H','W','T','M','C','Q','G','Z','N','P','Y','F','V','O','E'},
			{'B','D','F','H','J','L','C','P','R','T','X','V','Z','N','Y','E','I','W','G','A','K','M','U','S','Q','O'},
			{'E','S','O','V','P','Z','J','A','Y','Q','U','I','R','H','X','L','N','F','T','G','K','D','C','M','W','B'},
			{'V','Z','B','R','G','I','T','Y','U','P','S','D','N','H','L','X','A','W','M','J','Q','O','F','E','C','K'}
		};
		
		//a char array that stores each of the turnover characters
		char[] turnoverChars={'Q', 'E', 'V', 'J', 'Z'};
		
		//we set it so the first rotor in rotors has the first circuit and first turn over char, the second 
		//the second circuit and second turnover char etc.
		//also sets it so all the rotors use printer
		for(int i=0;i<5;i++){
			rotors[i]= new Rotor();
			rotors[i].setCircuitry(rotorCircuits[i]);
			rotors[i].setTurnoverChar(turnoverChars[i]);
			rotors[i].setPrinter(printer);
		}
		
		//stores each of the 3 reflectors that can be used by the enigma machine
		reflectors = new NonRotReflector[3];
		//stores circuits for each reflector
		char[][] reflectorCircuits = {
			{'E','J','M','Z','A','L','Y','X','V','B','W','F','C','R','Q','U','O','N','T','S','P','I','K','H','G','D'},
			{'Y','R','U','H','Q','S','L','D','P','X','N','G','O','K','M','I','E','B','F','Z','C','W','V','J','A','T'},
			{'F','V','P','J','I','A','O','Y','E','D','R','Z','X','W','G','C','T','K','U','Q','S','B','N','M','H','L'}			
		};
		
		//sets up each of the reflectors so the first reflector has the first circuit, the second has the second circuit
		//sets it so each reflector has the same printer
		for(int i=0; i<3;i++){
			reflectors[i] = new NonRotReflector();
			reflectors[i].setCircuitry(reflectorCircuits[i]);
			reflectors[i].setPrinter(printer);
		}
		
		//sets it up so the enigma machine has the initial rotor permutation A,1,2,3
		rotors[2].setNextRotor(rotors[1]);
		rotors[1].setNextRotor(rotors[0]);
		rotors[0].setNextRotor(reflectors[0]);
		
		//sets it so that the middle rotor has doublestepping enabled
		rotors[1].enableDoubleStep();
		
		//sets up a new scrambler to manage the current rotor permutation
		scrambler = new Scrambler();
		scrambler.setFirstRotor(rotors[2]);
		//scrambler.setPrinter(printer);
		
		//sets up a plugboard to manage the scrambler
		plugboard = new Plugboard();
		plugboard.setScrambler(scrambler);
		plugboard.setPrinter(printer);
		
		//sets up a scanner to take input from the console
		scanner = new Scanner(System.in);
		
		//enables the printer for the plugboard and any other enigma components it contains
		plugboard.enablePrinter();
		
		//stores the initial permutation in a string for outputting
		permutation="A, 1, 2, 3";
	}
	
	public void setPermutation(String inputPerm){		
		String[] stringPositions = inputPerm.replaceAll("\\s","").split(",");
		
		//set the rotor of the left to be the last rotor in the string
		int rotor = Integer.parseInt(stringPositions[3]);
		Rotor prevRotor = rotors[rotor-1];
		Rotor firstRotor = prevRotor;
		
		//disable the doublestep
		firstRotor.disableDoubleStep();
		
		//stores the permutation as it is been processed
		permutation= "" + rotor;
		
		//sets the rotor in the middle and right 
		for(int i=2; i>0;i--){
			rotor = Integer.parseInt(stringPositions[i]);
			//sets the previous rotor to point to the next rotor
			prevRotor.setNextRotor(rotors[rotor-1]);
			prevRotor = rotors[rotor-1];
			//stores the permutation as it is been processed
			permutation=""+rotor+", "+permutation;
			prevRotor.disableDoubleStep();
		}
		
		//gets the reflector used 
		String reflectorString = stringPositions[0];
		permutation= reflectorString + ", "+ permutation;
		
		char reflectorChar = reflectorString.charAt(0);
		
		//has the left rotor point to the reflector
		Reflector reflector = reflectors[(int)Character.toLowerCase(reflectorChar) - 97];
		prevRotor.setNextRotor(reflector);
		
		//enables doublestep for the middle rotor
		((Rotor)firstRotor.getNextRotor()).enableDoubleStep();
		
		//sets the scrambler to point to the first rotor/rotor on the right
		scrambler.setFirstRotor(firstRotor);
	}
	
	//#@
	//method for setting the plugboard settings
	public void editPlugboardSettings(){
		String input="";
		while(!(input.equals("menu"))){
			
			//outputs the menu options
			System.out.println("1. view plugboard settings");
			System.out.println("2. swap two letters");
			System.out.println("3. unswap two letters");
			System.out.println("4. reset plugboard");
			System.out.println("5. edit entire plugboard settings");
			
			//gets the input
			input = scanner.nextLine();
			
			try{
				
				//if 1 is selected then the current plugboard setting is shown
				if(input.equals("1")){
					
					ArrayList<String> plugboardSettings = plugboard.getSwapMapping();
					String output="";
					for(String swap: plugboardSettings){
						output+=swap+", ";
					}
					if(output!=""){
						output=output.substring(0,output.length()-2);
						System.out.println("The plugboard setting is:\n"+output);
					}
					else{
						System.out.println("No letters have been swapped");
					}
					
				}
				
				//if 2 is selected then the user inputs the 2 letters to be swapped
				else if(input.equals("2")){
					
					System.out.println("Input first character");
					input = scanner.nextLine();
					
					char firstCharacter = input.charAt(0);
					
					System.out.println("Input second character");
					input = scanner.nextLine();
					
					char secondCharacter = input.charAt(0);
					
					plugboard.swap(firstCharacter, secondCharacter);
				}
				
				//if 3 is selected then the user inputs the 2 letters to be unswapped
				else if(input.equals("3")){
					
					System.out.println("Input first character to unswap");
					input = scanner.nextLine();
					
					char firstCharacter = input.charAt(0);
					
					System.out.println("Input second character to unswap");
					input = scanner.nextLine();
					
					char secondCharacter = input.charAt(0);
					
					plugboard.unswap(firstCharacter, secondCharacter);
				}
				
				//if 4 is selected the plugboard is reset
				else if(input.equals("4")){
					
					plugboard.resetMapping();
				}
				
				//if 5 is selected then the entire letter swaps are input all at once
				else if(input.equals("5")){
					//outputs how the settings should be inputted
					System.out.println("Input the swapping as a list where each \"swap\" is the first letter/the second letter e.g. inputting A/C, F/T would swap A with C and F with T.");
					
					//gets the input
					input = scanner.nextLine();
					//removes the whitespace
					input = input.replaceAll("\\s+","");
					
					//splits the string into an array
					String[] settingsStringArray = input.split(",");
					ArrayList<String> swapSettings = new ArrayList<String>(Arrays.asList(settingsStringArray));
					
					//sets the plugboard setting
					plugboard.setSwapMapping(swapSettings);
				}
				
				//if the input is invalid an error message is shown
				else{
					System.out.println("Invalid input");
				}
			}
			
			//if an error occurs the error message is outputted
			catch(IllegalArgumentException e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	//gets the current rotor permutation
	public String getPermutation(){
		return permutation;
	}
	
	//returns the scrambler
	public Scrambler getScrambler(){
		return scrambler;
	}
	
	//returns the plugboard
	public Plugboard getPlugboard(){
		return plugboard;
	}
	
	public String encryptString(String message){
		message = message.replaceAll("\\s","");
		String encryptedString = "";
		
		//temporarily disable the printer for the plugboard
		boolean temp = plugboard.printerEnabled();
		plugboard.disablePrinter();
		
		//encrypt each letter in the string
		for(int i=0; i<message.length(); i++){
			
			encryptedString= encryptedString + plugboard.encrypt(message.charAt(i));
			
			if(((i+1)%5)==0){
				
				encryptedString = encryptedString + " ";
				
			}
			
		}
		
		if(temp){
			
			plugboard.enablePrinter();
			
		}
		//output the encrypted string
		return encryptedString;
	}
	
	
}