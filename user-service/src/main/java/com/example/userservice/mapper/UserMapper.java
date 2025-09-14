package com.example.userservice.mapper;

import com.example.userservice.domain.po.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.swing.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * from admin_profile where id = 1")
     User getUserById();
    @Select("SELECT * from user where user_name = #{userName}")
    User getUserByName(String userName);
    @Insert("INSERT into user  (user_name,password,real_name,email,phone,role,status,gender,avatar,created_at,updated_at)" +
            "values(#{userName},#{password},#{realName},#{email},#{phone},#{role},#{status},#{gender},#{avatar},#{createdAt},#{updatedAt}) ")
    int insert(User user);
}
