package jpql;

import javax.persistence.*;
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
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            /**
             * 조인
             * */
            // inner
            List<Member> resultList1 = em.createQuery("select m from Member m inner join m.team t", Member.class)
                                        .getResultList();
            //outer
            List<Member> resultList2 = em.createQuery("select m from Member m left join m.team t", Member.class)
                                         .getResultList();
            //setter
            List<Member> resultList3 = em.createQuery("select m from Member m, Team t where m.username = t.name", Member.class)
                                         .getResultList();



            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
