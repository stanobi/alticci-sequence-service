package pt.winprovit.exercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pt.winprovit.exercise.api.AlticciApi;
import pt.winprovit.exercise.dto.SequenceDto;
import pt.winprovit.exercise.dto.SequenceResponseDto;
import pt.winprovit.exercise.exception.AlticciException;
import pt.winprovit.exercise.service.AlticciSequenceService;
import pt.winprovit.exercise.util.Constant;

@CrossOrigin(originPatterns = "*")
@RestController
@RequiredArgsConstructor
public class AlticciSequenceController implements AlticciApi {

    private final AlticciSequenceService alticciSequenceService;

    @Override
    public ResponseEntity<SequenceResponseDto> generateAlticciSequeuce(String number) {
        SequenceResponseDto sequenceResponseDto = new SequenceResponseDto();
        try {
            SequenceDto sequenceDto = alticciSequenceService.generateAlticciSequence(number);
            sequenceResponseDto.setData(sequenceDto);
            sequenceResponseDto.setMessage(Constant.SUCCESS_MESSAGE);
            return ResponseEntity.ok(sequenceResponseDto);
        } catch (AlticciException e) {
            sequenceResponseDto.setMessage(e.getMessage());
            return ResponseEntity.status(e.getHttpStatus()).body(sequenceResponseDto);
        }
    }
}
