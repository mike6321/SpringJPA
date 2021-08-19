package me.choi.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.choi.querydsl.entity.Member;
import me.choi.querydsl.entity.QMember;
import me.choi.querydsl.entity.QTeam;
import me.choi.querydsl.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static me.choi.querydsl.entity.QMember.member;
import static me.choi.querydsl.entity.QTeam.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    private EntityManager entityManager;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(entityManager);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
    }

    @Test
    public void startJPQL() {
        //member1을 찾아라!
        String qlString = "select m from Member m where m.username = :username";
        Member findMember = entityManager.createQuery(qlString, Member.class)
                                         .setParameter("username", "member1")
                                         .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {
        Member findMember = queryFactory.select(member)
                                        .from(member)
                                        .where(member.username.eq("member1"))
                                        .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search() {
        Member findMember = queryFactory.selectFrom(QMember.member)
                                        .where(QMember.member.username.eq("member1")
                                                                      .and(QMember.member.age.eq(10)))
                                        .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void searchAndParam() {
        Member findMember = queryFactory.selectFrom(QMember.member)
                .where(
                        member.username.eq("member1"),
                        member.age.between(10, 20)
                )
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void resultFetch() {
//        List<Member> fetch = queryFactory.selectFrom(member)
//                                         .fetch();
//
//        Member fetchOne = queryFactory.selectFrom(QMember.member)
//                                      .fetchOne();
//
//        Member fetchFirst = queryFactory.selectFrom(QMember.member)
//                                        .fetchFirst();

//        QueryResults<Member> queryResults = queryFactory.selectFrom(member)
//                                                        .fetchResults();
//
//        queryResults.getTotal();
//        List<Member> contents = queryResults.getResults();


        long total = queryFactory.selectFrom(member)
                                 .fetchCount();

    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순
     * 2. 회원 이름 오름차순
     * 단 2 에서 회원 이름이 없으면 마지막에 출력 (null last)
     * */
    @Test
    public void sort() {
        entityManager.persist(new Member(null, 100));
        entityManager.persist(new Member("member5", 100));
        entityManager.persist(new Member("member6", 100));

        List<Member> result = queryFactory.selectFrom(member)
                                            .where(
                                                    member.age.eq(100)
                                            )
                                            .orderBy(member.age.desc(), member.username.asc().nullsLast())
                                            .fetch();
        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    public void paging1() {
        List<Member> result = queryFactory.selectFrom(member)
                                            .orderBy(member.username.desc())
                                            .offset(1)
                                            .limit(2)
                                            .fetch();
        assertThat(result.size()).isEqualTo(2);

        QueryResults<Member> queryResults = queryFactory.selectFrom(member)
                                                        .orderBy(member.username.desc())
                                                        .offset(1)
                                                        .limit(2)
                                                        .fetchResults();
        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }

    @Test
    public void aggregation() {
        List<Tuple> result = queryFactory.select(
                                                member.count(),
                                                member.age.sum(),
                                                member.age.avg(),
                                                member.age.max(),
                                                member.age.min()
                                                )
                                            .from(member)
                                            .fetch();
        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    /**
     * 팀의 이름과 평균 연령을 구해라
     * */
    @Test
    public void group() throws Exception {
        List<Tuple> result = queryFactory.select(team.name, member.age.avg())
                                            .from(member)
                                            .join(member.team, team)
                                            .groupBy(team.name)
                                            .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    /**
     * 팀 A에 소속된 모든 회원
     * */
    @Test
    public void join() {
        List<Member> result = queryFactory.selectFrom(member)
                                            .leftJoin(member.team, team)
                                            .where(team.name.eq("teamA"))
                                            .fetch();

        assertThat(result).extracting("username")
                            .containsExactly("member1", "member2");
    }

    /**
     * 세타 조인
     * 회원의 이름이 팀 이름과 같은 회원 조회
     * */
    @Test
    public void theta_join() {
        entityManager.persist(new Member("teamA"));
        entityManager.persist(new Member("teamB"));

        List<Member> result = queryFactory.select(member)
                                            .from(member, team)
                                            .where(team.name.eq(member.username))
                                            .fetch();
        assertThat(result).extracting("username")
                            .containsExactly("teamA", "teamB");
    }

    /**
     * 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * */
    @Test
    public void join_on_filtering() {
        List<Tuple> result = queryFactory.select(member, team)
                                            .from(member)
                                            .leftJoin(member.team, team).on(team.name.eq("teamA"))
                                            .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

        List<Tuple> result2 = queryFactory.select(member, team)
                                            .from(member)
                                            .join(member.team, team)
                                            .where(team.name.eq("teamA"))
                                            .fetch();
        for (Tuple tuple : result2) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 연관관계가 없는 엔티티 외부 조인
     * 회원의 이름이 팀 이름과 같은 대상 외부조인
     * */
    @Test
    public void join_on_on_relation() {
        entityManager.persist(new Member("teamA"));
        entityManager.persist(new Member("teamB"));

        List<Tuple> result = queryFactory.select(member, team)
                                            .from(member)
                                            .leftJoin(team).on(member.username.eq(team.name))
                                            .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }
}
