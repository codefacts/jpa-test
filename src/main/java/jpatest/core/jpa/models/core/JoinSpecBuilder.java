package jpatest.core.jpa.models.core;

public class JoinSpecBuilder {
    private String joinTable;
    private String joinColumn;
    private JoinType joinType;
    private String joinTableAlias;

    public JoinSpecBuilder setJoinTable(String joinTable) {
        this.joinTable = joinTable;
        return this;
    }

    public JoinSpecBuilder setJoinColumn(String joinColumn) {
        this.joinColumn = joinColumn;
        return this;
    }

    public JoinSpecBuilder setJoinType(JoinType joinType) {
        this.joinType = joinType;
        return this;
    }

    public JoinSpecBuilder setJoinTableAlias(String joinTableAlias) {
        this.joinTableAlias = joinTableAlias;
        return this;
    }

    public JoinSpec createJoinSpec() {
        return new JoinSpec(joinTable, joinColumn, joinType, joinTableAlias);
    }
}