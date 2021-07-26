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

            System.out.println("*********************************");
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("*********************************");

            /**
             * 임베디드 타입 수정
             * homeCity -> newCity
             * 값 타입은 immutable 해야하기 때문에
             * 아래와 같이 하면 안된다.
             * */
//            findMember.getHomeAddress().setCity("newCity");

            /**
             * 아래와 같이 새로운 인스턴스로 생성해야한다.
             * */
//            Address address = findMember.getHomeAddress();
//            findMember.setHomeAddress(new Address("newCity", address.getStreet(), address.getZipcode()));

            /**
             * 문자열 타입 수정
             * delete
             *         from
             *             FAVORITE_FOOD
             *         where
             *             MEMBER_ID=?
             *             and FOOD_NAME=?
             * ;
             * insert
             *         into
             *             FAVORITE_FOOD
             *             (MEMBER_ID, FOOD_NAME)
             *         values
             *             (?, ?)
             * ;
             * 삭제 후 인서트 진행
             * */
//            findMember.getFavoriteFoods().remove("치킨");
//            findMember.getFavoriteFoods().add("한식");


            // TODO: 2021/07/26 값 타입 컬렉션의 제약사항
            /**
             * 값 타입은 엔티티와 다르게 식별자 개념이 없다.
             * 값 타입 컬렉션에 변경 사항이 발생하면, 중니 엔티티와 연관된 모든 데이터를 삭제 후 현재 값을 모두 다시 저장
             * */

            findMember.getAddressHistory().remove(new Address("old1", "street", "10000"));
            findMember.getAddressHistory().add(new Address("newCity1", "street", "10000"));
            /**
             * 컬렉션 타입 값 수정
             * : equals와 hashcode가 재정의 되어야한다.
             * : immutable하게 수정해야한다.
             * delete
             *         from
             *             ADDRESS
             *         whereAAA
             *             MEMBER_ID=?
             * 기대와는 달리 Member 아이디의 모든것을 삭제한다.
             *
             * -> 쓰지말자!
             * */

            /**
             * 그러면 어떻게 풀어야할까?
             * 값 타입 컬렉션 대신에 일대다 관계를 고려하자!
             * */

            tx.commit();
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
        }
        emf.close();

    }

}
