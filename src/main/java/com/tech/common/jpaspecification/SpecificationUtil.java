package com.tech.common.jpaspecification;

import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SpecificationUtil {
    public static String wrapValueWithPercentage(String value) {
        return MessageFormat.format("%{0}%", value);
    }

    public static <T> Specification<T> equalsIgnoreCase(String fieldName, String value) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get(fieldName)), value.toLowerCase());
    }

    public static <T> Specification<T> equal(String fieldName, T value) {
        return (root, query, cb) -> cb.equal(root.get(fieldName), value);
    }

    public static <T> Specification<T> like(String fieldName, String value) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(fieldName)), wrapValueWithPercentage(value));
    }

    public static <T> Specification<T> between(String fieldName, LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> cb.between(root.get(fieldName), from, to);
    }

    public static <T> Specification<T> inCollection(String fieldName, Collection<T> collection) {
        return (root, query, cb) -> root.get(fieldName).in(collection);
    }

    public static <T> Specification<T> buildAndSpecification(List<Specification<T>> conditions) {
        var nonNullConditions = conditions.stream().filter(Objects::nonNull).toList();
        if (nonNullConditions.isEmpty()) {
            return null;
        }

        var spec = Specification.where(nonNullConditions.get(0));
        for (int i = 1; i < nonNullConditions.size(); i++) {
            spec = spec.and(nonNullConditions.get(i));
        }
        return spec;
    }

}
