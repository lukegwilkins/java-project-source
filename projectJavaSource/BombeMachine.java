import enigmaComponents.*;
import java.util.*;
import java.lang.Character;

public class BombeMachine{
	//variables for use in the BombeMachine class
	private Scrambler scrambler;
	private Rotor[] rotors;
	private Reflector[] reflectors;
	private Printer printer;
	private String crib;
	private String cipherText;
	private String permutation;
	private HashMap<Character,HashMap<Character,ArrayList<Integer>>> menu;
	private int cribPosition;
	private Scanner scanner;
	private ArrayList<Character> useableReflectors;
	
	//constructor for the BombeMachine
	public BombeMachine(){
		//printer used for the enigma components
		printer = new Printer();
		
		//array to contain each of the rotors
		rotors = new Rotor[5];
		
		//circuits for each of the rotors that can be used
		char[][] rotorCircuits = {
			{'E','K','M','F','L','G','D','Q','V','Z','N','T','O','W','Y','H','X','U','S','P','A','I','B','R','C','J'},
			{'A','J','D','K','S','I','R','U','X','B','L','H','W','T','M','C','Q','G','Z','N','P','Y','F','V','O','E'},
			{'B','D','F','H','J','L','C','P','R','T','X','V','Z','N','Y','E','I','W','G','A','K','M','U','S','Q','O'},
			{'E','S','O','V','P','Z','J','A','Y','Q','U','I','R','H','X','L','N','F','T','G','K','D','C','M','W','B'},
			{'V','Z','B','R','G','I','T','Y','U','P','S','D','N','H','L','X','A','W','M','J','Q','O','F','E','C','K'}
		};
		
		//turnover characters for each of the rotors
		char[] turnoverChars={'Q', 'E', 'V', 'J', 'Z'};
		
		//sets up each of the rotors
		for(int i=0;i<5;i++){
			rotors[i]= new Rotor();
			rotors[i].setCircuitry(rotorCircuits[i]);
			rotors[i].setTurnoverChar(turnoverChars[i]);
			rotors[i].setPrinter(printer);
			rotors[i].disablePrinter();
		}
		
		//array to contain each of the reflectors
		reflectors = new NonRotReflector[3];
		
		//circuits for each of the reflectors
		char[][] reflectorCircuits = {
			{'E','J','M','Z','A','L','Y','X','V','B','W','F','C','R','Q','U','O','N','T','S','P','I','K','H','G','D'},
			{'Y','R','U','H','Q','S','L','D','P','X','N','G','O','K','M','I','E','B','F','Z','C','W','V','J','A','T'},
			{'F','V','P','J','I','A','O','Y','E','D','R','Z','X','W','G','C','T','K','U','Q','S','B','N','M','H','L'}			
		};
		
		//sets up each of the reflectors 
		for(int i=0; i<3;i++){
			reflectors[i] = new NonRotReflector();
			reflectors[i].setCircuitry(reflectorCircuits[i]);
			reflectors[i].setPrinter(printer);
			reflectors[i].disablePrinter();
		}
		
		//gives the scrambler an initial setting of A,1,2,3
		rotors[2].setNextRotor(rotors[1]);
		rotors[1].setNextRotor(rotors[0]);
		rotors[0].setNextRotor(reflectors[0]);
		
		//sets up a scrambler for use in the BombeMachine and disables the printer
		scrambler = new Scrambler();
		scrambler.setFirstRotor(rotors[2]);
		scrambler.disablePrinter();
		
		//sets up a new menu
		menu = new HashMap<Character,HashMap<Character,ArrayList<Integer>>>();
		
		//stores the initial permutation
		permutation="A, 1, 2, 3";
		
		//sets up a new scanner
		scanner= new Scanner(System.in);
		
		//stores which reflectors may be used for cracking
		useableReflectors= new ArrayList<Character>();
		useableReflectors.add('a');
		useableReflectors.add('b');
		useableReflectors.add('c');
	}
	
	//method to get the crib
	public String getCrib(){
		return crib;
	}
	
	//method to set the crib
	public void setCrib(String crib){
		this.crib = crib.replaceAll("\\s+","");
	}
	
	//method to get the ciphertext
	public String getCipherText(){
		return cipherText;
	}
	
	//method to set the ciphertext
	public void setCipherText(String cipherText){
		this.cipherText = cipherText.replaceAll("\\s+","");;
	}
	
	//method to get the menu
	public HashMap<Character,HashMap<Character,ArrayList<Integer>>> getMenu(){
		return menu;
	}
	
	//method used to change the rotor permutation
	private void changeRotorPermutation(String rotorPermutation){
		//splits the string into an array
		String[] stringPositions=rotorPermutation.split("");
		
		//gets the right rotor in the permutation
		int rotor = Integer.parseInt(stringPositions[stringPositions.length-1]);
		Rotor prevRotor = rotors[rotor-1];
		Rotor firstRotor = prevRotor;
		
		//disables the doublestep for the right rotor
		firstRotor.disableDoubleStep();
		permutation= "" + rotor;
		
		//sets up the middle and left rotors
		for(int i=2; i>0;i--){
			rotor = Integer.parseInt(stringPositions[i]);
			prevRotor.setNextRotor(rotors[rotor-1]);
			prevRotor = rotors[rotor-1];
			permutation=""+rotor+", "+permutation;
			prevRotor.disableDoubleStep();
		}
		
		//gets which reflector was used
		String reflectorString = stringPositions[0];
		permutation= reflectorString + ", "+ permutation;
		char reflectorChar = reflectorString.charAt(0);
		
		//sets up the reflector for use
		Reflector reflector = reflectors[(int)Character.toLowerCase(reflectorChar) - 97];
		prevRotor.setNextRotor(reflector);
		
		//((Rotor)firstRotor.getNextRotor()).enableDoubleStep();
		
		//sets up the scrambler
		scrambler.setFirstRotor(firstRotor);
	}
	
	//method used to generate all permutations of a character list, where you choose a given amount from the list
	public ArrayList<ArrayList<Character>> permutations(ArrayList<Character> charList, int amount){
		
		//returnlist used to store all the permutations
		ArrayList<ArrayList<Character>> returnList = new ArrayList<ArrayList<Character>>();
		
		//if the amount is greater than the size of the character list then the amount is set to the size of the list
		if(amount>charList.size()){
			amount=charList.size();
		}
		
		//if the character list size is less than 1 then we add the character list to the returnlist and return it
		if(charList.size()<=1){
			returnList.add(charList);
			return returnList;
		}
		//if the amount is 1 or less then for each character we create a list with just that character in the list
		//and add each list to our list of permutations
		else if(amount<=1){
			for(Character letter: charList){
				ArrayList<Character> temp = new ArrayList<Character>();
				temp.add(letter);
				returnList.add(temp);
			}
			
			return returnList;
		}
		//else for each character we have it as the first character in the permutations, then generate all the permutations
		//recursively with that character removed
		else{
			for(int i=0; i<charList.size();i++){
				//we create a new arraylist object which is the same as the given character list
				ArrayList<Character> reducedList = new ArrayList<Character>(charList);
				//we remove the current char which we have first in the permutation
				char firstPermutationElement = reducedList.get(i);
				reducedList.remove(i);
				
				//we get all the permutations with the first character removed
				//and the first character to all those permutations
				ArrayList<ArrayList<Character>> reducedListPermutations = permutations(reducedList,amount-1);
				for(ArrayList<Character> permutation: reducedListPermutations){
					permutation.add(0,firstPermutationElement);
					returnList.add(permutation);
				}
			}
			//we return the list of permutations
			return returnList;
		}
	}
	
	//method used to generate the plugboard settings for a closure for a specific rotor positions
	public ArrayList<HashMap<Character,Character>> generatePlugboardSettings(String closure,String positions){
		//gets the rotors used in the scrambler 
		Rotor firstRotor = (Rotor) scrambler.getFirstRotor();
		Rotor secondRotor = (Rotor) firstRotor.getNextRotor();
		Rotor thirdRotor = (Rotor) secondRotor.getNextRotor();
		secondRotor.setPosition(positions.charAt(1));
		thirdRotor.setPosition(positions.charAt(0));
		
		//sets up the plugboardSettings as an arraylist of hashmaps
		ArrayList<HashMap<Character,Character>> plugboardSettings = new ArrayList<HashMap<Character,Character>>();
		
		//sets up an arraylist of character arrays which stores the alphabet encryptions for each positions
		ArrayList<char[]> encryptions = new ArrayList<char[]>();
		
		//we store how many letters were used in the closure
		int noOfLetters=0;
		//scrambler.setCrackingMode(false);
		
		//splits the closures into an array
		String[] closureArray = closure.split(",");
		
		//gets the alphabet encryptions for each position in the closure
		for(int i=1;i<closureArray.length;i+=2){
			
			//we rotate then encrypt, so you don't encrypt with the initial position you encrypt with the position after,
			//so if the positions are a,a,a and the first position in the closure is 1 then you encrypt with a,a,b
			//we mod by 97 since 'a' is 97 in ascii and we mod by 26 as the alphabet has size 26 we add 97 to get the ascii code
			//back into the lowercase letters range
			char position = (char)(((int)Character.toLowerCase(positions.charAt(2))%97+Integer.parseInt(closureArray[i]))%26 + 97);
			
			//we assume only the first rotor rotates so we only change the first rotors position
			firstRotor.setPosition(position);
			
			//we get the alphabet encryption for the rotor positions
			encryptions.add(scrambler.encryptAlphabet());
			
			//we increment the number of letters
			noOfLetters+=1;
		}
		
		//an arraylist of characters which stores the alphabet
		ArrayList<Character> alphabet=new ArrayList<Character>(Arrays.asList('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'));
		//we go through each closure with every letter swapped with the starting letter
		while(!alphabet.isEmpty()){
			//stores the ascii of the starting character -1
			int startWire = (int)alphabet.get(0)-97;
			int wire = startWire;
			String loop = ""+(char)(97+wire);
			
			//we go through the closure and encrypt to get the plugboard settings
			do{
				for(int j=0; j<encryptions.size(); j++){
					//we store the encrypts in a string
					loop+=encryptions.get(j)[wire];
					wire = (int)encryptions.get(j)[wire]-97;
				}
				//whenever we go fully through the closure we can remove that character from the alphabet
				//since starting with it later is pointless as we will have already used it earlier
				alphabet.remove((Character)(char)(wire+97));
			//we keep looping until we get back to the letter we were swapped with
			}while(wire!=startWire);
			
			//we got back to the letter we swapped with after going through the closure once
			if(loop.length()==noOfLetters+1){
				//we create a hashmap to store the plugboard settigns
				HashMap<Character,Character> plugboardMapping = new HashMap<Character,Character>();
				//we assume the settings are valid 
				boolean validSettings=true;					
				
				//goes through each letter that was encrypted in the closure and makes sure that they 
				int i = 0;
				while(i<loop.length()-1&&validSettings){
					//if the hashmap already has the ith letter in the closure array as a key
					//then we check that the hashmap swaps that letter as the same letter it was 
					//encrypted as in the loop generated in the closure
					if(plugboardMapping.containsKey(closureArray[2*i].charAt(0))){
						
						if(plugboardMapping.get(closureArray[2*i].charAt(0))!=loop.charAt(i)){
							validSettings=false;
						}
						
					}
					//else we add the swap to the plugboard mapping
					else{
						
						
						plugboardMapping.put(closureArray[2*i].charAt(0),loop.charAt(i));
						
					}
				
					//if the encrypted ith letter is in the hashmap already then we check that it is
					//swapped with is the ith letter in the closure
					if(plugboardMapping.containsKey(loop.charAt(i))){							
						if(plugboardMapping.get(loop.charAt(i))!=closureArray[2*i].charAt(0)){
							validSettings=false;
						}
					}
					//else we add the swap to the plugboard
					else{							
							
					plugboardMapping.put(loop.charAt(i),closureArray[2*i].charAt(0));
							
					}
					
					/*if(plugboardMapping.containsKey(closureArray[2*i].charAt(0))){
						if(plugboardMapping.get(closureArray[2*i].charAt(0))!=loop.charAt(i)){
							validSettings=false;
						}
					}
					else{
						
						plugboardMapping.put(closureArray[2*i].charAt(0),loop.charAt(i));
					}*/
					
					//increment i
					i++;
				
				}
				
				//if the settings are valid we add them to the plugboard settings
				if(validSettings){
					plugboardSettings.add(plugboardMapping);
				}
			}
		}
		
		//we return the plugboard settings
		return plugboardSettings;
	}
	
	//method used to generate the menu
	public void generateMenu(int position){
		//we get whatever the crib is encrypted as at the given position
		String cribCipher = cipherText.substring(position-1, position-1 + crib.length());
		cribPosition=position;
		
		//we get a new hashmap to store the menu as an neighbour list with weights
		menu = new HashMap<Character,HashMap<Character,ArrayList<Integer>>>();
		
		for(int i=0; i<crib.length();i++){
			
			//if the menu doesn't contain the current letter we current on in the crib
			//then we create a new hashmap and store it in the menu with the letter as the key
			if(!menu.containsKey(crib.charAt(i))){
				menu.put(crib.charAt(i), new HashMap<Character,ArrayList<Integer>>());
			}
			
			//we do the same thing if the letter in the cribCipher isn't in the menu
			if(!menu.containsKey(cribCipher.charAt(i))){
				menu.put(cribCipher.charAt(i), new HashMap<Character,ArrayList<Integer>>());
			}
			
			//if the letter in the crib has already been encrypted as the letter in the cipherText
			//before then we add the position to the weights for neighbour list for the letters in
			//the ciphertext and the crib
			if(menu.get(crib.charAt(i)).containsKey(cribCipher.charAt(i))){
				menu.get(crib.charAt(i)).get(cribCipher.charAt(i)).add(i+1);
			}
			//else we create a new arraylist and add it to the hashmap
			else{
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(i+1);
				menu.get(crib.charAt(i)).put(cribCipher.charAt(i),temp);
			}
			
			//we do the same but for the letter in the ciphertext instead
			if(menu.get(cribCipher.charAt(i)).containsKey(crib.charAt(i))){
				menu.get(cribCipher.charAt(i)).get(crib.charAt(i)).add(i+1);
			}
			else{
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(i+1);
				menu.get(cribCipher.charAt(i)).put(crib.charAt(i),temp);
			}
		}
	}
	
	//method for the rotor permutation option, works the same as in the enigma machine
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
	
	//method for cracking the closures
	public void crackClosures(ArrayList<String> closures){
		//stores the rotor positions and sets up the initial positions
		String rotorPositions;
		char leftRotorPos='a';
		char middleRotorPos='a';
		char rightRotorPos='a';
		
		//sets up a new arraylist to store the plugboard settings for each of the closures
		ArrayList<ArrayList<HashMap<Character,Character>>> closuresPlugboardSettings;
		
		for(int i=0; i<26*26*26;i++){
			//makes a new arraylist to store the plugboard settings
			closuresPlugboardSettings = new ArrayList<ArrayList<HashMap<Character,Character>>>();
			//stores the rotor positions in a string
			rotorPositions=""+leftRotorPos+middleRotorPos+rightRotorPos;
			
			//for each of the closures it gets the plugboard settings and adds them to the arraylist
			for(int j=0; j<closures.size();j++){
				closuresPlugboardSettings.add(generatePlugboardSettings(closures.get(j),rotorPositions));
			}
			
			//if the right rotor has position z then we increase the middle rotors position by one
			if(rightRotorPos=='z'){
				
				//if the middle rotor has position z then we increase the left rotors position by one
				if(middleRotorPos=='z'){
					leftRotorPos=(char)((int)leftRotorPos+1);
					middleRotorPos='a';
				}
				else{
					middleRotorPos=(char)((int)middleRotorPos+1);
				}
					
				rightRotorPos='a';
			}
			//else we increase the right rotor's position
			else{
					
				rightRotorPos=(char)((int)rightRotorPos+1);
			}
			
			//if any of the closures has an empty list for the plugboard settings then we stop
			//since that position is no longer valid
			boolean emptyArraylist = false;
			int j =0;
			while(!emptyArraylist && j<closuresPlugboardSettings.size()){
				if(closuresPlugboardSettings.get(j).size()==0){
					emptyArraylist=true;
				}
				j++;
			}
			
			if(!emptyArraylist){
				//if there are more than 1 closures then we refine the settings
				if(closuresPlugboardSettings.size()>1){
					//we move any obvious plugboard settings that don't work
					refinePlugboardSettings(closuresPlugboardSettings);
					
					emptyArraylist = false;
					j =0;
					
					//if any of the lists of settings are now empty we stop
					while(!emptyArraylist && j<closuresPlugboardSettings.size()){
						if(closuresPlugboardSettings.get(j).size()==0){
							emptyArraylist=true;
						}
						j++;
					}
					
					
					if(!emptyArraylist){
						//we create an arraylist to store the consistent merged plugboard settings
						ArrayList<HashMap<Character, Character>> consistentMergedPlugboardSettings = new ArrayList<HashMap<Character, Character>>();
						
						//have a new array list to store which settings we are picking from each list in the closuresPlugboardSettings
						int[] indices = new int[closuresPlugboardSettings.size()];
						//we go through all combinations of settings from each list and attempt to merge them
						while(indices[indices.length-1]<closuresPlugboardSettings.get(indices.length-1).size()){
							
							//arraylist to store each of the plugboard settings from each list for the current combination
							ArrayList<HashMap<Character, Character>> combinationSettings = new ArrayList<HashMap<Character, Character>>();
							
							for(int k=0;k<closuresPlugboardSettings.size();k++){
								combinationSettings.add(closuresPlugboardSettings.get(k).get(indices[k]));
							}
							
							//attempts to merge the settings and stores it in mergedSettings
							HashMap<Character,Character> mergedSettings=attemptToMergeSettings(combinationSettings);
							
							//if the hashmap isn't empty then we add it to the list of plugboard settings
							if(!mergedSettings.isEmpty()){
								consistentMergedPlugboardSettings.add(mergedSettings);
							}
							
							//we then increment the first index in indices
							indices[0]+=1;
							for(int k=1;k<indices.length;k++){
								//if the index in indices at index k-1  is equal to the size of the k-1 th list in closurePlugboardSettings
								//then we reset that index and increase the next index
								if(indices[k-1]==closuresPlugboardSettings.get(k-1).size()){
									indices[k]+=1;
									indices[k-1]=0;
								}
							}
						}
						
						//if consistentMergedPlugboardSettings is not empty then we output the rotor permutation and positions
						//and each of the plugboard settigns
						if(!consistentMergedPlugboardSettings.isEmpty()){
							System.out.println("rotor permutation is "+permutation);
							System.out.println("rotor positions are "+rotorPositions);
							String outputString;
							for(HashMap<Character,Character> plugboardSettings: consistentMergedPlugboardSettings){
								outputString="";
								
								//for each of the plugboard settings it converts it to a string and outputs it
								for(Character key: plugboardSettings.keySet()){
									if(outputString.indexOf(key)==-1){
										if((int)key < (int)plugboardSettings.get(key)){
											outputString+=key+"/"+plugboardSettings.get(key)+", ";
										}
										else{
											outputString+=plugboardSettings.get(key)+"/"+key+", ";
										}
									}
								}
								outputString=outputString.substring(0,outputString.length()-2);
								System.out.println(outputString);
							}
						}
					}
				}
				//else if the size of closurePlugboardSettings is 1, i.e. there was 1 closure then we just output all of its settings
				else{
					System.out.println("rotor permutation is "+permutation);
					System.out.println("rotor positions are "+rotorPositions);
					String outputString;			
					for(HashMap<Character,Character> plugboardSettings: closuresPlugboardSettings.get(0)){
						outputString="";
								
						for(Character key: plugboardSettings.keySet()){
							if(outputString.indexOf(key)==-1){
								if((int)key < (int)plugboardSettings.get(key)){
									outputString+=key+"/"+plugboardSettings.get(key)+", ";
								}
								else{
									outputString+=plugboardSettings.get(key)+"/"+key+", ";
								}
							}
						}
						outputString=outputString.substring(0,outputString.length()-1);
						System.out.println(outputString);
					}
				}
			}
		}
	}
	
	public HashMap<Character,Character> tailSettings(char rightRotorPos, HashMap<Character,Character> plugboardSettings, ArrayList<ArrayList<String>> tails){
		//don't need to set left and middle rotor since they are already done in crackClosures, make sure this is true or change if this changes in the future
		ArrayList<HashMap<Character,Character>> tailsPlugboardSettings = new ArrayList<HashMap<Character,Character>>();
			
		for(ArrayList<String> tail:tails){
			HashMap<Character,Character> temp = settingsFromTail(rightRotorPos, plugboardSettings, tail);
			if(temp.isEmpty()){
				System.out.println("A tail was inconsistent");
				return new HashMap<Character,Character>();
			}
			else{
				tailsPlugboardSettings.add(temp);
			}
		}
		tailsPlugboardSettings.add(plugboardSettings);
		HashMap<Character, Character> returnSettings = attemptToMergeSettings(tailsPlugboardSettings);
		System.out.println(returnSettings);
		return returnSettings;	
	}
	
	public HashMap<Character,Character> settingsFromTail(char rightRotorPos, HashMap<Character,Character> plugboardSettings, ArrayList<String> tail){
		System.out.println(tail);
		HashMap<Character,Character> settings = new HashMap<Character,Character>();
		
		Rotor firstRotor = (Rotor) scrambler.getFirstRotor();
		settings.put(plugboardSettings.get(tail.get(0).charAt(0)),tail.get(0).charAt(0));
		settings.put(tail.get(0).charAt(0),plugboardSettings.get(tail.get(0).charAt(0)));
		
		System.out.println(settings);
		
		scrambler.setCrackingMode(true);
		for(int i=1;i<tail.size();i+=2){
			System.out.println(tail.get(i)+","+tail.get(i-1)+tail.get(i+1));
			//rotate then encrypt
			char currentRightPos = (char)(((int)Character.toLowerCase(rightRotorPos)%97+Integer.parseInt(tail.get(i)))%26 + 97);
			
			firstRotor.setPosition(currentRightPos);
			//System.out.println(firstRotor.getCharOnTop());
			char swap=scrambler.encrypt(settings.get(tail.get(i-1).charAt(0)));
			System.out.println(swap);
			
			//check that if the original settings contain either swap or the current character, then the current char and swap must be swapped
			//otherwise return an empty hashmap and output an error message
			if(plugboardSettings.containsKey(tail.get(i+1).charAt(0))){				
				if(!(plugboardSettings.get(tail.get(i+1).charAt(0)).equals(swap))){
					//System.out.println("tail is inconsistent");
					return new HashMap<Character,Character>();
				}
			}
			else if(plugboardSettings.containsKey(swap)){
				if(!(plugboardSettings.get(swap).equals(tail.get(i+1).charAt(0)))){
					//System.out.println("tail is inconsistent");
					return new HashMap<Character,Character>();
				}
			}
			
			settings.put(tail.get(i+1).charAt(0),swap);
			settings.put(swap,tail.get(i+1).charAt(0));
		}
		System.out.println(settings);
		
		return settings;
	}
	
	//attempts to merge plugboardSettings
	public HashMap<Character,Character> attemptToMergeSettings(ArrayList<HashMap<Character,Character>> settings){
		
		//for each settings in the list we check it is consistent with all other settings
		for(int i = 0;i<settings.size();i++){
			//we do j=i+1 as you do not need to check if you are consistent with settings that occur before you in the
			//list since they will already of been doen
			for(int j = i+1;j<settings.size();j++){				
				if(!consistentPlugboardSettings(settings.get(i), settings.get(j))){
					//if some settings are inconsistent we return an empty hashmap
					return new HashMap<Character,Character>();
				}
			}
		}
		
		//we create a hashmap to store the merged settings
		HashMap<Character,Character> mergedSettings = new HashMap<Character,Character>();
		
		//for each key value pair in each of the settings we add it to the mergedSettings
		for(int i=0; i<settings.size();i++){
			for(Character key: settings.get(i).keySet()){
				if(!mergedSettings.containsKey(key)){
					mergedSettings.put(key,settings.get(i).get(key));
				}
			}
		}
		//we return the merged settings
		return mergedSettings;
	}
	
	//method used to refine the plugboard settings
	public void refinePlugboardSettings(ArrayList<ArrayList<HashMap<Character,Character>>> closuresPlugboardSettings){
		int i=0;
		//for each settings in each list in the closuresPlugboardSettings there must exist some other settings in the other lists
		//which it is consistent with, i.e. they don't swap the same letter with different letters
		while(i<closuresPlugboardSettings.size()){
			
			int j=0;
			//for each settings in the list i
			while(j<closuresPlugboardSettings.get(i).size()){
				boolean validSettings=true;		
				int n=0;
				
				//we check that there is some other setting in each list it is consistent with
				while(validSettings && n<closuresPlugboardSettings.size()){
					if(n != i){
						validSettings=false;
						int k = 0;						
						
						//there is some setting in the list n that is consistent with our settings j then we can stop
						while(!validSettings && k<closuresPlugboardSettings.get(n).size()){
							if(consistentPlugboardSettings(closuresPlugboardSettings.get(i).get(j),closuresPlugboardSettings.get(n).get(k))){
								validSettings=true;
							}
							
							k++;
						}
					}
					n++;
				}
				
				//if settings j are not consistent with some settings in every other list then it is removed
				if(!validSettings){
					closuresPlugboardSettings.get(i).remove(j);
					for(ArrayList<HashMap<Character,Character>> closureSettings: closuresPlugboardSettings){
						//if one list has all of its settings removed then we return, since the original closuresPlugboardSettings object will have
						//an empty list
						if(closureSettings.size()==0){
							return;
						}
					}
				}
				j++;
			}
			
			i++;
		}
	}
	
	//method that checks if 2 plugboard settings are consistent
	public boolean consistentPlugboardSettings(HashMap<Character,Character> firstSettings, HashMap<Character,Character> secondSettings){
			//we have an arraylist to store common keys
			ArrayList<Character> commonSwapCharacters = new ArrayList<Character>();
			
			for(Character key: firstSettings.keySet()){
				if(secondSettings.containsKey(key)){
					commonSwapCharacters.add(key);
				}
			}
			
			//for each key we check it has the same value in each hashmap
			boolean consistentSettings = true;
			int i = 0;
			char key;
			
			while(consistentSettings && i<commonSwapCharacters.size()){
				key=commonSwapCharacters.get(i);
				
				if(firstSettings.get(key)!=secondSettings.get(key)){
					consistentSettings=false;
				}
				
				i++;
			}
			
			return consistentSettings;
	}
	
	//method for cracking the enigma machine
	public void crackEnigma(ArrayList<String> closures){
		
		//arraylist to store each of the rotors 
		ArrayList<Character> temp = new ArrayList<Character>();
		
		temp.add('1');
		temp.add('2');
		temp.add('3');
		temp.add('4');
		temp.add('5');
		
		//gets all rotor permutations
		ArrayList<ArrayList<Character>> rotorPermutations= permutations(temp,3);
		
		//we go through all permutations with the reflector as each of the reflectors
		for(char reflector: useableReflectors){			
			for(ArrayList<Character> permutation: rotorPermutations){
				String rotorPermutation=""+reflector;
				
				for(Character rotorPosition: permutation){
					rotorPermutation+=rotorPosition;
				}
				
				//we change the rotor permutation
				changeRotorPermutation(rotorPermutation);
				//we output the current permutation
				System.out.println(this.permutation);
				//we then run the crackClosures method for the closures with the current permutation
				crackClosures(closures);
			}
		}
	}
	
	//used to find all paths starting with the given path for the menu
	public ArrayList<ArrayList<String>> depthFirstPathFinder(HashMap<Character,HashMap<Character,ArrayList<Integer>>> menu,ArrayList<String> path){
			//get the last letter in the path
			char startingChar=path.get(path.size()-1).charAt(0);
			//an arraylist to store all of the paths continuing from the given path
			ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
			
			//for each neighbour of the last letter in the list we create a new path from the old path
			//and add that character to the path as long as that position has not been visited before
			for(char neighbour:menu.get(startingChar).keySet()){
				for(int position:menu.get(startingChar).get(neighbour)){
					ArrayList<String> newPath = new ArrayList<String>(path);
					newPath.add(""+position);
					newPath.add(""+neighbour);
					
					if(path.size()>1){
						//if the new path contains the letter but at a different position
						//we stop the recursion and add the newPath to the list of paths
						if(path.contains(""+neighbour)&&!path.contains(""+position)){
							paths.add(newPath);
						}
						//else if the path doesn't contain the letter and doesn't contain the position
						//then we continue recusively with the new path as the starting path
						else if(!path.contains(""+position)){
							paths.addAll(depthFirstPathFinder(menu,newPath));								
						}
					}
					//if the starting path has just 1 letter we immediately go into the recursive mode
					else{
						paths.addAll(depthFirstPathFinder(menu,newPath));
					}
				}
			}
			
			//if paths is empty, i.e. we have reached the end of the path and the only way to continue
			//is to go to a letter we have already gone to before at the same position then 
			//we return the given path in a list
			if(paths.isEmpty()){
				ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
				temp.add(path);
				return temp;
			}
			else{
				return paths;
			}
	}
	
	//used to find all unique paths for a given menu
	public ArrayList<ArrayList<String>>	depthFirstSearch(HashMap<Character,HashMap<Character,ArrayList<Integer>>> menu){
		//array list to store all new paths
		ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
		
		//get all paths starting with each letter in the menu
		for(char vertex:menu.keySet()){
			ArrayList<String> temp= new ArrayList<String>();
			temp.add(""+vertex);
			ArrayList<ArrayList<String>> returnedPaths = depthFirstPathFinder(menu,temp);
			
			for(ArrayList<String> arr : returnedPaths){
				//remove paths that contain a closure but aren't a closure themselves, i.e. tail into closure
				if(arr.get(arr.size()-1).equals(arr.get(0)) || arr.indexOf(arr.get(arr.size()-1))==arr.size()-1){
					paths.add(arr);
					
				}
			}
			
			//paths.addAll(returnedPaths);
		}
		
		//remove edges from paths that are in a closure
		paths = removeClosuresInPaths(paths);
		
		//remove paths that are a sub path of another path
		paths = removePartialPaths(paths);
		
		//normalise closures
		for(int i =0;i<paths.size();i++){
			if(paths.get(i).get(0).equals(paths.get(i).get(paths.get(i).size()-1))){
				paths.set(i,normalise(paths.get(i)));
			}
		}
		
		//remove copies /reverse copies
		for(int i=0;i<paths.size();i++){
			ArrayList<String> reversedList = new ArrayList<String>(paths.get(i));
			
			//set up the reversedList
			for(int j=0; j<reversedList.size()/2;j++){
				String temp = reversedList.get(j);
				reversedList.set(j,reversedList.get(reversedList.size()-j-1));
				reversedList.set(reversedList.size()-j-1,temp);
			}
			
			//if 2 paths equal one another we remove that latter one
			int j=0;
			//use a while loop since we are changing the size of paths as we loop over it
			while(j<paths.size()){
				if(j!=i && paths.get(i).equals(paths.get(j))){
					paths.remove(j);
				}
				else if(i!=j && paths.get(j).equals(reversedList)){
					paths.remove(j);
				}
				//we only increment j if we haven't removed a path
				else{
					j++;
				}
			}
		}
		
		return paths;
	}
	
	//method for removing closures in paths
	public ArrayList<ArrayList<String>> removeClosuresInPaths(ArrayList<ArrayList<String>> paths){		
		for(int i=0;i<paths.size();i++){
			ArrayList<String> arr = paths.get(i);
			//if the path is a closure then we remove all other paths that contain 2 letters from the end of the closures
			//at the start
			if(arr.get(arr.size()-1).equals(arr.get(0))){
				for(int j=0; j<paths.size();j++){
					if(j!=i && !((paths.get(j).get(0)).equals(paths.get(j).get(paths.get(j).size()-1)))){
						/*if(paths.get(j).indexOf(arr.get(0))==paths.get(j).indexOf(arr.get(1))-1){
		
							int index=0;
							while(arr.get(index).equals(paths.get(j).get(index))){
								index++;
							}
							
							for(int k=0;k<index-1;k++){
								paths.get(j).remove(0);
							}
						}
						else{*/
						
						//check if the path contains at least 2 nodes from the closure i, if it does mark it for removal
						int noOfClosureNodes=0;
						int k=0;
						
						//size -1 as last element is a repeat of the first
						while(k<arr.size()-1&& noOfClosureNodes < 2){
							if(paths.get(j).contains(arr.get(k))){
								//System.out.println(arr.get(k));
								noOfClosureNodes++;
							}
							//increment k by 2 to skip positions
							k+=2;
						}
						
						//if the path j contains at least 2 letters that are in i then we mark it for removal
						if(noOfClosureNodes>=2){
							//mark array for removal and remove it later, not elegant better to replace with iterators
							//but it works for now
							
							//we replace it with an arraylist ["remove","remove"]
							ArrayList<String> temp = new ArrayList<String>();
							temp.add("remove");
							temp.add("remove");
							paths.set(j,temp);
							
							
						}
						//}					
					}
				}
			}
		}
		
		//we remove all the ["remove","remove"] lists
		int i=0;		
		while(i<paths.size()){
			if(paths.get(i).get(0).equals("remove")){
				paths.remove(i);
			}
			else{
				i++;
			}
		}
		
		return paths;
	}
	
	//method used to normalise closures to tell when closures are a repeat
	public ArrayList<String> normalise(ArrayList<String> closure){
		
		//we find the smallest letter in the closure
		int index=0;
		for(int i=2; i<closure.size();i+=2){
			
			if(closure.get(i).charAt(0)<closure.get(index).charAt(0)){
				index=i;
			}
		}
		
		//we then shift the closure so the smallest letter is the letter at the start of the closure
		if(index>0){
			ArrayList<String> newList = new ArrayList<String>();
			for(int i=0; i<closure.size();i++){
				
				if(i==0){
					//we mod as we starting at index in the closure
					//but starting at 0 in newlist
					newList.add(closure.get((i+index)%closure.size()));
				}
				
				//if the current letter in closure doesn't equal the previous letter in newList then we add it
				else if(!(closure.get((i+index)%closure.size()).equals(newList.get(newList.size()-1)))){
					newList.add(closure.get((i+index)%closure.size()));
				}
			}
			
			//the smallest letter only occurs once in the original closure so we add it again to make it into a list
			newList.add(newList.get(0));
			
			return newList;
		}
		else{
			return closure;
		}
	}
	
	//method used to remove partial paths
	public ArrayList<ArrayList<String>> removePartialPaths(ArrayList<ArrayList<String>> paths){
		for(int i=0; i<paths.size();i++){
			ArrayList<String> arr = paths.get(i);
			
			//for each path in the list of paths, if it is not a closure and it is a subpath of another
			//path in the list then we mark it for removal
			if(!(arr.get(0).equals(arr.get(arr.size()-1)))){
				for(int j=0 ;j<paths.size();j++){
					if(j!=i){
						//if arr is larger than or equal to j in size then it cannot be a proper subpath
						if(arr.size()<paths.get(j).size()){
							int index=paths.get(j).indexOf(arr.get(0));
							
							//if j contains the first letter in arr then we check if arr is a subpath of j
							if(index!=-1){
								int k=0;
								//we assume arr is a subpath until we can prove otherwise
								boolean subPath = true;
								
								//we loop through and check that each letter in arr and j match 
								while(k<arr.size()&&(index)<paths.get(j).size()&&subPath){
									if(!arr.get(k).equals(paths.get(j).get(index))){
										subPath=false;
									}
									k++;
									index++;
								}
								
								//if subPath is true we mark the array for removal
								if(subPath){
									ArrayList<String> temp = new ArrayList<String>();
									temp.add("subPath");
									temp.add("subPath");
									paths.set(i,temp);
									//break out of inner loop
									j=paths.size();
								}
							}
						}
					}
				}
			}
		}
		
		//we then remove all arrays that were a subpath
		int i=0;
		while(i<paths.size()){
			if(paths.get(i).get(0).equals("subPath")){
				paths.remove(i);
			}
			else{
				i++;
			}
		}
		
		return paths;
	}
	
	//options menu for editing the crib or ciphertext
	public void editCribOrCipher(){
			
			String input ="";
			while(!(input.equals("menu"))){
				//presents the menu options 
				System.out.println("Current crib is: "+crib);
				System.out.println("Current ciphertext is: "+cipherText);				
				System.out.println("1. edit crib");
				System.out.println("2. edit cipherText");
				System.out.println("Type menu to return to the main menu");
				
				//prompts the user for input
				input = scanner.nextLine();
				
				//if the user inputted 1 then it prompts them to input a new crib and stores it
				if(input.equals("1")){
					System.out.println("Type in the new crib");
					crib=scanner.nextLine();
				}
				
				//if the user inputted 2 then it prompts them for the ciphertext and stores it
				else if(input.equals("2")){
					System.out.println("Type in the ciphertext");
					cipherText=scanner.nextLine();
				}
			}
	}
	
	//generates closures and tails for the current crib and ciphertext
	public void generateClosuresAndTails(){
		String input="";
		while(!(input.equals("menu"))){
			//outputs the current crib and ciphertext and then asks the user for the position of the crib in the ciphertext
			System.out.println("Current crib is: "+crib);
			System.out.println("Current ciphertext is: "+cipherText);
			System.out.println("input position of crib in the ciphertext");
			System.out.println("Type menu to return to the main menu");
			
			input = scanner.nextLine();
			
			if(!(input.equals("menu"))){
				int position = Integer.parseInt(input);
				//generates the menu for the crib and ciphertext at the inputted position
				generateMenu(position);
				
				//gets the paths for the menu
				ArrayList<ArrayList<String>> paths= depthFirstSearch(getMenu());
				ArrayList<ArrayList<String>> closures= new ArrayList<ArrayList<String>>();
				ArrayList<ArrayList<String>> tails =  new ArrayList<ArrayList<String>>();
				
				//splits the paths into closures and tails
				for(ArrayList<String> path: paths){
					if(path.get(0).equals(path.get(path.size()-1))){
						closures.add(path);
					}
					else{
						tails.add(path);
					}
				}
				
				//outputs them
				System.out.println("Closures are: ");
				//make closures nicer
				for(int i=0;i<closures.size();i++){
					System.out.println(""+(i+1)+". "+closures.get(i));
				}
				System.out.println("Tails are: "+ tails);
				
				//prompts user for which closures to use for cracking the enigma machine
				while(!(input.equals("menu"))){
					System.out.println("Type menu to go back to the main menu");
					System.out.println("Or type which closures you want to use as a list, e.g. if you wish to use closures 2,4,6 & 8, type 2,4,6,8");
					
					//gets input
					input=scanner.nextLine();
					if(!(input.equals("menu"))){
						//remove spaces
						input=input.replaceAll("\\s+","");
					
						ArrayList<String> closuresToBeUsed = new ArrayList<String>();
						String[] closureNumbers=input.split(",");
					
						for(String closureNumber:closureNumbers){
							//gets the closure index
							int index = Integer.parseInt(closureNumber)-1;
							String closure = closures.get(index).get(0);
							
							//converts the closure to a string
							for(int i=1;i<closures.get(index).size()-1;i++){
								closure=closure+","+closures.get(index).get(i);
							}
							
							//adds the closure to the array list
							closuresToBeUsed.add(closure);
						}
						
						System.out.println(closuresToBeUsed);
						
						//cracks enigma using the closures
						crackEnigma(closuresToBeUsed);
					}
				}
			}
		}
	}
	
	//method to set what reflectors will be used in cracking
	public void setUseableReflectors(){
		
		String input ="";
		System.out.println("made this prettier.\nCurrent reflectors are "+useableReflectors);
		
		while(!(input.equals("menu"))){
			System.out.println("Current Reflectors for use are a,b,c.");
			System.out.println("Input which reflectors you want to use, e.g. if you want to use just a, type a, if you want to use a and b type a,b");
			System.out.println("Type menu to exit");
			
			//gets input and splits it by comma
			input=scanner.nextLine();
			if(!(input.equals("menu"))){
				input = input.replaceAll("\\s+","");
				String[] inputtedReflectors=input.split(",");
			
				//makes a new ArrayList object and adds each reflector to it
				useableReflectors=new ArrayList<Character>();
				for(String reflector:inputtedReflectors){
					useableReflectors.add(reflector.charAt(0));
				}
			
				System.out.println(useableReflectors);
			}
		}
	}
	
	//run method for the bombe
	public void run(){
		String input ="";
		System.out.println("Current rotor permutation is: "+permutation);
		/*ArrayList<String> closures = new ArrayList<String>();
		closures.add("e,14,n,20");
		closures.add("l,16,k,6,w,12,a,4");
		closures.add("o,18,w,24,j,13,y,17");*/
		
		while(!(input.equals("quit"))){
			//System.out.println("1. rotor permutation settings");
			//displays the options menu for the user
			System.out.println("1. input closures");
			System.out.println("2. edit crib/ciphertext");
			System.out.println("3. generate closures/tails");
			System.out.println("4. input reflectors for use in cracking");
			System.out.println("type quit to exit");
			
			//prompts the user for input 
			input = scanner.nextLine();
			
			//dummy if for edit rotor permutation which might be added back 
			if(input.equals("-1")){
				//editRotorPermutation();
			}
			//else if the user inputted 1 then the user is prompted for the 
			else if(input.equals("1")){
				System.out.println("Current rotor permutation is: "+ permutation);
				ArrayList<String> closures= new ArrayList<String>();
				
				//the user inputs closures until they have typed in end
				while(!input.equals("end")){
					System.out.println("Input a closure");
					System.out.println("Type end when you have input all closures.");
					input = scanner.nextLine().toLowerCase();
					if(!input.equals("end")){
						closures.add(input);
					}
				}
				
				//the enigma is then cracked with the current inputted closures
				crackEnigma(closures);
				
			}
			//else if the user inputted 2 then they are taken to crib/cipher menu
			else if(input.equals("2")){
				editCribOrCipher();
			}
			
			//else if they inputted 3 then they are taken to automatic generation of closures menu
			else if(input.equals("3")){
				generateClosuresAndTails();
			}
			//else if they inputted 4 then they are taken to the reflector menu
			else if(input.equals("4")){
				setUseableReflectors();
			}
		}
	}
	
	//main method for the bombe
	public static void main(String args[]){
		BombeMachine bombe = new BombeMachine();
		/*ArrayList<Character> list = new ArrayList<Character>();
		list.add('1');
		list.add('2');
		list.add('3');
		list.add('4');
		list.add('5');
		System.out.println(bombe.permutations(list,3));
		bombe.changeRotorPermutation("b245");*/
		/*HashMap<Character,HashMap<Character,ArrayList<Integer>>> menu = new HashMap<Character,HashMap<Character,ArrayList<Integer>>>();
		HashMap<Character,ArrayList<Integer>> neighboursOfA = new HashMap<Character,ArrayList<Integer>>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.add(1);
		neighboursOfA.put('b',temp);
		temp = new ArrayList<Integer>();
		temp.add(2);
		neighboursOfA.put('c',temp);
		temp = new ArrayList<Integer>();
		temp.add(7);
		neighboursOfA.put('g',temp);
		menu.put('a',neighboursOfA);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfB = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(1);
		neighboursOfB.put('a',temp);
		temp = new ArrayList<Integer>();
		temp.add(3);
		neighboursOfB.put('d',temp);
		menu.put('b',neighboursOfB);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfC = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(2);
		neighboursOfC.put('a',temp);
		temp = new ArrayList<Integer>();
		temp.add(4);
		neighboursOfC.put('d',temp);
		temp = new ArrayList<Integer>();
		temp.add(6);
		neighboursOfC.put('f',temp);
		menu.put('c',neighboursOfC);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfD = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(3);
		neighboursOfD.put('b',temp);
		temp = new ArrayList<Integer>();
		temp.add(4);
		neighboursOfD.put('c',temp);
		temp = new ArrayList<Integer>();
		temp.add(5);
		temp.add(10);
		neighboursOfD.put('e',temp);
		menu.put('d',neighboursOfD);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfE = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(5);
		temp.add(10);
		neighboursOfE.put('d',temp);
		temp = new ArrayList<Integer>();
		temp.add(8);
		neighboursOfE.put('f',temp);
		menu.put('e',neighboursOfE);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfF = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(6);
		neighboursOfF.put('c',temp);
		temp = new ArrayList<Integer>();
		temp.add(8);
		neighboursOfF.put('e',temp);
		temp = new ArrayList<Integer>();
		temp.add(9);
		neighboursOfF.put('h',temp);
		menu.put('f',neighboursOfF);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfG = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(7);
		neighboursOfG.put('a',temp);
		menu.put('g',neighboursOfG);
		
		HashMap<Character,ArrayList<Integer>> neighboursOfH = new HashMap<Character,ArrayList<Integer>>();
		temp = new ArrayList<Integer>();
		temp.add(9);
		neighboursOfH.put('f',temp);
		menu.put('h', neighboursOfH);
		
		ArrayList<String> start = new ArrayList<String>();
		start.add("a");
		ArrayList<String> temp = new ArrayList<String>();
		//temp.add("a");
		temp.add("b");
		temp.add("c");
		ArrayList<String> temp2 = new ArrayList<String>();
		temp2.add("a");
		temp2.add("b");
		temp2.add("c");
		ArrayList<ArrayList<String>> tempList= new ArrayList<ArrayList<String>>();
		tempList.add(temp2);
		tempList.add(temp);
		System.out.println(bombe.removePartialPaths(tempList));
		bombe.setCipherText("abcde");
		bombe.setCrib("hello");
		bombe.generateMenu(1);
		System.out.println(bombe.depthFirstSearch(bombe.getMenu()));
		//System.out.println();*/
		HashMap<Character,Character> temp = new HashMap<Character,Character>();
		temp.put('a','b');
		temp.put('b','a');
		temp.put('n','z');
		temp.put('z','n');
		//temp.put('o','k');
		//temp.put('k','o');
		//temp.put('k','z');
		//temp.put('z','k');
		
		ArrayList<String> tempTail = new ArrayList<String>();
		tempTail.add("b");
		tempTail.add("3");
		tempTail.add("c");
		tempTail.add("5");
		tempTail.add("d");
		
		ArrayList<String> tempTailTwo = new ArrayList<String>();
		tempTailTwo.add("a");
		tempTailTwo.add("4");
		tempTailTwo.add("f");
		tempTailTwo.add("1");
		tempTailTwo.add("m");
		
		ArrayList<ArrayList<String>> tails = new ArrayList<ArrayList<String>>();
		tails.add(tempTail);
		tails.add(tempTailTwo);
		
		bombe.tailSettings('a',temp,tails);
		bombe.run();
	}
}