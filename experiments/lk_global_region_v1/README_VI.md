# LK thử bỏ giới hạn ROM CN/Global — chưa phải fix 5G

File riêng: [lk_h4_unlock_CN_GLOBAL_REGION_TEST.img](lk_h4_unlock_CN_GLOBAL_REGION_TEST.img), 4.083.424 byte.

SHA256: `782df4cb90d2bc773a1a599d6d1c77dfc765c62efe3357185013ebbd2e731095`.

Đây là bản thử chỉ sửa LK cho máy `dash` China trong dự án này. Giữ nguyên patch unlock đã boot, H3 BL2 còn chữ ký, AEE và DTB/DTBO H4. Bản đã chạy `fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img` vẫn nguyên byte, không bị ghi đè.

**Chưa flash hoặc kiểm thử boot bản mới trên điện thoại. Chưa có ROM Global cụ thể để kiểm tra tương thích. Không có bằng chứng bản này làm máy bắt được 5G.** Không thay Android, modem, preloader, thuộc tính vùng hoặc cài module.

## Ba chỗ sửa trong LK

| Vị trí trong file | Chức năng trước sửa | Sau sửa |
|---|---|---|
| `0x6924c`, 4 byte | Máy CN dừng boot nếu mã vùng ROM không bắt đầu bằng CN/NATIVE | Tiếp tục sau kiểm tra vùng; không in log giả rằng vùng đã khớp |
| `0x88ad0`, 28 byte | Với máy CN, khóa ảnh không đúng khóa CN bị từ chối dù qua xác thực chuỗi chứng chỉ | So thêm đúng khóa GL256 byte có sẵn trong LK; khóa khác vẫn bị từ chối ở đường kiểm tra đang bật |
| `0xc5c50`, 4 byte | Callback xác thực boot chọn khóa CN hoặc GL512 byte theo vùng máy | Chấp nhận một trong hai khóa có sẵn; khóa lạ vẫn không được tin |

Mã vùng ROM ở đây là thuộc tính `com.android.build.init_boot.sku` trong dữ liệu xác thực boot. Nó khác thuộc tính radio `ro.vendor.radio.build_region`.

Chốt Secure ID thứ hai có thông báo `CN hardware not allow to flash GL Sw..` đã được patch unlock cũ vượt qua bằng trạng thái3, nên không sửa thêm. Thông báo này xuất hiện trong đường boot; chưa phải chứng cứ fastboot từ chối ghi partition.

Giữ nguyên bước xác thực chuỗi chứng chỉ, xử lý lỗi trích khóa, xác thực ảnh/header, chống hạ phiên bản và logic EP/MP. Không thêm lệnh ghi efuse; logic efuse có sẵn của ROM giữ nguyên. Chỉ checksum prefix của main LK được tính lại theo cơ chế đã dùng ở bản boot thành công, không ký lại DER bằng private key.

## Phạm vi cần hiểu rõ

- Chốt SKU được bỏ cho mọi SKU không phải CN/NATIVE, gồm cả SKU trống; đây là bỏ giới hạn vùng, không phải bộ kiểm tra ROM Global hợp lệ.
- Ở nhánh khóa ảnh của phần cứng nonCN, bản gốc trả thành công khi mismatch; fallback mới yêu cầu khóa GL và làm nhánh đó nghiêm hơn. Artifact này nhắm phần cứng CN `dash` của người dùng.
- Những đường bỏ kiểm tra có sẵn như dconfig hợp lệ vẫn giữ tác dụng. Không mô tả bản này là bật xác thực ở mọi trường hợp.
- Chưa đối chiếu ROM Global, model, phân vùng, driver hoặc modem của ROM đích. Bỏ chốt vùng không tạo tương thích giữa hai model khác nhau và không vượt xác thực ở các tầng boot chạy trước LK.
- Chưa tìm được patch LK làm modem đăng ký NR. Type13 LK truyền xuống đã có bit N/NR0x40 trong kernel; đổi con số này không phải bật thêm NR.

## Build và kiểm thử

```powershell
python experiments/lk_global_region_v1/build_region_lk.py
python experiments/lk_global_region_v1/validate_region_lk.py
```

[manifest.json](manifest.json) ghi input/output và bytes thay đổi. [validation.json](validation.json) ghi 532 ca CPU, 10 chữ ký DER giữ lại và 6 ca parser BL2. Các ca CPU dùng lệnh ARM64 thực, chạy cả mã cũ và mới, so sánh byte/chuỗi và epilogue thật. Các biên phần cứng, kết quả xác thực chain, trích khóa, đọc SKU và logging được giả lập; PAC được bỏ qua trong mô phỏng. Không phải kiểm thử toàn chuỗi boot hoặc ROM Global thật.

Giữ bản đã boot để quay lại; không ghi đè nó bằng bản thử. Checkpoint trước thay đổi: tag `before-lk-global-region-patch-20261103` trong repo private. Tài liệu kỹ thuật: [docs/08](../../docs/08-lk-rom-region.md).
