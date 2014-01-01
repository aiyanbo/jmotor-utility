package org.jmotor.util;

import junit.framework.TestCase;

/**
 * Component:
 * Description:
 * Date: 14-1-1
 *
 * @author Andy Ai
 */
public class StringUtilitiesTest extends TestCase {
    public void testSplitWords() {
        assertEquals("I love", StringUtilities.splitWords(" I love you  ", 2));
        assertEquals("go to parent", StringUtilities.splitWords(" go to parent", 3));
    }
}
