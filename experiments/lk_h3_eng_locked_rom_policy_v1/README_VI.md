# LK ENG gốc + nhận khóa boot China/Global H3

Ngày 2026-11-04. Theo yêu cầu cuối: **bắt đầu từ LK ENG gốc chưa unlock**, giữ nguyên cơ chế khóa của nó, thêm khả năng nhận ROM China và Global. Người dùng làm rõ lỗi của bản ENG gốc xảy ra **khi khởi động sau flash**, không phải lệnh ghi phân vùng bị từ chối.

Artifact thử: [lk_hyperos3_ENG_STOCK_CN_GLOBAL_BOOT_FLASH_TEST.img](lk_hyperos3_ENG_STOCK_CN_GLOBAL_BOOT_FLASH_TEST.img), **4.043.792 byte**.

SHA256: `ad3d66e3c374ff3a83fc73ba30880fe1d18ee049b3c2ec54ff6e97e45815760b`.

**Cập nhật theo báo cáo người dùng:** ROM Global vào recovery của chính ROM và hiện “This system version can't be installed on this device”; cùng ROM dùng ENG unlock thì boot bình thường. Chưa đối chiếu hash LK đang chạy bằng readback. Bản v1 không được coi là đã hỗ trợ boot Global thành công. Xem [điều tra lỗi recovery](recovery_region_v2/README_VI.md) và [báo cáo người dùng](recovery_region_v2/runtime_failure_report.json). Giữ nguyên file `.img` và hash v1 để đối chiếu; các phép mô phỏng bên dưới không phải xác nhận boot máy thật.

## Nguồn và trạng thái khóa

Input nguyên byte: [lk_eng_original.img](../lk_h3_eng_unlock_v1/input/lk_eng_original.img), bản sao bảo toàn của `Downloads/lk.img`, 4.043.616 byte, SHA256 `6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d`.

Không dùng artifact ENG unlocked `0d237263...` hoặc fakelock `28f7f7b9...` làm đầu vào. Giữ nguyên getter đọc token/RPMB, setter, wrapper, phản hồi trạng thái fastboot, đầu ra trạng thái boot và đường truyền root of trust. **Không ép getter thành unlocked3 hoặc locked4, không giả màu xanh và không ghi trạng thái khóa lưu bền.** Máy vốn đang lưu trạng thái nào thì logic gốc tiếp tục đọc trạng thái đó; file này không tự khóa lại một thiết bị đã mở khóa.

Không ghép main, BL2, AEE hoặc cây thiết bị của H3 thương mại/H4. Dữ liệu tham chiếu duy nhất từ H3 thương mại là hai SHA256 của modulus khóa công khai China/Global; không có private key, token unlock hoặc dữ liệu nhận diện thiết bị.

## Vì sao bỏ chốt vùng đơn lẻ không đủ?

Lệnh `oem flash_check` trong ENG có nhánh từ chối non-CN trên phần cứng CN. Nhưng một ROM China vốn qua được nhánh này, nên nó không giải thích việc người dùng chỉ boot được ROM ENG. Bản sửa một nhánh đã cân nhắc ban đầu không được bàn giao riêng.

Đã định vị và chạy bộ kiểm tra khóa boot gốc tại payload `0xc5888`, địa chỉ `0xffff0000907c5888`. Nó đọc số bit của khóa AVB, chỉ đi vào so modulus khi `(bits & 0xfffffff8) == 2048`, rồi so 256 byte với khóa có sẵn ở `0x130728`. Khóa 4096 bit rơi vào đường không tin cậy. Return0 của callback chỉ có nghĩa thao tác hoàn tất; byte `trusted_out=0` vẫn là từ chối khóa.

LK H3 thương mại có các modulus 4096 bit riêng cho China và Global. Khi đang mở khóa, đường AVB có thể tiếp tục dù kết quả xác thực không đạt; khi khóa thì việc không tin khóa ngăn khởi động. Đây là bằng chứng phù hợp với hiện tượng người dùng báo, **chưa phải kết luận duy nhất về toàn bộ lỗi boot trên máy thật**.

## Ba vùng sửa trong main ENG

Tất cả offset dưới đây là tính từ đầu payload main; whole-file offset cộng `0x200`.

| Offset | Số byte | Thay đổi |
|---|---:|---|
| `0x68e7c` | 4 | `cbz w8,0x68ec0` → `b 0x68e80`, bỏ chốt CN/non-CN trong `oem flash_check` |
| `0x68ec0` | 64 | Lưu hai digest SHA256 trong block từ chối vùng nay không còn được đi vào |
| `0xc5888` | 216 | Thay callback kiểm tra khóa trong đúng ranh giới hàm gốc, nhận ENG2048 hoặc China/Global4096 đã biết |

Tổng span284 byte, **259 byte thực sự khác input trong main**. Không thêm vùng nhớ, không tăng kích thước payload main hoặc chèn mã vào vùng dữ liệu zero có thể bị khởi tạo lại. Mô phỏng và quét nhánh trực tiếp kiểm tra block chứa digest không còn bị đi vào như mã lệnh.

Callback mới:

1. Kiểm con trỏ và kích thước cấu trúc khóa AVB; mặc định đặt `trusted_out=0`.
2. Với RSA2048, yêu cầu cấu trúc520 byte và so modulus256 byte với chính khóa ENG có sẵn. Giữ đường boot ENG.
3. Với RSA4096, yêu cầu cấu trúc1032 byte, tính SHA256 của modulus512 byte bằng **hàm SHA256 phần mềm thật trong ENG** tại `0xa4b20`.
4. Chỉ đặt `trusted_out=1` nếu digest trùng China hoặc Global bên dưới. Khóa lạ, sai một bit, số bit không hỗ trợ, kích thước sai hoặc lỗi hash không được tin.
5. Việc kiểm chữ ký của ảnh boot vẫn chạy bằng mã AVB gốc. Tin thêm hai khóa không phải bỏ qua mọi lỗi chữ ký, digest, rollback hoặc định dạng ảnh.

Nguồn khóa: `lk_hyperos3_original/lk.img`, SHA256 `e23e07cab7754cc99c9484765b9ec758b2c8d5110dfb3e0eb6009415719bf4cb`.

| Khóa | Payload offset trong H3 thương mại | SHA256 của modulus512 byte |
|---|---|---|
| China | `0x138638` | `8a0ecaf46f057182eebfd3ce7f7ff04c7e97d9898e54a0df20ed3d9a0f9bef1e` |
| Global | `0x138838` | `798f1f2bdeed4ab83d5df6a879170d32ef97cf332894d5f00c668634f38c49cb` |

Chốt vùng được bỏ cho mọi chuỗi vùng, không chỉ GL. Tuy nhiên khóa boot lạ vẫn bị từ chối. Điều kiện từ chối flash khi RPMB chưa provision và máy locked vẫn giữ nguyên.

## Đóng gói và tầng nạp

Main cert2 nhận prefix hash180 byte theo cơ chế đã kiểm tra trên chính bộ đọc BL2 ENG. DER gốc vẫn nguyên byte; **không ký lại bằng private key**. Cert2 tăng982→1162 byte; căn record16 byte làm file tăng176 byte.

Toàn bộ13 record ngoài main và main cert2 giữ nguyên byte từ input ENG. Trong main cert2, DER giữ nguyên; thêm prefix, cập nhật kích thước header và căn lại padding zero từ10 xuống6 byte. Vì vậy prefix thêm180 byte nhưng file chỉ tăng176 byte. BL2, AEE, các cây thiết bị và chứng chỉ đi kèm không đổi.

## Kiểm chứng

[validation.json](validation.json) đạt:

- **78 ca khóa:** chạy callback gốc/mới, khóa ENG/China/Global, khóa lạ, sửa một bit ở các vị trí, kích thước sai, con trỏ null, lỗi hash và digest CN bị sửa. SHA256, memcmp và nhánh trả trusted chạy thật; chỉ bypass PAC/logging, và mô hình hóa lỗi SHA256 trong đúng một ca âm tính.
- **224 ca `flash_check`:** chạy toàn hàm, strcmp/strncmp thật; tổ hợp CN/GL/ENG/vùng lạ/factory/rỗng, efuse vùng, secure boot, lỗi efuse, cấu hình bypass và lỗi RPMB. Ranh giới đọc phần cứng/cấu hình, cache fastboot, delay và truyền phản hồi được mô hình hóa.
- **24 ca predicate khóa:** chứng minh các trạng thái1/2/3/4 vẫn đi qua AVB predicate, lock boolean và phản hồi fastboot gốc như trước. Getter được mô hình hóa trong các ca có nhãn này; không phải đọc RPMB máy thật.
- Tổng **326 ca CPU main**, kiểm bảo toàn stack/thanh ghi và byte cạnh kết quả.
- **10 chữ ký DER gốc** kiểm RSA-PSS offline hợp lệ; **3 ca bộ đọc BL2 ENG thật** chọn đúng hash prefix mới. Hash payload gồm padding căn16 byte.
- Build và assembly lặp lại trùng byte, input khác hash bị từ chối. Kiểm mọi byte ngoài span cho phép và các khoảng mã khóa/AVB/root of trust được bảo toàn.

[target_h4_cn_vbmeta.json](target_h4_cn_vbmeta.json) bổ sung một đối chiếu thực tế trên máy tính: `vbmeta.img` của ROM `OS4.0.3.0.XPLCNXM` người dùng đã cung cấp có chữ ký RSA4096 hợp lệ, modulus trùng khóa China ở trên. Callback ENG gốc trả trusted0, callback mới trả trusted1. **Đây là kiểm tra khóa/chữ ký của riêng vbmeta; không xác nhận LK ENG H3 boot được cả bộ firmware H4.** Không kiểm toàn bộ partition descriptor, chuỗi ảnh hoặc rollback máy thật.

## Tái tạo

```powershell
python experiments/lk_h3_eng_locked_rom_policy_v1/assemble_callback.py
python experiments/lk_h3_eng_locked_rom_policy_v1/build_stock_flash_region.py
python experiments/lk_h3_eng_locked_rom_policy_v1/validate_stock_flash_region.py
```

Assembly dùng clang AArch64 đã có sẵn; pyelftools kiểm không còn relocation. File [trust_cn_global.bin](trust_cn_global.bin) chỉ là216 byte mã callback, **không phải ảnh để flash**. Chỉ artifact `.img` nêu đầu tài liệu là container LK đầy đủ.

Nguồn phục hồi gốc và assembly theo hàm nằm trong `verified_analysis`; đây là pseudocode Ghidra, không phải source chính thức. [Manifest](manifest.json) ghi input/output hash và từng byte sửa. Bản ENG gốc và các bản unlock/fakelock cũ không đổi.

`analysis`, `policy_analysis` và `PolicyFocus.java` giữ lại lượt thăm dò trước khi người dùng chốt phạm vi. Một số seed ban đầu chỉ là giả thuyết vị trí hàm và không hợp lệ trên ENG; **không dùng các export này làm bằng chứng patch hoặc bản đồ offset**. Chỉ `verified_analysis`, instruction input trong manifest và các ca CPU kiểm lại trực tiếp là căn cứ của bản bàn giao.

Checkpoint trước sửa đã push vào repo private: tag `before-eng-locked-rom-policy-20261104`, commit `560a0b7`.

## Giới hạn còn lại

Đợt điều tra tiếp đã có `vbmeta.img` và `vendor_boot.img` Global `OS3.0.304.0.WPLMIXM`: khóa ký được v1 chấp nhận và hash hai ảnh khớp nhau, nhưng người dùng vẫn báo vào recovery với lỗi version mismatch. Chưa có đủ thành phần Android normal boot để truy điều kiện phát lỗi; xem [trạng thái nghiên cứu](recovery_region_v2/README_VI.md). Một ROM Global đổi khóa, khác model, khác bố cục phân vùng hoặc vi phạm rollback vẫn có thể không boot. Không sửa preloader, vùng bảo mật, modem, Android, trạng thái khóa lưu bền hoặc bất kỳ thiết lập điện thoại nào. Không gọi artifact này là fix5G, relock chính thức hay bản đã chứng minh tương thích mọi ROM.
