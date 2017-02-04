package com.example.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created by yanhao on 2017/1/23.
 */
@Mapper
@Repository
public interface AccessTokenMapper {

    @Select("select access_token accessToken,expires_in expiresIn,create_time createTime from wechat_access_token where id = #{id}")
    AccessTokenBean findAccessTokenById(@Param("id")Integer id);

    @Update("UPDATE wechat_access_token SET access_token =#{accessToken},expires_in =#{expiresIn},create_time =#{createTime} where id = #{id}")
    void UpdateAccessTokenById(@Param("id")Integer id,
                              @Param("accessToken")String accessToken,
                              @Param("expiresIn")Integer expiresIn,
                              @Param("createTime")String createTime);
}
