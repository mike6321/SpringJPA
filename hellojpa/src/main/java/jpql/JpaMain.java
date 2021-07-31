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
            member.setType(MemberType.ADMIN);
            em.persist(member);

            em.flush();
            em.clear();

            /**
             * JPQL 타입 표현과 기타식
             * */
            List<Object[]> resultList = em.createQuery("select m.username, 'HELLO', TRUE from Member m")
                                .getResultList();
            for (Object[] o : resultList) {
                System.out.println("objects = " + o[0]);
                System.out.println("objects = " + o[1]);
                System.out.println("objects = " + o[2]);
            }

            //Enum
            List<Object[]> resultList2 = em.createQuery("select m.username, 'HELLO', TRUE from Member m where m.type = jpql.MemberType.ADMIN")
                                            .getResultList();
            for (Object[] objects : resultList2) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
            }




            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
