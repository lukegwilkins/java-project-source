import java.util.*;

public class ClosureComparator implements Comparator<ArrayList<String>>{
	//method that is used to sort the list of closures
	@Override
	public int compare(ArrayList<String> listOne, ArrayList<String> listTwo){
		return listTwo.size()-listOne.size();
	}
}