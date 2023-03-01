package com.bookcode.worksprotectingsystem.Dao;

import com.bookcode.worksprotectingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User,Long> {
    User findByUname(String uname);
    User findByUnameAndPassword(String uname,String password);

    User findByToken(String token);

    User findByUid(long id);
}
