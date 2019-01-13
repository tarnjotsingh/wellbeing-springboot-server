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
public class TestResourceTest {

    private MockMvc mock;

    @InjectMocks
    private TestResource testResource;

    @Before
    public void setUp() throws Exception {
        mock = MockMvcBuilders.standaloneSetup(testResource)
                .build();
    }

    @Test
    public void testTestResource() throws Exception {
        mock.perform(MockMvcRequestBuilders.get("/test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Test"));
    }


}