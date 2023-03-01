package com.bookcode.worksprotectingsystem.Dao;

import com.bookcode.worksprotectingsystem.entity.Works;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository  extends JpaRepository<Works,Long>{
    Works findByworkid(long id);
    Works findByworkfile(String filename);
    List<Works> findByworkname(String name);
}
