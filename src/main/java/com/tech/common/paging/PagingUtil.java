package com.tech.common.paging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public class PagingUtil<T> {
    public Page<T> getPaginatedAndSortedResults(JpaRepository<T, Long> repository,
                                                int page,
                                                int size,
                                                String sortBy,
                                                String direction) {

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(pageable);
    }
}
