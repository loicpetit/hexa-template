package hexa.template.email.api.web;

import hexa.template.email.api.dto.EmailDto;
import hexa.template.email.api.mapper.EmailMapper;
import hexa.template.email.core.model.Email;
import hexa.template.email.core.usecase.DeleteEmail;
import hexa.template.email.core.usecase.GetEmails;
import hexa.template.email.core.usecase.SaveEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@RestController
@Slf4j
public class EmailController implements hexa.template.email.api.web.EmailsApi {
    private static final String INVALIDATE_CACHE_HEADER = "X-Invalidate-Cache";

    private final SaveEmail save;
    private final GetEmails get;
    private final DeleteEmail delete;
    private final EmailMapper mapper;
    private final String pathPattern;

    public EmailController(
            final SaveEmail save,
            final GetEmails get,
            final DeleteEmail delete,
            final EmailMapper mapper
    ) {
        this.save = save;
        this.get = get;
        this.delete = delete;
        this.mapper = mapper;
        this.pathPattern = getPathPattern();
    }

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
        return ResponseEntity.noContent()
                .header(INVALIDATE_CACHE_HEADER, getInvalidateCache(id))
                .build();
    }

    @Override
    public ResponseEntity<Void> deleteEmailById(Long id) {
        log.info("Delete email with id {}", id);
        delete.byId(id);
        return ResponseEntity.noContent()
                .header(INVALIDATE_CACHE_HEADER, getInvalidateCache(id))
                .build();
    }

    private String getPathPattern() {
        final Method method = getApiMethod("getEmailById", Long.class);
        return method.getAnnotation(RequestMapping.class).value()[0];
    }

    private Method getApiMethod(
            final String methodName,
            final Class<?>... parameterTypes
    ) {
        try {
            return EmailsApi.class.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to retrieve EmailsApi method " + methodName, e);
        }
    }

    private String getInvalidateCache(final Long id) {
        return pathPattern.replace("{id}", id.toString());
    }
}
