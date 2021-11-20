package by.leonovich.hibernatecrm.common.collection;

import java.io.Serial;
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
public final class MagicList<E> extends ArrayList<E> {
    @Serial
    private static final long serialVersionUID = -6382952508051769260L;
    private static final Random RANDOM = new Random();

    public MagicList() {
        super();
    }

    public MagicList(final List<E> elements) {
        super(elements);
    }

    public E randomEntity() {
        return super.get(randIndex());
    }

    public int randIndex() {
        return RANDOM.nextInt(super.size() - 1);
    }

    public E lastElement() {
        return super.get(super.size() - 1);
    }
}
