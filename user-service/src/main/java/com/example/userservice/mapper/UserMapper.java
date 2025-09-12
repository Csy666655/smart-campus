package com.example.userservice.mapper;

import com.example.userservice.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * from admin_profile where id = 1")
     User getUserById();
}
