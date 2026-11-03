# Changelog

## 2026-11-03 — Hoàn nguyên radio và bộ thử region riêng

- Người dùng báo hai lần sọc màu/đen màn, có mở MBBank; evidence còn lại không đủ xác định nguyên nhân. Lưu log thô riêng tư ngoài Git và metadata chọn lọc trong crash_review.
- Hoàn nguyên thử nghiệm mạng theo yêu cầu: property 5g=2, key SA absent/null, preferred mode 33,33. Checkpoint đã push: `0d74255`, tag `radio-restored-before-region-research-20261103`.
- Tạo `experiments/radio_region_global_v1`: LK đã boot giữ nguyên byte, kèm module chỉ đổi `ro.vendor.radio.build_region=global`. Chưa cài, chưa xác nhận sửa được 5G; không tuyên bố đây là LK patch 5G.
- Kiểm tra ZIP/source, LF, hash LK, syntax shell và các guard installer bằng mock trên máy tính. Không flash hoặc thay cấu hình máy trong bước này.

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

## 2026-11-03 — Nghiên cứu 5G sau LK

- Lưu checkpoint before-5g-research-20261103, khảo sát LK/DTB/modem/MCF và đọc shipped telephony code qua ADB theo yêu cầu.
- Xác định VinaPhone kế thừa SA-off, Viettel có SA-on override; máy có ro.vendor.radio.5g=2 chọn SA-only.
- Người dùng cho phép thử mạng. Đã thử SA override, thử runtime SA+NSA; modem từ chối NSA và các thử trước đã rollback.
- Theo yêu cầu mới, bật lại SA cho VinaPhone để người dùng mang ra ngoài thử; trạng thái cuối lưu trong vina_outdoor_setup.json. Chưa xác nhận đăng ký NR.
- LK và firmware giữ nguyên SHA256. Không flash hoặc chỉnh NVRAM/cài module persistent.
