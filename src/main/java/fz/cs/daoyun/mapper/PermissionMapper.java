package fz.cs.daoyun.mapper;

import fz.cs.daoyun.domain.Permission;
import fz.cs.daoyun.mapper.provider.PermissionSqlProvider;
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
public interface PermissionMapper {
    @Delete({
        "delete from t_permission",
        "where permission_id = #{permissionId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer permissionId);

    @Insert({
        "insert into t_permission (permission_id, type, ",
        "name, CreationDate, ",
        "Creator, Modifier, ",
        "ModificationDate)",
        "values (#{permissionId,jdbcType=INTEGER}, #{type,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{creationdate,jdbcType=TIMESTAMP}, ",
        "#{creator,jdbcType=VARCHAR}, #{modifier,jdbcType=VARCHAR}, ",
        "#{modificationdate,jdbcType=TIMESTAMP})"
    })
    int insert(Permission record);

    @InsertProvider(type= PermissionSqlProvider.class, method="insertSelective")
    int insertSelective(Permission record);

    @Select({
        "select",
        "permission_id, type, name, CreationDate, Creator, Modifier, ModificationDate",
        "from t_permission",
        "where permission_id = #{permissionId,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="permission_id", property="permissionId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="CreationDate", property="creationdate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="Creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="Modifier", property="modifier", jdbcType=JdbcType.VARCHAR),
        @Result(column="ModificationDate", property="modificationdate", jdbcType=JdbcType.TIMESTAMP)
    })
    Permission selectByPrimaryKey(Integer permissionId);

    @UpdateProvider(type=PermissionSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Permission record);

    @Update({
        "update t_permission",
        "set type = #{type,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "CreationDate = #{creationdate,jdbcType=TIMESTAMP},",
          "Creator = #{creator,jdbcType=VARCHAR},",
          "Modifier = #{modifier,jdbcType=VARCHAR},",
          "ModificationDate = #{modificationdate,jdbcType=TIMESTAMP}",
        "where permission_id = #{permissionId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Permission record);



    @Select({
            "select",
            "permission_id, type, name, CreationDate, Creator, Modifier, ModificationDate",
            "from t_permission"
    })
    @Results({
            @Result(column="permission_id", property="permissionId", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="CreationDate", property="creationdate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="Creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="Modifier", property="modifier", jdbcType=JdbcType.VARCHAR),
            @Result(column="ModificationDate", property="modificationdate", jdbcType=JdbcType.TIMESTAMP)
    })
    List<Permission> selectAll();


    @Select({
            "select",
            "permission_id, type, name, CreationDate, Creator, Modifier, ModificationDate",
            "from t_permission",
            "where name = #{name,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="permission_id", property="permissionId", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="CreationDate", property="creationdate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="Creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="Modifier", property="modifier", jdbcType=JdbcType.VARCHAR),
            @Result(column="ModificationDate", property="modificationdate", jdbcType=JdbcType.TIMESTAMP)
    })
    Permission selectByName(String name);


    @Select({
            "select",
            "permission_id, type, name, CreationDate, Creator, Modifier, ModificationDate",
            "from t_permission",
            "where type = #{type,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="permission_id", property="permissionId", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="CreationDate", property="creationdate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="Creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="Modifier", property="modifier", jdbcType=JdbcType.VARCHAR),
            @Result(column="ModificationDate", property="modificationdate", jdbcType=JdbcType.TIMESTAMP)
    })
    Permission selectByType(String type);


    @Delete({
            "delete from t_permission",
            "where name = #{name,jdbcType=VARCHAR}"
    })
    void deleteByName(String name);


    @Select("select type from t_permission")
    List<String> getTypes();
}
