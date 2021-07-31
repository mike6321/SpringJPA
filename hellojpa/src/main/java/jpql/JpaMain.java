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
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);


            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);

            /**
             * getResultList
             * 결과가 하나 이상일 때 리스트 반환
             * 결과가 없으면 빈 리스트 반환
             * */
            List<Member> resultList = query1.getResultList();
            /**
             * getSingleResult
             * 걀과가 정확히 하나
             * 결과가 없거나 둘 이상이면 예외 발생
             * */
            Member singleResult = query1.getSingleResult();


            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
