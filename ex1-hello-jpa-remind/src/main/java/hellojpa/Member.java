package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Project : ex1-hello-jpa-remind
 *
 * @author : jwdeveloper
 * @comment :
 * Time : 4:33 오후
 */
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    /**
     * 값 타입 컬렉션
     * : 값 타입 하나 이상 저장할 떄 사용
     * @ElementCollection, @CollectionTable
     *
     * 사용이유
     * : DB는 데이터베이스를 같은 테이블에 저장할 수 없기 때문
     * -> 컬렉션을 위한 별도의 테이블이 필요
     * */
    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns =
    @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ADDRESS", joinColumns =
            @JoinColumn(name = "MEMBER_ID"))
    private List<Address> addressHistory = new ArrayList<>();

    /*
    * create table Member (
       MEMBER_ID bigint not null,
        city varchar(255),
        street varchar(255),
        zipcode varchar(255),
        USERNAME varchar(255),
        TEAM_ID bigint,
        primary key (MEMBER_ID)
    )
    * create table ADDRESS (
       MEMBER_ID bigint not null,
        city varchar(255),
        street varchar(255),
        zipcode varchar(255)
    )
    * create table FAVORITE_FOOD (
       MEMBER_ID bigint not null,
        FOOD_NAME varchar(255)
    )
    * */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public List<Address> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<Address> addressHistory) {
        this.addressHistory = addressHistory;
    }

}
