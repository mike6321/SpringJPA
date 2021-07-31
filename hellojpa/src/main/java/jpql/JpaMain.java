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
            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member1" + i);
                member.setAge(i);
                em.persist(member);
            }


            em.flush();
            em.clear();

            /**
             * 페이징
             * 방언에 따라서 전략을 설정하여 페이징 쿼리가 나간다.
             *
             * */
            List<Member> result = em.createQuery("select m from Member m order by m.age desc ", Member.class)
                                    .setFirstResult(1)
                                    .setMaxResults(10)
                                    .getResultList();

            System.out.println("result.size" + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }

            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
