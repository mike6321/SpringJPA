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

            em.flush();
            em.clear();

            /**
             * 엔티티 프로젝션(1)
             * result 또한 영속성 컨텍스트에 의해 관리된다.
             * */
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                                    .getResultList();
            Member findMember = result.get(0);
            findMember.setAge(20);

            /**
             * 엔티티 프로젝션(2)
             * JOIN을 명시해야 유지보수 측면에서 좋다.
             * */
            List<Team> result2 = em.createQuery("select t from Member m join m.team t", Team.class)
                                    .getResultList();

            /**
             * 임베디드 타입 프로젝션
             * */
            List<Address> result3 = em.createQuery("select o.address from Order o", Address.class)
                                        .getResultList();

            /**
             * 스칼라 타입 프로젝션
             * */
            List result4 = em.createQuery("select m.username, m.age from Member m")
                                .getResultList();


            /**
             * 여러가지 값을 가져올 때
             * */
            //step01
            Object o = result4.get(0);
            Object[] resultArray = (Object[]) o;
            System.out.println("username = " + resultArray[0]);
            System.out.println("age = " + resultArray[1]);

            //step02
            List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m")
                                            .getResultList();

            //step03 DTO를 통해 호출
            List<MemberDTO> resultList1 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();
            MemberDTO memberDTO = resultList1.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());


            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
