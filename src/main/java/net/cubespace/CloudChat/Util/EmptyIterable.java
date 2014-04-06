package net.cubespace.CloudChat.Util;

import java.util.Iterator;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class EmptyIterable<T> implements Iterable<T> {
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                return null;
            }

            @Override
            public void remove() {

            }
        };
    }
}
