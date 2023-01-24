package com.baseballshop.repository;

import com.baseballshop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository <Member, Long> {

    Member findByUserId(String userId);

    Member findById(String id);

    Member findByEmail(String email);
    Member findByName(String name);

    Member findByPhone(String phone);


}
