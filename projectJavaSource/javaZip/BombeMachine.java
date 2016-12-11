import enigmaComponents.*;
import java.util.*;
import java.lang.Character;

public class BombeMachine{
	private Scrambler scrambler;
	private Rotor[] rotors;
	private Reflector[] reflectors;
	private Printer printer;
	private String crib;
	private String cipherText;
	private String permutation;
	private ArrayList<char[]> menu;
	private int cribPosition;
	private Scanner scanner;
	
	public BombeMachine(){
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
			rotors[i].disablePrinter();
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
			reflectors[i].disablePrinter();
		}
		/*rotors[1].setRingPosition(2);
		rotors[3].setRingPosition(21);
		rotors[4].setRingPosition(12);*/
		rotors[2].setNextRotor(rotors[1]);
		rotors[1].setNextRotor(rotors[0]);
		rotors[0].setNextRotor(reflectors[0]);
		
		scrambler = new Scrambler();
		scrambler.setFirstRotor(rotors[2]);
		scrambler.disablePrinter();
		/*menu = new HashMap<Character,HashMap<Character,HashSet<Integer>>>();
		HashMap<Character,HashSet<Integer>> row;
		for(int i = 97;i<123;i++){
			row = new HashMap<Character,HashSet<Integer>>();
			for(int j = 97;j<123;j++){
				HashSet<Integer> edges = new HashSet<Integer>();
				edges.add(0);
				row.put((char)j,edges);
			}
			menu.put((char)i,row);
		}*/
		menu = new ArrayList<char[]>();
		//System.out.println(menu);
		permutation="A, 1, 2, 3";
		
		scanner= new Scanner(System.in);
	}
	
	public String getCrib(){
		return crib;
	}
	
	public void setCrib(String crib){
		this.crib = crib.replaceAll("\\s+","");;
	}
	
	public String getCipherText(){
		return cipherText;
	}
	
	public void setCipherText(String cipherText){
		this.cipherText = cipherText.replaceAll("\\s+","");;
	}
	
	private void changeRotorPermutation(String rotorPermutation){
		String[] stringPositions=rotorPermutation.split("");
		//System.out.println(Arrays.toString(stringPositions));
		//String temp = permutation;
		int rotor = Integer.parseInt(stringPositions[stringPositions.length-1]);
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
	
	public ArrayList<ArrayList<Character>> permutations(ArrayList<Character> charList, int amount){
		ArrayList<ArrayList<Character>> returnList = new ArrayList<ArrayList<Character>>();
		if(amount>charList.size()){
			amount=charList.size();
		}
		if(charList.size()<=1){
			returnList.add(charList);
			return returnList;
		}
		else if(amount<=1){
			for(Character letter: charList){
				ArrayList<Character> temp = new ArrayList<Character>();
				temp.add(letter);
				returnList.add(temp);
			}
			
			return returnList;
		}
		else{
			for(int i=0; i<charList.size();i++){
				ArrayList<Character> reducedList = new ArrayList<Character>(charList);
				char firstPermutationElement = reducedList.get(i);
				reducedList.remove(i);
				ArrayList<ArrayList<Character>> reducedListPermutations = permutations(reducedList,amount-1);
				for(ArrayList<Character> permutation: reducedListPermutations){
					permutation.add(0,firstPermutationElement);
					returnList.add(permutation);
				}
			}
			return returnList;
		}
	}
	
	public ArrayList<HashMap<Character,Character>> generatePlugboardSettings(String closure,String positions){
		Rotor firstRotor = (Rotor) scrambler.getFirstRotor();
		Rotor secondRotor = (Rotor) firstRotor.getNextRotor();
		Rotor thirdRotor = (Rotor) secondRotor.getNextRotor();
		secondRotor.setPosition(positions.charAt(1));
		thirdRotor.setPosition(positions.charAt(0));
		
		ArrayList<HashMap<Character,Character>> plugboardSettings = new ArrayList<HashMap<Character,Character>>();
		//System.out.println(secondRotor.getCharOnTop());
		//System.out.println(thirdRotor.getCharOnTop());
		
		ArrayList<char[]> encryptions = new ArrayList<char[]>();
		int noOfLetters=0;
		//scrambler.setCrackingMode(false);
		String[] closureArray = closure.split(",");
		for(int i=1;i<closureArray.length;i+=2){
			//System.out.println(closureArray[i]);
			//we rotate then encrypt, so you don't encrypt with the initial position you encrypt with the position after
			char position = (char)(((int)Character.toLowerCase(positions.charAt(2))%97+Integer.parseInt(closureArray[i]))%26 + 97);
			firstRotor.setPosition(position);
			
			encryptions.add(scrambler.encryptAlphabet());
			//System.out.println(firstRotor.getCharOnTop());
			noOfLetters+=1;
			//System.out.println(new String(scrambler.encryptAlphabet()));
		}
		//System.out.println(noOfLetters);
		ArrayList<Character> alphabet=new ArrayList<Character>(Arrays.asList('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'));
		while(!alphabet.isEmpty()){
			int startWire = (int)alphabet.get(0)-97;
			int wire = startWire;
			String loop = ""+(char)(97+wire);
			do{
				for(int j=0; j<encryptions.size(); j++){
					loop+=encryptions.get(j)[wire];
					wire = (int)encryptions.get(j)[wire]-97;
				}
				alphabet.remove((Character)(char)(wire+97));
			}while(wire!=startWire);
			//System.out.println(out.length());
			if(loop.length()==noOfLetters+1){
				//String settings="";
				HashMap<Character,Character> plugboardMapping = new HashMap<Character,Character>();
				boolean validSettings=true;					
				
				/*settings=loop.charAt(0)+"/"+closureArray[0];
				plugboardMapping.put(loop.charAt(0),closureArray[0].charAt(0));
				plugboardMapping.put(closureArray[0].charAt(0),loop.charAt(0));*/
								
				int i = 0;
				while(i<loop.length()-1&&validSettings){
					/*if((int)(loop.charAt(i))<(int)(closureArray[2*i].charAt(0))){
						settings+=", "+loop.charAt(i)+"/"+closureArray[2*i];
						
					}
					else{
						settings+=", "+closureArray[2*i]+"/"+loop.charAt(i);*/
					if(plugboardMapping.containsKey(closureArray[2*i].charAt(0))){
						
						if(plugboardMapping.get(closureArray[2*i].charAt(0))!=loop.charAt(i)){
							validSettings=false;
						}
						
					}
					else{
						
						
						plugboardMapping.put(closureArray[2*i].charAt(0),loop.charAt(i));
						
					}
				
					
					if(plugboardMapping.containsKey(loop.charAt(i))){							
						if(plugboardMapping.get(loop.charAt(i))!=closureArray[2*i].charAt(0)){
							validSettings=false;
						}
					}
					else{							
							
					plugboardMapping.put(loop.charAt(i),closureArray[2*i].charAt(0));
							
					}
					
					if(plugboardMapping.containsKey(closureArray[2*i].charAt(0))){
						if(plugboardMapping.get(closureArray[2*i].charAt(0))!=loop.charAt(i)){
							validSettings=false;
						}
					}
					else{
						
						plugboardMapping.put(closureArray[2*i].charAt(0),loop.charAt(i));
					}

					i++;
				
				}
				
				if(validSettings){
					plugboardSettings.add(plugboardMapping);
					//System.out.println("Plugboard settings loop:\n"+settings);
				}
			}
		}
		//System.out.println(encryptions);
		return plugboardSettings;
	}
	
	public void generateMenu(int position){
		String cribCipher = cipherText.substring(position-1, position-1 + crib.length());
		cribPosition=position;
		menu = new ArrayList<char[]>();
		/*HashMap<Character,HashSet<Integer>> row;
		for(int i = 97;i<123;i++){
			row = new HashMap<Character,HashSet<Integer>>();
			for(int j = 97;j<123;j++){
				HashSet<Integer> edges = new HashSet<Integer>();
				row.put((char)j,edges);
			}
			menu.put((char)i,row);
		}*/
		
		for(int i = 0;i<crib.length();i++){
			char cribChar = crib.charAt(i);
			char cribCipherChar = cribCipher.charAt(i);
			//menu.get(cribChar).get(cribCipherChar).add(position+i);
			//menu.get(cribCipherChar).get(cribChar).add(position+i);
			menu.add(new char[]{cribChar,cribCipherChar});
		}
		
		
		for(char[] letters: menu){
			System.out.println(letters[0]+", "+letters[1]);
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
	
	public void crackClosures(ArrayList<String> closures){
		String rotorPositions;
		char leftRotorPos='a';
		char middleRotorPos='a';
		char rightRotorPos='a';
		
		ArrayList<ArrayList<HashMap<Character,Character>>> closuresPlugboardSettings;
		
		for(int i=0; i<26*26*26;i++){
			closuresPlugboardSettings = new ArrayList<ArrayList<HashMap<Character,Character>>>();
			rotorPositions=""+leftRotorPos+middleRotorPos+rightRotorPos;

			for(int j=0; j<closures.size();j++){
				closuresPlugboardSettings.add(generatePlugboardSettings(closures.get(j),rotorPositions));
			}
			
			if(rightRotorPos=='z'){
					
				if(middleRotorPos=='z'){
					leftRotorPos=(char)((int)leftRotorPos+1);
					middleRotorPos='a';
				}
				else{
					middleRotorPos=(char)((int)middleRotorPos+1);
				}
					
				rightRotorPos='a';
			}
			else{
					
				rightRotorPos=(char)((int)rightRotorPos+1);
			}
			
			boolean emptyArraylist = false;
			int j =0;
			while(!emptyArraylist && j<closuresPlugboardSettings.size()){
				if(closuresPlugboardSettings.get(j).size()==0){
					emptyArraylist=true;
				}
				j++;
			}
			
			if(!emptyArraylist){
				if(closuresPlugboardSettings.size()>1){
					refinePlugboardSettings(closuresPlugboardSettings);
					
					emptyArraylist = false;
					j =0;
					
					while(!emptyArraylist && j<closuresPlugboardSettings.size()){
						if(closuresPlugboardSettings.get(j).size()==0){
							emptyArraylist=true;
						}
						j++;
					}
					
					if(!emptyArraylist){
						ArrayList<HashMap<Character, Character>> consistentMergedPlugboardSettings = new ArrayList<HashMap<Character, Character>>();
						int[] indices = new int[closuresPlugboardSettings.size()];
						while(indices[indices.length-1]<closuresPlugboardSettings.get(indices.length-1).size()){
							
							ArrayList<HashMap<Character, Character>> combinationSettings = new ArrayList<HashMap<Character, Character>>();
							
							for(int k=0;k<closuresPlugboardSettings.size();k++){
								combinationSettings.add(closuresPlugboardSettings.get(k).get(indices[k]));
							}
							
							HashMap<Character,Character> mergedSettings=attemptToMergeSettings(combinationSettings);
							if(!mergedSettings.isEmpty()){
								consistentMergedPlugboardSettings.add(mergedSettings);
							}
							
							indices[0]+=1;
							for(int k=1;k<indices.length;k++){
								if(indices[k-1]==closuresPlugboardSettings.get(k-1).size()){
									indices[k]+=1;
									indices[k-1]=0;
								}
							}
						}
						
						if(!consistentMergedPlugboardSettings.isEmpty()){
							System.out.println("rotor permutation is "+permutation);
							System.out.println("rotor positions are "+rotorPositions);
							String outputString;
							for(HashMap<Character,Character> plugboardSettings: consistentMergedPlugboardSettings){
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
								outputString=outputString.substring(0,outputString.length()-2);
								System.out.println(outputString);
							}
						}
					}
				}
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
	
	public HashMap<Character,Character> attemptToMergeSettings(ArrayList<HashMap<Character,Character>> settings){
		
		for(int i = 0;i<settings.size();i++){
			for(int j = i+1;j<settings.size();j++){				
				if(!consistentPlugboardSettings(settings.get(i), settings.get(j))){
					//System.out.println("inconsistent combination");
					return new HashMap<Character,Character>();
				}
			}
		}
		
		//System.out.println("consistent combination, can merge");
		HashMap<Character,Character> mergedSettings = new HashMap<Character,Character>();
		
		for(int i=0; i<settings.size();i++){
			for(Character key: settings.get(i).keySet()){
				if(!mergedSettings.containsKey(key)){
					mergedSettings.put(key,settings.get(i).get(key));
				}
			}
		}
		return mergedSettings;
	}
	
	public void refinePlugboardSettings(ArrayList<ArrayList<HashMap<Character,Character>>> closuresPlugboardSettings){
		int i=0;
		while(i<closuresPlugboardSettings.size()){
			
			int j=0;
			while(j<closuresPlugboardSettings.get(i).size()){
				boolean validSettings=true;		
				int n=0;
				
				while(validSettings && n<closuresPlugboardSettings.size()){
					if(n != i){
						validSettings=false;
						int k = 0;
						
						while(!validSettings && k<closuresPlugboardSettings.get(n).size()){
							if(consistentPlugboardSettings(closuresPlugboardSettings.get(i).get(j),closuresPlugboardSettings.get(n).get(k))){
								validSettings=true;
							}
							
							k++;
						}
					}
					n++;
				}
				
				if(!validSettings){
					closuresPlugboardSettings.get(i).remove(j);
					for(ArrayList<HashMap<Character,Character>> closureSettings: closuresPlugboardSettings){
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
	
	public boolean consistentPlugboardSettings(HashMap<Character,Character> firstSettings, HashMap<Character,Character> secondSettings){
			ArrayList<Character> commonSwapCharacters = new ArrayList<Character>();
			
			for(Character key: firstSettings.keySet()){
				if(secondSettings.containsKey(key)){
					commonSwapCharacters.add(key);
				}
			}
			
			//System.out.println(commonSwapCharacters);
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
			//System.out.println(consistentSettings);
			return consistentSettings;
	}
	
	public void crackEnigma(ArrayList<String> closures){
		ArrayList<Character> temp = new ArrayList<Character>();
		
		temp.add('1');
		temp.add('2');
		temp.add('3');
		temp.add('4');
		temp.add('5');
		
		ArrayList<ArrayList<Character>> rotorPermutations= permutations(temp,3);
		
		char[] reflectors = {'a','b','c'};
		for(char reflector: reflectors){			
			for(ArrayList<Character> permutation: rotorPermutations){
				String rotorPermutation=""+reflector;
				
				for(Character rotorPosition: permutation){
					rotorPermutation+=rotorPosition;
				}
				
				changeRotorPermutation(rotorPermutation);
				System.out.println(this.permutation);
				crackClosures(closures);
			}
		}
	}
	
	public void run(){
		String input ="";
		System.out.println("Current rotor permutation is: "+permutation);
		/*ArrayList<String> closures = new ArrayList<String>();
		closures.add("e,14,n,20");
		closures.add("l,16,k,6,w,12,a,4");
		closures.add("o,18,w,24,j,13,y,17");*/
		
		while(!(input.equals("quit"))){
			System.out.println("1. rotor permutation settings");
			System.out.println("2. input closures");
			System.out.println("type quit to exit");
			
			input = scanner.nextLine();
			if(input.equals("1")){
				editRotorPermutation();
			}
			else if(input.equals("2")){
				System.out.println("Current rotor permutation is: "+ permutation);
				ArrayList<String> closures= new ArrayList<String>();
				
				while(!input.equals("end")){
					System.out.println("Input a closure");
					System.out.println("Type end when you have input all closures.");
					input = scanner.nextLine();
					if(!input.equals("end")){
						closures.add(input);
					}
				}
				crackEnigma(closures);
				/*
				System.out.println("input closure, e.g. L,20,R,3,T,13");
				String closure = scanner.nextLine();
				
				System.out.println("input starting positions, e.g. ABC");
				String positions = scanner.nextLine();
				
				generatePlugboardSettings(closure, positions);*/
			}
		}
	}
	
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
		bombe.run();
	}
}