import enigmaComponents.Scrambler;
import enigmaComponents.Rotor;
import enigmaComponents.NonRotReflector;
import enigmaComponents.BasicRotor;
import enigmaComponents.Printer;

import java.util.Scanner;
import java.util.Arrays;

public class DemoEnigma{
	
	public static void main(String args[]){
		Printer printer = new Printer();
		BasicRotor[] rotors = new BasicRotor[4];
		Rotor firstRotor = new Rotor();
		Rotor secondRotor = new Rotor();
		Rotor thirdRotor = new Rotor();
		NonRotReflector reflector = new NonRotReflector();

		thirdRotor.setNextRotor(reflector);
		secondRotor.setNextRotor(thirdRotor);
		firstRotor.setNextRotor(secondRotor);
		
		reflector.setPrinter(printer);
		thirdRotor.setPrinter(printer);
		secondRotor.setPrinter(printer);
		firstRotor.setPrinter(printer);
		
		rotors[0] = firstRotor;
		rotors[1] = secondRotor;
		rotors[2] = thirdRotor;
		rotors[3] = reflector;
		
		int[] testCircuit={25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
		
		reflector.setCircuitry(testCircuit);
		thirdRotor.setCircuitry(testCircuit);
		secondRotor.setCircuitry(testCircuit);
		firstRotor.setCircuitry(testCircuit);
		
		Scrambler scrambler = new Scrambler();
		scrambler.setFirstRotor(firstRotor);
		scrambler.setPrinter(printer);
		//scrambler.encrypt(args[0].charAt(0));
		Scanner scanner = new Scanner(System.in);
		String input ="";
		String orientation="1, 2, 3";
		while(!(input.equals("quit"))){
			System.out.println("1. To encrypt letters");
			System.out.println("2. To edit rotors settings");
			System.out.println("3. Edit rotor orientation");
			System.out.println("Type quit to quit");
			
			input = scanner.nextLine();
			if(!(input.equals("quit"))){
				if (input.equals("1")){
					Rotor rotorOne= (Rotor) scrambler.getFirstRotor();
					Rotor rotorTwo = (Rotor) rotorOne.getNextRotor();
					Rotor rotorThree = (Rotor) rotorTwo.getNextRotor();
					System.out.println(""+rotorOne.getCharOnTop()+", "+rotorTwo.getCharOnTop()+", "+rotorThree.getCharOnTop());
					while(!(input.equals("menu"))){
						System.out.println("Input character to encrypt, type menu to go back to the main menu");
						input=scanner.nextLine();
						if(!(input.equals("menu"))){
							scrambler.encrypt(input.charAt(0));
							System.out.println(""+rotorOne.getCharOnTop()+", "+rotorTwo.getCharOnTop()+", "+rotorThree.getCharOnTop());
						}
					}
				}
				else if(input.equals("2")){
					
					while(!(input.equals("menu"))){	
						System.out.println("Which rotor do you wish to edit.\n1, 2, 3 or R the Reflector, type menu to go back to the main menu");
						input = scanner.nextLine();
						if(!(input.equals("menu"))){
							if(!(input.equals("R"))){
								int rotor = Integer.parseInt(input);
								while(!(input.equals("exit"))){
									System.out.println("Editing "+rotor);							
									System.out.println("1. view circuitry");
									System.out.println("2. edit circuitry");
									System.out.println("3. view position");
									System.out.println("4. change position");
									System.out.println("5. get ring position");
									System.out.println("6. set ring position");
									System.out.println("7. get turnover char");
									System.out.println("8. set turnover char");
									System.out.println("exit to return to rotor menu");
									
									input = scanner.nextLine();
									if(input.equals("1")){
										int[] circuit = ((Rotor) rotors[rotor-1]).getCircuitry();
										for(int i=0; i<26; i++){
											System.out.println(""+i+" -> "+circuit[i]);
										}
									}
									else if(input.equals("2")){
										System.out.println("Input the circuit, as a list, which the first entry been what the first wire goes to, the second, what the second wire goes to and so on.");
										input=scanner.nextLine();
										input = input.replaceAll("\\s+","");
										String[] stringArray=input.split(",");
										
										int[] circuit = new int[26];										
										for(int i = 0;i<26;i++){
											circuit[i] = Integer.parseInt(stringArray[i]);
										}
										System.out.println(Arrays.toString(circuit));
										rotors[rotor-1].setCircuitry(circuit);
									}
									else if(input.equals("3")){
										System.out.println("Position for rotor "+rotor+" is:\n"+rotors[rotor-1].getPosition());
										
									}
									else if(input.equals("4")){
										System.out.println("Input position");
										int position = scanner.nextInt();
										rotors[rotor-1].setPosition(position);
										System.out.println(((Rotor)scrambler.getFirstRotor()).getPosition());
									}
									else if(input.equals("5")){
										System.out.println("The ring position is: \n"+((Rotor) rotors[rotor-1]).getRingPosition());
									}
									else if(input.equals("6")){
										System.out.println("Input character");
										char character = scanner.nextLine().charAt(0);
										System.out.println("Input what wire that character is over");
										int position = scanner.nextInt();
										((Rotor) rotors[rotor-1]).setRingPosition(character, position);
									}
									else if(input.equals("7")){
										System.out.println("The turnover char is "+((Rotor) rotors[rotor-1]).getTurnoverChar());
									}
									else if(input.equals("8")){
										System.out.println("Input the new turnover char");
										char character = scanner.nextLine().charAt(0);
										((Rotor) rotors[rotor-1]).setTurnoverChar(character);
									}
								}
							}
							else{
								int rotor =4;
								while(!(input.equals("exit"))){
									System.out.println("Editing R");							
									System.out.println("1. view circuitry");
									System.out.println("2. edit circuitry");
									System.out.println("3. view position");
									System.out.println("4. change position");
									System.out.println("exit to return to rotor menu");
									
									input = scanner.nextLine();
									if(input.equals("1")){
										int[] circuit = ((Rotor) rotors[rotor-1]).getCircuitry();
										for(int i=0; i<26; i++){
											System.out.println(""+i+" -> "+circuit[i]);
										}
									}
									else if(input.equals("2")){
										System.out.println("Input the circuit, as a list, which the first entry been what the first wire goes to, the second, what the second wire goes to and so on.");
										input=scanner.nextLine();
										input = input.replaceAll("\\s+","");
										String[] stringArray=input.split(",");
										
										int[] circuit = new int[26];										
										for(int i = 0;i<26;i++){
											circuit[i] = Integer.parseInt(stringArray[i]);
										}
										System.out.println(Arrays.toString(circuit));
										rotors[rotor-1].setCircuitry(circuit);
									}
									else if(input.equals("3")){
										System.out.println("Position for rotor R is:\n"+rotors[rotor-1].getPosition());
										
									}
									else if(input.equals("4")){
										System.out.println("Input position");
										int position = scanner.nextInt();
										rotors[rotor-1].setPosition(position);
									}
								}
							}
						}
					}
				}
				else if(input.equals("3")){					
					while(!(input.equals("menu"))){
						System.out.println("1. view orientation");
						System.out.println("2. edit orientation");
						System.out.println("Type menu to return to the main menu");
						input = scanner.nextLine();
						if(input.equals("1")){
							System.out.println("The orientation is "+orientation);
						}
						else if(input.equals("2")){
							System.out.println("Input the orientation, e.g. input 1, 2, 3 if you want to have rotor 1 as the Left rotor, 2 as the middle, and 3 as the right rotor");
							input = scanner.nextLine();
							orientation = input;
							input = input.replaceAll("\\s+","");
							String[] stringPositions = input.split(",");
							int rotor;
							BasicRotor lastRotor = reflector;
							for(int i=2; i>-1;i--){
								rotor = Integer.parseInt(stringPositions[i]);
								((Rotor) rotors[rotor-1]).setNextRotor(lastRotor);
								lastRotor = rotors[rotor-1];
							}
							scrambler.setFirstRotor(lastRotor);
						}
					}
				}
			}
		}
	}
		
}
	