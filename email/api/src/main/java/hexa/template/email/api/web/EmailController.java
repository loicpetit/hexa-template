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
public class EmailController {
    final SaveEmail save;
    final GetEmails get;
    final DeleteEmail delete;
    final EmailMapper mapper;
    final EmailDtoMapper dtoMapper;
    final EmailDao dao;

    @GetMapping("/{id}")
    public EmailDto getEmails(@PathVariable("id") final Long id) {
        return dtoMapper.toDto(get.getEmailById(id));
    }

    @PostMapping
    public Long createEmail(@RequestBody final EmailDto dto) {
        final Email savedEmail = save.save(mapper.toEmail(dto));
        return savedEmail.id();
    }

    @PutMapping("/{id}")
    public void updateEmail(@PathVariable("id") final Long id, @RequestBody final EmailDto dto) {
        final Email emailToUpdate = mapper.toEmail(id, dto);
        save.save(emailToUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteEmail(@PathVariable("id") final Long id) {
        delete.byId(id);
    }
}
