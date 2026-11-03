# Nghiên cứu 5G/NR

Tài liệu tổng hợp hiện tại: [docs/07-nr5g-investigation.md](../../docs/07-nr5g-investigation.md).

Đã tìm thấy policy SA của VinaPhone và nhánh Phone SA-only. Thử cho phép NSA bị modem trả ERROR. Chưa có NR registration thành công.

**Trạng thái được người dùng yêu cầu giữ để thử ngoài đường:** ro.vendor.radio.5g=2 (gốc), 5g_sa_mode_disabled=0, VinaPhone modem query mode3, SA status true. Chờ kết quả field test. Không tự rollback trước khi người dùng thử.

LK/firmware không đổi. Các phép thử và rollback đều có backup/evidence/commit riêng. Không coi script thử là module fix đã được kiểm chứng.
