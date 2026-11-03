# Changelog

## 2026-11-03 — Versioning và cập nhật kết quả

- Dừng kiểm tra preloader theo yêu cầu người dùng sau khi họ báo tự test được.
- Ghi ROM OS4.0.3.0.XPLCNXM, boot với ENG H3, fastboot unlocked yes và báo cáo test stock H4 theo ngữ cảnh.
- Tạo GitHub repo private, commit checkpoint `134da6d`, push tag `boot-working-before-docs-20261103` trước khi sửa docs.
- Thêm root README, docs đầy đủ, runtime sidecar và workflow phiên bản/rollback cho agent tương lai.
- Giữ nguyên firmware/hash; cập nhật bundle và tài liệu bằng commit riêng, tag `docs-confirmed-boot-20261103`.

## 2026-11-03 — Reverse và bàn giao ban đầu

- Reverse ba LK và hai preloader từ đầu; phát hiện patch getter state3/return0 dài 16 byte.
- Xác định donor H3 khác build, không port thay đổi DTBO/sạc không liên quan.
- Phục hồi ASN.1 hash prefix, hash domains, DER/PSS và donor signature relationship.
- Phát hiện BL2 H4 chọn hash trong TBS, chặn cách prefix H3 ở đường đã xét.
- Loại đối chứng chỉ patch main H4; tạo hybrid H4 BL33/AEE/DTBs + intact signed H3 BL2 triple.
- Chạy CPU/parser/crypto/rebuild checks, xuất pseudocode và bàn giao artifact.
- Người dùng báo boot thành công sau bàn giao.
