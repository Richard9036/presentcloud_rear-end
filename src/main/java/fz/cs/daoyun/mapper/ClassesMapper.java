package fz.cs.daoyun.mapper;


import fz.cs.daoyun.domain.Classes;
import fz.cs.daoyun.mapper.provider.ClassesSqlProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface ClassesMapper {
    @Delete({
        "delete from t_class",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into t_class (id, classes_id, ",
        "classes_name, school, ",
        "department, teacher_id, ",
        "teacher_name)",
        "values (#{id,jdbcType=INTEGER}, #{classesId,jdbcType=INTEGER}, ",
        "#{classesName,jdbcType=VARCHAR}, #{school,jdbcType=VARCHAR}, ",
        "#{department,jdbcType=VARCHAR}, #{teacherId,jdbcType=VARCHAR}, ",
        "#{teacherName,jdbcType=VARCHAR})"
    })
    int insert(Classes record);

    @InsertProvider(type=ClassesSqlProvider.class, method="insertSelective")
    int insertSelective(Classes record);

    @Select({
        "select",
        "id, classes_id, classes_name, school, department, teacher_id, teacher_name, ",
        "from t_class",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="classes_id", property="classesId", jdbcType=JdbcType.INTEGER),
        @Result(column="classes_name", property="classesName", jdbcType=JdbcType.VARCHAR),
        @Result(column="school", property="school", jdbcType=JdbcType.VARCHAR),
        @Result(column="department", property="department", jdbcType=JdbcType.VARCHAR),
        @Result(column="teacher_id", property="teacherId", jdbcType=JdbcType.VARCHAR),
        @Result(column="teacher_name", property="teacherName", jdbcType=JdbcType.VARCHAR),
    })
    Classes selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ClassesSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Classes record);

    @Update({
        "update t_class",
        "set classes_id = #{classesId,jdbcType=INTEGER},",
          "classes_name = #{classesName,jdbcType=VARCHAR},",
          "school = #{school,jdbcType=VARCHAR},",
          "department = #{department,jdbcType=VARCHAR},",
          "teacher_id = #{teacherId,jdbcType=VARCHAR},",
          "teacher_name = #{teacherName,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Classes record);

    @Select("select * from t_class")
    List<fz.cs.daoyun.domain.Classes> selectAll();

    @Select("select * from  t_class where classes_id = #{classesId,jdbcType=INTEGER}")
    fz.cs.daoyun.domain.Classes selectByClassId(Integer classId);

    @Delete("delete  from t_class where classes_id = #{classesId,jdbcType=INTEGER}")
    void deleteByClassId(Integer classesId);


    @Select("select classes_id, classes_name, school, department, teacher_id, teacher_name from t_class where teacher_id =  #{name,jdbcType=VARCHAR}")
    List<fz.cs.daoyun.domain.Classes> selectByTeacherId(String name);
}
