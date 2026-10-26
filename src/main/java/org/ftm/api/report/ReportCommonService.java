package org.ftm.api.report;

import lombok.RequiredArgsConstructor;
import org.ftm.api.partner_contact.PartnerContact;
import org.ftm.api.partner_contact.PartnerContactRepository;
import org.ftm.api.security.CustomUserDetails;
import org.ftm.api.user.User;
import org.ftm.api.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportCommonService {

    private final UserRepository userRepository;
    private final PartnerContactRepository partnerContactRepository;
    private final PartnerReportRepository partnerReportRepository;

    public User getCurrentUser(CustomUserDetails userDetails) {
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable."
                ));
    }

    public PartnerContact getPartnerContact(CustomUserDetails userDetails) {
        return partnerContactRepository.findByUserId(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Aucun contact partenaire n'est associé à cet utilisateur."
                ));
    }

    public PartnerReport getReport(UUID reportId) {
        return partnerReportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Rapport introuvable."
                ));
    }

    public void ensureCanRead(PartnerReport report, User actor) {
        boolean owner = report.getPartnerContact().getUser() != null
                && report.getPartnerContact().getUser().getId()
                .equals(actor.getId());

        boolean assigned = report.getAssignedTo() != null
                && report.getAssignedTo().getId().equals(actor.getId());

        if (!owner && !assigned) {
            // Les utilisateurs habilités à superviser tous les rapports
            // doivent être autorisés ici par votre politique d'accès.
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Accès refusé."
            );
        }
    }

    public PartnerReport getReportForUpdate(UUID reportId) {
        return partnerReportRepository.findByIdForUpdate(reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Rapport introuvable."
                ));
    }

    public void ensureOwner(PartnerReport report, User actor) {
        if (report.getPartnerContact().getUser() == null
                || !report.getPartnerContact().getUser().getId()
                .equals(actor.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Accès refusé."
            );
        }
    }

    public void ensureEditable(PartnerReport report) {
        if (report.getStatus() != ReportStatus.DRAFT
                && report.getStatus() != ReportStatus.RETURNED) {
            throw conflict("Le rapport n'est pas modifiable dans cet état.");
        }
    }

    public ResponseStatusException conflict(String message) {
        return new ResponseStatusException(HttpStatus.CONFLICT, message);
    }
}
