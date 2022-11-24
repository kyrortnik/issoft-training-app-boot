package com.issoft.ticketstoreapp.service;

import com.issoft.ticketstoreapp.annotation.Audit;
import com.issoft.ticketstoreapp.dto.RowDTO;
import com.issoft.ticketstoreapp.mapper.RowMapper;
import com.issoft.ticketstoreapp.model.Row;
import com.issoft.ticketstoreapp.repository.RowRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RowService.class);

    private final RowRepository rowRepository;
    private final RowMapper mapper;

//    @Autowired
//    public RowService(RowRepository rowRepository, RowMapper mapper) {
//        this.rowRepository = rowRepository;
//        this.mapper = mapper;
//    }

    @Audit
    public RowDTO saveRow(RowDTO rowDTO) {
        LOGGER.debug("Entering RowService.saveRow()");
        Row row = this.mapper.toRow(rowDTO);

        RowDTO savedOrUpdatedCinema = mapper.toDto(this.rowRepository.save(row));

        LOGGER.debug("Exiting RowService.saveRow()");
        return savedOrUpdatedCinema;
    }

    @Audit
    public void deleteRow(Long rowId) {
        LOGGER.debug("Entering RowService.deleteRow()");

        this.rowRepository.deleteById(rowId);

        LOGGER.debug("Exiting RowService.deleteRow()");
    }
}