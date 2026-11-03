# Nghiên cứu 5G/NR

Tài liệu tổng hợp hiện tại: [docs/07-nr5g-investigation.md](../../docs/07-nr5g-investigation.md).

Đã tìm thấy policy SA của VinaPhone và nhánh Phone SA-only. Thử cho phép NSA bị modem trả ERROR. Chưa có NR registration thành công.

**Trạng thái cuối sau yêu cầu hoàn nguyên:** ro.vendor.radio.5g=2 (gốc), 5g_sa_mode_disabled absent/null, preferred_network_mode=33,33. Bản ghi để Vina SA bật trước đó chỉ là lịch sử. Hai lần sọc màu/đen màn chưa xác định nguyên nhân; không tự bật lại thử nghiệm cũ.

[Bộ thử region global riêng](../../experiments/radio_region_global_v1/README_VI.md) gồm module property-only và LK giữ nguyên. Chưa cài hoặc kiểm thử NR trên máy thật. Các bằng chứng lỗi/hoàn nguyên ở `crash_review`; log thô chứa dữ liệu riêng giữ ngoài Git.

LK/firmware không đổi. Các phép thử và rollback đều có backup/evidence/commit riêng. Không coi script thử là module fix đã được kiểm chứng.
