package com.example.project.MyNeighborhood.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class RequestForm {
    Type type;
    Float latitude;
    Float longitude;
    String address;
    String description;
}
