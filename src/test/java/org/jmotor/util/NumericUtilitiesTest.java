package org.jmotor.util;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Component:
 * Description:
 * Date: 11-11-10
 *
 * @author Andy.Ai
 */
public class NumericUtilitiesTest extends TestCase {
    @Test
    public void test() {
        int random = NumericUtilities.randomInteger("[-666,789)");
        System.out.println(random);
    }
}
