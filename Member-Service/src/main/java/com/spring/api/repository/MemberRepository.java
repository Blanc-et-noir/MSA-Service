package com.spring.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.api.entity.MemberEntity;

@Repository("memberRepository")
public interface MemberRepository extends JpaRepository<MemberEntity, String>{
	Optional<MemberEntity> findByMemberEmail(String memberEmail);
}
