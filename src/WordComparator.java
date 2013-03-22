import java.util.Comparator;

public class WordComparator implements Comparator<Word> {
    @Override
    public int compare(Word o1, Word o2) {
    	int cmp = Double.compare(o1.getLookAhead(), o2.getLookAhead());
//    	int cmp = o1.getLookAhead() > o2.getLookAhead() ? +1 : o1.getLookAhead() < o2.getLookAhead() ? -1 : 0; 
    	return cmp;
    }
}

