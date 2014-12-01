package com.example.serj.inmobiliaria;

import java.io.Serializable;

public class Inmueble implements Comparable<Inmueble>, Serializable{
    private String _id;
    private String localidad;
    private String direccion;
    private String tipo;
    private double precio;
    private String foto;

    public Inmueble() {}

    public Inmueble(String _id, String localidad, String direccion, String tipo, double precio, String foto) {
        this._id = _id;
        this.localidad = localidad;
        this.direccion = direccion;
        this.tipo = tipo;
        this.precio = precio;
        this.foto = foto;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object) this).getClass() != o.getClass()) return false;

        Inmueble inmueble = (Inmueble) o;

        if (_id != null ? !_id.equals(inmueble._id) : inmueble._id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return _id != null ? _id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "_id='" + _id + '\'' +
                ", localidad='" + localidad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                ", foto='" + foto + '\'' +
                '}';
    }

    @Override
    public int compareTo(Inmueble another) {
        return 0;
    }
}
