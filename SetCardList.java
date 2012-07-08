import java.util.ArrayList;

public class SetCardList extends ArrayList<SetCard>{
	/*public SetCardList(SetGameGrid grid){
		this.grid = grid;
	}*/
	
	public void deselect(){
		for(int i = 0; i < size(); ++i){
			get(i).setChosen(false);
		}
	}
	
	public void pick(SetCard card){
		if(card.getChosen()) remove(card);
		else add(card);
		card.setChosen(!card.getChosen());
	}
	
	public static int getThird(int one, int two){
		int ret = 0;
		for(int mask = 1; mask < 81; mask *= 3){
			ret += ((2 * 81 - one/mask - two/mask) %3) * mask;
		}
		return ret;
	}
	
	public static boolean isSet(int one, int two, int three){
		for(int mask = 1; mask < 81; mask *= 3){
			if(((one / mask) + (two / mask) + (three / mask)) % 3 != 0) return false;
		}
		return true;
	}
	
	private static boolean isSuperSet(int a, int b, int c, int d)	{
		for(int mask = 1; mask < 81; mask *= 3){
			int x = (a/mask)+b/mask;
			x %= 3;
			int y = c/mask + d/mask;
			y %= 3;
			if(x != y)
				return false;
		}
		return true;
	}
	
	public static boolean isSet(SetCard one, SetCard two, SetCard three){
		return isSet(one.getMod(), two.getMod(), three.getMod());
	}
	
	public static boolean isSuperSet(SetCard one, SetCard two, SetCard three, SetCard four){
		return isSuperSet(one.getMod(), two.getMod(), three.getMod(), four.getMod());
	}
	
	public boolean isSet(){
		if(size() != 3) return false;
		return isSet(get(0), get(1), get(2));
	}
	
	public boolean isSuperSet()	{
		if(size() != 4) return false;
		return isSuperSet(get(0), get(1), get(2), get(3));
	}
	
	public boolean equals(Object o){
		SetCardList other;
		try{
			other = (SetCardList) o;
		}
		catch(Exception e){
			return false;
		}
		for(SetCard card : this){
			if(!other.contains(card)) return false;
		}
		return true;
	}
	
	//SetGameGrid grid;
}
