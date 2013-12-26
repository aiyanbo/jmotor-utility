package org.jmotor.util.aop;

import org.jmotor.util.aop.callback.DataLoader;
import org.jmotor.util.aop.cglib.CGLibUtilities;
import org.jmotor.util.aop.meta.ClassMeta;
import org.jmotor.util.aop.meta.StudentMeta;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

import static java.lang.System.out;

/**
 * Component:
 * Description:
 * Date: 12-2-15
 *
 * @author Andy.Ai
 */
public class CGLibUtilitiesTest extends TestCase {
    @Test
    public void test() {

    }

    @Test
    public void testCreateProxyObject() {
        DataLoader dataLoader = new DataLoader();
        ClassMeta _class = CGLibUtilities.createProxyObject(ClassMeta.class, dataLoader);
        List<StudentMeta> students = _class.getStudents();
        out.println(students.size());
    }

}
