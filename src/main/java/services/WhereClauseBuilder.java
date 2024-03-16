package services;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class WhereClauseBuilder {
    
    private final List<String> conditions = new ArrayList<>();
    private StringBuilder currentCondition = new StringBuilder();

    public WhereClauseBuilder column(String columnName) {
        appendToCurrentCondition(columnName);
        return this;
    }

    public WhereClauseBuilder equalsTo(String value) {
        currentCondition.append(" = '").append(value.replace("'", "''")).append("'");
        return this;
    }

    public WhereClauseBuilder in(String... values) {
        StringJoiner valueJoiner = new StringJoiner("', '", "('", "')");
        for (String val : values) {
            valueJoiner.add(val.replace("'", "''"));
        }
        currentCondition.append(" IN ").append(valueJoiner);
        return this;
    }

    public WhereClauseBuilder like(String value) {
        currentCondition.append(" LIKE '%").append(value.replace("'", "''")).append("%'");
        return this;
    }

    public WhereClauseBuilder and() {
        appendLogicalOperator("AND");
        return this;
    }

    public WhereClauseBuilder or() {
        appendLogicalOperator("OR");
        return this;
    }

    public WhereClauseBuilder openParenthesis() {
        conditions.add("(");
        return this;
    }

    public WhereClauseBuilder closeParenthesis() {
        applyCurrentCondition();
        conditions.add(")");
        return this;
    }

    private void appendToCurrentCondition(String condition) {
        if (currentCondition.length() > 0) {
            currentCondition.append(" ");
        }
        currentCondition.append(condition);
    }

    private void appendLogicalOperator(String operator) {
        applyCurrentCondition();
        if (!conditions.isEmpty() && !conditions.get(conditions.size() - 1).equals("(")) {
            conditions.add(operator);
        }
    }

    private void applyCurrentCondition() {
        if (currentCondition.length() > 0) {
            conditions.add(currentCondition.toString());
            currentCondition = new StringBuilder();
        }
    }

    public String build() {
        applyCurrentCondition();
        return String.join(" ", conditions);
    }
}
