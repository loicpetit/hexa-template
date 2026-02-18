package hexa.template.email.api.web;

import hexa.template.email.api.dto.EmailDto;
import hexa.template.email.api.mapper.EmailDtoMapper;
import hexa.template.email.core.usecase.GetEmails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {
    final GetEmails getter;
    final EmailDtoMapper mapper;

    @GetMapping("/{id}")
    public EmailDto emails(@PathVariable("id") final Long id) {
        return mapper.toDto(getter.getEmailById(id));
    }
}
