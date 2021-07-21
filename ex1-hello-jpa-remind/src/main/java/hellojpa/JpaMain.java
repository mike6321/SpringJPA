package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
            member.setUsername("junwoo");
            em.persist(member);

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member.getId());
            System.out.println("refMember.getClass() = " + refMember.getClass());

            em.clear();

            refMember.getUsername();

        } catch (Exception e) {
            em.close();
            e.printStackTrace();
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
