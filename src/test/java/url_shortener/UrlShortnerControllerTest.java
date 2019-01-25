package url_shortener;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.jpa.JpaSystemException;
import org.hibernate.exception.ConstraintViolationException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.hamcrest.core.StringContains.containsString;
import url_shortener.model.Account;
import url_shortener.model.Reference;
import url_shortener.service.UrlShortenerService;
import url_shortener.web.UrlShortenerController;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Map;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@WebMvcTest(UrlShortenerController.class)
public class UrlShortnerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerService service;

    @Test
    public void whenAccountPageThenCreated() throws Exception {
        when(this.service.createAccount(any())).thenAnswer(
                             invocation -> {
                               Object[] args = invocation.getArguments();
                               Account account = (Account) args[0];
                               account.setPassword("password");
                               return account;});
        MockHttpServletRequestBuilder builder = post("/account")
                           .contentType("application/json;charset=UTF-8")
                           .content("{\"accountId\":\"one\"}");
        this.mockMvc.perform(builder).andDo(print())
          .andExpect(status().is(201))
          .andExpect(content().string(
            "{\"success\":true,\"description\":\"Your account is opened\",\"password\":\"password\"}"));
    }

    @Test
    public void whenAccountPageThenAccountExists() throws Exception {
        ConstraintViolationException cause =
                                mock(ConstraintViolationException.class);
        JpaSystemException exception = new JpaSystemException(cause);
        when(this.service.createAccount(any())).thenThrow(exception);
        MockHttpServletRequestBuilder builder = post("/account")
                           .contentType("application/json;charset=UTF-8")
                           .content("{\"accountId\":\"one\"}");
        this.mockMvc.perform(builder).andDo(print())
          .andExpect(status().is(200))
          .andExpect(content().string(
            "{\"success\":false,\"description\":\"Account with that ID already exists\",\"password\":null}"));
    }

    @Test
    public void whenRegisterPageThenCreated() throws Exception {
        Reference reference = new Reference();
        reference.setShortUrl("http://short.com/one");
        when(this.service.createReference(any(), any(), any()))
                                                  .thenReturn(reference);
        MockHttpServletRequestBuilder builder = post("/register")
              .header("Authorization", "Basic dXNlcjpwYXNzd29yZA==")
              .contentType("application/json;charset=UTF-8")
              .content(
                "{\"url\":\"http://onelong.com\",\"redirectType\":301}");
        this.mockMvc.perform(builder).andDo(print())
          .andExpect(status().is(201))
          .andExpect(content().string("{\"shortUrl\":\"http://short.com/one\"}"));
    }

    @Test
    public void whenRegisterPageAuthorizationMismatchThenUnauthorized()
                                                       throws Exception {
        Reference reference = new Reference();
        reference.setShortUrl("http://short.com/one");
        when(this.service.createReference(any(), any(), any()))
                                                  .thenReturn(reference);
        MockHttpServletRequestBuilder builder = post("/register")
              .header("Authorization", "Basic ........==")
              .contentType("application/json;charset=UTF-8")
              .content(
                "{\"url\":\"http://onelong.com\",\"redirectType\":301}");
        this.mockMvc.perform(builder).andDo(print())
          .andExpect(status().is(401));
    }

    @Test
    public void whenStatisticPageThenOk() throws Exception {
        Map<String, Integer> response = new HashMap<>();
        response.put("http://twolong.com/resource", 2);
        response.put("http://onelong.com/resource", 0);
        response.put("http://threelong.com/resource", 1000);
        when(this.service.findReferenceByAccountId(anyString()))
                                                   .thenReturn(response);
        MockHttpServletRequestBuilder builder = get("/statistic/user")
                  .header("Authorization", "Basic dXNlcjpwYXNzd29yZA==");
        this.mockMvc.perform(builder).andDo(print())
          .andExpect(status().is(200))
          .andExpect(content().string(
            "{\"http://onelong.com/resource\":0,\"http://twolong.com/resource\":2,\"http://threelong.com/resource\":1000}"));
    }

    @Test
    public void whenStatisticPageAuthorizationMismatchThenUnauthorized()
                                                       throws Exception {
        Map<String, Integer> response = new HashMap<>();
        response.put("http://twolong.com/resource", 2);
        response.put("http://onelong.com/resource", 0);
        response.put("http://threelong.com/resource", 1000);
        when(this.service.findReferenceByAccountId(anyString()))
                                                   .thenReturn(response);
        MockHttpServletRequestBuilder builder = get("/statistic/user")
                  .header("Authorization", "Basic ........==");
        this.mockMvc.perform(builder).andDo(print())
          .andExpect(status().is(401));
    }

    @Test
    public void whenRedirectPageRegisteredThenMovedPermanently() throws Exception {
        Reference reference = mock(Reference.class);
        when(reference.getUrl()).thenReturn("http://real.com");
        when(reference.getRedirectType()).thenReturn((short)301);
        when(this.service.findReferenceByShortUrl(anyString()))
                                          .thenReturn(reference);
        this.mockMvc.perform(get("/aaaaaa")).andDo(print())
          .andExpect(status().is(301));
    }

    @Test
    public void whenRedirectPageRegisteredThenMovedTemporarily() throws Exception {
        Reference reference = mock(Reference.class);
        when(reference.getUrl()).thenReturn("http://real.com");
        when(reference.getRedirectType()).thenReturn((short)302);
        when(this.service.findReferenceByShortUrl(anyString()))
                                          .thenReturn(reference);
        this.mockMvc.perform(get("/aaaaaa")).andDo(print())
          .andExpect(status().is(302));
    }

    @Test
    public void whenRedirectPageNotRegisteredThenBadRequest()
                                                       throws Exception {
        this.mockMvc.perform(get("/aaaaaa")).andDo(print())
          .andExpect(status().is(400));
    }

    @Test
    public void whenHelpPageThenOk() throws Exception {
        this.mockMvc.perform(get("/help")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(
                            "<b>http://localhost:8080/{shortUrl}</b>")));
    }
}
