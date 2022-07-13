package pt.winprovit.exercise.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pt.winprovit.exercise.dto.SequenceDto;
import pt.winprovit.exercise.exception.AlticciException;
import pt.winprovit.exercise.util.Constant;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class AlticciSequenceService {

    private static final Map<BigInteger, BigInteger> CACHE = new HashMap<>();

    public SequenceDto generateAlticciSequence(String number) throws AlticciException {
        if (Objects.isNull(number) || !isValidNumber(number)) {
           throw new AlticciException(HttpStatus.BAD_REQUEST, Constant.INVALID_NUMBER);
        }

        BigInteger value = new BigInteger(number);
        SequenceDto sequenceDto = new SequenceDto();
        sequenceDto.setSeries(getAlticciSequenceSeries(value));
        sequenceDto.setValue(getAlticciSequence(value).toString());
        return sequenceDto;
    }

    private String getAlticciSequenceSeries(BigInteger number) throws AlticciException {

        if (Objects.isNull(number) || BigInteger.ZERO.compareTo(number) > 0) {
            throw new AlticciException(HttpStatus.BAD_REQUEST, Constant.INVALID_NUMBER);
        }

        StringBuilder seriesBuilder = new StringBuilder();
        BigInteger currentNumber = BigInteger.ZERO;
        while (number.compareTo(currentNumber) >= 0) {
            seriesBuilder.append(getAlticciSequence(currentNumber));
            if (number.compareTo(currentNumber) > 0) {
                seriesBuilder.append(",");
            }
            currentNumber = currentNumber.add(BigInteger.ONE);
        }

        return seriesBuilder.toString();
    }

    public BigInteger getAlticciSequence(BigInteger number) throws AlticciException {

        if (Objects.isNull(number) || BigInteger.ZERO.compareTo(number) > 0) {
            throw new AlticciException(HttpStatus.BAD_REQUEST, Constant.INVALID_NUMBER);
        }

        if (CACHE.containsKey(number)) {
            return CACHE.get(number);
        }

        if (number.compareTo(BigInteger.ZERO) == 0) {
            return  getFromCache(number, BigInteger.ZERO);
        }

        if (number.compareTo(BigInteger.ONE) == 0) {
            return getFromCache(number, BigInteger.ONE);
        }

        if (number.compareTo(BigInteger.TWO) == 0) {
            return getFromCache(number, BigInteger.ONE);
        }

        BigInteger computedValue = getAlticciSequence(number.subtract(BigInteger.valueOf(3))).add(getAlticciSequence(number.subtract(BigInteger.TWO)));
        return getFromCache(number, computedValue);
    }

    private BigInteger getFromCache(BigInteger number, BigInteger computedValue) {
        CACHE.putIfAbsent(number, computedValue);
        return CACHE.get(number);
    }

    private boolean isValidNumber(String number) {
        Pattern pattern = Pattern.compile("\\d+");
        return pattern.matcher(number).matches();
    }
}
