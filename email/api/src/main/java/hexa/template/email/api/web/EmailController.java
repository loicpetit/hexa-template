package hexa.template.email.api.web;

import hexa.template.email.api.dto.EmailDto;
import hexa.template.email.api.mapper.EmailDtoMapper;
import hexa.template.email.api.mapper.EmailMapper;
import hexa.template.email.core.model.Email;
import hexa.template.email.core.usecase.DeleteEmail;
import hexa.template.email.core.usecase.GetEmails;
import hexa.template.email.core.usecase.SaveEmail;
import hexa.template.email.persistence.adapter.EmailDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    final SaveEmail save;
    final GetEmails get;
    final DeleteEmail delete;
    final EmailMapper mapper;
    final EmailDtoMapper dtoMapper;
    final EmailDao dao;

    @GetMapping("/{id}")
    public ResponseEntity<EmailDto> getEmails(
            @PathVariable("id") final Long id
    ) {
        log.info("Get email from id {}", id);
        final EmailDto dto = dtoMapper.toDto(get.getEmailById(id));
        return ResponseEntity.ok()
                .eTag(Integer.toString(dto.hashCode())) // TODO hashcode temp solution, use sha1 or md5
                .body(dto);
    }

    @PostMapping
    public Long createEmail(@RequestBody final EmailDto dto) {
        log.info("Create email");
        log.debug("Email to create: {}", dto);
        final Email savedEmail = save.save(mapper.toEmail(dto));
        return savedEmail.id();
    }

    @PutMapping("/{id}")
    public void updateEmail(@PathVariable("id") final Long id, @RequestBody final EmailDto dto) {
        log.info("Update email with id {}", id);
        final Email emailToUpdate = mapper.toEmail(id, dto);
        save.save(emailToUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteEmail(@PathVariable("id") final Long id) {
        log.info("Delete email with id {}", id);
        delete.byId(id);
    }
}
