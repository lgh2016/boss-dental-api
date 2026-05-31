package mx.com.bossdental.api.appointments.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.appointments.constants.AppointmentStatusCode;
import mx.com.bossdental.api.appointments.dto.response.AppointmentDayScheduleResponse;
import mx.com.bossdental.api.appointments.dto.response.AppointmentDetailResponse;
import mx.com.bossdental.api.appointments.dto.response.AppointmentMonthScheduleResponse;
import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.appointments.mapper.AppointmentMapper;
import mx.com.bossdental.api.appointments.repository.AppointmentRepository;
import mx.com.bossdental.api.exceptions.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentScheduleService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    /**
     * Consulta la agenda diaria.
     *
     * Permite filtrar por doctor
     * y sucursal.
     *
     * @param date fecha consultada.
     * @param doctorId doctor opcional.
     * @param branchId sucursal opcional.
     * @return citas del día.
     */
    public List<AppointmentDayScheduleResponse> getDaySchedule(
            LocalDate date,
            Long doctorId,
            Long branchId
    ) {

        /*
         * Status visibles en agenda.
         */
        List<String> statuses = List.of(
                AppointmentStatusCode.CONFIRMED,
                AppointmentStatusCode.COMPLETED
        );

        /*
         * Consultar citas.
         */
        List<Appointment> appointments =
                appointmentRepository.findDaySchedule(
                        date,
                        doctorId,
                        branchId,
                        statuses
                );

        /*
         * Construir response.
         */
        return appointmentMapper.toDayScheduleResponseList(
                appointments
        );
    }


    /**
     * Consulta la agenda mensual.
     *
     * Permite filtrar por doctor
     * y sucursal.
     *
     * @param year año consultado.
     * @param month mes consultado.
     * @param doctorId doctor opcional.
     * @param branchId sucursal opcional.
     * @return resumen mensual.
     */
    public List<AppointmentMonthScheduleResponse> getMonthSchedule(
            Integer year,
            Integer month,
            Long doctorId,
            Long branchId
    ) {

        /*
         * Obtener rango de fechas.
         */
        LocalDate startDate =
                LocalDate.of(
                        year,
                        month,
                        1
                );

        LocalDate endDate =
                startDate.withDayOfMonth(
                        startDate.lengthOfMonth()
                );

        /*
         * Consultar citas.
         */
        List<Appointment> appointments =
                appointmentRepository.findMonthSchedule(
                        startDate,
                        endDate,
                        doctorId,
                        branchId,
                        List.of(
                                AppointmentStatusCode.CONFIRMED,
                                AppointmentStatusCode.COMPLETED,
                                AppointmentStatusCode.CANCELLED
                        )
                );

        /*
         * Construir resumen mensual.
         */
        return buildMonthSchedule(
                appointments,
                startDate,
                endDate
        );
    }



    // PRIVATE

    /**
     * Consulta el detalle de una cita.
     *
     * @param appointmentId ID de la cita.
     * @return detalle de la cita.
     */
    public AppointmentDetailResponse getAppointmentDetail(
            Long appointmentId
    ) {

        /*
         * Buscar cita.
         */
        Appointment appointment =
                appointmentRepository.findById(
                                appointmentId
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Appointment not found."
                                )
                        );

        /*
         * Construir response.
         */
        return appointmentMapper.toDetailResponse(
                appointment
        );
    }

    /**
     * Construye el resumen mensual
     * agrupando citas por fecha.
     *
     * @param appointments citas del mes.
     * @param startDate fecha inicial.
     * @param endDate fecha final.
     * @return resumen mensual.
     */
    private List<AppointmentMonthScheduleResponse> buildMonthSchedule(
            List<Appointment> appointments,
            LocalDate startDate,
            LocalDate endDate
    ) {

        /*
         * Agrupar citas por fecha.
         */
        Map<LocalDate, List<Appointment>> appointmentsByDate =
                appointments.stream()
                        .collect(
                                Collectors.groupingBy(
                                        Appointment::getAppointmentDate
                                )
                        );

        /*
         * Construir respuesta por día del mes.
         */
        List<AppointmentMonthScheduleResponse> response =
                new ArrayList<>();

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {

            List<Appointment> dayAppointments =
                    appointmentsByDate.getOrDefault(
                            currentDate,
                            List.of()
                    );

            /*
             * Construir día.
             */
            AppointmentMonthScheduleResponse item =
                    new AppointmentMonthScheduleResponse();

            item.setDate(
                    currentDate
            );

            item.setTotalAppointments(
                    dayAppointments.size()
            );

            item.setConfirmedCount(
                    countByStatus(
                            dayAppointments,
                            AppointmentStatusCode.CONFIRMED
                    )
            );

            item.setCompletedCount(
                    countByStatus(
                            dayAppointments,
                            AppointmentStatusCode.COMPLETED
                    )
            );

            item.setCancelledCount(
                    countByStatus(
                            dayAppointments,
                            AppointmentStatusCode.CANCELLED
                    )
            );

            item.setLoadLevel(
                    calculateLoadLevel(
                            dayAppointments.size()
                    )
            );

            response.add(
                    item
            );

            currentDate = currentDate.plusDays(
                    1
            );
        }

        return response;
    }

    /**
     * Cuenta citas por status.
     *
     * @param appointments citas consultadas.
     * @param statusCode código de status.
     * @return total por status.
     */
    private Integer countByStatus(
            List<Appointment> appointments,
            String statusCode
    ) {

        return Math.toIntExact(
                appointments.stream()
                        .filter(appointment ->
                                statusCode.equals(
                                        appointment.getStatus().getCode()
                                )
                        )
                        .count()
        );
    }



    /**
     * Calcula el nivel de carga
     * según el total de citas del día.
     *
     * @param totalAppointments total de citas.
     * @return nivel de carga.
     */
    private String calculateLoadLevel(
            Integer totalAppointments
    ) {

        if (totalAppointments == null
                || totalAppointments == 0) {
            return "VACIO";
        }

        if (totalAppointments <= 3) {
            return "BAJA";
        }

        if (totalAppointments <= 6) {
            return "MEDIA";
        }

        return "ALTA";
    }
}
