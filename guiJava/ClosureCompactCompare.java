import java.util.*;

public class ClosureCompactCompare implements Comparator<ArrayList<String>>{
	
	@Override
	public int compare(ArrayList<String> listOne, ArrayList<String> listTwo){
		int listOneNo=0;
		int listTwoNo=0;
		
		for(int i=1; i<listOne.size();i+=2){
			if(Integer.parseInt(listOne.get(i))>listOneNo){
				listOneNo=Integer.parseInt(listOne.get(i));
			}
		}
		
		for(int i=1; i<listTwo.size();i+=2){
			if(Integer.parseInt(listTwo.get(i))>listTwoNo){
				listTwoNo=Integer.parseInt(listTwo.get(i));
			}
		}
		return listOneNo-listTwoNo;
	}
}