import enigmaComponents.*;

import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

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
		printer = new Printer();
		
		rotors = new Rotor[5];
		char[][] rotorCircuits = {
			{'E','K','M','F','L','G','D','Q','V','Z','N','T','O','W','Y','H','X','U','S','P','A','I','B','R','C','J'},
			{'A','J','D','K','S','I','R','U','X','B','L','H','W','T','M','C','Q','G','Z','N','P','Y','F','V','O','E'},
			{'B','D','F','H','J','L','C','P','R','T','X','V','Z','N','Y','E','I','W','G','A','K','M','U','S','Q','O'},
			{'E','S','O','V','P','Z','J','A','Y','Q','U','I','R','H','X','L','N','F','T','G','K','D','C','M','W','B'},
			{'V','Z','B','R','G','I','T','Y','U','P','S','D','N','H','L','X','A','W','M','J','Q','O','F','E','C','K'}
		};
		
		char[] turnoverChars={'Q', 'E', 'V', 'J', 'Z'};
		
		for(int i=0;i<5;i++){
			rotors[i]= new Rotor();
			rotors[i].setCircuitry(rotorCircuits[i]);
			rotors[i].setTurnoverChar(turnoverChars[i]);
			rotors[i].setPrinter(printer);
		}
		
		reflectors = new NonRotReflector[3];
		char[][] reflectorCircuits = {
			{'E','J','M','Z','A','L','Y','X','V','B','W','F','C','R','Q','U','O','N','T','S','P','I','K','H','G','D'},
			{'Y','R','U','H','Q','S','L','D','P','X','N','G','O','K','M','I','E','B','F','Z','C','W','V','J','A','T'},
			{'F','V','P','J','I','A','O','Y','E','D','R','Z','X','W','G','C','T','K','U','Q','S','B','N','M','H','L'}			
		};
		
		for(int i=0; i<3;i++){
			reflectors[i] = new NonRotReflector();
			reflectors[i].setCircuitry(reflectorCircuits[i]);
			reflectors[i].setPrinter(printer);
		}
		
		rotors[2].setNextRotor(rotors[1]);
		rotors[1].setNextRotor(rotors[0]);
		rotors[0].setNextRotor(reflectors[0]);
		
		rotors[1].enableDoubleStep();
		
		scrambler = new Scrambler();
		scrambler.setFirstRotor(rotors[2]);
		//scrambler.setPrinter(printer);
		
		plugboard = new Plugboard();
		plugboard.setScrambler(scrambler);
		plugboard.setPrinter(printer);
		
		scanner = new Scanner(System.in);
		
		plugboard.enablePrinter();
		
		permutation="A, 1, 2, 3";
	}
	
	public void Run(){
		String input ="";
		while(!(input.equals("quit"))){
			System.out.println("1. To encrypt");
			System.out.println("2. To edit rotors settings");
			System.out.println("3. Edit rotor permutation");
			System.out.println("4. Edit Plugboard settings");
			System.out.println("5. Input settings and encrypt in one go");
			System.out.println("6. Additional options");
			System.out.println("Type quit to quit");
			try{
				input = scanner.nextLine();
				if(!(input.equals("quit"))){
				
					if(input.equals("1")){
					
						encrypt();
					
					}
					else if(input.equals("2")){
					
						editRotorSettingsMenu();
					
					}
					else if(input.equals("3")){
					
						editRotorPermutation();
					
					}
					else if(input.equals("4")){
					
						editPlugboardSettings();
					
					}
					else if(input.equals("5")){
						
						inputSettingsAndEncrypt();
					}
					else if(input.equals("6")){
						
						additionalOptions();
						
					}
					else{
						
						System.out.println("That is not a valid input");
					}
				}
			}
			catch(Exception e){
				//System.out.println(e.getMessage());
				System.out.println("An error occurred and you have been brought back to the main menu");
			}
		}
	}
	
	public void inputSettingsAndEncrypt(){		
		String input="";
		System.out.println("Input settings and text to encrypt as (permutation), (ring positions), (rotor positions), (plugboard settings), (text to encrypt). e.g. (A, 1, 2, 4), (1, 20, 3), (a ,b ,c), (A/M, B/D),(hello)");
		
		input = scanner.nextLine();
		input = input.replaceAll("\\s+","");
		input = input.replaceAll("\\(","");
		String[] settingsStringArray = input.split("\\),");
		
		if(settingsStringArray.length!=5){
			
			System.out.println("Invalid input");
			
		}
		else{
			
			String[] permutationArray = settingsStringArray[0].split(",");
			String[] ringPositions = settingsStringArray[1].split(",");
			String[] rotorPositions = settingsStringArray[2].split(",");
			
			if(permutationArray.length != 4){
				
				System.out.println("Permutation is incorrect");
				
			}
			else if(ringPositions.length != 3){
				
				System.out.println("The ring positions are incorrect");
				
			}
			else if(rotorPositions.length != 3){
				
				System.out.println("The rotor positions are incorrect");
				
			}
			else{
				try{
					
					permutation = "";
					
					int rotor = Integer.parseInt(permutationArray [3]);
					Rotor prevRotor = rotors[rotor-1];
					scrambler.setFirstRotor(prevRotor);
					
					prevRotor.setRingPosition(Integer.parseInt(ringPositions[2]));
					
					try{
							
							prevRotor.setPosition(Integer.parseInt(rotorPositions[2]));
					}
					catch(NumberFormatException e){
						
							prevRotor.setPosition(rotorPositions[2].charAt(0));
					}
						
					permutation= "" + rotor;
					
					for(int i=2; i>0;i--){
						
						rotor = Integer.parseInt(permutationArray [i]);
						prevRotor.setNextRotor(rotors[rotor-1]);
						prevRotor = rotors[rotor-1];
						prevRotor.setRingPosition(Integer.parseInt(ringPositions[i-1]));
						
						try{
							
							prevRotor.setPosition(Integer.parseInt(rotorPositions[i-1]));
						}
						catch(NumberFormatException e){
							prevRotor.setPosition(rotorPositions[i-1].charAt(0));
						}
						permutation=""+rotor+", "+permutation;
						
					}
						
					String reflectorString = permutationArray [0];
					permutation= reflectorString + ", "+ permutation;
						
					char reflectorChar = reflectorString.charAt(0);
						
					Reflector reflector = reflectors[(int)Character.toLowerCase(reflectorChar) - 97];
					prevRotor.setNextRotor(reflector);
					
					String[] plugBoardSettingsStringArray = settingsStringArray[3].split(",");
					ArrayList<String> swapSettings = new ArrayList<String>(Arrays.asList(plugBoardSettingsStringArray));
					System.out.println(swapSettings.get(0).equals(""));
					
					if(!(swapSettings.size()==1 && swapSettings.get(0).equals(""))){
						plugboard.setSwapMapping(swapSettings);
					}
					else{
						plugboard.resetMapping();
					}
					
					String plainText = settingsStringArray[4].replaceAll("\\)","");
					String encryptedString = "";
					
					boolean temp = plugboard.printerEnabled();
					plugboard.disablePrinter();
					
					for(int i=0; i<plainText.length(); i++){
						
						encryptedString= encryptedString + plugboard.encrypt(plainText.charAt(i));					
						if(((i+1)%5)==0){						
							encryptedString = encryptedString + " ";						
						}
						
					}
					
					if(temp){					
						plugboard.enablePrinter();
					}
					
					System.out.println("The encrypted text is:\n" + encryptedString);
				}
				catch(IllegalArgumentException e){
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	public void encrypt(){
		String input = "";
		
		while(!(input.equals("menu"))){
			
			System.out.println("1. Encrypt letter by letter");
			System.out.println("2. Encrypt entire String");
			System.out.println("type menu to return to the previous menu");
			
			input = scanner.nextLine();
			
			if(!(input.equals("menu"))){
				
				if(input.equals("1")){
					
					encryptLetterByLetter();
				}
				else if(input.equals("2")){
					
					encryptString();
					
				}
				else{
					
					System.out.println("Invalid input");
					
				}
				
			}
		}
	}
	
	public void encryptString(){
		Rotor rightRotor = (Rotor) scrambler.getFirstRotor();
		Rotor middleRotor = (Rotor) rightRotor.getNextRotor();
		Rotor leftRotor = (Rotor) middleRotor.getNextRotor();
		
		String input = "";		
		while(!(input.equals("menu"))){
			System.out.println("Input a piece of text to encrypt, type menu to go back to the main menu");
			System.out.println("Permutation is: "+permutation);
			System.out.println("Ring positions are: " + leftRotor.getRingPosition() + " " + middleRotor.getRingPosition() + " " + rightRotor.getRingPosition());
			System.out.println(""+leftRotor.getCharOnTop() + " " + middleRotor.getCharOnTop() + " " + rightRotor.getCharOnTop());
			
			if(scrambler.getCrackingMode()){
				
				System.out.println("Cracking mode is enabled");
				
			}
			try{
				input=scanner.nextLine();
				input = input.replaceAll("\\s+","");
				
				if(!(input.equals("menu"))){
					String encryptedString = "";
					
					boolean temp = plugboard.printerEnabled();
					plugboard.disablePrinter();
					
					for(int i=0; i<input.length(); i++){
						
						encryptedString= encryptedString + plugboard.encrypt(input.charAt(i));
						
						if(((i+1)%5)==0){
							
							encryptedString = encryptedString + " ";
							
						}
						
					}
					
					if(temp){
						
						plugboard.enablePrinter();
						
					}
					
					System.out.println("The encrypted text is:\n" + encryptedString);
				}
			}
			catch(IllegalArgumentException e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void encryptLetterByLetter(){
		Rotor rightRotor = (Rotor) scrambler.getFirstRotor();
		Rotor middleRotor = (Rotor) rightRotor.getNextRotor();
		Rotor leftRotor = (Rotor) middleRotor.getNextRotor();
		
		String input = "";
		while(!input.equals("menu")){
			System.out.println("Input a character to encrypt, type menu to go back to the main menu");
			System.out.println("Permutation is: "+permutation);
			System.out.println("Ring positions are: " + leftRotor.getRingPosition() + " " + middleRotor.getRingPosition() + " " + rightRotor.getRingPosition());
			System.out.println(""+leftRotor.getCharOnTop() + " " + middleRotor.getCharOnTop() + " " + rightRotor.getCharOnTop());
			
			if(scrambler.getCrackingMode()){
				
				System.out.println("Cracking mode is enabled");
				
			}
			
			try{
				input=scanner.nextLine();
				if(!(input.equals("menu"))){
					printer.addTab();
					char encryptedLetter = plugboard.encrypt(input.charAt(0));
					printer.removeTab();
					System.out.println("This encrypts as "+encryptedLetter);
				}
			}
			catch(IllegalArgumentException e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void editRotorSettingsMenu(){
		String input = "";
		
		while(!(input.equals("menu"))){
			
			System.out.println("Which rotor do you wish to edit.\n 1, 2, 3, 4 or 5\nType menu to return to the previous menu");
			input = scanner.nextLine();
			try{
				if(!(input.equals("menu"))){
					
					int rotor = Integer.parseInt(input);
					if(rotor<1 || rotor>5 ){
						System.out.println("the rotor number must bet between 1 and 5");
					}
					else{
						editRotorSettings(rotor);
					}
				}
			}
			catch(Exception e){
				System.out.println("you have to input a number");
			}
		}
	}
	
	public void editRotorSettings(int rotor){
		String input = "";
		while(!(input.equals("menu"))){
			System.out.println("Editing "+rotor);							
			System.out.println("1. view circuitry");
			System.out.println("2. edit circuitry");
			System.out.println("3. view position");
			System.out.println("4. change position");
			System.out.println("5. get ring position");
			System.out.println("6. set ring position");
			System.out.println("7. get turnover char");
			System.out.println("8. set turnover char");
			System.out.println("Type menu to return to rotor menu");
			
			try{
				input = scanner.nextLine();
				if(!(input.equals("menu"))){
					if(input.equals("1")){
						System.out.println("Circuitry is:\n"+"ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"+(new String(rotors[rotor-1].getCharCircuitry())));
					}
					else if(input.equals("2")){
						System.out.println("Input the circuit as a list, with the first entry been what A encrypts to, the second entry been what B encrypts to etc.\ne.g. if you wanted to map A to C & B to K, you'd put CK... followed by the rest of the mapping");
						input=scanner.nextLine();
						char[] circuit = input.toCharArray();
						rotors[rotor-1].setCircuitry(circuit);
					}
					else if(input.equals("3")){
						System.out.println("Position is:\n"+rotors[rotor-1].getPosition());
					}
					else if (input.equals("4")){
						System.out.println("Input the position as a character or an integer");
						input = scanner.nextLine();
						try{
							int position = Integer.parseInt(input);
							rotors[rotor-1].setPosition(position);
						}
						catch(Exception e){
							char position = input.charAt(0);
							rotors[rotor-1].setPosition(position);
						}
					}
					else if(input.equals("5")){
						System.out.println("Ring position is:\n" + rotors[rotor-1].getRingPosition());
					}
					else if(input.equals("6")){
						System.out.println("Input the ring position");
						input = scanner.nextLine();
						int ringPosition = Integer.parseInt(input);
						rotors[rotor-1].setRingPosition(ringPosition);
					}
					else if(input.equals("7")){
						System.out.println("The turnover char is:\n"+rotors[rotor-1].getTurnoverChar());
					}
					else if(input.equals("8")){
						System.out.println("Input the turnover char");
						input = scanner.nextLine();
						rotors[rotor-1].setTurnoverChar(input.charAt(0));
					}
					else{						
						System.out.println("Invalid input");
					}
				}
			}
			catch(Exception e){
				System.out.println("Invalid input");
			}
		}
		
	}
	
	public void editRotorPermutation(){
		String input="";
		while(!(input.equals("menu"))){
			System.out.println("1. view permutation");
			System.out.println("2. edit permutation");
			System.out.println("Type menu to return to the main menu");
			
			input = scanner.nextLine();
			if(input.equals("1")){
				
				System.out.println("The permutation is "+permutation);
			}
			else if(input.equals("2")){
				
				System.out.println("Input the permutation, e.g. input A, 4, 3, 5 if you want rotor 5 as the right rotor, 3 as the middle, 4 as the left and reflector A as the reflector");
				
				input = scanner.nextLine();
				input = input.replaceAll("\\s+","");
				String[] stringPositions = input.split(",");
				
				if(stringPositions.length !=4){
					
					System.out.println("Invalid permutation");					
				}
				else{
					String temp = permutation;
					
					try{
						int rotor = Integer.parseInt(stringPositions[3]);
						Rotor prevRotor = rotors[rotor-1];
						Rotor firstRotor = prevRotor;
						
						firstRotor.disableDoubleStep();
						permutation= "" + rotor;
						
						for(int i=2; i>0;i--){
							rotor = Integer.parseInt(stringPositions[i]);
							prevRotor.setNextRotor(rotors[rotor-1]);
							prevRotor = rotors[rotor-1];
							permutation=""+rotor+", "+permutation;
							prevRotor.disableDoubleStep();
						}
						
						String reflectorString = stringPositions[0];
						permutation= reflectorString + ", "+ permutation;
						
						char reflectorChar = reflectorString.charAt(0);
						
						Reflector reflector = reflectors[(int)Character.toLowerCase(reflectorChar) - 97];
						prevRotor.setNextRotor(reflector);
						
						((Rotor)firstRotor.getNextRotor()).enableDoubleStep();
						scrambler.setFirstRotor(firstRotor);
					}
					catch(Exception e){
						System.out.println("That permutation is invalid");
						permutation = temp;
					}
					
				}
			}
		}
	}
	
	public void editPlugboardSettings(){
		String input="";
		while(!(input.equals("menu"))){
			System.out.println("1. view plugboard settings");
			System.out.println("2. swap two letters");
			System.out.println("3. unswap two letters");
			System.out.println("4. reset plugboard");
			System.out.println("5. edit entire plugboard settings");
			input = scanner.nextLine();
			
			try{
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
				else if(input.equals("2")){
					
					System.out.println("Input first character");
					input = scanner.nextLine();
					
					char firstCharacter = input.charAt(0);
					
					System.out.println("Input second character");
					input = scanner.nextLine();
					
					char secondCharacter = input.charAt(0);
					
					plugboard.swap(firstCharacter, secondCharacter);
				}
				else if(input.equals("3")){
					
					System.out.println("Input first character to unswap");
					input = scanner.nextLine();
					
					char firstCharacter = input.charAt(0);
					
					System.out.println("Input second character to unswap");
					input = scanner.nextLine();
					
					char secondCharacter = input.charAt(0);
					
					plugboard.unswap(firstCharacter, secondCharacter);
				}
				else if(input.equals("4")){
					
					plugboard.resetMapping();
				}
				else if(input.equals("5")){
					System.out.println("Input the swapping as a list where each \"swap\" is the first letter/the second letter e.g. inputting A/C, F/T would swap A with C and F with T.");
					
					input = scanner.nextLine();
					input = input.replaceAll("\\s+","");
					
					String[] settingsStringArray = input.split(",");
					ArrayList<String> swapSettings = new ArrayList<String>(Arrays.asList(settingsStringArray));
					
					plugboard.setSwapMapping(swapSettings);
				}
				else{
					System.out.println("Invalid input");
				}
			}
			catch(IllegalArgumentException e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void additionalOptions(){
		
		String input ="";
		while(!(input.equals("menu"))){
			System.out.println("1. enable/disable additional encrypting information");
			System.out.println("2. enable/disable cracking mode");
			System.out.println("type menu to return to the main menu");
			
			input = scanner.nextLine();
			if(!(input.equals("menu"))){
				if(input.equals("1")){
					
					additionalEncyptingInfoMenu();
					
				}
				else if(input.equals("2")){
					
					crackingModeMenu();
					
				}
			}
		}
		
	}
	
	public void additionalEncyptingInfoMenu(){
		if(plugboard.printerEnabled()){
			System.out.println("Additional encrypting information is enabled");
		}
		else{
			
			System.out.println("Additional encrypting information is disabled");
			
		}
		
		System.out.println("1. enable additional encrypting information");
		System.out.println("2. disable additional encrypting information");
		System.out.println("type menu to return to the previous menu");
		
		String input=scanner.nextLine();
		if(input.equals("1")){
			
			plugboard.enablePrinter();
			System.out.println("Additional encrypting information is enabled");
			
		}
		else if(input.equals("2")){
			
			plugboard.disablePrinter();
			System.out.println("Additional encrypting information is disabled");
			
		}
		else if(!(input.equals("menu"))){
			
			System.out.println("invalid input");
			
		}
		
	}
	
	public void crackingModeMenu(){
		if(scrambler.getCrackingMode()){
			
			System.out.println("Cracking mode is currently enabled");
			
		}
		else{
			
			System.out.println("Cracking mode is currently disabled");
			
		}
		
		System.out.println("1. enable cracking mode");
		System.out.println("2. disable cracking mode");
		System.out.println("type menu to return to the previous menu");
		
		String input = scanner.nextLine();
		if(input.equals("1")){
			
			scrambler.setCrackingMode(true);
			System.out.println("cracking mode enabled");
			
		}
		else if(input.equals("2")){
			
			scrambler.setCrackingMode(false);
			System.out.println("cracking mode disabled");
			
		}
		else if(!(input.equals("menu"))){
			
			System.out.println("invalid input");
			
		}
		
	}
	
	public static void main(String args[]){
		EnigmaMachine enigmaMachine = new EnigmaMachine();
		enigmaMachine.Run();
	}
}