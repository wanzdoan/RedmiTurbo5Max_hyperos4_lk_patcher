# LK ENG unlocked + fakelock cho Android và fastboot

Artifact riêng: [lk_hyperos3_ENG_UNLOCKBL_FAKELOCK_TEST.img](lk_hyperos3_ENG_UNLOCKBL_FAKELOCK_TEST.img), **4.043.792 byte**.

SHA256: `28f7f7b918a5462685eddf5e4814df1c28302bc9450de4d983dea81da58371ec`.

Input đúng là bản ENG unlocked trước đó, SHA256`0d237263a24f6bf41fa33b5dc43bb7d71e5b61f1977331c3f9f1812ad5000466`. File input, LK ENG gốc và các artifact khác giữ nguyên. Checkpoint trước sửa: `before-eng-fakelock-20261104`, tại commit`5d4a753` trên repo private.

## Hành vi được yêu cầu

| Bề mặt báo trạng thái | Giá trị bản thử xuất ra |
|---|---|
| Android bootarg `androidboot.vbmeta.device_state` | `locked` |
| Android bootarg `androidboot.verifiedbootstate` | `green` |
| Android bootarg `androidboot.flash.locked` | `1` |
| `fastboot getvar unlocked` | `no` |
| `fastboot getvar secure` | `yes` |
| Hai biến trên trong `fastboot getvar all` | Cùng giá trị đã fake |
| Truy vấn lock-status riêng của vendor tại payload`0xc770` | `lks = 1` |

**Nội bộ vẫn unlocked:** getter payload`0x896e4` tiếp tục trả state3/success0; predicate xác thực boot, biến trạng thái thật và cache biến fastboot giữ nguyên. Patch chỉ sửa nơi xuất thông tin. Không gọi lệnh khóa thật, không ghi RPMB/efuse và không thêm bước bật Secure Boot.

## Vì sao không sửa biến fastboot thành locked?

LK ENG có một nhánh xử lý lệnh cũng đọc cache biến `unlocked`. Nếu sửa cache để giao diện thấy `no`, lệnh có thể bị xử lý như máy khóa thật. Bản này giữ cache và chỉ thay giá trị ngay trước khi trả lời USB, nên code nội bộ vẫn đọc giá trị cũ. Cả truy vấn đơn và `getvar all` được xử lý.

Hàm hỏi `lks` cũ được viết gọn trong đúng phạm vi140 byte vốn có. Phần trống trong cùng phạm vi chứa helper định dạng kết quả và literal bootarg mới. Không mở rộng main payload, không đè lệnh của command khác, không thay danh sách command hoặc biến đã đăng ký. Nhánh flash/erase và các hàm thay đổi lock thật giữ nguyên byte.

Các công cụ PC có thể tự từ chối flash khi nhận `unlocked:no`, dù thiết bị vẫn giữ quyền xử lý lệnh. Không thể đồng thời bảo đảm mọi công cụ PC sẽ bỏ qua kết quả mà chính nó yêu cầu. Các lệnh nạp/xóa trên máy thật chưa được chạy trong tác vụ này.

## Vị trí sửa

Tất cả offset sau là **payload**, cộng`0x200` để ra offset trong file:

- `0xc770..0xc7fb`: public vendor lock reply, helper getvar-only tại`0xc790`, literal `androidboot.flash.locked=1` tại`0xc7e0`.
- `0xb014`: gọi helper lúc trả lời một biến getvar.
- `0xb040`: gọi helper lúc định dạng một dòng `getvar all`.
- `0xbc79c`: chỉ chọn chuỗi `locked` cho bootarg AVB, không sửa callback báo trạng thái dùng trong xác thực.
- `0x69da4..0x69dd7`: xuất bootarg green/flash.locked; giữ nguyên biến trạng thái verified boot nội bộ, hàm cảnh báo và đường truyền trạng thái thật khác. Trạng thái lỗi trả về từ helper append được giữ.
- Prefix main cert2 được tính lại; toàn bộ DER có chữ ký và các member khác main/prefix giữ nguyên.

## Phạm vi fakelock

Đây là **báo trạng thái giả**, không làm bootloader khóa thật hoặc làm ROM đã sửa trở thành ROM đã được OEM xác minh. Trạng thái thực đưa cho TEE/phần cứng không được sửa. Không có cam kết vượt hardware attestation, Play Integrity, ngân hàng hoặc mọi API kiểm tra lock trong Android.

Bootarg được LK xuất đúng trong mô phỏng; việc Android của ROM đang dùng nhập thành `ro.boot.*` cần kiểm tra trên máy thật. Nếu ROM/các tầng khác thêm giá trị trùng, kết quả cuối còn phụ thuộc cách chúng được nhập. Khi boot đến hàm báo trạng thái này, nó báo green cho mọi state; quyết định boot, state gốc và các cảnh báo ở nhánh khác vẫn giữ nguyên.

Không dùng `fastboot flashing lock` hoặc lệnh khóa thật để bật chức năng này. Chỉ việc dùng artifact này mới thay đổi phần báo trạng thái; không cần thao tác khóa lưu bền.

## Kiểm chứng

[validation.json](validation.json) đạt:

- **74 ca CPU báo trạng thái:** so code cũ/mới, getvar đơn/all, biến khác không đổi, danh sách biến rỗng/đảo thứ tự, vendor lks, bootargs và lỗi append. Sau các phản hồi fastboot, chạy getter biến nội bộ thật trên cùng danh sách và kiểm tra vẫn đọc được `unlocked=yes`, `secure=no` theo fixture ban đầu; bộ nhớ cache không bị ghi.
- **335 ca CPU unlock nội bộ:** getter/hai wrapper, predicate xác thực boot và lock boolean, gồm controls trạng thái locked/unlocked. Bản mới vẫn force-unlocked ở nhánh thật.
- **10 chữ ký DER giữ lại hợp lệ**,3 ca parser BL2 ENG thật chọn đúng hash mới; toàn bộ BL2/AEE/DTB/DTBO và certificate đi kèm giữ nguyên từ input ENG.
- ABI/stack, build xác định, input sai bị từ chối, không có direct branch ngoài hàm vào các byte nội bộ được tái sử dụng của hàm lks trong vùng code đã quét.

Mô phỏng bỏ qua PAC/logging và mô hình hóa USB, snprintf, cmdline append cùng một số boundary đã liệt kê trong JSON. Không phải kiểm thử toàn chuỗi boot, thực thi flash/erase phần cứng hoặc xác nhận property trên Android thật. **Chưa flash/boot thử artifact mới.**

## Tái tạo và kiểm tra sau khi người dùng tự test

```powershell
python experiments/lk_h3_eng_fakelock_v1/build_fakelock.py
python experiments/lk_h3_eng_fakelock_v1/validate_fakelock.py
```

Các truy vấn chỉ đọc có thể dùng để đối chiếu sau boot:

```text
adb shell getprop ro.boot.vbmeta.device_state
adb shell getprop ro.boot.verifiedbootstate
adb shell getprop ro.boot.flash.locked
fastboot getvar unlocked
fastboot getvar secure
fastboot getvar all
```

Giữ bản ENG unlocked cũ để quay lại. Xem [manifest](manifest.json) cho bytes gốc/thay thế và [builder](build_fakelock.py) cho cách bảo toàn các phạm vi nội bộ.
