package org.jmotor.util.persistence.dto;

/**
 * Component:
 * Description:
 * Date: 13-11-13
 *
 * @author Andy Ai
 */
public class ColumnMeta {
    private String name;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
