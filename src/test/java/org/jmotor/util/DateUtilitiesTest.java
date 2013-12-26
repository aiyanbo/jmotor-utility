package org.jmotor.util;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Date;

/**
 * Component:
 * Description:
 * Date: 11-8-18
 *
 * @author Andy.Ai
 */
public class DateUtilitiesTest extends TestCase {
    public void test() {
        testFormatDate(new Date());
    }

    @Test
    public void testFormatDate(Date date) {
        System.out.println(DateUtilities.formatDate(date));
    }
}
