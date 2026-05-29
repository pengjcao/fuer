ALTER TABLE pi_info
    ADD COLUMN id_card_copy_path VARCHAR(500) NULL COMMENT '身份证复印件文件路径'
    AFTER pi_photo_path;
