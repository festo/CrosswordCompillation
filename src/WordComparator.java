import java.util.Comparator;

/**
 * Két Word objektumot összehasonlít
 * @author Munkácsy Gergely
 *
 */
public class WordComparator implements Comparator<Word> {
    @Override
    public int compare(Word o1, Word o2) {
    	int cmp = Double.compare(o1.getLookAhead(), o2.getLookAhead());
    	return cmp;
    }
}

