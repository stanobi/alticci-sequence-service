package pt.winprovit.exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import pt.winprovit.exercise.dto.SequenceDto;
import pt.winprovit.exercise.dto.SequenceResponseDto;
import pt.winprovit.exercise.exception.AlticciException;
import pt.winprovit.exercise.service.AlticciSequenceService;
import pt.winprovit.exercise.util.Constant;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AlticciSequenceController.class)
class AlticciSequenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlticciSequenceService alticciSequenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void given_validInput_when_generateAlticciSequence_ReturnsSequenceAndSeries() throws Exception {
        SequenceDto sequenceDto = new SequenceDto();
        sequenceDto.setSeries("0,1,1,1,2");
        sequenceDto.setValue("2");
        Mockito.when(alticciSequenceService.generateAlticciSequence(Mockito.anyString())).thenReturn(sequenceDto);

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/alticci/4"));
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        SequenceResponseDto sequenceResponseDto = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), SequenceResponseDto.class);
        Assertions.assertNotNull(sequenceResponseDto);
        Assertions.assertEquals(Constant.SUCCESS_MESSAGE, sequenceResponseDto.getMessage());
        Assertions.assertEquals(sequenceDto.getSeries(), sequenceResponseDto.getData().getSeries());
        Assertions.assertEquals(sequenceDto.getValue(), sequenceResponseDto.getData().getValue());
    }

    @Test
    void given_invalidInput_when_generateAlticciSequence_throwsException() throws Exception {
        Mockito.when(alticciSequenceService.generateAlticciSequence(Mockito.anyString())).thenThrow(new AlticciException(HttpStatus.BAD_REQUEST, "Exception occurred"));

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/alticci/-1"));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mockHttpServletResponse.getStatus());

        SequenceResponseDto sequenceResponseDto = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), SequenceResponseDto.class);
        Assertions.assertNotNull(sequenceResponseDto);
        Assertions.assertEquals("Exception occurred", sequenceResponseDto.getMessage());
        Assertions.assertNull(sequenceResponseDto.getData());
    }

    public static MockHttpServletResponse getMockMvc(MockMvc mockMvc, MockHttpServletRequestBuilder mockHttpServletRequestBuilder)
            throws Exception {
        return mockMvc.perform(mockHttpServletRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();
    }
}