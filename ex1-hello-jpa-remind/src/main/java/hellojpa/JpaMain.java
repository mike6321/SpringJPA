package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

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
            Member member1 = new Member();
            member1.setUsername("junwoo");

            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("junwoo");

            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("junwoo");

            em.persist(member3);

            em.flush();
            em.clear();

            Member m1 = em.find(Member.class, member1.getId());
            Member m2 = em.find(Member.class, member2.getId());
            Member m3 = em.getReference(Member.class, member3.getId());

            System.out.println("m1 == m2 :: " + (m1.getClass() == m2.getClass()));
            System.out.println("m1 == m3 :: " + (m1.getClass() == m3.getClass()));

            logic(m1, m3);

            tx.commit();
        } catch (Exception e) {
            em.close();
        }
        emf.close();

    }

    /**
     * 실제로는 이렇게 쓰이기 때문에
     * 프록시 타입인지 아닌지를 구분하기 힘들다.
     * 그렇기 때문에 == 비교 대신 instanceof 를 사용하자!
     * */
    private static void logic(Member m1, Member m3) {
        System.out.println(m1 instanceof Member);
        System.out.println(m3 instanceof Member);
    }

}
