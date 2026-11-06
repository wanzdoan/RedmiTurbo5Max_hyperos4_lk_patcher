# 04 — Kết quả máy thật và preloader

## Thiết bị, ROM và artifact

- Thiết bị theo người dùng: Redmi Turbo 5 Max, Dimensity 9500s.
- ROM: **OS4.0.3.0.XPLCNXM**; package tên `dash_images_OS4.0.3.0.XPLCNXM_17.0`.
- Thư mục ROM: `D:/dash_images_OS4.0.3.0.XPLCNXM_20261021.0000.00_17.0_cn_658e7c4052_2/dash_images_OS4.0.3.0.XPLCNXM_17.0`.
- Hybrid: `lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img`, 4.083.424 byte, SHA256 `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`.

## Các xác nhận của người dùng

| Giai đoạn | Cấu hình/ngữ cảnh | Kết quả được báo |
|---|---|---|
| Sau bàn giao hybrid | Preloader ENG H3, ROM H4 đã nêu | Boot thành công |
| Bổ sung cấu hình | Vẫn ENG H3 | Fastboot unlock yes |
| Sau câu hỏi thay stock preloader H4 | Người dùng tự test rồi yêu cầu dừng kiểm tra | “tôi test và thấy được rồi” |

Bản ghi cuối được hiểu theo ngữ cảnh thay preloader stock H4 đang bàn luận. Không bổ sung log flash, hash readback partition hay kết quả fastboot riêng sau thay nếu người dùng chưa cung cấp. Kết quả boot/fastboot có nguồn là người dùng; agent không tự nạp firmware.

## Danh tính file PC

ROM thực tế đã được hash: `images/lk.img` SHA256 `82b4e1f2547b501f1e61987b9251337318a72a7c091e8965c28b664b5efac95b`; `images/preloader_dash.bin` SHA256 `104bb972383b39771ec5bcc4bc79207244306de1d40f370ed2ab3863fb7ceb77`. Chúng trùng hai input H4 đã reverse.

File ENG người dùng cung cấp tại `C:/Users/Administrator/Downloads/preloader_dash.bin` trùng từng byte `preloader_hyperos3/preloader_dash.bin`: SHA256 `bcde516c37a8845287cc57441780391e89c16c51eabfd4c5e3c625ecbdf4ad8b`, 1.064.924 byte.

## Kết luận về preloader

Trên cấu hình cụ thể này, người dùng báo cả lần boot với ENG H3 và lần tự test thay stock H4 đều được. Dừng điều tra thêm theo yêu cầu. Không ghi kết luận rằng mọi stock H4, mọi mức fuse/rollback hay mọi thiết bị đều tương thích.

Các kết quả tĩnh đã có trước khi dừng vẫn được giữ trong `fresh_analysis/stock_h4_preloader_review`: danh tính ROM, policy và mapping. Không hoàn thiện giả một phép chứng minh toàn boot chain từ các phân tích dở dang. Việc thay preloader không thay nguồn BL2: trong hybrid, BL2 vẫn là H3 signed.

## Chưa có báo cáo cụ thể

Số cold boot/reboot, recovery, OTA, relock, thời gian sử dụng dài và readback fuse/RPMB chưa được ghi nhận. Thành công của bản hybrid không đồng nghĩa raw unlock state trong RPMB đã được sửa vĩnh viễn.

Biên bản máy đọc được: [runtime_confirmation.json](../fresh_analysis/output/runtime_confirmation.json). ROM inventory: [rom_inventory.json](../fresh_analysis/stock_h4_preloader_review/rom_inventory.json).


## 2026-11-06 — Policy H4 v3 MTE_OFF đã vào Android

Người dùng báo “lần này đã lên rồi” và chọn rõ **Policy v3 MTE_OFF**, file [lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_MTE_OFF_TEST.img](../experiments/lk_h4_policy_bl2_control_v3/lk_H4_POLICY_V3_BOOTED_RELEASE_BL2_MTE_OFF_TEST.img), SHA256 `6282d02b422573b42ce789bec8fca4f37f6f26069489332d3016589088073d52`, 4.083.424 byte. [Biên bản runtime](../experiments/lk_h4_policy_bl2_control_v3/runtime_confirmation.json) ghi nguồn xác nhận là báo cáo người dùng; không có đọc lại thiết bị/log boot trong lần cập nhật này.

V2 MTE_OFF trước đó bị recovery, trong khi v3 giữ main/cert main nguyên byte v2, chỉ thay bộ ba BL2/chứng chỉ bằng đúng bộ H3 thương mại từ H4 unlocked f57 đã boot. Kết quả giúp khoanh vùng khác biệt tương thích bộ nạp/bàn giao, chưa chỉ ra cơ chế nội bộ gây lỗi. Đây vẫn là main H4 đã patch + BL2 H3 thương mại, không phải H4 ENG đầy đủ.

Ngữ cảnh trước đó là China OS4.0.3.0.XPLCNXM sạch, không root và preloader ENG `75e93936…`; người dùng chưa xác nhận lại cấu hình này trong báo cáo boot mới. Không kế thừa kết quả stock preloader H4 của f57 sang v3. Chưa có báo cáo mới về fastboot locked, banking/Integrity, OTA, Global, root, reboot nhiều lần hoặc MTE thực tế. Normal và diagnostic unlocked chưa được xác nhận boot. Agent không flash hay thao tác thiết bị.


### Báo cáo bổ sung: banking và Integrity với cùng LK

Người dùng xác nhận banking H4 hoạt động đầy đủ. Giữ nguyên LK v3 MTE_OFF `6282d02b…`, cài ROM H3 được đủ ba mức Integrity; cài ROM H4 China OS4.0.3.0.XPLCNXM không root thì không có mức nào khi kiểm bằng app Play Integrity API Checker. Đây là báo cáo cùng LK/hai ROM, không phải so hai LK. Chưa nhận nguyên văn verdict/lỗi, phiên bản Google services hoặc đọc lại máy. [Điều tra riêng](../fresh_analysis/h4_integrity_20261106/README_VI.md) không sửa firmware hoặc thao tác thiết bị.
