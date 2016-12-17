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
	private HashMap<Character,HashMap<Character,ArrayList<Integer>>> menu;
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
		menu = new HashMap<Character,HashMap<Character,ArrayList<Integer>>>();
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
	
	public HashMap<Character,HashMap<Character,ArrayList<Integer>>> getMenu(){
		return menu;
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
		menu = new HashMap<Character,HashMap<Character,ArrayList<Integer>>>();
		
		for(int i=0; i<crib.length();i++){
			if(!menu.containsKey(crib.charAt(i))){
				menu.put(crib.charAt(i), new HashMap<Character,ArrayList<Integer>>());
			}
			
			if(!menu.containsKey(cribCipher.charAt(i))){
				menu.put(cribCipher.charAt(i), new HashMap<Character,ArrayList<Integer>>());
			}
			
			if(menu.get(crib.charAt(i)).containsKey(cribCipher.charAt(i))){
				menu.get(crib.charAt(i)).get(cribCipher.charAt(i)).add(i+1);
			}
			else{
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(i+1);
				menu.get(crib.charAt(i)).put(cribCipher.charAt(i),temp);
			}
			
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
	
	public ArrayList<ArrayList<String>> depthFirstPathFinder(HashMap<Character,HashMap<Character,ArrayList<Integer>>> menu,ArrayList<String> path){
			char startingChar=path.get(path.size()-1).charAt(0);
			ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
			
			for(char neighbour:menu.get(startingChar).keySet()){
				for(int position:menu.get(startingChar).get(neighbour)){
					ArrayList<String> newPath = new ArrayList<String>(path);
					newPath.add(""+position);
					newPath.add(""+neighbour);
					
					if(path.size()>1){
						if(path.contains(""+neighbour)&&!path.contains(""+position)){
							paths.add(newPath);
						}
						else if(!path.contains(""+position)){
							paths.addAll(depthFirstPathFinder(menu,newPath));								
						}
					}
					else{
						paths.addAll(depthFirstPathFinder(menu,newPath));
					}
				}
			}
			
			if(paths.isEmpty()){
				ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
				temp.add(path);
				return temp;
			}
			else{
				return paths;
			}
	}
	
	public ArrayList<ArrayList<String>>	depthFirstSearch(HashMap<Character,HashMap<Character,ArrayList<Integer>>> menu){
		ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
		
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
		//System.out.println(paths);
		paths = removeClosuresInPaths(paths);
		//System.out.println(paths);
		paths = removePartialPaths(paths);
		//System.out.println(paths);
		//System.out.println(paths);
		//normalise closures
		for(int i =0;i<paths.size();i++){
			if(paths.get(i).get(0).equals(paths.get(i).get(paths.get(i).size()-1))){
				paths.set(i,normalise(paths.get(i)));
			}
		}
		//remove copies /reverse copies
		for(int i=0;i<paths.size();i++){
			ArrayList<String> reversedList = new ArrayList<String>(paths.get(i));
			
			for(int j=0; j<reversedList.size()/2;j++){
				String temp = reversedList.get(j);
				reversedList.set(j,reversedList.get(reversedList.size()-j-1));
				reversedList.set(reversedList.size()-j-1,temp);
			}
			
			int j=0;
			while(j<paths.size()){
				if(j!=i && paths.get(i).equals(paths.get(j))){
					paths.remove(j);
				}
				else if(i!=j && paths.get(j).equals(reversedList)){
					paths.remove(j);
				}
				else{
					j++;
				}
			}
		}
		
		return paths;
	}
	
	public ArrayList<ArrayList<String>> removeClosuresInPaths(ArrayList<ArrayList<String>> paths){
		
		for(int i=0;i<paths.size();i++){
			ArrayList<String> arr = paths.get(i);
			
			if(arr.get(arr.size()-1).equals(arr.get(0))){
				//System.out.println(arr);
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
						//size -1 has last element is a repeat of the first
						while(k<arr.size()-1&& noOfClosureNodes < 2){
							if(paths.get(j).contains(arr.get(k))){
								//System.out.println(arr.get(k));
								noOfClosureNodes++;
							}
							k+=2;
						}
						
						if(noOfClosureNodes>=2){
							//System.out.println(paths.get(j));
							//System.out.println(j);
							//mark array for removal and remove it later, not elegant better to replace with iterators
							//but it works for now
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
	
	public ArrayList<String> normalise(ArrayList<String> closure){
		int index=0;
		for(int i=2; i<closure.size();i+=2){
			if(closure.get(i).charAt(0)<closure.get(index).charAt(0)){
				index=i;
			}
		}
		
		if(index>0){
			ArrayList<String> newList = new ArrayList<String>();
			for(int i=0; i<closure.size();i++){
				if(i==0){
					newList.add(closure.get((i+index)%closure.size()));
				}
				else if(!(closure.get((i+index)%closure.size()).equals(newList.get(newList.size()-1)))){
					newList.add(closure.get((i+index)%closure.size()));
				}
			}
			
			newList.add(newList.get(0));
			
			return newList;
		}
		else{
			return closure;
		}
	}
	
	public ArrayList<ArrayList<String>> removePartialPaths(ArrayList<ArrayList<String>> paths){
		for(int i=0; i<paths.size();i++){
			ArrayList<String> arr = paths.get(i);
			if(!(arr.get(0).equals(arr.get(arr.size()-1)))){
				for(int j=0 ;j<paths.size();j++){
					if(j!=i){
						if(arr.size()<paths.get(j).size()){
							int index=paths.get(j).indexOf(arr.get(0));
							//System.out.println(index);
							if(index!=-1){
								int k=0;
								boolean subPath = true;
								while(k<arr.size()&&(index)<paths.get(j).size()&&subPath){
									if(!arr.get(k).equals(paths.get(j).get(index))){
										subPath=false;
									}
									k++;
									index++;
								}
								
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
	
	public void editCribOrCipher(){
			
			String input ="";
			while(!(input.equals("menu"))){
				System.out.println("Current crib is: "+crib);
				System.out.println("Current ciphertext is: "+cipherText);				
				System.out.println("1. edit crib");
				System.out.println("2. edit cipherText");
				System.out.println("Type menu to return to the main menu");
				
				input = scanner.nextLine();
				if(input.equals("1")){
					System.out.println("Type in the new crib");
					crib=scanner.nextLine();
				}
				else if(input.equals("2")){
					System.out.println("Type in the ciphertext");
					cipherText=scanner.nextLine();
				}
			}
	}
	
	public void generateClosuresAndTails(){
			String input="";
			while(!(input.equals("menu"))){
				System.out.println("Current crib is: "+crib);
				System.out.println("Current ciphertext is: "+cipherText);
				System.out.println("input position of crib in the ciphertext");
				System.out.println("Type menu to return to the main menu");
				
				input = scanner.nextLine();
				
				if(!(input.equals("menu"))){
					int position = Integer.parseInt(input);
					generateMenu(position);
					
					ArrayList<ArrayList<String>> paths= depthFirstSearch(getMenu());
					ArrayList<ArrayList<String>> closures= new ArrayList<ArrayList<String>>();
					ArrayList<ArrayList<String>> tails =  new ArrayList<ArrayList<String>>();
					
					for(ArrayList<String> path: paths){
						if(path.get(0).equals(path.get(path.size()-1))){
							closures.add(path);
						}
						else{
							tails.add(path);
						}
					}
					
					System.out.println("Closures are: "+closures);
					System.out.println("Tails are: "+ tails);
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
			//System.out.println("1. rotor permutation settings");
			System.out.println("1. input closures");
			System.out.println("2. edit crib/ciphertext");
			System.out.println("3. generate closures/tails");
			System.out.println("type quit to exit");
			
			input = scanner.nextLine();
			if(input.equals("-1")){
				//editRotorPermutation();
			}
			else if(input.equals("1")){
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
			else if(input.equals("2")){
				editCribOrCipher();
			}
			else if(input.equals("3")){
				generateClosuresAndTails();
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
		start.add("a");*/
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
		//System.out.println();
		bombe.run();
	}
}