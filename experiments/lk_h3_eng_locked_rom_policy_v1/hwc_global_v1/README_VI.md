# Bản thử HWC=GLOBAL trên LK ENG giữ cơ chế khóa gốc

Theo yêu cầu người dùng “cứ thử đổi hwc xem sao”, ngày2026-11-04. **Đây là phép thử giả thuyết, chưa xác nhận sửa được lỗi recovery Global.** Không cần chờ đủ ROM để tạo biến thể thử này; việc tìm điều kiện gốc của lỗi vẫn còn mở.

File: [lk_hyperos3_ENG_STOCK_CN_GLOBAL_HWC_GLOBAL_TEST.img](lk_hyperos3_ENG_STOCK_CN_GLOBAL_HWC_GLOBAL_TEST.img), **4.043.792 byte**.

SHA256: `9eabe1a50088d875348270908b5303b89dba0eb49ac40d3c5b09750b9a9605cb`.

Input là đúng [bản stock-policy v1](../README_VI.md), SHA256 `ad3d66e3c374ff3a83fc73ba30880fe1d18ee049b3c2ec54ff6e97e45815760b`. Bản input và mọi artifact cũ giữ nguyên tên/bytes. Không dùng bản ENG unlocked hoặc fakelock làm nền.

## Sửa đúng một giá trị xuất

LK trước đây lấy chuỗi HWC từ record phần cứng rồi định dạng:

```text
androidboot.hwc=<HWC thật> androidboot.hwlevel=<level thật> androidboot.hwversion=<version thật>
```

Bản thử đổi riêng đối số HWC thành literal `GLOBAL`. HW level và HW version vẫn dùng các con trỏ/giá trị cũ. Record phần cứng do getter đọc ra không bị ghi đổi. Android dự kiến nhập thành `ro.boot.hwc=GLOBAL`, nhưng chưa có readback trên máy thật.

Hai span sửa trong main (offset payload, cộng`0x200` để ra whole-file):

| Offset | Số byte | Thay đổi |
|---|---:|---|
| `0x13380` | 4 | `add x3,x19,#0x24` → `adr x3,0xc5954` |
| `0xc5954` | 8 | Hai NOP không được đi vào ở cuối callback v1 chứa `GLOBAL\0\0` |

Đây là **4 byte lệnh mới +8 byte literal**, cộng tính lại prefix hash main cert2. Vị trí literal nằm sau `ret` và nhánh vô điều kiện quay về epilogue, trước hàm kế tiếp; không thay lệnh chạy của callback tin khóa. Kiểm tra không có nhánh trực tiếp hoặc con trỏ tuyệt đối sẵn có tới slot này, và chạy lại các đường callback lỗi/thành công.

Không thêm vùng nhớ hoặc tăng kích thước ảnh. Giữ nguyên mọi member ENG ngoài main/prefix, toàn bộ DER có chữ ký và padding.

## Những phần giữ nguyên

- Getter/setter và token/RPMB logic của ENG gốc; không ép unlocked hoặc locked và không thêm fakelock. Trạng thái lưu thật vẫn được cơ chế gốc đọc như v1.
- Cổng nhận khóa ENG2048/China4096/Global4096 và patch flash-region của v1.
- Xác thực boot, rollback, trạng thái verified boot và dữ liệu root-of-trust; không ghép bản deep-fakelock.
- Getter/record phần cứng, HW level, HW version, CPUID và đường xuất `ptcountrycode`.
- Preloader, modem, Android và thiết lập điện thoại.

Override áp dụng mọi lần hàm bootarg này chạy, gồm normal boot/recovery; **không tự nhận biết ROM đang là China hay Global**. Đây là biến thể thử riêng cho tình huống Global hiện tại. Chưa xác nhận hành vi ROM China khi báo HWC=GLOBAL; bản v1 cũ là điểm đối chiếu.

## Kiểm chứng

[validation.json](validation.json) đạt **234 ca CPU main**:

- **160 ca HWC:** so v1/bản mới với HWC CN/GLOBAL/IN/CN_TEST/rỗng, hai HW level, hai HW version, lỗi formatter và các kết quả append. Chạy mã ARM64 thật của phần đặt con trỏ/nhánh gọi, native memset; mô hình hóa getter phần cứng, snprintf, logging và append. Kiểm chuỗi được truyền, hai trường HW còn lại, record phần cứng và byte lân cận không bị ghi, frame/thanh ghi không liên quan giữ nguyên.
- **50 ca callback khóa:** ENG/CN/GL hợp lệ, khóa lạ/sửa một bit, kích thước sai, null, lỗi hash và khóa trích từ đúng vbmeta Global người dùng đã gửi. Callback, SHA256, memcmp chạy thật. Literal trong padding không làm thay hành vi callback.
- **24 ca khóa có control:** AVB predicate, lock boolean và phản hồi fastboot vẫn theo trạng thái getter gốc. Getter được mô hình hóa ở các ca này; không phải đọc RPMB thiết bị.
- **10 chữ ký DER gốc hợp lệ**, **3 ca bộ đọc hash BL2 ENG thật** chọn đúng hash mới. Build lặp lại trùng byte, input sai hash bị từ chối, chỉ đổi các span lệnh/literal/prefix đã ghi trong manifest.

**Không flash/boot máy thật**, không kiểm chứng toàn bộ Android/recovery hoặc cách ROM nhập bootargs. Các test không chứng minh đã xử lý được nguyên nhân phát `show_version_mismatch`. HWC có consumer khác ngoài recovery; hành vi của chúng sau override chưa được kiểm tra.

## Tái tạo và đối chiếu sau khi người dùng tự thử

```powershell
python experiments/lk_h3_eng_locked_rom_policy_v1/hwc_global_v1/build_hwc_global.py
python experiments/lk_h3_eng_locked_rom_policy_v1/hwc_global_v1/validate_hwc_global.py
```

Nếu máy boot được và có ADB, truy vấn chỉ đọc:

```text
adb shell getprop ro.boot.hwc
adb shell getprop ro.boot.hwlevel
adb shell getprop ro.boot.hwversion
adb shell getprop ro.boot.vbmeta.device_state
adb shell getprop ro.boot.verifiedbootstate
```

Kỳ vọng mới là HWC=`GLOBAL`; các giá trị khóa/verified boot phải vẫn theo cơ chế ENG gốc, không bị file này ép thành trạng thái giả. Ghi rõ kết quả boot hoặc nguyên văn thông báo recovery, không chỉ việc dòng HWC đổi.

Xem [manifest](manifest.json) cho bytes/hashes. Checkpoint trước sửa đã push private: `before-eng-stock-hwc-global-test-20261104`, commit`1276552`. Không sửa hoặc ghi đè artifact v1 để quay lại được bản cũ.
