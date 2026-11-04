# MTE: preloader đang dùng và điểm tắt phía LK

Ngày 2026-11-05. Phân tích offline từ các bản đã có; không gọi ADB, không flash, không đổi `misc` hoặc cấu hình máy. Phần này tách khỏi nghiên cứu lock/OTA đã dừng.

**Đã tìm được điểm điều khiển thật trong LK để buộc nhánh tắt MTE phía kernel.** Đây không phải tắt ngược lại toàn bộ việc preloader đã thực hiện trước khi chạy LK, và chưa có xác nhận trên máy thật.

## Đúng input, đúng nguyên nhân bật mặc định

| Input | SHA256 |
|---|---|
| ENG LK gốc `experiments/lk_h3_eng_unlock_v1/input/lk_eng_original.img` | `6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d` |
| File preloader người dùng cung cấp `Downloads/preloader_dash.bin` | `bcde516c37a8845287cc57441780391e89c16c51eabfd4c5e3c625ecbdf4ad8b` |
| Bản boot-LUN đã đọc trước đó, giữ ngoài Git | `e987fa22c5ee5d03cf992581b268466e0f557f9e47e6d9580b7283a5e4bdd800` |

File preloader cũ người dùng cung cấp **khác** bản boot-LUN đọc từ máy. Không lấy nhãn ENG/H3 làm bằng chứng chúng cùng mã. Bản boot-LUN có chuỗi `ARM_MTE_DEBUG mode (userdebug)` ở raw offset `0xcab4b`; hàm raw `0x7e6a0` thực sự in chuỗi đó rồi chạy:

```text
raw 0x7e6d4: ldr w8, [x20,#0x74c]
raw 0x7e6dc: orr w21,w8,#7
raw 0x7e6e0: str w21,[x20,#0x74c]
```

Nghĩa là OR ba bit thấp vào `mte_info`, không chỉ có một chuỗi debug nằm trong binary. Hàm tương ứng của file preloader cũ tại raw `0x7b464` không có bước OR mặc định `#7` đó.

Sau đó hàm đọc cấu trúc 64 byte ở `misc` offset `0x8040`, kiểm version `1` và magic `0x5afefe5a`. Flags user-space `0x1/0x2` OR `3`; flags kernel `0x4/0x8` OR `0x405`; flag off `0x10` xóa trạng thái về `0`. Tiếp sau, dconfig `MTE_INFO` nếu tồn tại có thể ghi đè giá trị. Vì vậy mặc định debug bật **có các nhánh ghi đè**, không mô tả là lúc nào cũng bật bất chấp mọi cấu hình. Nếu đọc misc lỗi, hàm báo lỗi và thoát sớm, giá trị đã OR `7` vẫn còn.

Những thao tác ghi misc ở đây là hành vi mã gốc được mô hình hóa trong emulator; nghiên cứu này chỉ bắt lời gọi tại biên, không ghi phân vùng thật. `MTEE` ở các vị trí khác chỉ môi trường thực thi tin cậy MediaTek, không đồng nghĩa với tính năng Memory Tagging Extension đang xét.

## Luồng LK và nhánh tắt sẵn có

Các offset dưới đây tính từ đầu **main payload**, bằng whole-file offset trừ `0x200` cho input ENG này; không áp dụng sang LK H4/release khác.

- Hook `0x34ca8`, được registry boottag ghi tên `mte_info_boottag_hook`, lấy word ở `[tag+8]`, lưu vào global `main+0x16da28` và log giá trị. Registry entry chứa type `0x88610022`.
- Hàm `0x34cd4`, có entry init tên `kernel_mte_init`, tìm reserved-memory `security_booker_tag`, rồi kiểm `(mte_info & 6)` tại `0x34d18..0x34d20`.
- Khi không có bit 1/2, hàm đi tới nhánh `0x34d64`: log suppress, đặt low nibble của thanh ghi booker `0xffff00000c2cf5f4` về `0xe`, và dùng hàm append bootargs gốc xuất **`arm64.nomte`**.
- Khi có bit 1/2 và reserved memory tồn tại, low nibble là `0xf`. Nếu bit 2 bằng 0, xuất `kasan=off`; nếu bit 2 bằng 1, xuất cấu hình stacktrace KASAN, cùng sample theo bits 8–11 nếu khác 0.
- Khi không tìm được reserved memory, nhánh gốc cũng xuất `arm64.nomte`, kể cả khi bit enable đang bật.

Thiết kế patch tối thiểu đã kiểm thử trong RAM emulator:

```text
main offset 0x34d20; whole-file offset 0x34f20
old 20020054   b.eq 0x34d64
new 11000014   b    0x34d64
```

Nó buộc đi đúng đường suppress mà LK đã có, giữ nguyên `mte_info`, không chèn lệnh ghi thanh ghi hệ thống CPU, không sửa preloader, không ghi misc/dconfig. Hàm vẫn thực hiện lookup/log và khôi phục stack/thanh ghi theo luồng gốc. Một bản firmware áp dụng thiết kế này còn phải cập nhật prefix hash và qua các kiểm thử của đúng biến thể; file này không phải hướng dẫn dùng offset cho input khác.

## Kiểm thử và giới hạn

Chạy `python fresh_analysis/mte_research/probe_mte.py`. [validation.json](validation.json) ghi:

- **48 case LK native**: mọi giá trị bits thấp `0..7`, thêm `0x405`, `0x407`, `0xf07`, `0xffffffff`; reserved memory có/không; nhánh gốc đối chiếu nhánh ép tắt trong RAM. Kiểm tra bootargs, low nibble booker, global không bị thay đổi, thanh ghi bảo toàn.
- **168 case preloader native** từ đúng raw boot-LUN đã lưu: trạng thái ban đầu, valid/invalid misc header, read error, flags user/kernel/off và dconfig override.
- Các lệnh ARM64 của chính hàm chạy bằng Unicorn. Biên lookup reserved-memory, storage, dconfig, append/log/snprintf và PAC được mô hình hóa; MMIO là RAM emulator. Không mô phỏng DRAM tag storage, secure monitor hay kernel.

Có thể replay trực tiếp **file output cuối** bằng `lk_case(image_bytes,bits,reserved,image_forced_off=True)`: tùy chọn này kiểm tra lệnh ép tắt đã có trong input, không sửa RAM code. Khác với `forced=True`, chỉ dùng cho probe thiết kế trên image gốc.

Trong [Linux v6.12 `idreg-override.c`](https://github.com/torvalds/linux/blob/v6.12/arch/arm64/kernel/pi/idreg-override.c#L207), `arm64.nomte` được dịch thành `id_aa64pfr1.mte=0` ở bước xử lý CPU feature đầu boot. [Tài liệu tham số kernel](https://www.kernel.org/doc/html/v6.9/admin-guide/kernel-parameters.html) định nghĩa đây là tham số tắt hỗ trợ MTE. [ABI bootloader Android](https://source.android.com/docs/security/test/memory-safety/bootloader-support) cũng dùng đường xuất `arm64.nomte` khi MTE bị tắt. Đây là cơ sở để kết luận đường LK tìm được phù hợp với giao diện kernel chuẩn; **chưa chứng minh kernel vendor trong điện thoại đã nhận và thực thi bootarg sau patch**.

Phạm vi kỳ vọng là kernel không bật hỗ trợ MTE cho Android/userspace khi nhận tham số đó. Không tuyên bố patch xóa các vùng nhớ tag preloader đã giữ, hoàn nguyên trạng thái secure-world hay tắt MTE trong mọi giai đoạn trước LK. Chưa thực thi cả chuỗi boot để loại trừ consumer/override phía sau; chưa đo `/proc/cmdline`, kernel boot log hoặc `HWCAP2_MTE` trên máy chạy bản mới. Chưa có bằng chứng liên hệ MTE với hai lần sọc/đen màn trước đó. Không coi mô phỏng này là xác nhận boot hay sửa được crash.

Báo cáo và script không chứa CPUID/token/provisioning của máy. [policy_disassembly.txt](policy_disassembly.txt) chỉ xuất hai vùng mã policy liên quan.
