package com.project.withpet.member.model.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.project.withpet.member.model.vo.Member;

@Repository
public class MemberDao {
	
	public int insertMember(SqlSession sqlSession, Member member) {
		return sqlSession.insert("memberMapper.insertMember", member);
	}
	
	public int idDouble(SqlSession sqlSession, String checkId) {
		return sqlSession.selectOne("memberMapper.idDouble", checkId);
	}

}