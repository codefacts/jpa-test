package jpatest.core.jpa.models;

import jpatest.core.jpa.models.core.JavaType;

public class RelationTableBuilder {
    private String tableName;
    private String tableNameAlias;
    private String leftColumn;
    private String rightColumn;

    public RelationTableBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public RelationTableBuilder setTableNameAlias(String tableNameAlias) {
        this.tableNameAlias = tableNameAlias;
        return this;
    }

    public RelationTableBuilder setLeftColumn(String leftColumn) {
        this.leftColumn = leftColumn;
        return this;
    }

    public RelationTableBuilder setRightColumn(String rightColumn) {
        this.rightColumn = rightColumn;
        return this;
    }

    public RelationTable createRelationTable() {
        return new RelationTable(tableName, tableNameAlias, leftColumn, rightColumn);
    }
}