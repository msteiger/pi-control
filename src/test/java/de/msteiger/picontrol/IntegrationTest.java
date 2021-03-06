package de.msteiger.picontrol;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import de.msteiger.picontrol.model.RelayInfo;
import de.msteiger.picontrol.services.GpioService;

/**
 * TODO: describe
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @MockBean
    private GpioService gpioService;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testToggle() {
        assertEquals(template.postForEntity("/relays/light1/toggle", null, String.class).getStatusCode(), HttpStatus.OK);
        verify(gpioService).toggle(4);
    }

    @Test
    public void testListAll() {
        RelayInfo[] list = template.getForObject("/relays", RelayInfo[].class);
        assertEquals("Light 1", list[1].getName());
    }
}
