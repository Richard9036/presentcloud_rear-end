package fz.cs.daoyun.mapper.provider;

import fz.cs.daoyun.domain.UserClasses;
import org.apache.ibatis.jdbc.SQL;

public class UserClassesSqlProvider {

    public String insertSelective(UserClasses record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("t_user_class");

        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }

        if (record.getClassId() != null) {
            sql.VALUES("class_id", "#{classId,jdbcType=INTEGER}");
        }

        if (record.getUserName() != null) {
            sql.VALUES("user_name", "#{userName,jdbcType=VARCHAR}");
        }

        return sql.toString();
    }

    public String updateByPrimaryKeySelective(UserClasses record) {
        SQL sql = new SQL();
        sql.UPDATE("t_user_class");

        if (record.getClassId() != null) {
            sql.SET("class_id = #{classId,jdbcType=INTEGER}");
        }

        if (record.getUserName() != null) {
            sql.SET("user_name = #{userName,jdbcType=VARCHAR}");
        }

        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }
}
