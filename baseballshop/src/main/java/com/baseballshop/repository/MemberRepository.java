package com.baseballshop.repository;

import com.baseballshop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository <Member, Long> {

    Member findByMemberId(String memberId);

}
