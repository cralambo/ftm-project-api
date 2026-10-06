package org.ftm.api.position;

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
public class PositionService {

    private final PositionRepository positionRepository;

    public String createPosition(PositionRequest request) {
        final Position position = PositionMapper.mapToCreateRequest(request);
        return positionRepository.save(position).getId().toString();
    }

    public PositionResponse getPositionById(UUID positionId) {
        return positionRepository.findById(positionId)
                .map(PositionMapper::mapToPositionResponse)
                .orElseThrow(() -> new EntityNotFoundException("No position found with id: " + positionId));
    }

    public PageResponse<PositionResponse> getPositions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Position> positions = positionRepository.findAll(pageable);
        List<PositionResponse> content = positions.stream()
                .map(PositionMapper::mapToPositionResponse)
                .toList();
        return PageResponse.<PositionResponse>builder()
                .content(content)
                .number(positions.getNumber())
                .size(positions.getSize())
                .totalElements(positions.getTotalElements())
                .totalPages(positions.getTotalPages())
                .first(positions.isFirst())
                .last(positions.isLast())
                .build();
    }

    public List<PositionResponse> getAllPositions() {
        List<Position> positions = positionRepository.findAll(Sort.by("createdDate"));
        return positions.stream()
                .map(PositionMapper::mapToPositionResponse)
                .toList();
    }

    public String updatePosition(UUID positionId, PositionRequest request) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new EntityNotFoundException("No position found with id: " + positionId));
        position.setCode(request.code());
        position.setName(request.name());
        return positionRepository.save(position).getId().toString();
    }

    public void deletePosition(UUID positionId) {
        final Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new EntityNotFoundException("No position found with id: " + positionId));
        positionRepository.delete(position);
    }
}
