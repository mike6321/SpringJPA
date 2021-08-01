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

            String query2 = "select distinct t from Team t join fetch t.Members";
            List<Team> result2 = em.createQuery(query2, Team.class)
                                   .getResultList();
            System.out.println("result2 = " + result2.size());
            /**
             * distinct는 데이터가 완전히 동일해만 중복이 제가 된다.
             * JPA가 중복이 될 만한 것을 어플리케이션 레벨에서 삭제한다.
             * */

            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
