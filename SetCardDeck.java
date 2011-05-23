import java.util.ArrayList;

public class SetCardDeck extends ArrayList<SetCard> {
	public boolean add(SetCard card){
		boolean ret = super.add(card);
		top++;
		return ret;
	}
	
	public boolean hasNext(){
		return top != 0;
	}
	
	public SetCard pop(){
		return(get(--top));
	}
	
	int top = 0;
}
