package services;

import java.util.ArrayList;
import java.util.List;


public class QueryBuilder {
    private StringBuilder selectClause = new StringBuilder();
    private String fromClause = "";
    private List<String> joinClauses = new ArrayList<>();
    private StringBuilder groupByClause = new StringBuilder();
    private StringBuilder havingClause = new StringBuilder();
    private StringBuilder orderByClause = new StringBuilder();
    private WhereClauseBuilder whereBuilder = new WhereClauseBuilder();
    private Boolean stamp = false;
    private String whereClause = new String();

    public QueryBuilder select(String... columns) {
        this.selectClause.append(String.join(", ", columns));
        return this;
    }

    public QueryBuilder from(String table) {
        this.fromClause = table;
        return this;
    }

    public QueryBuilder join(String joinExpression) {
        this.joinClauses.add(joinExpression);
        return this;
    }

    public WhereClauseBuilder where() {
        return this.whereBuilder;
    }
    public QueryBuilder where(WhereClauseBuilder whereBuilder) {
        this.whereBuilder = whereBuilder;
        return this;
    }
    public QueryBuilder where(String whereClause) {
        this.whereClause = whereClause;
        return this;
    }
    
    public QueryBuilder stamp(boolean stamp){
        this.stamp = stamp;
        return this;
    }

    public QueryBuilder groupBy(String... groupingExpressions) {
        if (groupingExpressions.length > 0) {
            this.groupByClause.append(String.join(", ", groupingExpressions));
        }
        return this;
    }

    public QueryBuilder having(String condition) {
        if (this.havingClause.length() > 0) {
            this.havingClause.append(" AND ");
        }
        this.havingClause.append(condition);
        return this;
    }

    public QueryBuilder orderBy(String... orderingExpressions) {
        if (orderingExpressions.length > 0) {
            this.orderByClause.append(String.join(", ", orderingExpressions));
        }
        return this;
    }
    
    public String build() {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(selectClause);
        query.append(" FROM ").append(fromClause);

        if (!joinClauses.isEmpty()) {
            joinClauses.forEach(join -> query.append(" JOIN ").append(join));
        }

        if (!this.whereBuilder.build().isEmpty() && !this.whereBuilder.build().isBlank()) {
            query.append(" WHERE ").append(whereBuilder.build());
        }else if(!this.whereClause.isEmpty() && !this.whereClause.isBlank()){
            query.append(" WHERE ").append(whereClause);
        }
        if (this.groupByClause.length() > 0) {
            query.append(" GROUP BY ").append(this.groupByClause);
        }

        if (this.havingClause.length() > 0) {
            query.append(" HAVING ").append(this.havingClause);
        }

        if (this.orderByClause.length() > 0) {
            query.append(" ORDER BY ").append(this.orderByClause);
        }

        if (this.stamp) {
            System.out.println(query.toString());
        }
        return query.toString();
    }
}
