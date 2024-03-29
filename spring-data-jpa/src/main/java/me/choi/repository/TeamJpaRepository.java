package me.choi.repository;

import me.choi.entity.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Team save(Team team) {
        entityManager.persist(team);

        return team;
    }

    public void delete(Team team) {
        entityManager.remove(team);
    }

    public List<Team> findAll() {
        return entityManager.createQuery("select t from Team t", Team.class)
                            .getResultList();
    }

    public Optional<Team> findById(Long id) {
        Team team = entityManager.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    public long count() {
        return entityManager.createQuery("select count(t) from Team t", Long.class)
                .getSingleResult();
    }
}
