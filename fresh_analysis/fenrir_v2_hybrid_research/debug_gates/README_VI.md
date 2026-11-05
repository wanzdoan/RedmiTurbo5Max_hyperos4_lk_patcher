# Gate fastboot trên nền debug: không cần chép patch AEE của Fenrir

Ngày 2026-11-05. Phân tích offline để phục vụ hai biến thể Fenrir + v2. Input debug gốc `experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img`, SHA256 `6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d`. Không flash, không ADB, không sửa firmware hoặc mã RAM emulator.

**Nền debug đã không có những gate lock cụ thể mà Fenrir phải bỏ trên H3 release. Vì vậy `DEBUG_GATE_PATCHES = []`; AEE debug không cần bị sửa chỉ để có các hành vi gate Fenrir này.** Không dùng offset H3 release `0x13be8/0x13bfc/0x16498/0x164a0` cho AEE debug, và không thêm prefix AEE nếu AEE không đổi byte.

## Mã đã đối chiếu

| Hành vi | Main debug | AEE debug |
|---|---|---|
| Loop dispatcher được scan | `0x9e6c..0xa3f0` | `0x13a24..0x13fb0` |
| Đoạn callback native | `0xa184..0xa1a4` | `0x13d3c..0x13d5c` |
| Entry handler flash | `0xcc90` | `0x15fd0` |
| Entry handler erase | `0xced0` | `0x16210` |
| Điểm dừng trước routine riêng theo partition | `0xdcd0` | `0x17010` |

Trong loop dispatcher đã scan, descriptor command ở `x25` được đọc tên/độ dài/callback nhưng không đọc hai thuộc tính `+0x14/+0x18` tại các gate tương ứng Fenrir. Không có direct call đến các common-lock helper đã liệt kê trong script. Đoạn callback đi thẳng từ descriptor đến `blr`.

Trong H3 release, prologue flash/erase gọi query locked và kiểm state trước khi xử lý tên. Debug main và AEE đi thẳng vào copy/xử lý tên; literal `Flashing is not allowed in Lock state` cũng không có trong hai payload debug. Đây là bằng chứng về các gate cụ thể, không phải chứng minh tất cả kiểm tra quyền hoặc lỗi query về sau đều biến mất.

## Replay native

```powershell
python fresh_analysis/fenrir_v2_hybrid_research/debug_gates/probe_debug_gates.py
```

[validation.json](validation.json): **128 ca ARM64 PASS**.

- 64 ca callback slice: hai surface × bốn state fixture `0/1/3/4` × hai secure fixture × hai giá trị mỗi thuộc tính descriptor. Callback luôn được gọi và đúng đối số; không query common lock tại slice này.
- 64 ca flash/erase entry: hai surface × hai command × bốn state fixture × bốn target `boot/init_boot/lk/boot_para`. Code thực chạy prologue, copy chuỗi, tìm chuỗi và strlen trước khi đến routine partition; không query common lock trước điểm dừng.
- Các state fixture được ghi rõ là giá trị để trả **nếu** helper bị gọi. Helper không được gọi ở các đường này, nên đây không phải probe kiểm getter fake-locked hoặc chứng minh đã đọc RPMB.

`validate(final_debug_image_bytes)` là hàm dùng lại cho ảnh cuối: chỉ đọc bytes, trả report, không sửa mã RAM và không ghi firmware. Script `main()` ghi JSON/disassembly trong chính thư mục nghiên cứu này. [disassembly.txt](disassembly.txt) lưu các đoạn entry/callback nguyên byte.

PAC được bỏ qua; callback là sink giả và routine partition là điểm dừng trước xử lý thật. Không chạy ghi partition, không kiểm toàn bộ command parser, kernel, secure monitor, preloader hay boot thành công. Các kiểm tra partition/protected-image/RPMB-query ở routine khác vẫn cần phân biệt riêng. Patch common lock wrapper, AVB flag và verified-boot cache thuộc scope builder của parent; kết quả ở đây chỉ xác nhận không cần chèn thêm các NOP/branch gate của bản release vào nền debug.
