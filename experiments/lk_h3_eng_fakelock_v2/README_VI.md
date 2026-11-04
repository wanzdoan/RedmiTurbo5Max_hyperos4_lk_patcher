# ENG unlock + fakelock v2: mở rộng tới dữ liệu gửi sang lớp bảo mật

Ngày2026-11-04. Input là đúng [bản fakelock v1](../lk_h3_eng_fakelock_v1/README_VI.md), SHA256 `28f7f7b918a5462685eddf5e4814df1c28302bc9450de4d983dea81da58371ec`. Không lấy bản stock ENG mở rộng khóa China/Global của tác vụ trước làm đầu vào.

Artifact: [lk_hyperos3_ENG_UNLOCKBL_FAKELOCK_DEEP_HANDOFF_TEST.img](lk_hyperos3_ENG_UNLOCKBL_FAKELOCK_DEEP_HANDOFF_TEST.img), **4.043.792 byte**.

SHA256: `72e17062caf8893b0c6a7c82f6d3c25f0e3ed27544e01b5c47fe3a0234e5b075`.

**Chưa flash/boot máy thật; chưa xác nhận VNeID hoặc Play Integrity pass.** File này làm giả thêm dữ liệu xuất từ LK. Nó không làm trạng thái boot thực trở nên hợp lệ hoặc tạo chứng chỉ attestation đáng tin.

## Phát hiện và yêu cầu người dùng

Người dùng báo máy không root, một số ngân hàng dùng được nhưng VNeID vẫn phát hiện mở bootloader và Integrity chưa sạch. Chưa có chi tiết verdict Basic/Device/Strong, bản chứng chỉ attestation hoặc xác nhận hash LK đang chạy trên máy. `adb devices -l` trong lần kiểm tra này chỉ thấy một kết nối offline; không đọc hoặc thay thiết lập điện thoại.

Bản v1 chỉ thay đầu ra cho Android/fastboot. Trong [hàm gửi root of trust gốc](../lk_h3_eng_locked_rom_policy_v1/verified_analysis/ffff0000907c4de0.c), trạng thái thật vẫn được xuất qua dịch vụ secure-monitor `0xc2000120`:

1. Hash của khóa OEM được tính theo đường gốc, nhưng khi unlocked thì bốn từ64 bit gửi đi bị thay bằng zero.
2. Gói tiếp theo có cờ khóa lấy từ local word ở stack và trạng thái boot lấy từ global `0x20d900`.
3. Các gói tiếp theo gửi các word metadata/digest khác, rồi chốt chuỗi truyền bằng gói zero.

Đây là một đường còn lộ trạng thái chưa xử lý ở v1. **Chưa chứng minh lớp bảo mật của thiết bị dùng riêng các giá trị này để tạo chứng chỉ, và chưa chứng minh đó là nguyên nhân duy nhất khiến VNeID fail.** Không suy diễn từ tên hàm thành kết quả kiểm thử ứng dụng.

Theo [định dạng attestation của Android](https://source.android.com/docs/security/features/keystore/attestation), root of trust có các trường `deviceLocked`, `verifiedBootState` và thông tin khóa boot. Theo [tài liệu Play Integrity](https://developer.android.com/google/play/integrity/verdicts), Device Integrity trên Android13+ còn yêu cầu bằng chứng phần cứng về khóa bootloader và ảnh hệ điều hành được nhà sản xuất chứng nhận; Strong Integrity còn có điều kiện cập nhật bảo mật. Vì vậy sửa các giá trị LK xuất ra không đủ để cam kết mọi verdict pass.

## Thay đổi v2

Các offset dưới đây tính từ đầu payload main; cộng `0x200` để ra offset trong file. [Manifest](manifest.json) chứa đủ bytes gốc và bytes mới.

| Offset | Sửa | Ý nghĩa |
|---|---|---|
| `0xc4eac` | `ldr w8,[sp,#0x2c]` → `mov w8,#1` | Gói1 chọn hash khóa OEM đã tính sẵn theo đường gốc, thay vì chọn zero vì đang unlocked |
| `0xc4f0c` | `ldrsw x1,[sp,#0x2c]` → `mov x1,#1` | Gói2 xuất cờ khóa bằng1; không đổi local word hoặc getter thật |
| `0xc4f14` | `ldr w2,[x8,#0x900]` → `mov w2,#0` | Gói2 xuất trạng thái boot bằng0; không đổi global trạng thái thật |
| `0xc838` | `ldur w3,[x29,#-4]` → `mov w3,#1` | Truy vấn vendor bổ sung xuất `seccfg_state is 1`; không ghi trạng thái seccfg |
| `0x69e68` | Nhánh có điều kiện → nhánh tới epilogue | Bỏ riêng cảnh báo orange/unlocked; giữ nhánh lỗi yellow/red |

Tổng **20 byte span lệnh** so với v1, cộng tính lại prefix hash main cert2 tại chỗ. Không chèn vùng mã, không tăng kích thước main/container hoặc thay cách đóng gói. Giữ toàn bộ DER có chữ ký, padding và mọi thành phần ENG khác nguyên byte từ v1.

Giữ các tính năng v1:

- Getter thật vẫn trả unlocked3/success0; các quyết định xác thực boot vẫn dùng trạng thái thật.
- Cache biến và mã quyền fastboot không đổi; các phản hồi `getvar unlocked=no`, `secure=yes`, vendor `lks=1` vẫn được giữ.
- Android vẫn nhận các bootarg locked/green/flash.locked1 như v1. Cần đọc trên máy thật để xác nhận cách ROM nhập các giá trị cuối cùng.
- Không ghi RPMB/efuse, không chạy lệnh khóa, không đổi preloader, modem, Android hoặc cài root/module.
- Không ghép mở rộng khóa boot China/Global từ bản stock ENG thử riêng. Không sửa chứng chỉ/private key attestation hay giả mức bản vá bảo mật.

## Kiểm thử

[validation.json](validation.json) đạt **537 ca CPU main**:

- **120 ca handoff:** cả v1/v2, năm giá trị trạng thái boot, getter force-unlocked thật cùng các control có nhãn, lỗi lấy khóa và trạng thái trả về của secure-monitor được mô hình hóa. Chạy thật đường hàm gửi, getter/lock boolean không bị mock ở ca chính, cùng SHA256 phần mềm trong ENG; so toàn bộ năm gói theo thứ tự. Kiểm local lock word, global trạng thái, các word metadata/digest khác và ABI không bị ghi đổi.
- **20 ca truy vấn seccfg:** các trạng thái0/1/3/4/không hợp lệ và lỗi getter. Chỉ chuỗi trả về đổi; word thật không bị ghi. Phản hồi lỗi gốc được giữ, không biến ERROR thành thành công.
- **24 ca cảnh báo UI:** bỏ orange; giữ nhánh cảnh báo và handler lỗi yellow/red trong các chế độ được kiểm.
- **38 ca public v1:** getvar đơn/all, biến không liên quan, cache thật sau trả lời, bootargs và lỗi append.
- **335 ca unlock nội bộ:** getter/hai wrapper, predicate xác thực boot và lock boolean như trước.

Ngoài ra: **10 chữ ký DER gốc hợp lệ**, **3 ca bộ đọc hash BL2 ENG thật**, build lặp lại trùng byte, input khác hash bị từ chối, chỉ các span lệnh/prefix cho phép thay đổi.

**Ranh giới quan trọng:** secure-monitor là sink mô phỏng chỉ thu tham số và trả cấu trúc kết quả, không phải lớp bảo mật thật. Bộ kiểm thử không phát hành chứng chỉ attestation, không thực hiện yêu cầu Play Integrity, không chạy VNeID và không kiểm quyền flash trên phần cứng. Đầu vào khóa trong ca mô phỏng là fixture256 byte; artifact vẫn dùng đường lấy khóa thật của ENG khi chạy trên thiết bị.

Các lỗi lấy khóa/hash/secure-call vẫn đi qua logging gốc. Vì đây là giả trạng thái xuất, các giá trị locked/green không chứng minh quá trình mật mã đã thành công và không sửa lỗi đó. Bản v1 vốn đã fake các bootarg kể cả trạng thái lỗi; v2 không thay quyết định boot bên trong.

## Giới hạn và ảnh hưởng cần kiểm chứng

Đây là mức thay đổi sâu hơn bản v1 vì dữ liệu được gửi tới lớp bảo mật, không chỉ Android/fastboot. Root-of-trust có thể tham gia xử lý các khóa phần cứng; **chưa kiểm chứng khả năng tiếp tục sử dụng khóa Keystore/dữ liệu hiện có trên máy này**. Không mô tả bản thử là an toàn cho dữ liệu hay đã được xác nhận boot chỉ nhờ mô phỏng.

Chưa biết secure-monitor/TEE thực tế có chấp nhận, chuẩn hóa, đối chiếu hoặc thay thế những giá trị này hay không. Các trường trong chứng chỉ thật có thể vẫn khác. Chứng chỉ thiết bị, chứng nhận Google, provisioning/thu hồi khóa, ảnh ROM, mức cập nhật, dịch vụ Google và chính sách ứng dụng là các yếu tố riêng; không tạo khóa ký OEM/Google bằng patch LK.

Muốn đánh giá hiệu quả phải có kết quả attestation/verdict trước và sau trên thiết bị, kèm hash đúng artifact. Không dùng kết quả mô phỏng ở đây để tuyên bố đã vượt kiểm tra ngân hàng, VNeID hoặc Integrity. Trong tác vụ này không flash thiết bị, không đổi cơ chế khóa lưu bền, không xóa Keystore hay dữ liệu ứng dụng.

## Tái tạo và quay lại

```powershell
python experiments/lk_h3_eng_fakelock_v2/build_deep_fakelock.py
python experiments/lk_h3_eng_fakelock_v2/validate_deep_fakelock.py
```

Checkpoint trước sửa đã push repo private: `before-eng-fakelock-v2-20261104`, tại commit `c8b6bae`. V1 `28f7f7b9...`, ENG unlocked `0d237263...` và ENG gốc `6f386241...` giữ nguyên để lấy lại. Không cần hard reset Git hoặc force-push.
