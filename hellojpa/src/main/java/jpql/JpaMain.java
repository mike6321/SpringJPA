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

            String query = "select t from Team t";
            List<Team> result1 = em.createQuery(query, Team.class)
                                     .getResultList();

            System.out.println("result1 = " + result1.size());

            String query2 = "select t from Team t join fetch t.Members";
            List<Team> result2 = em.createQuery(query2, Team.class)
                                   .getResultList();
            System.out.println("result2 = " + result2.size());
            /**
             * join fetch 를 걸면서 데이터가 뻥튀기 되었다.
             * */

            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
