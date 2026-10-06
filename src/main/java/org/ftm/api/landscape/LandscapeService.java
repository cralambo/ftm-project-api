package org.ftm.api.landscape;

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
public class LandscapeService {

    private final LandscapeRepository landscapeRepository;

    public String createLandscape(LandscapeRequest request) {
        Landscape landscape = LandscapeMapper.mapToCreateLandscapeRequest(request);
        return landscapeRepository.save(landscape).getId().toString();
    }

    public PageResponse<LandscapeResponse> getLandscapes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Landscape> landscapes = landscapeRepository.findAll(pageable);
        List<LandscapeResponse> contents = landscapes.stream()
                .map(LandscapeMapper::mapToLandscapeResponse)
                .toList();
        return PageResponse.<LandscapeResponse>builder()
                .content(contents)
                .number(landscapes.getNumber())
                .size(landscapes.getSize())
                .totalElements(landscapes.getTotalElements())
                .totalPages(landscapes.getTotalPages())
                .first(landscapes.isFirst())
                .last(landscapes.isLast())
                .build();
    }

    public LandscapeResponse getLandscapeById(UUID landscapeId) {
        return landscapeRepository.findById(landscapeId)
                .map(LandscapeMapper::mapToLandscapeResponse)
                .orElseThrow(() -> new EntityNotFoundException("No landscape found with id: " + landscapeId));
    }

    public List<LandscapeResponse> getAllLandscapes() {
        List<Landscape> landscapes = landscapeRepository.findAll(Sort.by("createdDate").descending());
        return landscapes.stream()
                .map(LandscapeMapper::mapToLandscapeResponse)
                .toList();
    }

    public String updateLandscape(UUID landscapeId, LandscapeRequest request) {
        Landscape landscape = landscapeRepository.findById(landscapeId)
                .orElseThrow(() -> new EntityNotFoundException("No landscape found with id: " + landscapeId));
        landscape.setName(request.name());
        return landscapeRepository.save(landscape).getId().toString();
    }

    public void deleteLandscape(UUID landscapeId) {
        Landscape landscape = landscapeRepository.findById(landscapeId)
                .orElseThrow(() -> new EntityNotFoundException("No landscape found with id: " + landscapeId));
        landscapeRepository.delete(landscape);
    }
}
