package pt.winprovit.exercise.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import pt.winprovit.exercise.dto.SequenceDto;
import pt.winprovit.exercise.exception.AlticciException;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;

class AlticciSequenceServiceTest {

    private AlticciSequenceService alticciSequenceService;

    @BeforeEach
    void setUp() {
        alticciSequenceService = new AlticciSequenceService();
    }

    @Test
    void given_numberAsNull_when_generateAlticciSequence_should_ThrowException() {
        Assertions.assertThrows(AlticciException.class, () -> alticciSequenceService.generateAlticciSequence(null));
    }

    @Test
    void given_numberLessThanZero_when_generateAlticciSequence_should_ThrowException() {
        Assertions.assertThrows(AlticciException.class, () -> alticciSequenceService.generateAlticciSequence("-1"));
    }

    @Test
    void given_numberGreaterThanOrEqualsZero_when_generateAlticciSequence_should_ReturnValidSequenceDto() throws AlticciException {
        SequenceDto sequenceDto = alticciSequenceService.generateAlticciSequence("4");
        Assertions.assertAll("Validate sequenceDto",
                () -> Assertions.assertNotNull(sequenceDto),
                () -> Assertions.assertEquals("2", sequenceDto.getValue()),
                () -> Assertions.assertEquals("0,1,1,1,2", sequenceDto.getSeries()));

        SequenceDto sequenceDto2 = alticciSequenceService.generateAlticciSequence("0");
        Assertions.assertAll("Validate sequenceDto2",
                () -> Assertions.assertNotNull(sequenceDto2),
                () -> Assertions.assertEquals("0", sequenceDto2.getValue()),
                () -> Assertions.assertEquals("0", sequenceDto2.getSeries()));
    }

    @Test
    void given_numberAsNull_when_GetAlticciSequenceSeries_should_ThrowExpectation() {
        BigInteger number = null;
        UndeclaredThrowableException throwable = Assertions.assertThrows(UndeclaredThrowableException.class, () -> ReflectionTestUtils.invokeMethod(alticciSequenceService, "getAlticciSequenceSeries", number));
        Assertions.assertInstanceOf(AlticciException.class, throwable.getCause());
    }

    @Test
    void given_numberLessThanZero_when_GetAlticciSequenceSeries_should_ThrowExpectation() {
        UndeclaredThrowableException throwable = Assertions.assertThrows(UndeclaredThrowableException.class, () -> ReflectionTestUtils.invokeMethod(alticciSequenceService, "getAlticciSequenceSeries", BigInteger.valueOf(-1)));
        Assertions.assertInstanceOf(AlticciException.class, throwable.getCause());
    }

    @Test
    void given_numberGreaterThanOrEqualsZero_when_GetAlticciSequenceSeries_should_PassExpectation() throws AlticciException {
        Assertions.assertEquals("0,1,1,1,2,2,3", ReflectionTestUtils.invokeMethod(alticciSequenceService, "getAlticciSequenceSeries", BigInteger
                .valueOf(6)));
        Assertions.assertEquals("0", ReflectionTestUtils.invokeMethod(alticciSequenceService, "getAlticciSequenceSeries", BigInteger.ZERO));
        Assertions.assertEquals("0,1", ReflectionTestUtils.invokeMethod(alticciSequenceService, "getAlticciSequenceSeries", BigInteger.ONE));
    }

    @Test
    void given_numberAsNull_when_GetAlticciSequence_should_throwException() {
        Assertions.assertThrows(AlticciException.class, () -> alticciSequenceService.getAlticciSequence(null));
    }

    @Test
    void given_numberLessThanZero_when_GetAlticciSequence_should_throwException() {
        Assertions.assertThrows(AlticciException.class, () -> alticciSequenceService.getAlticciSequence(BigInteger.valueOf(-1)));
    }

    @Test
    void given_ValidNumber_when_GetAlticciSequence_should_PassExpectation() throws AlticciException {
        Assertions.assertEquals(BigInteger.ZERO, alticciSequenceService.getAlticciSequence(BigInteger.ZERO));
        Assertions.assertEquals(BigInteger.ONE, alticciSequenceService.getAlticciSequence(BigInteger.ONE));
        Assertions.assertEquals(BigInteger.ONE, alticciSequenceService.getAlticciSequence(BigInteger.TWO));
        Assertions.assertEquals(BigInteger.ONE, alticciSequenceService.getAlticciSequence(BigInteger.valueOf(3)));
        Assertions.assertEquals(BigInteger.TWO, alticciSequenceService.getAlticciSequence(BigInteger.valueOf(4)));
        Assertions.assertEquals(BigInteger.TWO, alticciSequenceService.getAlticciSequence(BigInteger.valueOf(5)));
        Assertions.assertEquals(BigInteger.valueOf(3), alticciSequenceService.getAlticciSequence(BigInteger.valueOf(6)));
        Assertions.assertEquals(BigInteger.valueOf(4), alticciSequenceService.getAlticciSequence(BigInteger.valueOf(7)));
        Assertions.assertEquals(BigInteger.valueOf(5), alticciSequenceService.getAlticciSequence(BigInteger.valueOf(8)));
        Assertions.assertEquals(BigInteger.valueOf(7), alticciSequenceService.getAlticciSequence(BigInteger.valueOf(9)));
        Assertions.assertEquals(BigInteger.valueOf(9), alticciSequenceService.getAlticciSequence(BigInteger.valueOf(10)));
    }

    @Test
    void given_numberGreaterThanOrEqualToZero_when_isValidNumber_shouldReturnTrue() {
        Boolean isValidNumber = ReflectionTestUtils.invokeMethod(alticciSequenceService, "isValidNumber", "2");
        Assertions.assertNotNull(isValidNumber);
        Assertions.assertTrue(isValidNumber);

        isValidNumber = ReflectionTestUtils.invokeMethod(alticciSequenceService, "isValidNumber", "0");
        Assertions.assertNotNull(isValidNumber);
        Assertions.assertTrue(isValidNumber);
    }

    @Test
    void given_numberLessThanZero_when_isValidNumber_shouldReturnFalse() {
        Boolean isValidNumber = ReflectionTestUtils.invokeMethod(alticciSequenceService, "isValidNumber", "-1");
        Assertions.assertNotNull(isValidNumber);
        Assertions.assertFalse(isValidNumber);
    }

    @Test
    void given_invalidNumber_when_isValidNumber_shouldReturnFalse() {
        Boolean isValidNumber = ReflectionTestUtils.invokeMethod(alticciSequenceService, "isValidNumber", "invalid");
        Assertions.assertNotNull(isValidNumber);
        Assertions.assertFalse(isValidNumber);
    }
}