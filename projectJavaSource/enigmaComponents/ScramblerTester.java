package enigmaComponents;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class ScramblerTester{
	private Scrambler scrambler = new Scrambler();
	
	//test the setFirstRotor method
	@Test
	public void testSetFirstRotorMethod(){
		//we check that the initialized version of scrambler has 4 rotors
		assertEquals(4,scrambler.getAmountOfRotors());
		
		//we create a new first rotor
		Rotor newFirstRotor = new Rotor();
		scrambler.setFirstRotor(newFirstRotor);
		
		//we check that the first rotor was set properly
		assertSame(newFirstRotor,scrambler.getFirstRotor());
		//we check that there are now 2 rotors, our new rotor and the reflector it was initialized
		//with
		assertEquals(2,scrambler.getAmountOfRotors());
	}
	
	@Test
	
	public void testGetRotorAtPos(){
		
		//we create a new linked list of rotors
		Rotor rotorOne = new Rotor();
		Rotor rotorTwo = new Rotor();
		
		rotorOne.setNextRotor(rotorTwo);
		
		//set the first rotor
		scrambler.setFirstRotor(rotorOne);
		
		//assert the objects are the same
		assertSame(rotorTwo,scrambler.getRotorAtPos(1));
	}
	
	@Test
	public void testSetRotorAtPos(){
		Rotor rotorOne = new Rotor();
		Rotor rotorTwo = new Rotor();
		Rotor rotorThree = new Rotor();
		
		rotorTwo.setNextRotor(rotorThree);
		rotorOne.setNextRotor(rotorTwo);
		scrambler.setFirstRotor(rotorOne);
		
		Rotor testRotor = new Rotor();
		
		scrambler.setRotorAtPos(testRotor, 0);
		assertSame(testRotor, scrambler.getFirstRotor());
		assertSame(rotorTwo,testRotor.getNextRotor());
		assertEquals(4,scrambler.getAmountOfRotors());
		
		scrambler.setFirstRotor(rotorOne);
		scrambler.setRotorAtPos(testRotor, 3);
		assertSame(testRotor,scrambler.getRotorAtPos(3));
		assertEquals(5, scrambler.getAmountOfRotors());
		
		rotorThree.setNextRotor(new NonRotReflector());
		rotorTwo.setNextRotor(rotorThree);
		rotorOne.setNextRotor(rotorTwo);
		scrambler.setFirstRotor(rotorOne);
		
		scrambler.setRotorAtPos(testRotor,1);
		assertSame(scrambler.getRotorAtPos(1), testRotor);
		assertSame(testRotor, rotorOne.getNextRotor());
		assertEquals(4,scrambler.getAmountOfRotors());
	}
	
	@Test
	public void testEncrypt(){
		Printer printer = new Printer();
		Rotor rotorOne = new Rotor();
		Rotor rotorTwo = new Rotor();
		Rotor rotorThree = new Rotor();
		
		Reflector reflector = new NonRotReflector();
		
		char[] rotorOneCir ={'E','K','M','F','L','G','D','Q','V','Z','N','T','O','W','Y','H','X','U','S','P','A','I','B','R','C','J'};
		rotorOne.setCircuitry(rotorOneCir);
		
		char[] rotorTwoCir = {'A', 'J', 'D', 'K', 'S', 'I', 'R', 'U', 'X', 'B', 'L', 'H', 'W', 'T', 'M', 'C', 'Q', 'G', 'Z', 'N', 'P', 'Y', 'F', 'V', 'O', 'E'};
		rotorTwo.setCircuitry(rotorTwoCir);
		
		char[] rotorThreeCir ={'B','D','F','H','J','L','C','P','R','T','X','V','Z','N','Y','E','I','W','G','A','K','M','U','S','Q','O'};
		rotorThree.setCircuitry(rotorThreeCir);

		int[] reflectorCir ={4, 9, 12, 25, 0, 11, 24, 23, 21, 1, 22, 5, 2, 17, 16, 20, 14, 13, 19, 18, 15, 8, 10, 7, 6, 3};
		reflector.setCircuitry(reflectorCir);
		
		rotorOne.setTurnoverChar('q');
		rotorTwo.setTurnoverChar('e');
		rotorThree.setTurnoverChar('v');
		
		rotorThree.setNextRotor(rotorOne);
		rotorOne.setNextRotor(rotorTwo);
		rotorTwo.setNextRotor(reflector);
		
		rotorThree.setPosition('l');
		rotorThree.setRingPosition(22);
		
		rotorOne.setPosition('b');
		rotorOne.setRingPosition(13);
		
		rotorTwo.setPosition('a');
		rotorTwo.setRingPosition(24);
		
		scrambler.setFirstRotor(rotorThree);
		
		scrambler.setPrinter(printer);
		
		rotorOne.setPrinter(printer);
		rotorTwo.setPrinter(printer);
		rotorThree.setPrinter(printer);
		
		reflector.setPrinter(printer);
		
		char result = scrambler.encrypt('G');
		assertEquals('i',result);
		
		result = scrambler.encrypt('c');
		assertEquals('e',result);
		
		result = scrambler.encrypt('d');
		assertEquals('f',result);
		
	}
	
	@Test
	public void testEncryptAlphabet(){
		Printer printer = new Printer();
		Rotor rotorOne = new Rotor();
		Rotor rotorTwo = new Rotor();
		Rotor rotorThree = new Rotor();
		
		Reflector reflector = new NonRotReflector();
		
		char[] rotorOneCir ={'E','K','M','F','L','G','D','Q','V','Z','N','T','O','W','Y','H','X','U','S','P','A','I','B','R','C','J'};
		rotorOne.setCircuitry(rotorOneCir);
		
		char[] rotorTwoCir = {'A', 'J', 'D', 'K', 'S', 'I', 'R', 'U', 'X', 'B', 'L', 'H', 'W', 'T', 'M', 'C', 'Q', 'G', 'Z', 'N', 'P', 'Y', 'F', 'V', 'O', 'E'};
		rotorTwo.setCircuitry(rotorTwoCir);
		
		char[] rotorThreeCir ={'B','D','F','H','J','L','C','P','R','T','X','V','Z','N','Y','E','I','W','G','A','K','M','U','S','Q','O'};
		rotorThree.setCircuitry(rotorThreeCir);
		
		char[] reflectorCir = {'Y','R','U','H','Q','S','L','D','P','X','N','G','O','K','M','I','E','B','F','Z','C','W','V','J','A','T'};
		reflector.setCircuitry(reflectorCir);
		
		rotorThree.setNextRotor(rotorTwo);
		rotorTwo.setNextRotor(rotorOne);
		rotorOne.setNextRotor(reflector);
		
		scrambler.setFirstRotor(rotorThree);
		
		scrambler.setPrinter(printer);
		
		rotorOne.setPrinter(printer);
		rotorTwo.setPrinter(printer);
		rotorThree.setPrinter(printer);
		
		reflector.setPrinter(printer);
		
		char[] expectedResult={'u','e','j','o','b','t','p','z','w','c','n','s','r','k','d','g','v','m','l','f','a','q','i','y','x','h'};
		char[] result=scrambler.encryptAlphabet();
		assertEquals(new String(expectedResult),new String(result));
		
	}
}