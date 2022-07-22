package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.dtos.request.PageSizeDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CommonUtils {
    public static Pageable createPageable(PageSizeDto pageSizeDto) {
        int page = pageSizeDto.getPage();
        int size = pageSizeDto.getSize();
        String sortBy = pageSizeDto.getSortBy();
        String sortDirection = pageSizeDto.getSortDirection();
        if (sortBy == null || sortDirection == null) {
            return PageRequest.of(page, size);
        }
        if (sortDirection.equalsIgnoreCase("ASC")) {
            return PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else if (sortDirection.equalsIgnoreCase("DESC")) {
            return PageRequest.of(page, size, Sort.by(sortBy).descending());
        } else {
            throw new IllegalArgumentException("sortDirection can be ASC or DESC only");
        }
    }

    public static boolean hasFiltration(PageSizeDto pageSizeDto) {
        String filterBy = pageSizeDto.getFilterBy();
        String filterValue = pageSizeDto.getFilterValue();
        if (filterBy == null || filterBy.equals("") || filterValue == null  || filterValue.equals("")) {
            return false;
        }
        return true;
    }
}
