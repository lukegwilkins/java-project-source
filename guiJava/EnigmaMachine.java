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
	
	//the run method
	public void Run(){
		//stores the input
		String input ="";
		//if the user inputs "quit" the program halts
		while(!(input.equals("quit"))){
			//outputs the menu
			System.out.println("1. To encrypt");
			System.out.println("2. To edit rotors settings");
			System.out.println("3. Edit rotor permutation");
			System.out.println("4. Edit Plugboard settings");
			System.out.println("5. Input settings and encrypt in one go");
			System.out.println("6. Additional options");
			System.out.println("7. Generate test data from files");
			System.out.println("Type quit to quit");
			
			//try catch to capture errors
			try{
				//gets the next line of input
				input = scanner.nextLine();
				if(!(input.equals("quit"))){
					
					//if it is 1 then we go to the encrypt function
					if(input.equals("1")){
					
						encrypt();
					
					}
					
					//if 2 the rotor settings menu
					else if(input.equals("2")){
					
						editRotorSettingsMenu();
					
					}
					
					//if 3 the rotor permutation
					else if(input.equals("3")){
					
						editRotorPermutation();
					
					}
					
					//if 4 edit the plugboard settings
					else if(input.equals("4")){
					
						editPlugboardSettings();
					
					}
					
					//if 5 then the user can input all the settings and the text to encrypt in one go
					else if(input.equals("5")){
						
						inputSettingsAndEncrypt();
					}
					
					//if 6 then the user can change the additional options
					else if(input.equals("6")){
						
						additionalOptions();
						
					}
					else if(input.equals("7")){
						generateTestData();
					}
					//else an error message is outputted
					else{
						
						System.out.println("That is not a valid input");
					}
				}
			}
			//if an error occurs which couldn't be handle by another function then we default back to the main menu
			catch(Exception e){
				//System.out.println(e.getMessage());
				System.out.println("An error occurred and you have been brought back to the main menu");
			}
		}
	}
	
	//the function to allow a user to input all the settings and text in one go
	public void inputSettingsAndEncrypt(){
		//string to store the input
		String input="";
		//requests the user for input
		System.out.println("Input settings and text to encrypt as (permutation), (ring positions), (rotor positions), (plugboard settings), (text to encrypt). e.g. (A, 1, 2, 4), (1, 20, 3), (a ,b ,c), (A/M, B/D),(hello)");
		
		//gets the input
		input = scanner.nextLine();
		//removes all the whitespace in the input
		input = input.replaceAll("\\s+","");
		//removes all the left brackets
		input = input.replaceAll("\\(","");
		//splits the string via "),"
		String[] settingsStringArray = input.split("\\),");
		
		//if not all the options have been inputted an error message is outputted
		if(settingsStringArray.length!=5){
			
			System.out.println("Invalid input");
			
		}
		else{
			
			//splits each of the settings up into arrays
			String[] permutationArray = settingsStringArray[0].split(",");
			String[] ringPositions = settingsStringArray[1].split(",");
			String[] rotorPositions = settingsStringArray[2].split(",");
			
			//if the permutation is incorrect an error is outputted
			if(permutationArray.length != 4){
				
				System.out.println("Permutation is incorrect");
				
			}
			//if ring settinsg are incorrect an error is outputted
			else if(ringPositions.length != 3){
				
				System.out.println("The ring positions are incorrect");
				
			}
			//if rotor settings are incorrect an error is outputted
			else if(rotorPositions.length != 3){
				
				System.out.println("The rotor positions are incorrect");
				
			}
			else{
				//trys to encrypt the message using the current settigns
				try{					
					permutation = "";
					
					//gets which rotor is on the right
					int rotor = Integer.parseInt(permutationArray [3]);
					//sets the scrambler to have the first rotor as the rotor on the right
					Rotor prevRotor = rotors[rotor-1];
					scrambler.setFirstRotor(prevRotor);
					//sets the right rotor's ring setting
					prevRotor.setRingPosition(Integer.parseInt(ringPositions[2]));
					
					//the position can be given as an integer or a character
					try{
							//try to set the position with as an integer
							prevRotor.setPosition(Integer.parseInt(rotorPositions[2]));
					}
					//if a number format error occurs set the position as a character
					catch(NumberFormatException e){
						
							prevRotor.setPosition(rotorPositions[2].charAt(0));
					}
					
					//stores the which rotor is on the right in permutation
					permutation= "" + rotor;
					
					//we repeat this process for the middle and left rotors
					for(int i=2; i>0;i--){
						
						rotor = Integer.parseInt(permutationArray [i]);
						//set it so the previous rotor has the current rotor as the next rotor
						prevRotor.setNextRotor(rotors[rotor-1]);
						prevRotor = rotors[rotor-1];
						prevRotor.setRingPosition(Integer.parseInt(ringPositions[i-1]));
						
						try{
							
							prevRotor.setPosition(Integer.parseInt(rotorPositions[i-1]));
						}
						catch(NumberFormatException e){
							prevRotor.setPosition(rotorPositions[i-1].charAt(0));
						}
						//adds which rotor is been used to the permutation
						permutation=""+rotor+", "+permutation;
						
					}
					
					//gets which reflector is been used
					String reflectorString = permutationArray [0];
					//stores the in permuation
					permutation= reflectorString + ", "+ permutation;
						
					char reflectorChar = reflectorString.charAt(0);
						
					Reflector reflector = reflectors[(int)Character.toLowerCase(reflectorChar) - 97];
					//sets it so that the left rotor has the reflector as its next rotor
					prevRotor.setNextRotor(reflector);
					
					//we get the plugboard settings and split it by "," into an array
					String[] plugBoardSettingsStringArray = settingsStringArray[3].split(",");
					//we convert the array to an arrayList
					ArrayList<String> swapSettings = new ArrayList<String>(Arrays.asList(plugBoardSettingsStringArray));
					//System.out.println(swapSettings.get(0).equals(""));
					
					//if there are swaps then we get the plugboard to set up the swap mapping using swapSettings
					if(!(swapSettings.size()==1 && swapSettings.get(0).equals(""))){
						plugboard.setSwapMapping(swapSettings);
					}
					//if there are no swaps we reset the mapping
					else{
						plugboard.resetMapping();
					}
					
					//we get the plaintext and remove the bracket
					String plainText = settingsStringArray[4].replaceAll("\\)","");
					//string to store the encrypted text
					String encryptedString = "";
					
					//we store whether the printer is enabled or disabled
					boolean temp = plugboard.printerEnabled();
					//then disable it
					plugboard.disablePrinter();
					
					//we encryupt each letter in the plaintext add it to the encrypted string
					for(int i=0; i<plainText.length(); i++){
						encryptedString= encryptedString + plugboard.encrypt(plainText.charAt(i));
						//if we have encrypted 5 letters then we add a space to make the output easy  to read
						if(((i+1)%5)==0){						
							encryptedString = encryptedString + " ";						
						}
						
					}
					
					//if the printer was originally enabled then we renabled it
					if(temp){					
						plugboard.enablePrinter();
					}
					
					//we then output the encrypted text
					System.out.println("The encrypted text is:\n" + encryptedString);
				}
				catch(IllegalArgumentException e){
					//if an error occurs we output it 
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	//encrypt menu function
	public void encrypt(){
		String input = "";
		//if the input equals "menu" then we return to the main menu
		while(!(input.equals("menu"))){
			//outputs the options
			System.out.println("1. Encrypt letter by letter");
			System.out.println("2. Encrypt entire String");
			System.out.println("type menu to return to the previous menu");
			
			//gets the next line of input
			input = scanner.nextLine();
			
			if(!(input.equals("menu"))){
				//if the input equals "1" then we encrypt letter by letter
				if(input.equals("1")){
					
					encryptLetterByLetter();
				}
				//else we encrypt the entire string
				else if(input.equals("2")){
					
					encryptString();
					
				}
				//if the input is invalid we output an error message
				else{
					
					System.out.println("Invalid input");
					
				}
				
			}
		}
	}
	
	//method to encrypt an entire string
	public void encryptString(){
		//gets each of the rotors in the scrambler
		Rotor rightRotor = (Rotor) scrambler.getFirstRotor();
		Rotor middleRotor = (Rotor) rightRotor.getNextRotor();
		Rotor leftRotor = (Rotor) middleRotor.getNextRotor();
		
		String input = "";		
		while(!(input.equals("menu"))){
			//prompts the user for input
			System.out.println("Input a piece of text to encrypt, type menu to go back to the main menu");
			//outputs the current permutation to the user
			System.out.println("Permutation is: "+permutation);
			//outputs the ring positions 
			System.out.println("Ring positions are: " + leftRotor.getRingPosition() + " " + middleRotor.getRingPosition() + " " + rightRotor.getRingPosition());
			//outputs the current character positions
			System.out.println(""+leftRotor.getCharOnTop() + " " + middleRotor.getCharOnTop() + " " + rightRotor.getCharOnTop());
			
			//if crackingmode is enabled we output that
			if(scrambler.getCrackingMode()){
				
				System.out.println("Cracking mode is enabled");
				
			}
			//try to encypt the inputted string
			try{
				//get the next line
				input=scanner.nextLine();
				//remove spaces
				input = input.replaceAll("\\s+","");
				
				//if the input isn't "menu" we encrypt the string
				if(!(input.equals("menu"))){
					String encryptedString = "";
					
					//temporarily disable the printer for the plugboard
					boolean temp = plugboard.printerEnabled();
					plugboard.disablePrinter();
					
					//encrypt each letter in the string
					for(int i=0; i<input.length(); i++){
						
						encryptedString= encryptedString + plugboard.encrypt(input.charAt(i));
						
						if(((i+1)%5)==0){
							
							encryptedString = encryptedString + " ";
							
						}
						
					}
					
					if(temp){
						
						plugboard.enablePrinter();
						
					}
					//output the encrypted string
					System.out.println("The encrypted text is:\n" + encryptedString);
				}
			}
			catch(IllegalArgumentException e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	//function for encrypting letter by letter
	public void encryptLetterByLetter(){
		//gets the rotors in the scrambler
		Rotor rightRotor = (Rotor) scrambler.getFirstRotor();
		Rotor middleRotor = (Rotor) rightRotor.getNextRotor();
		Rotor leftRotor = (Rotor) middleRotor.getNextRotor();
		
		String input = "";
		while(!input.equals("menu")){
			//outputs the current rotor settings and prompts the user for input
			System.out.println("Input a character to encrypt, type menu to go back to the main menu");
			System.out.println("Permutation is: "+permutation);
			System.out.println("Ring positions are: " + leftRotor.getRingPosition() + " " + middleRotor.getRingPosition() + " " + rightRotor.getRingPosition());
			System.out.println(""+leftRotor.getCharOnTop() + " " + middleRotor.getCharOnTop() + " " + rightRotor.getCharOnTop());
			
			if(scrambler.getCrackingMode()){
				
				System.out.println("Cracking mode is enabled");
				
			}
			
			try{
				//gets the nextline of input
				input=scanner.nextLine();
				if(!(input.equals("menu"))){
					//adds a tab to the printer
					printer.addTab();
					//encrypts the inputted letter
					char encryptedLetter = plugboard.encrypt(input.charAt(0));
					//removes the tabe
					printer.removeTab();
					//outputs the encrypted letter
					System.out.println("This encrypts as "+encryptedLetter);
				}
			}
			catch(IllegalArgumentException e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	//method that is called if the user chooses to edit the rotor settings
	public void editRotorSettingsMenu(){
		String input = "";
		
		//if menu is inputted then the program returns to the previous menu
		while(!(input.equals("menu"))){
			//output the numbers for each rotor
			System.out.println("Which rotor do you wish to edit.\n 1, 2, 3, 4 or 5\nType menu to return to the previous menu");
			//get input
			input = scanner.nextLine();
			try{
				if(!(input.equals("menu"))){
					//if the number isn't between 1 and 5 we output an error message
					int rotor = Integer.parseInt(input);
					if(rotor<1 || rotor>5 ){
						System.out.println("the rotor number must bet between 1 and 5");
					}
					else{
						//we call the method that is used to edit the rotor settings for the inputted rotor
						editRotorSettings(rotor);
					}
				}
			}
			//if a number isn't inputted then an error message is outputted
			catch(Exception e){
				System.out.println("you have to input a number");
			}
		}
	}
	
	//method that is used to edit a rotor's settings
	public void editRotorSettings(int rotor){
		String input = "";
		//if menu is inputted the program returns to the previous menu
		while(!(input.equals("menu"))){
			
			//outputs the options menu for editing a rotor
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
				//gets the input
				input = scanner.nextLine();
				if(!(input.equals("menu"))){
					//if 1 is inputted then the program displays the rotors circuitry
					if(input.equals("1")){
						System.out.println("Circuitry is:\n"+"ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"+(new String(rotors[rotor-1].getCharCircuitry())));
					}
					
					//if 2 is inputted then the user inputs the new circuitry and the program sets the rotors circuitry to the inputted circuitry
					else if(input.equals("2")){
						//gets the circuitry as a string then converts it to an array
						System.out.println("Input the circuit as a list, with the first entry been what A encrypts to, the second entry been what B encrypts to etc.\ne.g. if you wanted to map A to C & B to K, you'd put CK... followed by the rest of the mapping");
						input=scanner.nextLine();
						char[] circuit = input.toCharArray();
						rotors[rotor-1].setCircuitry(circuit);
					}
					
					//if 3 is pressed then the rotor's position is outputted
					else if(input.equals("3")){
						System.out.println("Position is:\n"+rotors[rotor-1].getPosition());
					}
					
					//if 4 is pressed then the user is prompted for the rotor's new position
					else if (input.equals("4")){
						System.out.println("Input the position as a character or an integer");
						//gets input
						input = scanner.nextLine();
						
						//attempts to set the position as an integer
						try{
							int position = Integer.parseInt(input);
							rotors[rotor-1].setPosition(position);
						}
						//else it sets the position using the first char in the string
						catch(Exception e){
							char position = input.charAt(0);
							rotors[rotor-1].setPosition(position);
						}
					}
					
					//if the user inputs 5 then the rotor's ring position is displayed
					else if(input.equals("5")){
						System.out.println("Ring position is:\n" + rotors[rotor-1].getRingPosition());
					}
					
					//if the user inputs 6 then the user is prompted for the new ring position
					else if(input.equals("6")){
						
						System.out.println("Input the ring position");
						input = scanner.nextLine();
						int ringPosition = Integer.parseInt(input);
						rotors[rotor-1].setRingPosition(ringPosition);
					}
					
					//if the user inputs 7 then the user then the turnover char for the rotor is displayed
					else if(input.equals("7")){
						System.out.println("The turnover char is:\n"+rotors[rotor-1].getTurnoverChar());
					}
					
					//if the user inputs 8 then the user inputs the new turnover char for the rotor
					else if(input.equals("8")){
						
						System.out.println("Input the turnover char");
						input = scanner.nextLine();
						rotors[rotor-1].setTurnoverChar(input.charAt(0));
					}
					
					//else an error message is outputted
					else{						
						System.out.println("Invalid input");
					}
				}
			}
			
			//if any setting of the rotors settings throws an error than an error message is outputted 
			catch(Exception e){
				System.out.println("Invalid input");
			}
		}		
	}
	
	//method that is called when the user wants to edit the rotor permuations
	public void editRotorPermutation(){
		String input="";
		while(!(input.equals("menu"))){
			//outputs the options
			System.out.println("1. view permutation");
			System.out.println("2. edit permutation");
			System.out.println("Type menu to return to the main menu");
			
			//gets the users input
			input = scanner.nextLine();
			
			//if 1 is inputted then the rotors permutation is outputted
			if(input.equals("1")){
				
				System.out.println("The permutation is "+permutation);
			}
			
			//if 2 is inputted then the user inputs the new rotor permutation
			else if(input.equals("2")){
				
				System.out.println("Input the permutation, e.g. input A, 4, 3, 5 if you want rotor 5 as the right rotor, 3 as the middle, 4 as the left and reflector A as the reflector");
				
				//gets the input
				input = scanner.nextLine();
				//removes whitespace from the input
				input = input.replaceAll("\\s+","");
				
				//splits the input by commas
				String[] stringPositions = input.split(",");
				
				//checks that a reflector and 3 rotors were inputted in the permuation
				if(stringPositions.length !=4){
					
					System.out.println("Invalid permutation");					
				}
				else{
					
					String temp = permutation;
					
					try{
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
					//if an error occurs an error message is output
					catch(Exception e){
						System.out.println("That permutation is invalid");
						permutation = temp;
					}
					
				}
			}
		}
	}
	
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
	
	//method for additional options
	public void additionalOptions(){
		
		String input ="";
		while(!(input.equals("menu"))){
			
			//outputs the menu
			System.out.println("1. enable/disable additional encrypting information");
			System.out.println("2. enable/disable cracking mode");
			System.out.println("type menu to return to the main menu");
			
			//gets the input
			input = scanner.nextLine();
			if(!(input.equals("menu"))){
				
				//if 1 is inputted then the additional encrypyting info menu is called
				if(input.equals("1")){
					
					additionalEncryptingInfoMenu();
					
				}
				//if 2 is inputted then the cracking mode menu is called
				else if(input.equals("2")){
					
					crackingModeMenu();
					
				}
			}
		}
		
	}
	
	//method for the additional encrypting  info menu
	public void additionalEncryptingInfoMenu(){
		
		//outputs whether additional encrypting info is enabled or not
		if(plugboard.printerEnabled()){
			System.out.println("Additional encrypting information is enabled");
		}
		else{
			
			System.out.println("Additional encrypting information is disabled");
			
		}
		
		//outputs the menu options
		System.out.println("1. enable additional encrypting information");
		System.out.println("2. disable additional encrypting information");
		System.out.println("type menu to return to the previous menu");
		
		//gets the next input line
		String input=scanner.nextLine();
		
		//if 1 is inputted then the printer is enabled for the plugboard
		if(input.equals("1")){
			
			plugboard.enablePrinter();
			System.out.println("Additional encrypting information is enabled");
			
		}
		
		//if 2 is inputted then the printer is disabled
		else if(input.equals("2")){
			
			plugboard.disablePrinter();
			System.out.println("Additional encrypting information is disabled");
			
		}
		else if(!(input.equals("menu"))){
			
			System.out.println("invalid input");
			
		}
		
	}
	
	//method for the cracking mode menu
	public void crackingModeMenu(){
		
		//outputs whether the cracking mode is enabled or not
		if(scrambler.getCrackingMode()){
			
			System.out.println("Cracking mode is currently enabled");
			
		}
		else{
			
			System.out.println("Cracking mode is currently disabled");
			
		}
		
		//outputs the options menu
		System.out.println("1. enable cracking mode");
		System.out.println("2. disable cracking mode");
		System.out.println("type menu to return to the previous menu");
		
		//gets the user input
		String input = scanner.nextLine();
		
		//if 1 is inputted then the cracking mode is enabled 
		if(input.equals("1")){
			
			scrambler.setCrackingMode(true);
			System.out.println("cracking mode enabled");
			
		}
		
		//if 2 is inputted then the cracking mode is disabled
		else if(input.equals("2")){
			
			scrambler.setCrackingMode(false);
			System.out.println("cracking mode disabled");
			
		}
		else if(!(input.equals("menu"))){
			
			System.out.println("invalid input");
			
		}
		
	}
	
	//method used to generate test data for the bombe using a file for key input and a file for plaintext input
	public void generateTestData(){
		System.out.println("Input plaintext file");
		String input = scanner.nextLine();
		
		try{
			//get plaintext
			String contents = new Scanner(new File(input)).useDelimiter("\\Z").next();
			contents=contents.toLowerCase();
			//replace '?' with spaces
			contents=contents.replace('?',' ');
			contents=contents.replaceAll("\\s|,|\\.|\"|-|\\d|","");
			
			//System.out.println(contents);
			
			
			
			String[] messageStrings = contents.split("#");
			ArrayList<String> messages = new ArrayList<String>(Arrays.asList(messageStrings));
			//System.out.println(messages);
			System.out.println("Input keys file");
			input = scanner.nextLine();
			
			//get enigma keys
			contents = new Scanner(new File(input)).useDelimiter("\\Z").next();
			String[] keyStrings = contents.split("\n");
			ArrayList<String> keys = new ArrayList<String>(Arrays.asList(keyStrings));
			//System.out.println(keys);
			
			Random rand = new Random();
			PrintWriter printer = new PrintWriter("output.txt","UTF-8");
			for(String key:keys){
				int messageIndex = rand.nextInt(messages.size());
				String outputString=key.replaceAll("\\s","");
				outputString=outputString + ", (" + messages.get(messageIndex)+"), (" + testingEncryption(key+", ("+messages.get(messageIndex)+")") +")";
				System.out.println(outputString);				
				//System.out.println(outputString+", (");
				//System.out.println();
				
				printer.println(outputString);
				//printer.println(outputString+", (");
				//printer.println();
			}
			printer.close();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println("Error reading in file");
		}
	}
	
	//method used to encrypt string for testing with the bombe
	//main method that calls the run method
	public String testingEncryption(String settingsAndMessage){
		//removes all the whitespace in the input
		settingsAndMessage = settingsAndMessage.replaceAll("\\s+","");
		//removes all the left brackets
		settingsAndMessage = settingsAndMessage.replaceAll("\\(","");
		//splits the string via "),"
		String[] settingsStringArray = settingsAndMessage.split("\\),");
		String encryptedString="";
		//if not all the options have been inputted an error message is outputted
		if(settingsStringArray.length!=5){
			
			System.out.println("Invalid input");
			
		}
		else{
			
			//splits each of the settings up into arrays
			String[] permutationArray = settingsStringArray[0].split(",");
			String[] ringPositions = settingsStringArray[1].split(",");
			String[] rotorPositions = settingsStringArray[2].split(",");
			
			//if the permutation is incorrect an error is outputted
			if(permutationArray.length != 4){
				
				System.out.println("Permutation is incorrect");
				
			}
			//if ring settinsg are incorrect an error is outputted
			else if(ringPositions.length != 3){
				
				System.out.println("The ring positions are incorrect");
				
			}
			//if rotor settings are incorrect an error is outputted
			else if(rotorPositions.length != 3){
				
				System.out.println("The rotor positions are incorrect");
				
			}
			else{
				//trys to encrypt the message using the current settigns
				try{					
					permutation = "";
					
					//gets which rotor is on the right
					int rotor = Integer.parseInt(permutationArray [3]);
					//sets the scrambler to have the first rotor as the rotor on the right
					Rotor prevRotor = rotors[rotor-1];
					scrambler.setFirstRotor(prevRotor);
					//sets the right rotor's ring setting
					prevRotor.setRingPosition(Integer.parseInt(ringPositions[2]));
					
					//the position can be given as an integer or a character
					try{
							//try to set the position with as an integer
							prevRotor.setPosition(Integer.parseInt(rotorPositions[2]));
					}
					//if a number format error occurs set the position as a character
					catch(NumberFormatException e){
						
							prevRotor.setPosition(rotorPositions[2].charAt(0));
					}
					
					//stores the which rotor is on the right in permutation
					permutation= "" + rotor;
					
					//we repeat this process for the middle and left rotors
					for(int i=2; i>0;i--){
						
						rotor = Integer.parseInt(permutationArray [i]);
						//set it so the previous rotor has the current rotor as the next rotor
						prevRotor.setNextRotor(rotors[rotor-1]);
						prevRotor = rotors[rotor-1];
						prevRotor.setRingPosition(Integer.parseInt(ringPositions[i-1]));
						
						try{
							
							prevRotor.setPosition(Integer.parseInt(rotorPositions[i-1]));
						}
						catch(NumberFormatException e){
							prevRotor.setPosition(rotorPositions[i-1].charAt(0));
						}
						//adds which rotor is been used to the permutation
						permutation=""+rotor+", "+permutation;
						
					}
					
					//gets which reflector is been used
					String reflectorString = permutationArray [0];
					//stores the in permuation
					permutation= reflectorString + ", "+ permutation;
						
					char reflectorChar = reflectorString.charAt(0);
						
					Reflector reflector = reflectors[(int)Character.toLowerCase(reflectorChar) - 97];
					//sets it so that the left rotor has the reflector as its next rotor
					prevRotor.setNextRotor(reflector);
					
					//we get the plugboard settings and split it by "," into an array
					String[] plugBoardSettingsStringArray = settingsStringArray[3].split(",");
					//we convert the array to an arrayList
					ArrayList<String> swapSettings = new ArrayList<String>(Arrays.asList(plugBoardSettingsStringArray));
					//System.out.println(swapSettings.get(0).equals(""));
					
					//if there are swaps then we get the plugboard to set up the swap mapping using swapSettings
					if(!(swapSettings.size()==1 && swapSettings.get(0).equals(""))){
						plugboard.setSwapMapping(swapSettings);
					}
					//if there are no swaps we reset the mapping
					else{
						plugboard.resetMapping();
					}
					
					//we get the plaintext and remove the bracket
					String plainText = settingsStringArray[4].replaceAll("\\)","");
					
					//we store whether the printer is enabled or disabled
					boolean temp = plugboard.printerEnabled();
					//then disable it
					plugboard.disablePrinter();
					
					//we encrypt each letter in the plaintext add it to the encrypted string
					Rotor middleRotor = (Rotor)((Rotor) scrambler.getFirstRotor()).getNextRotor();
					char prevCharOnTop=middleRotor.getCharOnTop();
					ArrayList<String> cribs= new ArrayList<String>();
					String currentCrib="1, ";
					
					for(int i=0; i<plainText.length(); i++){
						encryptedString= encryptedString + plugboard.encrypt(plainText.charAt(i));
						if(prevCharOnTop!=middleRotor.getCharOnTop()){
							cribs.add(currentCrib);
							currentCrib=""+(i+2)+", ";
							prevCharOnTop=middleRotor.getCharOnTop();
						}
						else{
							currentCrib=currentCrib+plainText.charAt(i);
						}
						
						//if we have encrypted 5 letters then we add a space to make the output easy  to read
						/*if(((i+1)%5)==0){						
							encryptedString = encryptedString + " ";						
						}*/
						
					}
					Random rand = new Random();
					int cribIndex = rand.nextInt(cribs.size());
					while(cribs.get(cribIndex).length()<8){
						cribIndex = rand.nextInt(cribs.size());
					}
					
					//System.out.println(cribs.get(cribIndex));
					encryptedString = encryptedString +", "+ cribs.get(cribIndex);
					//if the printer was originally enabled then we renabled it
					if(temp){					
						plugboard.enablePrinter();
					}
					
					//we then output the encrypted text
					//System.out.println("The encrypted text is:\n" + encryptedString);
					
				}
				catch(IllegalArgumentException e){
					//if an error occurs we output it 
					System.out.println(e.getMessage());
				}
			}
		}
		
		return encryptedString;
	}
	public static void main(String args[]){
		EnigmaMachine enigmaMachine = new EnigmaMachine();
		enigmaMachine.Run();
	}
}