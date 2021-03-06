package fz.cs.daoyun.mapper;

import fz.cs.daoyun.mapper.provider.ClassesSqlProvider;
import fz.cs.daoyun.domain.Dict;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface DictMapper {
    @Delete({
        "delete from t_dict",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into t_dict (id, name, ",
        "describe)",
        "values (#{id,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, ",
        "#{describe,jdbcType=VARCHAR})"
    })
    int insert(Dict record);

    @InsertProvider(type= ClassesSqlProvider.DictSqlProvider.class, method="insertSelective")
    int insertSelective(Dict record);

    @Select({
        "select",
        "id, name, describe",
        "from t_dict",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="describe", property="describe", jdbcType=JdbcType.VARCHAR)
    })
    Dict selectByPrimaryKey(Integer id);

    @UpdateProvider(type= ClassesSqlProvider.DictSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Dict record);

    @Update({
        "update t_dict",
        "set name = #{name,jdbcType=VARCHAR},",
          "describe = #{describe,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Dict record);


    @Select("select * from t_dict")
    List<Dict> findall();

    @Select("select * from t_dict where name = #{name,jdbcType=VARCHAR}")
    List<Dict> findByType(String name);
}
