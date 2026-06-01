ALTER TABLE activity_logs
    ADD COLUMN patient_id BIGINT;

ALTER TABLE activity_logs
    ADD CONSTRAINT fk_activity_logs_patient
        FOREIGN KEY (patient_id)
            REFERENCES patients(id);

CREATE INDEX idx_activity_logs_patient_id
    ON activity_logs(patient_id);