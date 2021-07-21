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

            Member newMember = new Member();
            newMember.setUsername("junwoo");
            em.persist(newMember);

            Member newMember2 = new Member();
            newMember2.setUsername("junwoo");
            em.persist(newMember2);

            em.flush();
            em.clear();

            Member member1 = em.find(Member.class, member.getId());
            System.out.println("member1.getClass() = " + member1.getClass());

            Member member2 = em.getReference(Member.class, member.getId());
            System.out.println("member2.getClass() = " + member2.getClass());

            /**
             * JPA는 같은 영속성 컨텍스트 및 트랜잭션 내에서는
             * 항상 == 비교시 true를 반환하게 설계되어 있다.
             * */
            System.out.println("member1 == member2 :: " + (member1 == member2));


            /**
             * 무조건 같은 영속성 컨텍스트에서는 == 이 true가 되게 강제한다.
             * */
            //step01
            Member member3 = em.getReference(Member.class, newMember.getId());
            System.out.println("member3.getClass() = " + member3.getClass());
            Member member4 = em.getReference(Member.class, newMember.getId());
            System.out.println("member4.getClass() = " + member4.getClass());

            System.out.println("member3 == member4 :: " + (member3 == member4));

            //step02
            Member member5 = em.getReference(Member.class, newMember2.getId());
            System.out.println("member5.getClass() = " + member5.getClass());
            Member member6 = em.find(Member.class, newMember2.getId());
            System.out.println("member6.getClass() = " + member6.getClass());

            System.out.println("member5 == member6 :: " + (member5 == member6));

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
