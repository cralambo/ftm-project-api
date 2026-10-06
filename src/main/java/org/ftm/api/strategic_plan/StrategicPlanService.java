package org.ftm.api.strategic_plan;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StrategicPlanService {

    private final StrategicPlanRepository strategicPlanRepository;

    public UUID createStrategicPlan(StrategicPlanRequest request) {
        StrategicPlan strategicPlan = StrategicPlanMapper.mapToCreateStrategicPlanRequest(request);
        return strategicPlanRepository.save(strategicPlan).getId();
    }

    public StrategicPlanResponse getStrategicPlanById(UUID strategicPlanId) {
        return strategicPlanRepository.findById(strategicPlanId)
                .map(StrategicPlanMapper::mapToStrategicPlanResponse)
                .orElseThrow(() -> new EntityNotFoundException("No strategic plan found with id: " + strategicPlanId));
    }

    public PageResponse<StrategicPlanResponse> getStrategicPlans(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<StrategicPlan> strategicPlans = strategicPlanRepository.findAll(pageable);
        List<StrategicPlanResponse> contents = strategicPlans.stream()
                .map(StrategicPlanMapper::mapToStrategicPlanResponse)
                .toList();
        return PageResponse.<StrategicPlanResponse>builder()
                .content(contents)
                .number(strategicPlans.getNumber())
                .size(strategicPlans.getSize())
                .totalElements(strategicPlans.getTotalElements())
                .totalPages(strategicPlans.getTotalPages())
                .first(strategicPlans.isFirst())
                .last(strategicPlans.isLast())
                .build();
    }

    public List<ShortStrategicPlanResponse> getAllStrategicPlans() {
        List<StrategicPlan> strategicPlans = strategicPlanRepository.findAll(Sort.by("createdDate").descending());
        return strategicPlans.stream()
                .map(StrategicPlanMapper::mapToShortStrategicPlanResponse)
                .toList();
    }

    public UUID updateStrategicPlan(UUID strategicPlanId, StrategicPlanRequest request) {
        StrategicPlan strategicPlan = strategicPlanRepository.findById(strategicPlanId)
                .orElseThrow(() -> new EntityNotFoundException("No strategic plan found with id: " + strategicPlanId));
        strategicPlan.setName(request.name());
        strategicPlan.setCode(request.code());
        strategicPlan.setStartDate(LocalDate.parse(request.start_date()));
        strategicPlan.setEndDate(LocalDate.parse(request.end_date()));
        strategicPlan.setDescription(request.description());
        return strategicPlanRepository.save(strategicPlan).getId();
    }

    public void deleteStrategicPlan(UUID strategicPlanId) {
        final StrategicPlan strategicPlan = strategicPlanRepository.findById(strategicPlanId)
                .orElseThrow(() -> new EntityNotFoundException("No strategic plan found with id: " + strategicPlanId));
        strategicPlanRepository.delete(strategicPlan);
    }
}
