# Đối chiếu họ LK và chuỗi chứng chỉ BL2 — 2026-11-05

Chỉ phân tích offline các file hiện có; không ADB, flash hay thay firmware. Script tái tạo: `python fresh_analysis/eng_family_compatibility_20261105/compare_families.py`. Kết quả chi tiết: [family_evidence.json](family_evidence.json).

## Điều đã xác nhận

- Bản H3 patched người dùng chỉ rõ là `lk_hyperos3_patched_unlockBL/dash.img`, SHA256 `e164fb22511d93584afab2183c275374d7ab892dd22e3a2132c4f3b1e245ac18`.
- H4 hybrid đã được báo boot với preloader H4 là `fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img`, SHA256 `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`.
- Hai file trên dùng **BL2 code/header giống hệt nhau**: payload SHA256 `e9aca46a63376b22daf9150319e27c8fc2da1e176781af8bc1392da4f178e76b`; cert1 record cũng giống hệt. Cert2 cùng TBS và mọi byte ngoài signature, nhưng signature RSA-PSS khác 255/256 byte, cả hai đều xác thực hợp lệ. Vì vậy không gọi hai bộ triple là nguyên byte giống hệt nhau. Bộ triple hybrid nguyên byte bằng H3 **original**.
- Cả hai không dùng BL2 H4 `d0cfaeaf...`. Vì vậy thay đổi chọn hash của **BL2 H4** không thể tự giải thích khác biệt boot giữa đúng hai tổ hợp người dùng báo: BL2 H4 không nằm trong chúng.
- H4 hybrid thay main/AEE/DTB/DTBO sang H4; H3 patched giữ các thành phần khác. Đây là nhiều biến số đồng thời, chưa cô lập được một trường handoff hoặc một hàm khởi tạo là nguyên nhân brick. H3 patched còn có AEE và DTBO khác bản H3 original tham chiếu, không chỉ main-getter 16 byte khác.
- Sáu file ENG mới giữ nguyên byte cả 12 record BL2/AEE/DTB/DTBO và certificate/padding từ ENG input `6f386241...`. BL2 ENG là payload `ab5a5fbcc6c5260deed7b21d835732d530a6a847bae8b907b6af924f77030631`, khác cả H3 release và H4.

## Không có bằng chứng về một khóa ký ENG khác tại BL2

Cert1 của BL2 ENG nguyên byte bằng cert1 BL2 release H3/H4: SHA256 `618277366ef443b01200d4ede8fcc12edc58ce08e40c23b118b10ffdeb67dba2`.

Root SPKI trong cert1 đều có hash `0035546f887cdf927378177dddb8356ce28e2c3af34f37b3a450a74b24059b05`; SPKI khóa image trong cert2 đều `775ccfa210b3452180241ebb1cfa7bb0e217be613f2a3a358ca4563b54f03c2e`. Khóa image được cert1 ràng buộc đúng; chữ ký PSS cert1/cert2 và hash header/payload BL2 đều đạt kiểm tra offline cho cả năm input.

Các custom field được kiểm tra trong cert2 ENG khác H3/H4 chỉ ở hash header và payload đúng theo ảnh. Các giá trị mã hóa còn lại giữ nguyên: `3.1=INTEGER0`, `2.2=INTEGER1`, `2.6=BITSTRING0000`, `3.2=INTEGER1`, `2.7=PrintableString"0"`, `2.8=BITSTRING0000`, `2.9=INTEGER0` (tiền tố OID chung `2.16.886.2454`). Không suy diễn tên/ý nghĩa các field chưa đủ chứng cứ thành một rollback counter thực trên thiết bị.

Đây là bằng chứng thuận lợi cho khả năng kiểm chain tại preloader: BL2 ENG không đưa vào một root SPKI hoàn toàn mới so với BL2 release đã dùng. **Không đồng nghĩa preloader thực đã chấp nhận, cũng không chứng minh boot thành công.** Chưa chạy loader/SoC, chưa đọc fuse/root tin cậy hoặc counter rollback trong phạm vi này.

## Ma trận kết luận cho sáu bản ENG

| Nhánh ENG all-region | Preloader stock H3 | Preloader stock H4 |
|---|---|---|
| Unlocked, MTE mặc định/off | Chưa kiểm chứng trên máy; không xác nhận flash được | Chưa kiểm chứng trên máy; không xác nhận flash được |
| Fakelock v2, MTE mặc định/off | Chưa kiểm chứng trên máy; không xác nhận flash được | Chưa kiểm chứng trên máy; không xác nhận flash được |
| Stock-policy, MTE mặc định/off | Chưa kiểm chứng trên máy; không xác nhận flash được | Chưa kiểm chứng trên máy; không xác nhận flash được |

Về các stage trước main, sáu bản có cùng chuỗi BL2 ENG nguyên byte. HWC/fakelock/MTE-off là thay đổi trong main và prefix, nên chúng không tự giải quyết việc BROM/preloader từ chối nạp BL2 trước khi main chạy. MTE-off cũng không sửa ngược preloader đã thực thi.

Đặc biệt, thư mục `preloader_hyperos3` chứa file `bcde...` nguyên byte bằng ENG cũ người dùng gửi; không dùng nhãn thư mục đó để tuyên bố đã test preloader **stock** H3. Các file stock H3 mới dưới `Preloader/` cần được đối chiếu riêng.

Chưa có log stage thất bại của tổ hợp H3 patched + stock H4, nên không đủ chứng cứ khẳng định nguyên nhân gốc hoặc một sửa cụ thể sẽ chữa brick. Metadata load/type/alignment ở các header được so sánh là giống nhau, nhưng không thay thế kiểm ABI khởi tạo phần cứng, reserved-memory và secure-world. Trạng thái tĩnh đã xác nhận và kết quả boot do người dùng báo phải giữ riêng.
