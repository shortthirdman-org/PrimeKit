package com.shortthirdman.sharedlibs.util;


import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class HibernateUtils {

    /**
     * Delete query helper
     *
     * @param session
     * @param clazz
     * @param map
     * @param <A>
     * @return
     */
    public static <A> int deleteEqualToColumn(Session session, Class<A> clazz, Map<String, Object> map) {
        if (session == null) {
            throw new NullPointerException("Hibernate session can not be null");
        }
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<A> delete = cb.createCriteriaDelete(clazz);
        Root<A> root = delete.from(clazz);

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
        return queryHelperBasedOnCol(session, clazz, map).stream();
    }

    public static <A> Long selectCountHelperBasedOnCol(Session session, Class<A> clazz, Map<String, Object> map) {
        if (session == null) {
            throw new NullPointerException("Hibernate session can not be null");
        }
        CriteriaBuilder builder = session.getCriteriaBuilder();
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

    private static <A> Query<A> queryHelperBasedOnCol(Session session, Class<A> clazz, Map<String, Object> map) {
        if (session == null) {
            throw new NullPointerException("Hibernate session can not be null");
        }
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<A> criteriaQuery =
                builder.createQuery(clazz);
        Root<A> root = criteriaQuery.from(clazz);
        if (!map.isEmpty()) {
            criteriaQuery.where(getPredicates(map, builder, root));
        }
        return session.createQuery(criteriaQuery);
    }
}
