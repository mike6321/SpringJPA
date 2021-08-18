package me.choi.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.choi.querydsl.entity.Hello;
import me.choi.querydsl.entity.QHello;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class QuerydslApplicationTests {

    @Autowired
    private EntityManager entityManager;

    @Test
    void contextLoads() {
        Hello hello = new Hello();
        entityManager.persist(hello);

        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QHello qHello = new QHello("H");

        Hello result = query.selectFrom(qHello)
                            .fetchOne();

        assertThat(result).isEqualTo(hello);
        assertThat(result.getId()).isEqualTo(hello.getId());
    }

}
