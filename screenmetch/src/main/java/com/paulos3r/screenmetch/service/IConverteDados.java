package com.paulos3r.screenmetch.service;

public interface IConverteDados {
  <T> T obterDados(String json, Class<T> classe);
}
