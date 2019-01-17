package e.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import e.AccountPageResponse;
import e.RegisterPageRequest;
import e.RegisterPageResponse;
import e.model.Account;
import e.model.Reference;
import e.service.UrlShortenerService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.InternalResourceView;

@Controller
public class UrlShortenerController {
    private UrlShortenerService service;

    /**
     * Opening of an account.
     * @param request Request body
     * @return Response
     */
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public ResponseEntity<AccountPageResponse> accountPage(@Valid @RequestBody Account request) {
        ResponseEntity<AccountPageResponse> result;
        AccountPageResponse response = new AccountPageResponse();
        if (this.service.createAccount(request) == null) {
            response.setSuccess(false);
            response.setDescription("Account with that ID already exists");
            result = new ResponseEntity<>(
                                                response, HttpStatus.OK);
        } else {
            response.setSuccess(true);
            response.setDescription("Your account is opened");
            response.setPassword(request.getPassword());
            result = new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        return result;
    }

    /**
     * Registration of URLs.
     * @param principal Authorization
     * @param request Request body
     * @return Response
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<RegisterPageResponse> registerPage(
                                              HttpServletRequest request,
                                                     Principal principal,
                   @Valid @RequestBody RegisterPageRequest requestBody) {
        int length = request.getRequestURL().length();
        String baseUrl = request.getRequestURL().substring(0,
                             length - request.getServletPath().length());
        Reference reference =
           this.service.createReference(requestBody, principal, baseUrl);
        return new ResponseEntity<>(
                       new RegisterPageResponse(reference.getShortUrl()),
                                                      HttpStatus.CREATED);
    }

    @RequestMapping(value = "/statistic/{accountId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Integer> statisticPage(
                   @PathVariable String accountId, Principal principal) {
        return this.service.findReferenceByAccountId(accountId);
    }

    /**
     * The application help page.
     */
    @RequestMapping(value = "/help")
    public @ResponseBody String helpPage() throws IOException {
        return this.getResourceAsString("/help.html");
    }

    private String getResourceAsString(String resource) throws IOException {
        InputStream in = getClass().getResourceAsStream(resource);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i;
        while((i = in.read()) != -1) {
            out.write(i);
        }
        out.close();
        return out.toString();
    }

    /**
     * Replaces the client on the configured address with the configured
     * http status.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return Redirect
     */
    @RequestMapping("/**")
    public View redirectPage(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        String requestUrl = request.getRequestURL().toString();
        Reference reference =
                        this.service.findReferenceByShortUrl(requestUrl);
        RedirectView result = null;
        if (reference == null) {
            response.sendError(400,
                     String.format("Short url \"%s\" was not registered",
                                                            requestUrl));
        } else {
            result = new RedirectView();
            result.setStatusCode(
                        HttpStatus.resolve(reference.getRedirectType()));
            result.setUrl(reference.getUrl());
        }
        return result;
    }

    @Autowired
    public void setUrlShortenerService(UrlShortenerService service) {
        this.service = service;
    }
}
