package com.example.serj.inmobiliaria;

import java.io.Serializable;

public class Inmueble implements Comparable<Inmueble>, Serializable{
    private int _id;
    private String localidad;
    private String direccion;
    private String tipo;
    private double precio;

    public Inmueble() {}

    public Inmueble(int _id, String localidad, String direccion, String tipo, double precio) {
        this._id = _id;
        this.localidad = localidad;
        this.direccion = direccion;
        this.tipo = tipo;
        this.precio = precio;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object) this).getClass() != o.getClass()) return false;

        Inmueble inmueble = (Inmueble) o;

        if (_id != inmueble._id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "_id='" + _id + '\'' +
                ", localidad='" + localidad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                '}';
    }

    @Override
    public int compareTo(Inmueble another) {
        return this._id-another._id;
    }
}
