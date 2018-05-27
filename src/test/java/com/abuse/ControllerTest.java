package com.abuse;

import com.abuse.module.FraudRepository;
import com.abuse.rule.FromEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.abuse.rule.MockData.*;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.empty;

@RunWith(SpringRunner.class)
@WebMvcTest(FraudController.class)
public class ControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private FraudRepository repository;

    @Test
    public void testFraud() throws Exception {
        given(repository.findById(eq(1l)))
            .willReturn(new HashSet<String>() {{
            add("RuleA");
        }});
        mvc.perform(get("/v1/fraud/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("@.rule").value("RuleA"));
    }
}
