# Bộ thử vùng radio global v1 — chưa xác nhận sửa được 5G

Theo yêu cầu thử đổi `ro.vendor.radio.build_region=cn`, bộ này tách riêng hai phần:

- `lk_unlock_known_working.img`: bản sao **nguyên byte** LK hybrid đã boot, giữ tính năng unlock cũ. Không có patch 5G mới trong LK.
- `dash_radio_region_global_test_v1.zip`: module KernelSU Next chỉ đặt `ro.vendor.radio.build_region=global` khi khởi động. `global` là giá trị thử nghiệm, chưa chứng minh là profile được modem hỗ trợ hoặc có thể mở NR.

Chưa cài module hoặc flash bất kỳ file nào lên máy trong bước đóng gói này. Không cần flash lại LK để thử module. File LK đi kèm chỉ để giữ bộ artifact độc lập, không cài nó như module.

## Vì sao không gộp thuộc tính này vào LK?

Thuộc tính được khai báo trong `vendor_boot` ramdisk (`prop.default`) và `/vendor/build.prop`. LK xuất mã vùng qua `androidboot.ptcountrycode`, một đầu vào khác. Chưa tìm thấy đường điều khiển trong LK có thể thay trực tiếp thuộc tính Android này. Thay text `cn` ở LK không đủ chứng minh Android/radio sẽ dùng `global`.

Mã radio đã phục hồi còn đọc `ro.miui.build.region` và `persist.vendor.radio.build_region`; module giữ nguyên cả hai. Vì vậy đổi duy nhất thuộc tính được yêu cầu có thể không tạo tác dụng với 5G. Cũng chưa chứng minh `cn` là nguyên nhân chặn NR.

## Cài thử và đọc kết quả

Module chặn cài nếu máy không phải `dash`, ROM không đúng `OS4.0.3.0.XPLCNXM`, hoặc giá trị hiện tại không phải `cn`.

1. Trong KernelSU Next Manager, cài **ZIP module**, rồi khởi động lại máy. Không flash ZIP trong fastboot/recovery.
2. Kiểm tra bằng ADB:

```text
adb shell getprop ro.vendor.radio.build_region
adb shell getprop ro.miui.build.region
adb shell getprop persist.vendor.radio.build_region
adb shell getprop ro.vendor.radio.5g
adb shell settings get system 5g_sa_mode_disabled
adb shell getprop gsm.network.type
```

Thuộc tính đầu tiên cần là `global`. Hai cấu hình thử 5G cũ vẫn phải là `ro.vendor.radio.5g=2` và key SA `null` (không tồn tại). Kết quả `global` chỉ xác nhận override được áp dụng, không xác nhận 5G hoạt động. Cần quan sát máy đăng ký NR thực tế; biểu tượng 5G riêng lẻ chưa đủ kết luận.

Module không sửa cấu hình SA/NSA, không ghi thuộc tính `persist.*`, không ghi bộ nhớ cấu hình modem, không đổi APK, không khởi động lại dịch vụ mạng bằng script. Tác dụng gián tiếp của thay đổi vùng với ROM này chưa được kiểm thử trên máy thật.

## Hoàn nguyên

Tắt hoặc gỡ `Dash radio region global - experimental` trong KernelSU Next Manager rồi khởi động lại. Kiểm tra thuộc tính đầu tiên trở lại `cn`. Không cần flash lại LK. Nếu không vào được giao diện, dùng cơ chế tắt module của trình quản lý root/recovery; khi truy cập được `/data`, tạo file `/data/adb/modules/dash_radio_region_global_test/disable` rồi khởi động lại.

Máy đã có hai lần sọc màu/đen màn trong đợt thử trước; nguyên nhân chưa xác định. Theo yêu cầu người dùng, các thay đổi SA/NSA đã được hoàn nguyên trước khi tạo bộ này. Không tự bật lại các phép thử cũ cùng module.

## Nguồn và kiểm chứng

Xem [manifest.json](manifest.json) để đối chiếu SHA256 và phạm vi. Source module nằm trong [module](module). Xem [docs nghiên cứu](../../docs/07-nr5g-investigation.md) và [bằng chứng hoàn nguyên](../../fresh_analysis/nr5g_research/crash_review/rollback_and_current_state.json).

Kiểm tra trên máy tính gồm cấu trúc module, nội dung ZIP, chỉ một property override, guard cài đặt và hash LK. Chưa kiểm thử cài module, thời điểm radio đọc thuộc tính, đăng ký NR hoặc độ ổn định trên máy thật.
