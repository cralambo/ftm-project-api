package org.ftm.api.disbursement_schedule;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.project.Project;
import org.ftm.api.project.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DisbursementScheduleService {

    private final DisbursementScheduleRepository disbursementScheduleRepository;
    private final ProjectRepository projectRepository;

    public String create(DisbursementScheduleRequest request) {
        final Project project = projectRepository.findById(UUID.fromString(request.project_id()))
                .orElseThrow(() -> new EntityNotFoundException("No project found with id: " + request.project_id()));
        DisbursementSchedule disbursementSchedule = DisbursementScheduleMapper.mapToCreateRequest(request);
        disbursementSchedule.setProject(project);
        return disbursementScheduleRepository.save(disbursementSchedule).getId().toString();
    }

    public DisbursementScheduleResponse getScheduleById(UUID scheduleId) {
        return disbursementScheduleRepository.findWithDetailsById(scheduleId)
                .map(DisbursementScheduleMapper::mapToDisbursementScheduleResponse)
                .orElseThrow(() -> new EntityNotFoundException("No disbursement schedule found with id:: " + scheduleId));
    }
}
