package Iterator;

import java.util.ArrayList;

public class CollectionIterator<T> implements Iterator<T> {
    
    private ArrayList<T> collection;
    private int index;
    
    public CollectionIterator(ArrayList<T> collection) {
        
        this.collection = collection;
    }
    
    @Override
    public boolean hasNext() {
        
        return index < collection.size();
    }
    
    @Override
    public T next() {
        
        return collection.get(index++);
    }
}