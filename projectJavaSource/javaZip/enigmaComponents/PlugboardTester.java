package enigmaComponents;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;

public class PlugboardTester{
	
	private Plugboard plugboard = new Plugboard();
	
	@Test
	public void testSwap(){
		plugboard.swap('a','z');
		plugboard.swap('g','f');
		ArrayList<String> result = new ArrayList<String>();
		
		result.add("A/Z");
		result.add("F/G");
		assertEquals(result,plugboard.getSwapMapping());
	}
	
	@Test public void testUnSwap(){
		plugboard.swap('a','z');
		ArrayList<String> result = new ArrayList<String>();
		
		result.add("A/Z");
		//check swap occurred
		assertEquals(result,plugboard.getSwapMapping());
		
		plugboard.unswap('a','z');
		
		result.remove(0);
		
		assertEquals(result,plugboard.getSwapMapping());
	}
	
	@Test
	public void testSetScrambler(){
		Scrambler scrambler = new Scrambler();
		plugboard.setScrambler(scrambler);
		
		assertSame(scrambler, plugboard.getScrambler());
	}
	
	@Test
	public void testSetSwapMapping(){
		ArrayList<String> result = new ArrayList<String>();
		result.add("B/D");
		result.add("L/M");
		
		plugboard.setSwapMapping(result);
		
		assertEquals(result, plugboard.getSwapMapping());
	}
	
	@Test
	public void testEncrypt(){
		Printer printer = new Printer();
		Rotor rotorOne = new Rotor();
		Rotor rotorTwo = new Rotor();
		Rotor rotorThree = new Rotor();
		Scrambler scrambler = new Scrambler();
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
		
		plugboard.setScrambler(scrambler);
		
		plugboard.swap('a','m');
		plugboard.swap('f','i');
		plugboard.swap('n','v');
		
		plugboard.swap('p','s');
		plugboard.swap('t','u');
		plugboard.swap('w','z');
		
		char result = plugboard.encrypt('G');
		assertEquals('f',result);
		
		result = plugboard.encrypt('c');
		assertEquals('e',result);
		
		result = plugboard.encrypt('d');
		assertEquals('i',result);
		
		result = plugboard.encrypt('s');
		assertEquals('n',result);
		
		result = plugboard.encrypt('e');
		assertEquals('d',result);
	}
}