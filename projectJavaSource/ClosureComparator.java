import java.util.*;

public class ClosureComparator implements Comparator<ArrayList<String>>{
	
	@Override
	public int compare(ArrayList<String> listOne, ArrayList<String> listTwo){
		return listTwo.size()-listOne.size();
	}
}