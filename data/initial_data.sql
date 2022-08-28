INSERT INTO
    mobile_operator (operator_name, organization_header)
VALUES
    ('Vodafone', 'vodafone'),
    ('Orange', 'orange'),
    ('Etisalat', 'etisalat');

DELIMITER $$
CREATE PROCEDURE populateData(startNumber INT, endNumber INT, mobileOperator VARCHAR(20)) 
BEGIN 
	DECLARE counter INT DEFAULT startNumber;
    DECLARE operator_id INT;

    -- START TRANSACTION;

	SELECT id INTO operator_id FROM mobile_operator	WHERE mobile_operator.operator_name=mobileOperator;
    WHILE counter <= endNumber DO 
        INSERT INTO mobile_subscriber(mobile_number) VALUES(CONCAT('0', CONVERT(counter, char))) ; 

        INSERT INTO
            mobile_subscriber_operator (
                mobile_subscriber_id,
                original_operator_id,
                current_operator_id
            )
        VALUES
            (LAST_INSERT_ID(), operator_id, operator_id);

        SET counter = counter + 1;

    END WHILE;
    -- COMMIT;

END$$ 
DELIMITER ;

CALL populateData(1000000000, 1000000100, 'Vodafone');

CALL populateData(1100000000, 1100000100, 'Etisalat');

CALL populateData(1200000000, 1200000100, 'Orange');


-- DELETE FROM mobile_number_operator WHERE 1=1;

-- DELETE FROM mobile_number WHERE 1=1;
