package mx.com.bossdental.api.quotations.repository;

import mx.com.bossdental.api.quotations.entity.Quotation;
import mx.com.bossdental.api.quotations.enums.QuotationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

    List<Quotation> findByPatientId(Long patientId);

    List<Quotation> findByStatus(QuotationStatus status);
}