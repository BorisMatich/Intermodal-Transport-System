package com.transport.parser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.dto.DepartureDTO;
import com.transport.dto.StationDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class TransportParser{
    private final ObjectMapper mapper;
    private final JsonNode root;

    public TransportParser(String filePath){
        try(FileInputStream file = new FileInputStream(filePath)){
            this.mapper = new ObjectMapper();
            this.root = mapper.readTree(file);
        }catch(IOException e){
            throw new RuntimeException("Fajl sa transportnim podacima nije pronadjen! " + e);
        }
    }

    public List<StationDTO> parseStations(){
          return mapper.convertValue(
                  root.get("stations"),
                  new TypeReference<List<StationDTO>>() {}
          );
    }

    public List<DepartureDTO> parseDeparture(){
        return mapper.convertValue(
                root.get("departures"),
                new TypeReference<List<DepartureDTO>>() {}
        );
    }

}
