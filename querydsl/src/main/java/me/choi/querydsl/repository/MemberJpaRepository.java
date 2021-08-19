package me.choi.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.choi.querydsl.entity.Member;
import me.choi.querydsl.entity.QMember;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static me.choi.querydsl.entity.QMember.*;

@Repository
//@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

//    public MemberJpaRepository(EntityManager entityManager, JPAQueryFactory queryFactory) {
//        this.entityManager = entityManager;
//        this.queryFactory = queryFactory;
//    }

    public MemberJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public void save(Member member) {
        entityManager.persist(member);
    }

    public Optional<Member> findById(Long id) {
        Member findMember = entityManager.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }
    public Optional<Member> findById_Querydsl(Long id) {
        Member member = queryFactory.selectFrom(QMember.member)
                                    .where(QMember.member.id.eq(id))
                                    .fetchOne();
        return Optional.ofNullable(member);
    }


    public List<Member> findAll() {
        return entityManager.createQuery("select m from Member m", Member.class)
                            .getResultList();
    }

    public List<Member> findAll_Querydsl() {
        return queryFactory.selectFrom(member)
                            .fetch();
    }

    public List<Member> findByUsername(String username) {
        return entityManager.createQuery("select m from Member m where m.username = :username", Member.class)
                            .setParameter("username", username)
                            .getResultList();
    }

    public List<Member> findByUsername_Querydsl(String username) {
        return queryFactory.selectFrom(member)
                            .where(member.username.eq(username))
                            .fetch();
    }

}
