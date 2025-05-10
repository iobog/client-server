package concurs.dto;

import concurs.model.*;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class UtilsDTO {

    public static ParticipantDTO toDTO(Participant participant) {
        if (participant == null) return null;
        return new ParticipantDTO(
                participant.getId(),
                participant.getCnp(),
                participant.getNume(),
                participant.getVarsta()
        );
    }

    public static Participant fromDTO(ParticipantDTO dto) {
        if (dto == null) return null;
        Participant participant = new Participant(
                dto.getCnp(),
                dto.getNume(),
                dto.getVarsta()
        );
        participant.setId(dto.getId());
        return participant;
    }

    public static PersoanaOficiuDTO toDTO(PersoanaOficiu persoana) {
        if (persoana == null) return null;
        return new PersoanaOficiuDTO(
                persoana.getId(),
                persoana.getOras(),
                persoana.getUsername(),
                persoana.getParola()
        );
    }

    public static PersoanaOficiu fromDTO(PersoanaOficiuDTO dto) {
        if (dto == null) return null;
        PersoanaOficiu persoana = new PersoanaOficiu(
                dto.getOras(),
                dto.getUsername(),
                dto.getParola()
        );
        persoana.setId(dto.getId());
        return persoana;
    }

    public static ProbaDTO toDTO(Proba proba) {
        if (proba == null) return null;
        return new ProbaDTO(
                proba.getId(),
                proba.getNume(),
                proba.getCategorieVarsta(),
                proba.getNumarParticipanti(),
                proba.getParticipantCount()
        );
    }

    public static ProbaDTO[] toDTO(Proba[] probe) {
        if (probe == null) return null;
        ProbaDTO[] probeDTO = new ProbaDTO[probe.length];
        System.out.println(probe.length);
        for (int i = 0; i < 9; i++) {
            System.out.println(probe[i].toString());
            probeDTO[i] = new ProbaDTO(
                    probe[i].getId(),
                    probe[i].getNume(),
                    probe[i].getCategorieVarsta(),
                    probe[i].getNumarParticipanti(),
                    probe[i].getParticipantCount()
            );

        }
        return probeDTO;
    }


    public static ParticipantDTO[] toDTO(Participant[] participanti) {
        if (participanti == null) return null;
        ParticipantDTO[] participantiDTO = new ParticipantDTO[participanti.length];
        for (int i = 0; i < participanti.length; i++) {
            participantiDTO[i] = new ParticipantDTO(
                    participanti[i].getId(),
                    participanti[i].getCnp(),
                    participanti[i].getNume(),
                    participanti[i].getVarsta()
            );
        }
        return participantiDTO;
    }


    public static Proba fromDTO(ProbaDTO dto) {
        if (dto == null) return null;
        Proba proba = new Proba(
                dto.getNume(),
                dto.getCategorieVarsta(),
                dto.getNumarParticipanti()
        );
        proba.setParticipantCount(dto.getParticipantCount());
        proba.setId(dto.getId());
        return proba;
    }

    public static InscriereDTO toDTO(Inscriere inscriere) {
        if (inscriere == null) return null;
        return new InscriereDTO(
                inscriere.getId(),
                inscriere.getParticipant(),
                inscriere.getProba()
        );

    }

    public static Inscriere fromDTO(InscriereDTO dto) {
        if (dto == null) return null;
        Inscriere inscriere = new Inscriere(
                dto.getParticipantId(),
                dto.getProbaId()
        );
        inscriere.setId(dto.getId());
        return inscriere;
    }

    public static Participant[] fromDTO(ParticipantDTO[] participantDTO) {
        if (participantDTO == null) return null;
        Participant[] participants = new Participant[participantDTO.length];
        for (int i = 0; i < participantDTO.length; i++) {
            participants[i] = new Participant(
                    participantDTO[i].getCnp(),
                    participantDTO[i].getNume(),
                    participantDTO[i].getVarsta()
            );
            participants[i].setId(participantDTO[i].getId());
        }
        return participants;
    }

    public static Proba[] fromDTO(ProbaDTO[] probe){
        if (probe == null) return null;
        Proba[] probeList = new Proba[probe.length];
        for (int i = 0; i < probe.length; i++) {
            probeList[i] = new Proba(
                    probe[i].getNume(),
                    probe[i].getCategorieVarsta(),
                    probe[i].getNumarParticipanti()
            );
            probeList[i].setId(probe[i].getId());
            probeList[i].setParticipantCount(probe[i].getParticipantCount());
        }
        return probeList;
    }

    public static Object toDTO(InscriereDTO inscriereDTO) {
        if (inscriereDTO == null) return null;
        return new InscriereDTO(
                inscriereDTO.getId(),
                inscriereDTO.getParticipantId(),
                inscriereDTO.getProbaId()
        );
    }
}
