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
		rotors[0].setNextRotor(reflectors[1]);
		
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
	
	public ArrayList<String> generatePlugboardSettings(String positions, String closure){
		Rotor firstRotor = (Rotor) scrambler.getFirstRotor();
		Rotor secondRotor = (Rotor) firstRotor.getNextRotor();
		Rotor thirdRotor = (Rotor) secondRotor.getNextRotor();
		
		secondRotor.setPosition(positions.charAt(1));
		thirdRotor.setPosition(positions.charAt(0));
		
		//System.out.println(secondRotor.getCharOnTop());
		//System.out.println(thirdRotor.getCharOnTop());
		
		ArrayList<char[]> encryptions = new ArrayList<char[]>();
		//scrambler.setCrackingMode(false);
		String[] closureArray = closure.split(",");
		for(int i=1;i<closureArray.length;i+=2){
			//System.out.println(closureArray[i]);
			char position = (char)(((int)Character.toLowerCase(positions.charAt(2))%97+Integer.parseInt(closureArray[i]))%26 + 97);
			//System.out.println(position);
			firstRotor.setPosition(position);
			
			encryptions.add(scrambler.encryptAlphabet());
			//System.out.println(firstRotor.getCharOnTop());
			System.out.println(new String(scrambler.encryptAlphabet()));
		}
		ArrayList<Character> alphabet=new ArrayList<Character>(Arrays.asList('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'));
		while(!alphabet.isEmpty()){
			int startWire = (int)alphabet.get(0)-97;
			int wire = startWire;
			String out = ""+(char)(97+wire);
			do{
				for(int j=0; j<encryptions.size(); j++){
					out+=encryptions.get(j)[wire];
					wire = (int)encryptions.get(j)[wire]-97;
				}
				alphabet.remove((Character)(char)(wire+97));
			}while(wire!=startWire);
			System.out.println("Plugboard settings loop:\n"+out);
		}
		//System.out.println(encryptions);
		return new ArrayList<String>();
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
	
	public void run(){
		String input ="";
		while(!(input.equals("quit"))){
			System.out.println("1. rotor permutation settings");
			System.out.println("2. input closure");
			System.out.println("type quit to exit");
			input = scanner.nextLine();
			if(input.equals("1")){
				editRotorPermutation();
			}
			else if(input.equals("2")){
				System.out.println("input closure, e.g. L,20,R,3,T,13");
				String closure = scanner.nextLine();
				
				System.out.println("input starting positions, e.g. ABC");
				String positions = scanner.nextLine();
				
				generatePlugboardSettings(positions,closure);
			}
		}
	}
	
	public static void main(String args[]){
		BombeMachine bombe = new BombeMachine();
		/*bombe.setCipherText("vpaag kddub tajnjkyodnntej");
		bombe.setCrib("hello");
		bombe.generateMenu(1);
		bombe.generatePlugboardSettings("ABC","E,2,O,5,L,3,M,10,K,16,E");*/
		bombe.run();
	}
}