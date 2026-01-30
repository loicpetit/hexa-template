package hexa.template.core.springboot.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpringbootAuthoritiesProviderTest {
    @InjectMocks
    SpringbootAuthoritiesProvider provider;

    @Mock
    AuthenticationProvider authenticationProvider;

    @Nested
    class WithAuthentication {
        @Mock
        Authentication authentication;

        @BeforeEach
        void before() {
            when(authenticationProvider.getAuthentication()).thenReturn(authentication);
        }

        @Test
        void ifAnyGrantedAuthoritiesShouldBeEmpty() {
            assertThat(provider.getUserAuthorities())
                    .isEmpty();
        }

        @Test
        void shouldReturnAuthorities() {
            doReturn(List.of(
                    new SimpleGrantedAuthority("abc"),
                    new SimpleGrantedAuthority("def")
            )).when(authentication).getAuthorities();

            assertThat(provider.getUserAuthorities())
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
            doReturn(List.of(
                    nullAuthority,
                    emptyAuthority,
                    blankAuthority,
                    authority
            )).when(authentication).getAuthorities();

            assertThat(provider.getUserAuthorities())
                    .containsOnly("abc");
        }

        @Test
        void shouldTrimValues() {
            doReturn(List.of(
                    new SimpleGrantedAuthority(" abc  "),
                    new SimpleGrantedAuthority("   def    ")
            )).when(authentication).getAuthorities();

            assertThat(provider.getUserAuthorities())
                    .containsOnly("abc", "def");
        }
    }

    @Nested
    class WithoutAuthentication {
        @Test
        void shouldBeEmpty() {
            assertThat(provider.getUserAuthorities())
                    .isEmpty();
        }
    }
}