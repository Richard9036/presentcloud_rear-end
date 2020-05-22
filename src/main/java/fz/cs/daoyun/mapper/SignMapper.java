package fz.cs.daoyun.mapper;

import fz.cs.daoyun.mapper.provider.SignSqlProvider;
import fz.cs.daoyun.domain.Sign;
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

public interface SignMapper {
    @Delete({
        "delete from t_sign",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into t_sign (id, user_name, ",
        "class_id, singn_num, ",
        "sign_time, score)",
        "values (#{id,jdbcType=BIGINT}, #{userName,jdbcType=VARCHAR}, ",
        "#{classId,jdbcType=INTEGER}, #{singnNum,jdbcType=INTEGER}, ",
        "#{signTime,jdbcType=TIMESTAMP}, #{score,jdbcType=INTEGER})"
    })
    int insert(Sign record);

    @InsertProvider(type= SignSqlProvider.class, method="insertSelective")
    int insertSelective(Sign record);

    @Select({
        "select",
        "id, user_name, class_id, singn_num, sign_time, score",
        "from t_sign",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR),
        @Result(column="class_id", property="classId", jdbcType=JdbcType.INTEGER),
        @Result(column="singn_num", property="singnNum", jdbcType=JdbcType.INTEGER),
        @Result(column="sign_time", property="signTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="score", property="score", jdbcType=JdbcType.INTEGER)
    })
    Sign selectByPrimaryKey(Long id);

    @UpdateProvider(type=SignSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Sign record);

    @Update({
        "update t_sign",
        "set user_name = #{userName,jdbcType=VARCHAR},",
          "class_id = #{classId,jdbcType=INTEGER},",
          "singn_num = #{singnNum,jdbcType=INTEGER},",
          "sign_time = #{signTime,jdbcType=TIMESTAMP},",
          "score = #{score,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Sign record);


    @Select({
            "select",
            "id, user_name, class_id, singn_num, sign_time, score",
            "from t_sign",
            "where user_name = #{userName,jdbcType=VARCHAR} and class_id = #{classId,jdbcType=INTEGER}"
    })
    List<Sign> findByusername(String username, Integer classId);


    @Select({
            "select",
            "id, user_name, class_id, singn_num, sign_time, score",
            "from t_sign",
            "where sign_time like CONCAT(#{date,jdbcType=VARCHAR}, '%')"
    })
    List<Sign> selectAllByDate(String date);


    @Select({
            "select *",
            "from t_sign",
            "where  user_name = #{userName,jdbcType=VARCHAR} and class_id = #{classId,jdbcType=INTEGER}" +
                    "sign_time like CONCAT(#{string_date,jdbcType=VARCHAR}, '%')"
    })
    Sign findCurrentRecord(String username, Integer classid, String string_date);
}
