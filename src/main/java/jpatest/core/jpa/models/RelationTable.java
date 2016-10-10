package jpatest.core.jpa.models;

import jpatest.core.jpa.models.core.JavaType;

/**
 * Created by Jango on 9/30/2016.
 */
public class RelationTable {
    private final String tableName;
    private final String tableNameAlias;
    private final String leftColumn;
    private final String rightColumn;

    public RelationTable(String tableName, String tableNameAlias, String leftColumn, String rightColumn) {
        this.tableName = tableName;
        this.tableNameAlias = tableNameAlias;
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public String getLeftColumn() {
        return leftColumn;
    }

    public String getRightColumn() {
        return rightColumn;
    }

}
