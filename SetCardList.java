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
	
	public static boolean isSet(SetCard one, SetCard two, SetCard three){
		for(int f = 0; f < 4; ++f){
			int sum = one.getFeature(f) + two.getFeature(f) + three.getFeature(f);
			if(sum % 3 != 0) return false;
		}
		return true;
	}
	
	public boolean isSet(){
		if(size() != 3) return false;
		return isSet(get(0), get(1), get(2));
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
