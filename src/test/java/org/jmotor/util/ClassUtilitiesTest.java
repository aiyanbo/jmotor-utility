package org.jmotor.util;

import junit.framework.TestCase;
import org.jmotor.util.interfaces.SupperInterface;
import org.jmotor.util.interfaces.impl.ResourceAdapter;
import org.jmotor.util.meta.AbstractClass;
import org.jmotor.util.meta.Student;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static java.lang.System.out;

/**
 * Component:
 * Description:
 * Date: 11-8-19
 *
 * @author Andy.Ai
 */
public class ClassUtilitiesTest extends TestCase {
    public void test() {
        testGetClassLoader();
        testGetClass();
        testGetSupperClass();
    }

    @Test
    public void testGetClassLoader() {
        out.println(ClassUtilitiesTest.class.getClassLoader());
        out.println(ClassUtilities.getClassLoader());
    }

    @Test
    public void testGetClass() {
        Student student = new Student();
        out.println(ObjectUtilities.getClass(student));
        out.println(AbstractClass.class.getName());
    }

    @Test
    public void testGetSupperClass() {
        Class<?>[] supperClasses = ClassUtilities.getSuperClasses(Student.class);
        Class<?>[] interfaces = ClassUtilities.getInterfaces(ResourceAdapter.class);
        out.println(supperClasses.length + "\t" + interfaces.length);
        out.println(ClassUtilities.isRelationship(List.class, Collection.class));
        out.println(ClassUtilities.isRelationship(ResourceAdapter.class, SupperInterface.class));
        out.println(ClassUtilities.isRelationship(ResourceAdapter.class, Collection.class));
    }
}
