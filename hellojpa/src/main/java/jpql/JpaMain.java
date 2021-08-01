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
            Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);


            em.flush();
            em.clear();

            /**
             * 상태필드
             * */
            String query1 = "select m.username from Member m";
            List<String> result1 = em.createQuery(query1, String.class)
                                     .getResultList();

            /**
             * 단일 값 연관 경로
             * : 묵시적 내부 조인 발생
             * => 묵시적인 내부조인이 발생하지 않게 짜야한다.
             *
             select
             team1_.id as id1_3_,
             team1_.name as name2_3_
             from
             Member member0_
             inner join
             Team team1_
             on member0_.TEAM_ID=team1_.id
             **/
            String query2 = "select m.team from Member m";
            List<Team> result2 = em.createQuery(query2, Team.class)
                                     .getResultList();

            /**
             * 컬렉션 값 연관 경로
             * 묵시적 내부 조인 발생, 탐색 X
             * */
            String query3 = "select t.Members.size from Team t";
            Integer result3 = em.createQuery(query3, Integer.class)
                    .getSingleResult();
            System.out.println("result3 = " + result3);

            String query4 = "select t.Members from Team t";
            Collection result4 = em.createQuery(query4, Collection.class)
                                   .getResultList();
            System.out.println("result4 = " + result4);

            /**
             * 명시적 조인
             * */
            String query5 = "select m.username from Team t join t.Members m";
            em.createQuery(query5, Collection.class);

            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
