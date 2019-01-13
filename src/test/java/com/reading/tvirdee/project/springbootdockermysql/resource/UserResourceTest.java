package com.reading.tvirdee.project.springbootdockermysql.resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(SpringJUnit4ClassRunner.class)
public class UserResourceTest {

    private MockMvc mock;

    @InjectMocks
    UserResource userResource;

    @Before
    public void setUp() {
        mock = MockMvcBuilders.standaloneSetup(userResource).build();
    }

    @Test
    public void userResourceTest() throws Exception {
        mock.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("This is a test bro"));
    }
}