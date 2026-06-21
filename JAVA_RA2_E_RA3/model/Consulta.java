package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Consulta implements Serializable {

    private LocalDate data;
    private LocalTime horario;

    private Medico medico;
    private Paciente paciente;

    public Consulta(
            LocalDate data,
            LocalTime horario,
            Medico medico,
            Paciente paciente) {

        this.data = data;
        this.horario = horario;
        this.medico = medico;
        this.paciente = paciente;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    @Override
    public String toString() {

        return data
                + " "
                + horario
                + " | Médico: "
                + medico.getNome()
                + " | Paciente: "
                + paciente.getNome();
    }
}