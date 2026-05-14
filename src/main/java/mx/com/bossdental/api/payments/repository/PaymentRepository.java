package mx.com.bossdental.api.payments.repository;

import mx.com.bossdental.api.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByPatientId(Long patientId);

    List<Payment> findTop5ByPatientIdOrderByPaymentDateDesc(Long patientId);

    List<Payment> findByQuotationId(Long quotationId);
}