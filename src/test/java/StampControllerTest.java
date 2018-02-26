import com.jarics.postit.Application;
import com.jarics.postit.Note;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    /**
     * Method: create(@RequestBody Person pPerson)
     */
    @Test
    public void testCreate() throws Exception {
        // mockMvc.perform(post("/notes").contentType(MediaType.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(wPerson))).andExpect(status().isOk());
        Note wNote = new Note();
        wNote.setNote("Hello from hell");
        mockMvc.perform(post("/notes").contentType(MediaType.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(wNote))).andExpect(status().isOk());
    }

    /**
     * Method: find(@RequestParam("name") String pName)
     */
    @Test
    public void testFind() throws Exception {
        Assert.assertTrue(true);
//TODO: Test goes here... 
    }


} 
