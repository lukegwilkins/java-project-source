package enigmaComponents;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class NonRotReflectorTester{

	private NonRotReflector reflector = new NonRotReflector();
	
	/*public void setUp(){
		reflector = new NonRotReflector();
	}*/
	@Test
	public void testSetCircuityCharArray(){
		char[] testCircuit = {'E', 'J', 'M', 'Z', 'A', 'L', 'Y', 'X', 'V', 'B', 'W', 'F', 'C', 'R', 'Q', 'U', 'O', 'N', 'T', 'S', 'P', 'I', 'K', 'H', 'G', 'D'};
		reflector.setCircuitry(testCircuit);
		
		assertEquals(new String(testCircuit),new String(reflector.getCharCircuitry()));
	}
	
	//tests that encryption while the reflector hasn't been rotated works
	@Test
	public void testPositionZeroEncryption() {
		int[] testCircuit = {25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
		reflector.setCircuitry(testCircuit);
		reflector.setPosition(1);
		Printer printer = new Printer();
		reflector.setPrinter(printer);
		assertEquals('z',reflector.encrypt('a'));
	}
	
	//checks that changing the position changes what output the input wires map to
	@Test
	public void testPositionChangeEncrypt(){
		int [] testCircuit = {25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
		reflector.setCircuitry(testCircuit);
		reflector.setPosition(1);
		Printer printer = new Printer();
		reflector.setPrinter(printer);
		assertEquals('z',reflector.encrypt('a'));
		
		reflector.setPosition(2);
		assertEquals('x',reflector.encrypt('a'));
	}
	
	//checks that you cannot have a circuit where an input is mapped to itself
	@Test
	public void testSetCircuitWireMappedToSelf(){
		try{
			int[] testCircuit = {0, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 25};
			reflector.setCircuitry(testCircuit);
			fail();
		}
		catch(IllegalArgumentException e){
			assertEquals("The Reflector unit can't encrypt a wire as itself", e.getMessage());
		}
	}
	
	//checks that the array must be size 26 exactly
	@Test
	public void testSetCircuitArrayWrongSize(){
		try{
			int[] testCircuit = {1,2};
			reflector.setCircuitry(testCircuit);
			fail();
		}
		catch(IllegalArgumentException e){
			assertEquals("The array must have length exactly 26", e.getMessage());
		}
	}
	
	//checks that array values must be between 0 and 25 inclusive
	@Test
	public void testSetCircuitArrayBadArrayValues(){
		try{
			int[] testCircuit = {25, -4, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
			reflector.setCircuitry(testCircuit);
			fail();
		}
		catch(IllegalArgumentException e){
			assertEquals("The array must have values between 0 and 25 inclusive", e.getMessage());
		}
	}
	
	//checks that repeat values can't occur
	@Test
	public void testSetCircuitArrayRepeatValues(){
		try{
			int[] testCircuit = {25, 25, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
			reflector.setCircuitry(testCircuit);
			fail();
		}
		catch(IllegalArgumentException e){
			assertEquals("The array cannot have repeat values", e.getMessage());
		}
	}
	
	//checks that the circuitry must have involution, e.g. if 5 is mapped to 10, then 10 must be mapped to 5
	@Test
	public void testSetCircuitArrayInvolution(){
		try{
			int[] testCircuit = {25, 0, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 24};
			reflector.setCircuitry(testCircuit);
			fail();
		}
		catch(IllegalArgumentException e){
			assertEquals("The Reflector must have involution, e.g. if 5 is connected to 10, 10 must be connected to 5", e.getMessage());
		}
	}
	
}