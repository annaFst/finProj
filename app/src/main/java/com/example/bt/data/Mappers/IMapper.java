package com.example.bt.data.Mappers;

public interface IMapper<From, To> {

    To map(From from);
}
