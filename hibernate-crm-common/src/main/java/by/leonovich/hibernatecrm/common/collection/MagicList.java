package by.leonovich.hibernatecrm.common.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created : 16/12/2020 20:56
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class MagicList<E> extends ArrayList<E> {
    protected static final Random r = new Random();

    public MagicList() {
        super();
    }

    public MagicList(List<E> elements) {
        super(elements);
    }

    public E randomEntity() {
        return super.get(randIndex());
    }

    public int randIndex() {
        return r.nextInt(super.size() - 1);
    }

    public E lastElement() {
        return super.get(super.size() - 1);
    }
}
