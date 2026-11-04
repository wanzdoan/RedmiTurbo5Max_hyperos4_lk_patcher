# ENG unlocked và fakelock v2 thêm HWC tự chọn vùng

Ngày 2026-11-05. Hai file mới được tạo riêng từ đúng hai input cũ, giữ nguyên mọi artifact trước đó. **Chưa flash hoặc boot test hai bản mới. Không chứa patch MTE.**

| Biến thể | File | SHA256 |
|---|---|---|
| ENG unlocked | [unlocked/LK](unlocked/lk_hyperos3_ENG_UNLOCKBL_AUTO_HWC_TEST.img) | `64e91b35a058bfff943897157cfb993515ce278a1ff67d3c7fc0d1fb370764e8` |
| ENG fakelock v2 | [fakelock_v2/LK](fakelock_v2/lk_hyperos3_ENG_FAKELOCK_V2_AUTO_HWC_TEST.img) | `5345d07edbb65d6519b02d10117ac2e32405faf3dd13c6b54546eb66fa259032` |

Mỗi file 4.043.792 byte. Manifest và validation nằm cùng thư mục với ảnh tương ứng.

## Hành vi vùng

Dùng cùng quy tắc với [bản stock-policy auto HWC tham chiếu](../lk_h3_eng_locked_rom_policy_v1/hwc_auto_region_v1/README_VI.md): đọc kết quả `countrycode` của slot hiện hành bằng đường đọc có sẵn trong LK.

| Countrycode | HWC truyền sang Android |
|---|---|
| Đúng chuỗi `cn`, không phân biệt hoa thường | `CN` |
| Chuỗi khác không rỗng, gồm Global, EEA, Taiwan | `GLOBAL` |
| Thiếu phân vùng, chuỗi rỗng, lỗi hoặc đọc thiếu | HWC phần cứng gốc |

Đây là hỗ trợ chọn HWC cho mọi giá trị vùng theo quy tắc trên. Không tạo các HWC giả `EEA`/`TW`, không thêm khóa ký ROM vào danh sách tin cậy và không chứng minh mọi ROM đều boot. Nếu giữ countrycode cũ khi đổi ROM, LK vẫn chọn theo dữ liệu cũ. Chưa có kiểm thử ROM EEA/TW thật.

Bản unlocked giữ getter nội bộ force-unlocked. Bản fakelock v2 giữ getter đó và toàn bộ lớp báo locked/green, Root of Trust handoff, phản hồi seccfg và xử lý cảnh báo của v2. Fakelock không phải khóa thật và không chứng minh OTA, Play Integrity hoặc chứng thực phần cứng thành công.

## Khác biệt triển khai so với bản tham chiếu

Mã chọn vùng thay block 128 byte ở payload `0x13364`; điểm nối sau countrycode là `0x139c0`. Đường lỗi đọc ở `0x13960` xóa buffer từng phần rồi giữ log/close gốc. Vẫn chỉ đọc phân vùng một lần và xuất HWC một lần; level/version và các con trỏ cần cho phần sau được giữ lại.

Trong bản stock-policy tham chiếu, literal `GLOBAL` nằm ở `0xc5954` sau khi callback khóa ký được thay thế. Trong hai input lần này, vị trí đó vẫn là lệnh epilogue đang dùng của callback gốc. Vì vậy **không sao chép literal vào đó**. Bản mới đặt `GLOBAL\0\0` tại `0x133dc`, trong chính block 128 byte đã thay, sau nhánh rời block nên không thực thi literal.

Để đủ chỗ, nhánh lỗi snprintf dùng đuôi chuỗi có sẵn `mi_hwid: snprintf error.\n`, bỏ phần tên file/dòng trong thông báo chẩn đoán. Lỗi vẫn được log, không append HWC lỗi, vẫn khôi phục con trỏ caller. Callback tin khóa ký `0xc5888..0xc5960` giữ nguyên từng byte.

Chỉ thay ba span code tổng cộng 144 byte và cập nhật prefix hash 180 byte trong cert2 của main LK. Giữ nguyên DER đã ký, toàn bộ BL2 ENG, các certificate/member khác và kích thước container. Không ghép H4, không sửa preloader/modem/Android/NVRAM.

## Tái tạo và kiểm thử

Chạy tại root repo với Python bình thường, không dùng `-O`:

```powershell
python experiments/lk_h3_eng_auto_hwc_v1/assemble_auto.py
python experiments/lk_h3_eng_auto_hwc_v1/build_variants.py
python experiments/lk_h3_eng_auto_hwc_v1/validate_variants.py
```

Builder buộc đúng hash từng input, kiểm tra literal/lệnh gốc, chỉ cho thay các span khai báo và xác nhận callback, DER, các member khác nguyên byte. Assembly được kiểm tra không có relocation chưa giải quyết, dài đúng 128 byte và tái tạo đúng hash.

| Kiểm thử trên bytes đầu ra | Unlocked | Fakelock v2 |
|---|---:|---:|
| Đọc countrycode/chọn HWC, hai slot, HWC gốc, lỗi I/O/formatter | 144 | 144 |
| Getter và các caller unlock nội bộ | 342 | 335 |
| Handoff, phản hồi seccfg và cảnh báo v2 | 0 | 82 |
| Báo trạng thái public của fakelock v1 giữ lại | 0 | 38 |
| Tổng ca main ARM64 | 486 | 599 |
| Chữ ký DER nguyên gốc xác thực offline | 10 | 10 |
| Parser BL2 ENG chạy native với prefix cuối | 3 | 3 |

Test thực thi mã ARM64 theo các đoạn giới hạn; mock giao tiếp phân vùng, formatter/log, append bootargs, PAC và đầu nhận secure-monitor. Không chạy TEE thật hoặc toàn bộ quá trình boot. HWC còn cần xác nhận giá trị Android trên máy thật. Không flash thiết bị trong đợt này.

Checkpoint trước sửa: `before-eng-unlocked-fakelock-auto-hwc-20261105` tại commit `0975d26`. Các bản input được giữ nguyên ở thư mục cũ; có thể lấy lại bằng Git mà không hard reset.
