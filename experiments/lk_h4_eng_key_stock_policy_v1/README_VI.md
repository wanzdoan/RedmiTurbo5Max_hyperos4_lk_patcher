# Prototype H4: khóa ENG và stock-policy China

Prototype offline ngày2026-11-06, **không phải bản chuyển đầy đủ tính năng ENG H3**. Người dùng sau đó yêu cầu lấy `lk_h3_eng_locked_rom_policy_v1` CN/Global làm chuẩn; prototype China này được giữ riêng làm checkpoint, không phải kết quả của phạm vi mở rộng đó.

File `lk_H4_ENG_KEY_STOCK_POLICY_H3ENG_BL2_TEST.img`,4100080byte, SHA256`9c4575c4be448059c114c5f2f1e1884982e3be8b3159e2d1fc43722df4c33773`.

Main/AEE/DTB/DTBO nền H4; BL2 và hai certificate nguyên byte từ H3 ENG. Main chỉ thay callback AVB tại payload`0xc5978..0xc5b17` để tin China4096 hoặc ENG2048. Không tin Global. Getter/setter, AVB flags, root handoff, HWC và MTE nguyên H4. Không ép trạng thái locked/unlocked, không giả green và không chuyển build release thành bản ENG chính thức.

Input H4`82b4e1f2547b501f1e61987b9251337318a72a7c091e8965c28b664b5efac95b`; H3ENG`6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d`. [Manifest](manifest.json) và [validation](validation.json) ghi55ca khóa,24đối chiếu AVB predicate,3parser BL2 ENG,10chữ ký DER. Bảo toàn stack/thanh ghi, input sai bị từ chối, rebuild trùng byte. PAC/logging/boot-selection/getter trạng thái fixture được mô hình hóa; không mô phỏng đầy đủ SoC.

Chưa flash, boot, lock, root, OTA, Play Integrity hoặc kiểm tương thích preloader trên máy. `init_boot` sửa có thể bị stock-policy từ chối khi locked; recovery/chained-key vẫn theo mã H4 gốc. Hash prefix ngoài DER không phải chữ ký OEM mới.

Tái tạo: `python experiments/lk_h4_eng_key_stock_policy_v1/build_h4.py`, sau đó`python experiments/lk_h4_eng_key_stock_policy_v1/validate_h4.py`. Không dùng Python`-O`.
