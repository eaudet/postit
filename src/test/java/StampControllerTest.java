import com.jarics.postit.Application;
import com.jarics.postit.Note;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * PersonController Tester.
 *
 * @author <Erick Audet>
 * @version 1.0
 * @since <pre>Feb 5, 2018</pre>
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class StampControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() throws Exception {

    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testAnnotate() throws Exception {

        File file = new File("/Users/erickaudet/dev/postit/src/main/resources/original.pdf");
        FileInputStream fis = new FileInputStream(file);
        MockMultipartFile firstFile = new MockMultipartFile("data", "original.pdf", MediaType.APPLICATION_PDF_VALUE, fis);
        //TODO check if return value is a pdf with annotation.
        MvcResult wMvcResult = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/notes/annotate")
                .file(firstFile)
                .param("note", "une note"))
                .andExpect(status().is(200)).andReturn();
        FileOutputStream fos = new FileOutputStream( "tested_from_test.pdf" );
        fos.write( wMvcResult.getResponse().getContentAsByteArray());
        fos.close();

    }


    @Test
    public void testGetStuff() throws Exception {
        mockMvc.perform(get("/notes?note={note}", "Hello from hell")).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andExpect(jsonPath("$.note").value("Hello Satan!"));
    }


}
