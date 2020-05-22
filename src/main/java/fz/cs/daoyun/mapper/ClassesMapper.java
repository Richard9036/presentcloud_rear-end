package fz.cs.daoyun.mapper;

import fz.cs.daoyun.domain.Classes;
import fz.cs.daoyun.mapper.provider.ClassesSqlProvider;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
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
        "department,  ",
        " desc)",
        "values (#{id,jdbcType=INTEGER}, #{classesId,jdbcType=INTEGER}, ",
        "#{classesName,jdbcType=VARCHAR}, #{school,jdbcType=VARCHAR}, ",
        "#{department,jdbcType=VARCHAR},  ",
        "#{desc,jdbcType=VARCHAR})"
    })
    int insert(Classes record);

    @InsertProvider(type= ClassesSqlProvider.class, method="insertSelective")
    int insertSelective(Classes record);

    @Select({
        "select",
        "id, classes_id, classes_name, school, department , desc",
        "from t_class",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="classes_id", property="classesId", jdbcType=JdbcType.INTEGER),
        @Result(column="classes_name", property="classesName", jdbcType=JdbcType.VARCHAR),
        @Result(column="school", property="school", jdbcType=JdbcType.VARCHAR),
        @Result(column="department", property="department", jdbcType=JdbcType.VARCHAR),
        @Result(column="desc", property="desc", jdbcType=JdbcType.VARCHAR)
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
          "desc = #{desc,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Classes record);


    @Select("select * from t_class")
    List<Classes> selectAll();

    @Select("select * from  t_class where classes_id = #{classesId,jdbcType=INTEGER}")
    Classes selectByClassId(Integer classId);

    @Delete("delete  from t_class where classes_id = #{classesId,jdbcType=INTEGER}")
    void deleteByClassId(Integer classesId);
}
