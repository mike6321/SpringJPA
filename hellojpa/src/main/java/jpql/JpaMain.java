package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * Project : ex1-hello-jpa-remind
 *
 * @author : jwdeveloper
 * @comment :
 * Time : 4:03 오후
 */
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);


            em.flush();
            em.clear();

            String query = "select m from Member m join fetch m.team";
            List<Member> result1 = em.createQuery(query, Member.class)
                                     .getResultList();

            for (Member member : result1) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                /**
                 * 더이상 team은 프록시가 아니다.
                 * -> 이미 member와 team의 데이터를 다가져왔다.
                 *
                 select
                 member0_.id as id1_0_0_,
                 team1_.id as id1_3_1_,
                 member0_.age as age2_0_0_,
                 member0_.TEAM_ID as TEAM_ID5_0_0_,
                 member0_.type as type3_0_0_,
                 member0_.username as username4_0_0_,
                 team1_.name as name2_3_1_
                 from
                 Member member0_
                 inner join
                 Team team1_
                 on member0_.TEAM_ID=team1_.id*/
            }

            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
