package org.ftm.api.common;

import lombok.Builder;

import java.util.Collection;

@Builder
public record PageResponse<T>(
        Collection<T> content,
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
}
