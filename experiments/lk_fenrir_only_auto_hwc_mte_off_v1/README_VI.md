# Fenrir gốc chỉ thêm HWC tự chọn vùng và MTE-off

Theo yêu cầu mới sau khi người dùng báo hai bản ghép không dùng được: tạo lại từ **đúng Fenrir gốc**, không lấy hai hybrid làm input và không thêm các patch v2.

File: [lk_FENRIR_ONLY_AUTO_HWC_MTE_OFF_TEST.img](lk_FENRIR_ONLY_AUTO_HWC_MTE_OFF_TEST.img), **4.083.872 byte**.

SHA256: `8d616abb51cd48373d7a892013c080b902a811983133bfc42d1a0accd73e30e8`.

Input giữ nguyên: [Fenrir gốc](../../fresh_analysis/fenrir_review/input/dash-fenrir-versioned.bin), SHA256 `dee32d186b36b07f7e49b2d67a708c84ef94eb7d4b0147548eea3bd450ccf113`.

**Chưa flash hoặc boot/root test file mới.** Các probe offline không được coi là bảo đảm hoạt động trên điện thoại. Không tiếp tục sửa cơ chế khóa hoặc chẩn đoán lại lock/OTA trong đợt này.

## Phạm vi thay đổi chính xác

| Main payload offset | Thay đổi |
|---|---|
| `0x13a78`, 136 byte | Giữ lần copy thông tin phần cứng sớm; định dạng HWC sau khi đọc countrycode |
| `0x14050`, 12 byte | Xóa dữ liệu countrycode đọc dở khi lỗi, giữ đường log/close |
| `0x140b0`, 4 byte | Nối kết quả countrycode vào phần xuất HWC muộn |
| `0x344a8`, 4 byte | `20020054` → `11000014`: luôn vào nhánh MTE suppress gốc ở `0x344ec` |
| Main cert2 prefix, 90 byte | Cập nhật hash cho main mới; giữ DER đã ký nguyên byte |

Tổng span code thay thế156 byte; không có mã mới chèn ngoài layout. [manifest.json](manifest.json) ghi từng byte old/new và toàn bộ hashes. Builder kiểm mọi byte khác đều bằng input. Main header, chứng chỉ ký gốc, BL2, AEE/cert của AEE, DTB/DTBO/cert và trailer768 zero giữ nguyên.

Giữ nguyên common wrapper locked4, setter green0, AVB allow-error, đường flash/erase, public seccfg/LKS, root handoff, build identity và lời gọi bảo trì phiên bản của **chính Fenrir**. Không thêm `flash.locked=1`, không fake thêm seccfg, không chèn guard bảo trì hoặc lớp v2. Không sửa preloader, misc, NVRAM, RPMB hoặc efuse.

## HWC và MTE

- Countrycode đúng `cn`, không phân biệt hoa thường → `CN`.
- Mã khác không rỗng, gồm Global/EEA/TW → `GLOBAL`.
- Thiếu/rỗng/lỗi đọc → HWC phần cứng gốc. Không tự nhận ROM nếu còn giữ countrycode cũ.
- Thông tin phần cứng vẫn được copy sớm đúng một lần; HWlevel/HWversion và các con trỏ caller được giữ. Một diagnostic log được bỏ/rút gọn để đủ chỗ; không ghi GLOBAL đè callback khóa ký.
- MTE-off đi đúng nhánh có sẵn của LK, cấu hình booker theo nhánh off và xuất **`arm64.nomte`** xuống kernel. Đây không phải tắt ngược những gì preloader đã thực hiện trước LK. Kernel vendor/HWCAP chưa được đo trên máy chạy ảnh mới.

HWC mapping không phải bằng chứng mọi ROM/preloader đều tương thích hoặc sửa được5G. Các đánh đổi fakelock của Fenrir gốc vẫn còn nguyên, không biến thành khóa thật/OTA/attestation hợp lệ.

## Kiểm thử

```powershell
python experiments/lk_fenrir_only_auto_hwc_mte_off_v1/build_fenrir_only.py
python experiments/lk_fenrir_only_auto_hwc_mte_off_v1/validate_fenrir_only.py
```

Không dùng Python `-O`. Build tái lập, từ chối input sai hash, kiểm tất cả byte ngoài allowlist và các vùng Fenrir cần giữ. HWC reuse code port đã kiểm nhưng input binary luôn là Fenrir gốc, không phải hybrid.

[validation.json](validation.json): 147 ca HWC; 48 ca MTE gốc/off với nhiều cờ và reserved-memory;9 ca core Fenrir;20 ca root handoff;6 parser BL2 với main/AEE cert cuối. **230 ca native, 10 chữ ký DER gốc đạt kiểm offline.** MTE test đọc instruction trong file cuối, không sửa code RAM. MMIO/storage/formatter/log/secure monitor là các biên mô hình hóa; không chạy kernel/TEE hoặc toàn bộ boot.

Checkpoint trước sửa: `before-fenrir-only-hwc-mte-20261105` tại `dafba4f`. Các bản ghép bị báo không dùng được chỉ giữ làm lịch sử; không lấy kết quả máy-test của chúng làm xác nhận bản mới. [runtime_confirmation.json](runtime_confirmation.json) giữ riêng báo cáo người dùng và trạng thái mới chưa boot.
