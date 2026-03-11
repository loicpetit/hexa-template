package hexa.template.user.springboot.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthoryMapperTest {
    final AuthoryMapper mapper = new AuthoryMapper();

    @Test
    void shouldReturnAuthorities() {
        final Collection<? extends GrantedAuthority> grantedAuthorities = List.of(
                new SimpleGrantedAuthority("abc"),
                new SimpleGrantedAuthority("def")
        );

        final var authorities = mapper.toAuthorities(grantedAuthorities);

        assertThat(authorities)
                .containsOnly("abc", "def");
    }

    @Test
    void ifNullOrBlankValuesShouldSkipThem() {
        final var nullAuthority = mock(GrantedAuthority.class);
        final var emptyAuthority = mock(GrantedAuthority.class);
        final var blankAuthority = mock(GrantedAuthority.class);
        final var authority = mock(GrantedAuthority.class);
        when(emptyAuthority.getAuthority()).thenReturn("");
        when(blankAuthority.getAuthority()).thenReturn("   ");
        when(authority.getAuthority()).thenReturn("abc");
        final Collection<? extends GrantedAuthority> grantedAuthorities = List.of(
                nullAuthority,
                emptyAuthority,
                blankAuthority,
                authority
        );

        final var authorities = mapper.toAuthorities(grantedAuthorities);

        assertThat(authorities)
                .containsOnly("abc");
    }

    @Test
    void shouldTrimValues() {
        final Collection<? extends GrantedAuthority> grantedAuthorities = List.of(
                new SimpleGrantedAuthority(" abc  "),
                new SimpleGrantedAuthority("   def    ")
        );

        final var authorities = mapper.toAuthorities(grantedAuthorities);

        assertThat(authorities)
                .containsOnly("abc", "def");
    }

    @Test
    void isEmptyShouldReturnEmpty() {
        assertThat(mapper.toAuthorities(List.of()))
                .isEmpty();
    }
}

