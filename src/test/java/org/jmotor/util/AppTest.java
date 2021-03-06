package org.jmotor.util;

import junit.framework.TestCase;
import org.jmotor.util.meta.Student;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AppTest extends TestCase {

    public void test() {
        testPropertyValue();
        testReplace();
    }

    @Test
    public void testPropertyValue() {
        Student stu = new Student();
        ObjectUtilities.setPropertyValue(stu, "stuId", "123");
        System.out.println(ObjectUtilities.getPropertyValue(stu, "stuId"));
    }

    @Test
    public void testReplace() {
        String text = "text|code|age|name|code|cc|bb+|ppp|code|ddd|mmmm|code";
        String[] result = StringUtilities.split(text, "|");
        System.out.println(result.length);
        for (String str : result) {
            System.out.println(str);
        }
        System.out.println(text.replace("|", ">"));
        System.out.println(StringUtilities.replace(text, "code", "CODE"));
        System.out.println(StringUtilities.replace(text, "code", "CODE").length());
        System.out.println(text.length());
    }

    @Test
    public void testDeleteFile() {
        System.out.println(FileUtilities.deleteFile("F:\\aicooc\\test"));
    }

    public void testNameOfDatabase() {
        System.out.println(StringUtilities.nameOfDatabase("fieldName"));
        System.out.println(StringUtilities.nameOfProperty("model_collection_shipment_id"));
        System.out.println(StringUtilities.nameOfProperty("model_collection_report_owner"));
        System.out.println(StringUtilities.nameOfProperty("schedulerName"));
    }

    public void testJoin() {
        Object[] array = new Object[]{"1", 2, 5, null, null, null, 7, 9, 10, null, null};
        System.out.println(StringUtilities.join(array, "-"));
        List list = new ArrayList();
        list.add(1);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add('R');
        list.add("PPPGGG");
        list.add(6);
        list.add(null);
        System.out.println(StringUtilities.join(list, "-"));
        System.out.println(StringUtilities.join("?", 4, ","));
    }

    public void testMD5() {
        System.out.println(MD5Utilities.encode("r"));
    }

    public void testCountMatch() {
        String address = "192.169.0.1";
        System.out.println(StringUtilities.countMatches(address, "|"));
    }

    public void testLoadProperties() throws IOException {
        Properties properties = ResourceUtilities.loadProperties("test.properties");
        System.out.println(properties.getProperty("allow"));
        System.out.println(properties.getProperty("deny"));
    }
}
