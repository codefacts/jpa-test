package jpatest.core.jpa.models.core;

/**
 * Created by Jango on 9/14/2016.
 */
final public class JoinSpec {
    private final String joinTable;
    private final String joinColumn;
    private final JoinType joinType;
    private final String joinTableAlias;

    public JoinSpec(String joinTable, String joinColumn, JoinType joinType, String joinTableAlias) {
        this.joinTable = joinTable;
        this.joinColumn = joinColumn;
        this.joinType = joinType;
        this.joinTableAlias = joinTableAlias;
    }

    public String getJoinTable() {
        return joinTable;
    }

    public String getJoinColumn() {
        return joinColumn;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public String getJoinTableAlias() {
        return joinTableAlias;
    }
}
