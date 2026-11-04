# 09 — Patch unlock riêng cho LK ENG mới

Ngày 2026-11-04. Người dùng cung cấp `C:\Users\Administrator\Downloads\lk.img` và mô tả là LK ENG HyperOS3. Đã sao lưu input nguyên byte, push checkpoint trước sửa và tạo [artifact riêng](../experiments/lk_h3_eng_unlock_v1/README_VI.md). Không flash lên thiết bị.

| Mục | Giá trị |
|---|---|
| Input SHA256 | `6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d` |
| Input size | 4.043.616 byte |
| Output | `lk_hyperos3_ENG_UNLOCKBL_TEST.img` |
| Output SHA256 | `0d237263a24f6bf41fa33b5dc43bb7d71e5b61f1977331c3f9f1812ad5000466` |
| Output size | 4.043.792 byte |
| Getter patch trong file | `0x898e4`,16 byte |
| Getter payload / VA | `0x896e4` / `0xffff0000907896e4` |
| Custom/main wrapper payload | `0x88c18` / `0x885fc` |
| Predicate xác thực boot payload | `0x6979c` |
| Fastboot lock query payload | `0xc770` |
| Bộ đọc cert2 của BL2 ENG payload | `0x6ea70` |

Getter gốc có đọc/kiểm magic, chữ ký và trạng thái RPMB. Patch bốn lệnh ghi state3 và return0, giống ngữ nghĩa đã xác định nhưng địa chỉ được tìm lại từ binary mới. Không coi ENG là đã unlocked. Bản sửa không ghi unlock bền vào RPMB.

BL2 ENG có payload SHA256`ab5a5fbcc6c5260deed7b21d835732d530a6a847bae8b907b6af924f77030631`, khác donor trước. Policy tại`0x5c660` vẫn trả yêu cầu xác thực khi helper đọc bảo mật được mô hình hóa cho0/1/2 hoặc lỗi5. Bộ đọc cert thật chọn được prefix hash bên ngoài DER. Vì vậy giữ nguyên toàn bộ BL2 ENG và thêm prefix180 byte cho main cert2, không ghép tầng boot khác.

Giữ nguyên DER có chữ ký; main signed digest cũ khác digest sau patch là thiết kế của cơ chế prefix, không phải ký lại bằng private key. Mọi record khác main/cert2 nguyên byte từ input: BL2, AEE, DTB/DTBO, các header, certificates và padding. Không thêm patch Global/5G trong yêu cầu unlock này.

[Validation](../experiments/lk_h3_eng_unlock_v1/validation.json) đạt342 ca CPU main,9 ca parser actual ENG,4 ca policy actual ENG và10 DER signature offline. Control có trạng thái locked và prefix hash khác signed hash. Stack, thanh ghi bảo toàn và vùng nhớ lân cận đầu ra được kiểm trong probe main. Các boundary được mô hình hóa ghi rõ trong JSON; không gọi đây là toàn bộ mô phỏng SoC hoặc xác nhận boot máy thật.

Builder và input nằm trong [thư mục riêng](../experiments/lk_h3_eng_unlock_v1/README_VI.md), tập trung pseudocode mới ở `analysis_main`. Checkpoint input: commit`51bd2ae`, tag`before-h3-eng-unlock-patch-20261104`. Mọi bản H3/H4 cũ giữ nguyên.
