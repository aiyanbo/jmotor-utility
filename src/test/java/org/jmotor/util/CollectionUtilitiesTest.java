package org.jmotor.util;

import junit.framework.TestCase;
import org.jmotor.util.meta.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Component:
 * Description:
 * Date: 12-3-8
 *
 * @author Andy.Ai
 */
public class CollectionUtilitiesTest extends TestCase {
    public void testFilter() {
        List list = new ArrayList();
        list.add(new Person());
        list.add(new Person());
        list.add(1);
        list.add(1);
        list.add(new Person());
        list.add(new Person());
        list.add(new Person());
        list.add(1);
        list.add(new Person());
        list.add(new Person());
        list.add(new Person());
        list.add(1);
        list.add(new Person());
        list.add(new Person());
        list.add(new Person());
        list.add(new Person());
        System.out.println(list.size());
        CollectionUtilities.filter(list, new Validator() {
            @Override
            public boolean validate(Object object) {
                return object instanceof Person;
            }
        });
        System.out.println(list.size());
    }
}
