package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

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
            member.setUsername("user1");
            member.setHomeAddress(new Address("homeCity", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");


            member.getAddressHistory().add(new Address("old1", "street", "10000"));
            member.getAddressHistory().add(new Address("old2", "street", "10000"));

            em.persist(member);

            em.flush();
            em.clear();

            /**
             * 아래 쿼리를 보면 Member 만 가지고 온것을 볼 수 있다.
             * -> 기본적으로 지연로딩
             *     select
             *         member0_.MEMBER_ID as MEMBER_I1_7_0_,
             *         member0_.city as city2_7_0_,
             *         member0_.street as street3_7_0_,
             *         member0_.zipcode as zipcode4_7_0_,
             *         member0_.USERNAME as USERNAME5_7_0_
             *     from
             *         Member member0_
             *     where
             *         member0_.MEMBER_ID=?
             *
             *  @Embedded
             *     private Address homeAddress;
             *  이것은 멤버의 소속된 값 타입이기 때문에 같이 불러와진다.
             *
             *
             * */
            System.out.println("*********************************");
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("*********************************");


            /**
             * 원하는 값을 찾을 때 그제서야 쿼리가 발생하는 것을 볼 수 있다.
             *     select
             *         addresshis0_.MEMBER_ID as MEMBER_I1_0_0_,
             *         addresshis0_.city as city2_0_0_,
             *         addresshis0_.street as street3_0_0_,
             *         addresshis0_.zipcode as zipcode4_0_0_
             *     from
             *         ADDRESS addresshis0_
             *     where
             *         addresshis0_.MEMBER_ID=?
             * */
            List<Address> addressHistory = findMember.getAddressHistory();
            for (Address address : addressHistory) {
                System.out.println("address = " + address.getCity());
            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
            }

            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
