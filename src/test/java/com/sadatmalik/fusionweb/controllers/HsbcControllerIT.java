package com.sadatmalik.fusionweb.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureMockMvc
class HsbcControllerIT extends ControllerTestBase {
//
//    @Autowired
//    HsbcController controller;
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @BeforeEach
//    void setUp() {
//        createPrincipal();
//    }
//
//    @Test
//    public void testWebOnlyContextLoads() {
//        assertThat(controller).isNotNull();
//    }
//
//    @Test
//    void testHsbcAuthorizationUrl() throws Exception {
//        mockMvc.perform(get("/hsbc")
//                        .with(user(principal)))
//
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name(containsString("redirect:" +
//                        HsbcAuthenticationEndpoints.AUTHORIZE_URL)));
//    }
//
//    @Test
//    void testHsbcCallbackWithoutAuthCode() throws Exception {
//        mockMvc.perform(get("/")
//                        .with(user(principal)))
//
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name(
//                        containsString("redirect:/quickstats")));
//    }
//
//    @Test
//    void testHsbcCallbackWithAuthCode() throws Exception {
//        mockMvc.perform(get("/?code=1234")
//                        .with(user(principal)))
//
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name(
//                        containsString("redirect:/dashboard")));
//    }
}