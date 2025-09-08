package com.shortthirdman.primekit.essentials.common.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class HibernateUtils {

    private HibernateUtils() {
    }

    private static <A> Query<A> queryHelperBasedOnColumn(Session session, Class<A> clazz, Map<String, Object> map) {
        StringBuilder queryString = new StringBuilder("FROM ").append(clazz.getSimpleName()).append(" WHERE ");
        map.forEach((key, value) -> queryString.append(key).append(" = :").append(key).append(" AND "));

        // Remove the trailing " AND " from the query string
        if (!map.isEmpty()) {
            queryString.delete(queryString.length() - 5, queryString.length());
        }

        Query<A> query = session.createQuery(queryString.toString(), clazz);
        map.forEach(query::setParameter);

        return query;
    }

    /**
     * Delete query helper
     *
     * @param session the hibernate session
     * @param clazz the class
     * @param map the parameter map
     * @param <T> the generic class
     * @return
     */
    public static <T> int deleteEqualToColumn(Session session, Class<T> clazz, Map<String, Object> map) {
        if (session == null) {
            throw new NullPointerException("Hibernate session can not be null");
        }
        CriteriaBuilder cb = (CriteriaBuilder) session.getCriteriaBuilder();
        CriteriaDelete<T> delete = cb.createCriteriaDelete(clazz);
        Root<T> root = delete.from(clazz);

        delete.where(getPredicates(map, cb, root));

        return session.createQuery(delete).executeUpdate();
    }

    /**
     * Update query helper
     *
     * @param session
     * @param clazz
     * @param whereMap
     * @param updateMap
     * @param <A>
     * @return
     */
    public static <A> int updateHelper(Session session, Class<A> clazz, Map<String, Object> whereMap, Map<String, Object> updateMap) {
        if (session == null) {
            throw new NullPointerException("Hibernate session can not be null");
        }
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<A> update = criteriaBuilder.createCriteriaUpdate(clazz);
        Root<A> root = update.from(clazz);

        updateMap.forEach((column, value) -> {
            update.set(column, value);
        });

        update.where(getPredicates(whereMap, criteriaBuilder, root));
        return session.createQuery(update).executeUpdate();
    }

    /**
     * Select query helper based on columns
     *
     * @param session
     * @param clazz
     * @param map
     * @param <A>
     * @return
     */
    public static <A> List<A> selectHelperBasedOnCol(Session session, Class<A> clazz, Map<String, Object> map) {
        return queryHelperBasedOnCol(session, clazz, map).getResultList();
    }

    /**
     * Select query helper
     *
     * @param session
     * @param clazz
     * @param <A>
     * @return
     */
    public static <A> List<A> selectHelper(Session session, Class<A> clazz) {
        return queryHelperBasedOnCol(session, clazz, Collections.EMPTY_MAP).getResultList();
    }

    public static <A> Stream<A> selectStreamBasedOnCol(Session session, Class<A> clazz, Map<String, Object> map) {
        Query<A> query = queryHelperBasedOnCol(session, clazz, map);
        return query != null ? query.getResultStream() : Stream.empty();
    }

    public static <A> Long selectCountHelperBasedOnCol(Session session, Class<A> clazz, Map<String, Object> map) {
        if (session == null) {
            throw new NullPointerException("Hibernate session can not be null");
        }

        CriteriaBuilder builder = (CriteriaBuilder) session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery =
                builder.createQuery(Long.class);
        Root<A> root = criteriaQuery.from(clazz);

        if (!map.isEmpty()) {
            criteriaQuery.where(getPredicates(map, builder, root));
        }

        criteriaQuery.select(builder.count(root));

        return session.createQuery(criteriaQuery).getSingleResult();
    }

    private static Predicate[] getPredicates(Map<String, Object> map, CriteriaBuilder criteriaBuilder, Root root) {
        int size = map.keySet().size();
        Predicate[] predicates = new Predicate[size];
        List<Predicate> list = new ArrayList<>();
        map.forEach((column, value) -> {
            list.add(criteriaBuilder.equal(root.get(column), value));
        });
        return list.toArray(predicates);
    }

    private static <T> Query<T> queryHelperBasedOnCol(Session session, Class<T> clazz, Map<String, Object> map) {
        if (session == null) {
            throw new NullPointerException("Hibernate session can not be null");
        }

        CriteriaBuilder builder = (CriteriaBuilder) session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);

        if (!map.isEmpty()) {
            criteriaQuery.where(getPredicates(map, builder, root));
        }

        return session.createQuery(criteriaQuery);
    }
}
