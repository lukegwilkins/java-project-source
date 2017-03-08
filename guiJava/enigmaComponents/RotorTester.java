package enigmaComponents;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Arrays;

public class RotorTester{
	
	private Rotor rotor = new Rotor();
	
	@Test
	public void testSetCircuityCharArray(){
		char[] testCircuit = {'A', 'J', 'D', 'K', 'S', 'I', 'R', 'U', 'X', 'B', 'L', 'H', 'W', 'T', 'M', 'C', 'Q', 'G', 'Z', 'N', 'P', 'Y', 'F', 'V', 'O', 'E'};
		rotor.setCircuitry(testCircuit);
		
		assertEquals(new String(testCircuit),new String(rotor.getCharCircuitry()));
	}
	
	//test that repeat values don't occur
	@Test
	public void testSetCircuitArrayRepeatValues(){
		try{
			int[] testCircuit = {25, 25, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
			rotor.setCircuitry(testCircuit);
			fail();
		}
		catch(IllegalArgumentException e){
			assertEquals("The array cannot have repeat values", e.getMessage());
		}
	}
	
	//checks that array values must be between 0 and 25 inclusive
	@Test
	public void testSetCircuitArrayBadArrayValues(){
		try{
			int[] testCircuit = {25, -4, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
			rotor.setCircuitry(testCircuit);
			fail();
		}
		catch(IllegalArgumentException e){
			assertEquals("The array must have values between 0 and 25 inclusive", e.getMessage());
		}
	}
	
	//checks that the array must be size 26 exactly
	@Test
	public void testSetCircuitArrayWrongSize(){
		try{
			int[] testCircuit = {1,2};
			rotor.setCircuitry(testCircuit);
			fail();
		}
		catch(IllegalArgumentException e){
			assertEquals("The array must have length exactly 26", e.getMessage());
		}
	}
	
	//test the set ring method
	@Test
	public void testSetRingPosition(){
		rotor.setRingPosition(24);
		assertEquals(24, rotor.getRingPosition());
	}
	
	//test the getCharOnTop method
	@Test
	public void testCharOnTop(){
		rotor.setPosition(3);
		assertEquals('c',rotor.getCharOnTop());
		
		rotor.setPosition('n');
		assertEquals('n',rotor.getCharOnTop());
	}
	
	//test the setTurnoverChar method
	@Test
	public void testSetTurnoverChar(){
		rotor.setTurnoverChar('d');
		assertEquals(4,rotor.getTurnoverPosition());
	}
	
	//tests the getTurnoverChar method
	@Test
	public void testGetTurnoverChar(){
		rotor.setTurnoverChar('d');
		assertEquals('d',rotor.getTurnoverChar());
	}
	
	//tests the setNextRotor method
	@Test
	public void testSetNextRotor(){
		Rotor nextRotor = new Rotor();
		rotor.setNextRotor(nextRotor);
		//we use assertTrue as we want to confirm that the objects are the actually the same objects
		assertSame(nextRotor, rotor.getNextRotor());
		
		//check that we cannot have a rotor as its own next rotor
		try{
			rotor.setNextRotor(rotor);
			fail();
		}
		catch(IllegalArgumentException e){
			assertEquals("A rotor cannot have itself as the next rotor",e.getMessage());
		}
	}
	
	//testing the rotate method
	@Test
	public void testRotateMethod(){
		//create a new rotor
		Rotor rotorOne = new Rotor();
		//set the turnoverChar to be 'c'
		rotorOne.setTurnoverChar('c');		
		rotorOne.setPosition('c');
		
		//create another rotor for rotorOne to be linked to
		Rotor rotorTwo = new Rotor();
		rotorTwo.setPosition(1);
		
		//set it so that the rotor next to rotorOne is rotorTwo
		rotorOne.setNextRotor(rotorTwo);
		
		//check the position of rotorOne & rotorTwo before rotating
		assertEquals(3, rotorOne.getPosition());
		assertEquals(1, rotorTwo.getPosition());
		rotorOne.rotate();
		
		//check that rotorOne has rotated
		assertEquals(4, rotorOne.getPosition());
		//check that rotorTwo has rotated
		assertEquals(2, rotorTwo.getPosition());
		
		//rotate rotorOne again
		rotorOne.rotate();
		//check that only rotorOne has rotated
		assertEquals(5, rotorOne.getPosition());
		assertEquals(2,rotorTwo.getPosition());
	}
	
	//tests the encrypt method
	@Test
	public void testEncryptMethod(){
		char[] testCircuit = {'A', 'J', 'D', 'K', 'S', 'I', 'R', 'U', 'X', 'B', 'L', 'H', 'W', 'T', 'M', 'C', 'Q', 'G', 'Z', 'N', 'P', 'Y', 'F', 'V', 'O', 'E'};
		Printer printer = new Printer();
		rotor.setCircuitry(testCircuit);
		rotor.setPrinter(printer);
		rotor.setPosition('a');
		rotor.setRingPosition(24);
		
		int[] reflectorCircuit = {4, 9, 12, 25, 0, 11, 24, 23, 21, 1, 22, 5, 2, 17, 16, 20, 14, 13, 19, 18, 15, 8, 10, 7, 6, 3};
		
		//sets up a reflector to be the next rotor
		BasicRotor reflector = new NonRotReflector();
		reflector.setPrinter(printer);
		//sets it to position 1
		reflector.setPosition(1);
		//sets up the circuitry
		reflector.setCircuitry(reflectorCircuit);
		//sets the reflector to be the next rotor
		rotor.setNextRotor(reflector);		
		
		//checks that 25 encrypts to 22
		assertEquals('f',rotor.encrypt('b'));
	}
	
	//tests faulty data for the encrypt method
	@Test
	public void testInvalidDataEncrypt(){
		int[] testCircuit={25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
		rotor.setCircuitry(testCircuit);
		
		//sets up a reflector to be the next rotor
		BasicRotor reflector = new NonRotReflector();
		//sets it to position 1
		reflector.setPosition(1);
		//sets up the circuitry
		reflector.setCircuitry(testCircuit);
		//sets the reflector to be the next rotor
		rotor.setNextRotor(reflector);
		
		try{
			rotor.encrypt('@');
			//if encrypt doesn't throw an error then we get the test to fail
			fail();
		}
		//we catch an IllegalArgumentException all other Exceptions will fail the test
		catch(IllegalArgumentException e){
			//we check the errors message
			assertEquals("letter must be between a and z inclusive",e.getMessage());
			
		}
		
		try{
			rotor.encrypt('(');
			fail();
		}
		catch(IllegalArgumentException e){
			assertEquals("letter must be between a and z inclusive", e.getMessage());
		}
	}
	
}