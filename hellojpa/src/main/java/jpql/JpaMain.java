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

            String query = "select m from Member m";
            List<Member> result1 = em.createQuery(query, Member.class)
                                     .getResultList();
            /**
             * select
             *             member0_.id as id1_0_,
             *             member0_.age as age2_0_,
             *             member0_.TEAM_ID as TEAM_ID5_0_,
             *             member0_.type as type3_0_,
             *             member0_.username as username4_0_
             *         from
             *             Member member0_*/
            for (Member member : result1) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                //회원1, 팀A (SQL)
                /**select
                 team0_.id as id1_3_0_,
                 team0_.name as name2_3_0_
                 from
                 Team team0_
                 where
                 team0_.id=?*/
                //회원2, 팀A (1차캐시)
                //회원3, 팀B (SQL)
                /**select
                 team0_.id as id1_3_0_,
                 team0_.name as name2_3_0_
                 from
                 Team team0_
                 where
                 team0_.id=?*/

                // 최악의 경우 회원이 100명인 경우 -> N + 1 = 101
            }




            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
