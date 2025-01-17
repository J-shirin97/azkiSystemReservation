
-- جدول زمان‌های خالی
CREATE TABLE available_slots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- شناسه یکتا برای هر زمان خالی
    start_time DATETIME NOT NULL,         -- زمان شروع
    end_time DATETIME NOT NULL,           -- زمان پایان
    is_reserved BOOLEAN DEFAULT FALSE    -- وضعیت رزرو (پیش‌فرض: خالی)
);

INSERT INTO available_slots (start_time, end_time, is_reserved) VALUES ('2024-12-29 09:00:00', '2024-12-29 10:00:00', FALSE);
INSERT INTO available_slots (start_time, end_time, is_reserved) VALUES ('2024-12-29 10:00:00', '2024-12-29 11:00:00', FALSE);
INSERT INTO available_slots (start_time, end_time, is_reserved) VALUES ('2024-12-29 11:00:00', '2024-12-29 12:00:00', FALSE);
INSERT INTO available_slots (start_time, end_time, is_reserved) VALUES ('2024-12-29 12:00:00', '2024-12-29 13:00:00', FALSE);
INSERT INTO available_slots (start_time, end_time, is_reserved) VALUES ('2024-12-29 13:00:00', '2024-12-29 14:00:00', FALSE);
INSERT INTO available_slots (start_time, end_time, is_reserved) VALUES ('2024-12-29 14:00:00', '2024-12-29 15:00:00', FALSE);
INSERT INTO available_slots (start_time, end_time, is_reserved) VALUES ('2024-12-29 15:00:00', '2024-12-29 16:00:00', FALSE);
INSERT INTO available_slots (start_time, end_time, is_reserved) VALUES ('2024-12-29 16:00:00', '2024-12-29 17:00:00', FALSE);
INSERT INTO available_slots (start_time, end_time, is_reserved) VALUES ('2024-12-30 09:00:00', '2024-12-30 10:00:00', FALSE);
INSERT INTO available_slots (start_time, end_time, is_reserved) VALUES ('2024-12-30 10:00:00', '2024-12-30 11:00:00', FALSE);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- شناسه یکتا برای هر کاربر
    username VARCHAR(255) NOT NULL UNIQUE, -- نام کاربری
    email VARCHAR(255) NOT NULL UNIQUE,   -- ایمیل کاربر
    password VARCHAR(255) NOT NULL,      -- رمز عبور کاربر (هش شده)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP -- زمان ثبت نام کاربر
);

INSERT INTO users (username, email, password) VALUES ('user1', 'johndoe@example.com', 'hashed_password_123');
INSERT INTO users (username, email, password) VALUES ('user2', 'janedoe@example.com', 'hashed_password_456');
INSERT INTO users (username, email, password) VALUES ('user3', 'user123@example.com', 'hashed_password_789');

CREATE TABLE RESERVATIONS ( ID BIGINT AUTO_INCREMENT PRIMARY KEY, SLOT_ID BIGINT NOT NULL, USER_ID BIGINT NOT NULL, RESERVED_AT TIMESTAMP NOT NULL, FOREIGN KEY (SLOT_ID) REFERENCES AVAILABLE_SLOTS(ID) ON DELETE CASCADE, FOREIGN KEY (USER_ID) REFERENCES USERS(ID) ON DELETE CASCADE );


                            
                              
                              
                             
