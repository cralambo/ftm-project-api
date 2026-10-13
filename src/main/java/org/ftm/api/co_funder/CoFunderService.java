package org.ftm.api.co_funder;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoFunderService {

    private final CoFunderRepository coFunderRepository;

    public String createCoFunder(CoFunderRequest request) {
        final CoFunder coFunder = CoFunderMapper.mapToCoFunderCreateRequest(request);
        return coFunderRepository.save(coFunder).getId().toString();
    }

    public PageResponse<CoFunderResponse> getCoFunders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<CoFunder> coFunders = coFunderRepository.findAll(pageable);
        List<CoFunderResponse> content = coFunders
                .stream()
                .map(CoFunderMapper::mapToCoFunderResponse)
                .toList();
        return PageResponse.<CoFunderResponse>builder()
                .content(content)
                .number(coFunders.getNumber())
                .size(coFunders.getSize())
                .totalElements(coFunders.getTotalElements())
                .totalPages(coFunders.getTotalPages())
                .first(coFunders.isFirst())
                .last(coFunders.isLast())
                .build();
    }

    public List<CoFunderResponse> getAllCoFunders() {
        List<CoFunder> coFunders = coFunderRepository.findAll(Sort.by("createdDate").descending());
        return coFunders.stream()
                .map(CoFunderMapper::mapToCoFunderResponse)
                .toList();
    }

    public CoFunderResponse getCoFunderById(UUID coFunderId) {
        return coFunderRepository.findById(coFunderId)
                .map(CoFunderMapper::mapToCoFunderResponse)
                .orElseThrow(() -> new EntityNotFoundException("No co-funder found with id: " + coFunderId));
    }

    public String updateCoFunder(UUID coFunderId, CoFunderRequest request) {
        CoFunder coFunder = coFunderRepository.findById(coFunderId)
                .orElseThrow(() -> new EntityNotFoundException("No co-funder found with id: " + coFunderId));

        coFunder.setName(request.name());
        return coFunderRepository.save(coFunder).getId().toString();
    }

    public void deleteCoFunder(UUID coFunderId) {
        final CoFunder coFunder = coFunderRepository.findById(coFunderId)
                .orElseThrow(() -> new EntityNotFoundException("No co-funder found with id: " + coFunderId));
        coFunderRepository.delete(coFunder);
    }
}
