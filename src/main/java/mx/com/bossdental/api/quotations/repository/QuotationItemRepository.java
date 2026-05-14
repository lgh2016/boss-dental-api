package mx.com.bossdental.api.quotations.repository;

import mx.com.bossdental.api.quotations.entity.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationItemRepository extends JpaRepository<QuotationItem, Long> {

    List<QuotationItem> findByQuotationId(Long quotationId);
}