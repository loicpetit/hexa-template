package hexa.template.email.api.web;

import hexa.template.email.api.dto.EmailDto;
import hexa.template.email.api.mapper.EmailMapper;
import hexa.template.email.core.model.Email;
import hexa.template.email.core.usecase.DeleteEmail;
import hexa.template.email.core.usecase.GetEmails;
import hexa.template.email.core.usecase.SaveEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController implements hexa.template.email.api.web.EmailsApi {
    final SaveEmail save;
    final GetEmails get;
    final DeleteEmail delete;
    final EmailMapper mapper;

    @Override
    public ResponseEntity<EmailDto> getEmailById(Long id) {
        log.info("Get email from id {}", id);
        final var email = get.getEmailById(id);
        final EmailDto dto = new EmailDto().value(email.value()).modified(email.modified());
        return ResponseEntity.ok()
                .eTag(Integer.toString(dto.hashCode())) // TODO hashcode temp solution, use sha1 or md5
                .body(dto);
    }

    @Override
    public ResponseEntity<Long> createEmail(EmailDto dto) {
        log.info("Create email");
        log.debug("Email to create: {}", dto);
        final Email savedEmail = save.save(mapper.toEmail(dto));
        return ResponseEntity.ok().body(savedEmail.id());
    }

    @Override
    public ResponseEntity<Void> updateEmailById(Long id, EmailDto dto) {
        log.info("Update email with id {}", id);
        final Email emailToUpdate = mapper.toEmail(id, dto);
        save.save(emailToUpdate);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteEmailById(Long id) {
        log.info("Delete email with id {}", id);
        delete.byId(id);
        return ResponseEntity.noContent().build();
    }
}
