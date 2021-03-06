package fz.cs.daoyun.mapper;

import fz.cs.daoyun.domain.Pagebutton;
import fz.cs.daoyun.mapper.provider.PagebuttonSqlProvider;
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


@Transactional
public interface PagebuttonMapper {
    @Delete({
        "delete from t_pagebutton",
        "where pageButton_id = #{pagebuttonId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer pagebuttonId);

    @Insert({
        "insert into t_pagebutton (pageButton_id, button_id, ",
        "menu_id)",
        "values (#{pagebuttonId,jdbcType=INTEGER}, #{buttonId,jdbcType=INTEGER}, ",
        "#{menuId,jdbcType=INTEGER})"
    })
    int insert(Pagebutton record);

    @InsertProvider(type= PagebuttonSqlProvider.class, method="insertSelective")
    int insertSelective(Pagebutton record);

    @Select({
        "select",
        "pageButton_id, button_id, menu_id",
        "from t_pagebutton",
        "where pageButton_id = #{pagebuttonId,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="pageButton_id", property="pagebuttonId", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="button_id", property="buttonId", jdbcType=JdbcType.INTEGER),
        @Result(column="menu_id", property="menuId", jdbcType=JdbcType.INTEGER)
    })
    Pagebutton selectByPrimaryKey(Integer pagebuttonId);

    @UpdateProvider(type=PagebuttonSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Pagebutton record);

    @Update({
        "update t_pagebutton",
        "set button_id = #{buttonId,jdbcType=INTEGER},",
          "menu_id = #{menuId,jdbcType=INTEGER}",
        "where pageButton_id = #{pagebuttonId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Pagebutton record);
}
