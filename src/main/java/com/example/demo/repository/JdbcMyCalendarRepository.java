package com.example.demo.repository;

import com.example.demo.model.UniversalRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class JdbcMyCalendarRepository implements MyCalendarRepository {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    @PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
    }

    @Override
    public String findMyCalendars(UniversalRequest request){
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("cntr_m2")
                .withProcedureName(request.getFunction())
//                .declareParameters(
//                        new SqlParameter("p_id_year_in", Types.INTEGER))
                .withoutProcedureColumnMetaDataAccess()
                .withNamedBinding();
//                .returningResultSet("calendars");
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        Map<String, Object> out = null;

        if ((request.getParam() == null || request.getValue() == null) || (request.getParameters() == null || request.getValues() == null)){

            out = simpleJdbcCall.execute();
        }

        if (request.getParam() != null && request.getValue() != null){
            simpleJdbcCall.declareParameters(new SqlParameter(request.getParam(), Types.INTEGER));
            parameters.addValue(request.getParam(), request.getValue());
            out = simpleJdbcCall.execute(parameters);
        }

        if (request.getParameters() != null && request.getValues() != null) {
            for (int i = 0; i < request.getParameters().length -1; i++){
                simpleJdbcCall.declareParameters(new SqlParameter(request.getParameters()[i], Types.INTEGER));
            }

            for (int i = 0; i < request.getParameters().length -1; i++){
                parameters.addValue(request.getParameters()[i], request.getValues()[i]);
            }
            out = simpleJdbcCall.execute(parameters);
        }


        if (out == null) {
            return "No result";
        }

        List<Map<String, Object>> list = (List<Map<String, Object>>) out.get("#result-set-1");
        Map<String, Object> results = (Map) list.get(0);
        ResultSet rs = (ResultSet) results.get("result");
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData rsmd = null;
        try {
            rsmd = rs.getMetaData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int columnCount = 0;
        try {
            columnCount = rsmd.getColumnCount();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            // Represent a row in DB. Key: Column name, Value: Column value
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                // Note that the index is 1-based
                String colName = null;
                try {
                    colName = rsmd.getColumnName(i);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                Object colVal = null;
                try {
                    colVal = rs.getObject(i);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                row.put(colName, colVal);
            }
            rows.add(row);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String result = null;
        try {
            result = objectMapper.writeValueAsString(rows);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
