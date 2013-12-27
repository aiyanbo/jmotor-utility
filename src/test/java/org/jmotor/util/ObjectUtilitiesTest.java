package org.jmotor.util;

import junit.framework.TestCase;
import org.jmotor.util.meta.Student;
import org.junit.Test;

import java.io.IOException;

/**
 * Component:
 * Description:
 * Date: 11-8-25
 *
 * @author Andy.Ai
 */
public class ObjectUtilitiesTest extends TestCase {
    @Test
    public void testInvoke() {
        Student student = new Student();
        ObjectUtilities.invoke(student, "setStuId", "0002");
        ObjectUtilities.invoke(student, "show", "0002", "Dog");
        System.out.println(student.getStuId());
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        Student student = new Student();
        student.setStuId("00001");
        student.setName("Jobs");
        student.setClassName("IT");
        String fileName = ObjectUtilities.writeObject(student);
        if (StringUtilities.isBlank(fileName)) {
            System.out.println("Failure");
        } else {
            Student readObject = ObjectUtilities.readObject(fileName);
            System.out.println(readObject);
        }
    }

    public void test() {
        System.out.println(StringUtilities.repeat("?", ",", 5));
    }


}
