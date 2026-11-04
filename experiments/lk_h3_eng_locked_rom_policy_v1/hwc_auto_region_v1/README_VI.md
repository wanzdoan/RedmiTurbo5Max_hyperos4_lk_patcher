# Một LK tự chọn HWC cho China và nhóm Global/EEA/Taiwan

Artifact: [lk_hyperos3_ENG_STOCK_AUTO_HWC_CN_GLOBAL_EEA_TW_TEST.img](lk_hyperos3_ENG_STOCK_AUTO_HWC_CN_GLOBAL_EEA_TW_TEST.img), **4.043.792 byte**.

SHA256: `fe60552c60d3495ad479e34e2610ab37acca064c367f8fce750989f13f4e12a0`.

**Bản tự động mới chưa flash/boot test.** Input là đúng [bản fixed-GLOBAL người dùng báo đã boot](../hwc_global_v1/runtime_confirmation.json), SHA256 `9eabe1a50088d875348270908b5303b89dba0eb49ac40d3c5b09750b9a9605cb`. Input và mọi artifact trước giữ nguyên bytes/tên; kết quả boot input không tự xác nhận bản tự động.

## Chọn theo dữ liệu ROM đang cài

LK đã có sẵn đường đọc phân vùng `countrycode` theo slot hiện hành và xuất `androidboot.ptcountrycode`. Bản này dùng lại kết quả đọc đó, không thêm một lần đọc phân vùng riêng và không sửa nội dung `countrycode`.

| Dữ liệu `countrycode` | HWC xuất sang Android |
|---|---|
| `cn`, `CN`, `Cn`, `cN` đúng chuỗi2 ký tự | `CN` |
| `global`, `eea`, `tw`, các biến thể quốc tế không rỗng khác | `GLOBAL` |
| Phân vùng thiếu, dữ liệu rỗng, lỗi/đọc thiếu | HWC phần cứng gốc |

EEA và Taiwan là vùng ROM; bản này chọn cho chúng cùng giá trị HWC quốc tế `GLOBAL` đã được thử trên Global. **Không tự tạo HWC mới tên EEA/TW.** Chưa có `vbmeta`/`countrycode` EEA và Taiwan thật được cung cấp, nên việc boot hai vùng đó chưa được xác nhận. Nếu các ROM này dùng `countrycode=global`, chúng vẫn chọn đúng nhánh quốc tế.

Đây là lựa chọn dựa trên phân vùng countrycode đang flash, không đọc tên file ROM hoặc đuôi tên build trên máy tính. Khi đổi ROM cần để countrycode đi cùng đúng bộ ảnh ROM. Trường hợp giữ lại countrycode cũ có thể chọn HWC theo dữ liệu cũ; không gọi đó là tự nhận diện toàn bộ ROM từ chữ ký vbmeta.

## Cách đổi trong LK

Trong hàm tạo bootargs `0x1320c`, HWC trước đây được xuất trước khi đọc countrycode. Bản này dời đúng phần định dạng HWC/HW level/HW version tới sau lần đọc countrycode, rồi tiếp tục luồng gốc. Chỉ xuất một dòng HWC; không dựa vào việc Android chọn giữa các bootarg trùng tên.

| Offset payload | Span | Thay đổi |
|---|---:|---|
| `0x13364..0x133e3` | 128 byte | Skip vị trí HWC sớm; chứa block nội bộ chọn CN/GLOBAL, dùng getter phần cứng gốc, snprintf/append và tiếp tục |
| `0x13960..0x1396b` | 12 byte | Khi đọc thiếu/lỗi, xóa dữ liệu đọc dở trong buffer stack; giữ logging và close phân vùng gốc |
| `0x139c0` | 4 byte | Từ điểm chung sau countrycode, nhảy tới block HWC rồi trở lại`0x139c4` |

Offset trong file cộng`0x200`. Tổng span lệnh144 byte và prefix hash main cert2 được tính lại. Không mở rộng stack frame/main/container, không thêm code vào vùng BSS hoặc tự sửa code khi chạy.

Getter phần cứng `0x3530` là hàm copy cache RAM có sẵn, giữ nguyên mã; chỉ dời thời điểm gọi, vẫn đúng một lần. Block mới giữ nguyên HW level/HW version và không ghi cache phần cứng. Nó khôi phục `x19=sp+0x130` để phần mã gốc tại`0x13a20` tiếp tục nhận đúng con trỏ HW version, đồng thời khôi phục con trỏ output cho block kế tiếp. Mã đi sau vẫn dùng stack layout gốc.

Giữ nguyên literal `GLOBAL` của bản input tại`0xc5954` và dùng lại chuỗi `CN` sẵn có tại`0xf34d4`. Callback nhận khóa ENG/CN/GL và mọi lệnh chạy của nó nguyên byte từ input.

## Phần không đổi về chính sách boot

Giữ cơ chế khóa ENG gốc, getter/setter/token, các quyết định xác thực boot, rollback, phản hồi trạng thái fastboot và đường gửi root of trust. Không ép unlocked hoặc locked, không ghép fakelock/deep-fakelock, không sửa chứng chỉ hay private key.

Cổng nhận các khóa ENG/CN/GL của v1 vẫn giữ nguyên; một ROM EEA/Taiwan đổi khóa ký hoặc khác model/layout/rollback vẫn có thể không được chấp nhận. Việc chọn HWC cho vùng đó không đồng nghĩa đã xác minh đủ chuỗi boot của ROM đó.

Toàn bộ member ngoài main/prefix và DER/padding giữ nguyên. Không flash điện thoại, không ghi efuse/RPMB, không đổi modem/preloader/Android hoặc dữ liệu phân vùng.

## Kiểm chứng

[validation.json](validation.json) đạt **264 ca CPU main**:

- **248 ca luồng countrycode/HWC:** chạy hai đoạn mã ARM64 gốc/mới của cùng hàm, đi qua đọc tên phân vùng theo slot A/B, điều kiện đủ16 byte, rỗng/thiếu/lỗi I/O, copy cache phần cứng và nhánh chọn HWC. Test các chuỗi CN có phân biệt hoa/thường, Global/EEA/TW và chuỗi lạ; HW level/version; lỗi formatter. Kiểm chỉ xuất một HWC, giữ ptcountrycode, không thêm lần đọc, cache/guard/frame không đổi, con trỏ HW version cho phần mã tiếp theo được giữ đúng.
- **4 ca callback khóa:** ENG/CN/GL vẫn được nhận, khóa4096 lạ bị từ chối. Chạy mã thật của callback, SHA256 và memcmp; toàn bộ callback còn nguyên byte.
- **12 ca control trạng thái khóa:** AVB predicate, lock boolean và vendor query vẫn theo trạng thái getter cũ. Getter chỉ được mô hình hóa trong các control có nhãn, không đọc RPMB thiết bị.
- **10 chữ ký DER gốc hợp lệ**, **3 ca parser BL2 ENG thật** chọn hash mới. Build và assembly lặp lại trùng byte; input sai hash bị từ chối; kiểm các nhánh cũ không đi vào vùng mã nội bộ được bố trí lại.

Ở các ca luồng, getter phần cứng `0x3530`, memcpy, strlen, memset, các nhánh và lựa chọn con trỏ chạy thật. Slot source/partition lookup/read/close, snprintf, logging và append là các boundary mô hình hóa. Hai đoạn được nối theo trạng thái caller, nhưng không chạy các bootarg không liên quan ở giữa; **không phải mô phỏng đầy đủ hàm hoặc toàn máy**.

[Bằng chứng countrycode China](china_countrycode_evidence.json) đọc từ ảnh China H4 có sẵn: dữ liệu đầu là `cn`, hash payload khớp descriptor có chữ ký trong vbmeta của bộ ảnh đó. [Bằng chứng Global đã có](../recovery_region_v2/global_vbmeta_descriptors.json) chứa hash ứng với `global` + zero padding. Không phải readback phân vùng điện thoại. EEA/Taiwan hiện là fixture kiểm thuật toán, không phải ROM đã boot test.

## Tái tạo và mốc quay lại

```powershell
python experiments/lk_h3_eng_locked_rom_policy_v1/hwc_auto_region_v1/assemble_auto.py
python experiments/lk_h3_eng_locked_rom_policy_v1/hwc_auto_region_v1/build_auto_hwc.py
python experiments/lk_h3_eng_locked_rom_policy_v1/hwc_auto_region_v1/validate_auto_hwc.py
```

`auto_hwc.bin` là128 byte mã cho builder, không phải ảnh để flash. Chỉ file `.img` nêu đầu tài liệu là LK đầy đủ. [Manifest](manifest.json) ghi chính xác bytes/input/output hashes.

Checkpoint trước sửa đã push private: tag `before-eng-auto-hwc-region-20261104`, commit`ff1a134`. Bản fixed-GLOBAL `9eabe1a5...` là điểm quay lại đã được người dùng báo boot; bản tự động mới phải được kiểm riêng trên máy thật.

Sau khi người dùng thử, cần ghi vùng ROM/build, kết quả boot và giá trị `adb shell getprop ro.boot.hwc` nếu có ADB. Không gán kết quả máy thật vào các ca mô phỏng hoặc suy thành công EEA/Taiwan từ một ca Global.
