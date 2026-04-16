package com.wendell.backend.infra.h2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.h2.api.Trigger;

public class AuditoriaNotaTrigger implements Trigger {

    private static final int IDX_STUDENT_ID = 1;
    private static final int IDX_EVALUATION_ID = 2;
    private static final int IDX_GRADE_VALUE = 3;

    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before,
            int type) {
    }

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        if (newRow == null) {
            return;
        }

        Long studentId = toLong(newRow[IDX_STUDENT_ID]);
        Long evaluationId = toLong(newRow[IDX_EVALUATION_ID]);
        BigDecimal newValue = toBigDecimal(newRow[IDX_GRADE_VALUE]);
        BigDecimal oldValue = oldRow == null ? newValue : toBigDecimal(oldRow[IDX_GRADE_VALUE]);
        Long modifiedBy = findModifiedBy(conn);

        if (oldRow != null && oldValue.compareTo(newValue) == 0) {
            return;
        }

        // Durante carga inicial (data.sql), inserts podem ocorrer sem contexto de usuario.
        // Ignora somente insert sem usuario para nao quebrar bootstrap.
        if (modifiedBy == null && oldRow == null) {
            return;
        }

        if (modifiedBy == null) {
            throw new SQLException("Usuario modificador nao informado para auditoria de nota");
        }

        Long disciplineId = findDisciplineId(conn, evaluationId);
        insertAudit(conn, studentId, evaluationId, disciplineId, modifiedBy, oldValue, newValue);
    }

    @Override
    public void close() {
        // no-op
    }

    @Override
    public void remove() {
        // no-op
    }

    private Long findDisciplineId(Connection conn, Long evaluationId) throws SQLException {
        String sql = "SELECT discipline_id FROM evaluation WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, evaluationId);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Avaliacao nao encontrada para auditoria: " + evaluationId);
                }
                return rs.getLong(1);
            }
        }
    }

    private Long findModifiedBy(Connection conn) throws SQLException {
        String sql = "SELECT @MODIFIED_BY";
        try (PreparedStatement ps = conn.prepareStatement(sql); var rs = ps.executeQuery()) {
            if (!rs.next()) {
                return null;
            }

            Object value = rs.getObject(1);
            if (value == null) {
                return null;
            }

            return toLong(value);
        } catch (SQLException ex) {
            // Em bootstrap inicial, a variavel de sessao pode ainda nao existir no H2.
            // Nessa situacao tratamos como ausente para nao quebrar inserts de seed.
            String message = ex.getMessage() == null ? "" : ex.getMessage().toLowerCase();
            if (message.contains("modified_by") && message.contains("not found")) {
                return null;
            }
            throw ex;
        }
    }

    private void insertAudit(Connection conn, Long studentId, Long evaluationId, Long disciplineId, Long modifiedBy,
            BigDecimal oldValue, BigDecimal newValue) throws SQLException {
        String sql = """
                INSERT INTO grade_audit
                    (student_id, evaluation_id, discipline_id, modified_by, old_value, new_value, modification_date)
                VALUES
                    (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, studentId);
            ps.setLong(2, evaluationId);
            ps.setLong(3, disciplineId);
            ps.setLong(4, modifiedBy);
            ps.setBigDecimal(5, oldValue);
            ps.setBigDecimal(6, newValue);
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
    }

    private Long toLong(Object value) {
        if (value instanceof Number n) {
            return n.longValue();
        }
        return Long.valueOf(String.valueOf(value));
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal bd) {
            return bd;
        }
        if (value instanceof Number n) {
            return BigDecimal.valueOf(n.doubleValue());
        }
        return new BigDecimal(String.valueOf(value));
    }
}
