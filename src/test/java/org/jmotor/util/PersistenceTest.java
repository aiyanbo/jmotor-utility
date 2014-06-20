package org.jmotor.util;

import org.jmotor.util.domain.Seller;
import org.jmotor.util.persistence.SqlGenerator;
import org.jmotor.util.persistence.dto.SqlStatement;
import org.jmotor.util.persistence.impl.SqlGeneratorImpl;
import org.junit.Test;

/**
 * Component:
 * Description:
 * Date: 14-6-20
 *
 * @author Andy Ai
 */
public class PersistenceTest {
    @Test
    public void testIgnore() {
        SqlGenerator sqlGenerator = new SqlGeneratorImpl();
        SqlStatement sqlStatement = sqlGenerator.generateUpdateSql(Seller.class);
        System.out.println(sqlStatement.getSql());
        sqlStatement = sqlGenerator.generateInsertSql(Seller.class);
        System.out.println(sqlStatement.getSql());
    }
}
