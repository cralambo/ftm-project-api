package org.ftm.api.sub_landscape;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.landscape.Landscape;
import org.ftm.api.landscape.LandscapeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubLandscapeService {

    private final SubLandscapeRepository subLandscapeRepository;
    private final LandscapeRepository landscapeRepository;

    public String createSubLandscape(SubLandscapeRequest request) {
        SubLandscape subLandscape = SubLandscapeMapper.mapToCreateSubLandscapeRequest(request);
        final Landscape landscape = landscapeRepository.findById(UUID.fromString(request.landscape_id()))
                .orElseThrow(() -> new EntityNotFoundException("No landscape found with id: " + request.landscape_id()));
        subLandscape.setLandscape(landscape);
        return subLandscapeRepository.save(subLandscape).getId().toString();
    }

    public SubLandscapeResponse getSubLandscapeById(UUID subLandscapeId) {
        return subLandscapeRepository.findById(subLandscapeId)
                .map(SubLandscapeMapper::mapToSubLandscapeResponse)
                .orElseThrow(() -> new EntityNotFoundException("No sub landscape found with id: " + subLandscapeId));
    }

    public PageResponse<SubLandscapeResponse> getSubLandscapes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<SubLandscape> subLandscapes = subLandscapeRepository.findAll(pageable);
        List<SubLandscapeResponse> contents = subLandscapes.stream()
                .map(SubLandscapeMapper::mapToSubLandscapeResponse)
                .toList();

        return PageResponse.<SubLandscapeResponse>builder()
                .content(contents)
                .number(subLandscapes.getNumber())
                .size(subLandscapes.getSize())
                .totalElements(subLandscapes.getTotalElements())
                .totalPages(subLandscapes.getTotalPages())
                .first(subLandscapes.isFirst())
                .last(subLandscapes.isLast())
                .build();
    }

    public List<SubLandscapeResponse> getAllSubLandscapes() {
        final List<SubLandscape> subLandscapes = subLandscapeRepository.findAll(Sort.by("createdDate").descending());
        return subLandscapes.stream()
                .map(SubLandscapeMapper::mapToSubLandscapeResponse)
                .toList();
    }

    public List<SubLandscapeResponse> getSubLandscapesByLandscapeId(UUID landscapeId) {
        final List<SubLandscape> subLandscapes = subLandscapeRepository.findByLandscapeId(landscapeId);
        return subLandscapes.stream()
                .map(SubLandscapeMapper::mapToSubLandscapeResponse)
                .toList();
    }

    public String updateSubLandscape(UUID subLandscapeId, SubLandscapeRequest request) {
        SubLandscape subLandscape = subLandscapeRepository.findById(subLandscapeId)
                .orElseThrow(() -> new EntityNotFoundException("No sub landscape found with id: " + subLandscapeId));
        subLandscape.setName(request.name());
        final Landscape landscape = landscapeRepository.findById(UUID.fromString(request.landscape_id()))
                .orElseThrow(() -> new EntityNotFoundException("No landscape found with id: " + request.landscape_id()));
        subLandscape.setLandscape(landscape);
        return subLandscapeRepository.save(subLandscape).getId().toString();
    }

    public void deleteSubLandscape(UUID subLandscapeId) {
        SubLandscape subLandscape = subLandscapeRepository.findById(subLandscapeId)
                .orElseThrow(() -> new EntityNotFoundException("No sub landscape found with id: " + subLandscapeId));
        subLandscapeRepository.delete(subLandscape);
    }
}
