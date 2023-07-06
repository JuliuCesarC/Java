package com.cesar.aula04.model;

public class Address {
  private String cep;
  private String street;
  private String district;
  private String city;
  private String state;
  private String complement;

  public Address(String cep) {
    this.cep = cep;
  }

  public Address(ViaCepAddress viaCepRecord) {
    this.cep = viaCepRecord.cep();
    this.street = viaCepRecord.logradouro();
    this.district = viaCepRecord.bairro();
    this.city = viaCepRecord.localidade();
    this.state = viaCepRecord.uf();
    this.complement = viaCepRecord.complemento();
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getComplement() {
    return complement;
  }

  public void setComplement(String complement) {
    this.complement = complement;
  }
}
