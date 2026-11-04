# Nghiên cứu khóa thật và OTA China — 2026-11-04

**Dừng theo yêu cầu người dùng ngày2026-11-05.** Giữ lại kết quả chỉ đọc; không tiếp tục thu thập thiết bị hoặc thử chuyển trạng thái.

**Chưa có quy trình khóa thật đã được chứng minh an toàn cho máy này. Không tạo file LK “lock thật” để che vấn đề còn lại.** Đợt này chỉ thu thập qua ADB/su hiện có và phân tích offline; không flash, reboot, erase, chạy lock hoặc gọi trực tiếp RPMB/efuse.

Người dùng chọn chứng minh China trước. Ca máy bạn của người dùng đã brick ngay khi thay LK stock rồi reboot, kể cả không chạy lock; ROM là China H3 nhưng không còn đúng bộ ảnh/hash/log của lần lỗi. Vì vậy không quy nguyên nhân ca đó từ dữ liệu máy hiện tại. Xem [user_case.json](user_case.json).

## Kết quả trực tiếp trên máy hiện tại

| Mục | Quan sát và ý nghĩa |
|---|---|
| ROM/slot | China `OS4.0.3.0.XPLCNXM`, slot A |
| LK hai slot | Cả hai container đúng hash`69089ebb...99a90`, trùng bản token-key clone; toàn bộ hai phân vùng còn trùng readback cũ, tail zero |
| Trạng thái thực xuất lúc boot | Kernel bootconfig`unlocked/orange`;37 lần getter trong log báo state3, magic PASS, signature length256 |
| Properties Android | `locked/green/flash.locked=1`, trái bootconfig/log; không dùng làm chứng cứ lock thật trong môi trường root+ẩn hiện tại |
| `secinfo` dự phòng | Header thật của stock khớp; chữ ký P-256/SHA256 hợp lệ bằng public key stock H3/H4; cả3 ràng buộc khớp bootconfig của LK |
| Bộ ảnh boot |4 metadata có chữ ký hợp lệ, root key China đã biết, flags0. Trong6 hash descriptor đã đọc,5 khớp và **init_boot_a không khớp** |
| Bản init_boot sạch | File H4 China trên host khớp descriptor được ký; toàn phân vùng init_boot_b cũng trùng nguyên byte với file gốc đó |
| Preloader | Hai boot LUN4MiB được đọc nguyên byte và trùng nhau. Không khớp nguyên file hoặc các cửa sổ code đã thử với các preloader tham chiếu hiện có; chưa gán nó là ENG/stock build nào |

Nguồn: [snapshot](live_readonly_snapshot.json), [log đã lọc](live_boot_log_selected.json), [kiểm secinfo](live_secinfo_validation.json), [AVB](live_avb_audit.json), [preloader](live_preloader_identity.json).

`init_boot_a` được kiểm bằng hash **salt + đúng số byte ảnh được descriptor ký yêu cầu**, không phải so nhầm hash cả phân vùng với ảnh ngắn. Hash mong đợi`6dd38f29...a8d531`, hash đọc thực`81f5c4c0...94bbb82`. File gốc trên host SHA256`3d2062ae...622b70a` khớp. Đây là điểm chưa đạt xác thực locked trên máy đang root, chưa phải bằng chứng nguyên nhân brick của máy khác.

Hồ sơ `secinfo` không bị thiếu/hỏng theo các kiểm tra đã làm. Literal thật là `SECINFO-BOOT!`; label Ghidra `SECINFO_BOOT` đã thay dấu câu bằng underscore và không được dùng làm literal. Record tại`0x40f`, data TLV400 byte, signed414 byte, signature64 byte. Tag0x41/0x43/0x42 khớp `cpuid/hwc/hwlevel` trong bootconfig. Chỉ ghi số byte và kết quả boolean, không đưa các giá trị nhận diện vào repo.

**Primary Secure ID trong RPMB còn chưa đọc.** Code stock chỉ dùng partition dự phòng khi bước đọc/format/xác thực primary lỗi. Primary có chữ ký hợp lệ nhưng ràng buộc sai có thể làm thất bại so hardware mà không thử fallback. Không suy từ fallback hợp lệ thành đã chắc toàn bộ gate pass. Đường đọc RPMB yêu cầu UFS security request/response, nonce/HMAC và đôi khi secure-monitor; chưa có Linux API chỉ đọc được kiểm chứng để dùng. Không thử ioctl/SMC hoặc lệnh RPMB tùy ý.

## Mã gốc thực hiện khóa như thế nào?

[152 ca CPU](stock_lock_replay.json) chạy getter/setter thực của H3 stock, H4 stock, ENG; thêm control từ token clone và các bản force getter:

- Getter native trả state3 khi xác minh token thành công, state4 khi token sai/không có hoặc nhiều lỗi đọc/format. Các nhánh này return0; không có panic chỉ vì token sai.
- Force getter luôn trả3 và bỏ toàn bộ đường đọc/khởi tạo. **Getter gốc có side effect:** magic thiếu/sai có thể gọi writer để khởi tạo RPMB. Vì vậy một truy vấn gọi trực tiếp getter không được mặc định là chỉ đọc.
- Lệnh khóa H3 chuẩn chuẩn bị descriptor chữ ký literal`do nothing`,10 byte; setter chấp nhận requested state3/4 nhưng dữ liệu ghi được chọn theo descriptor, không ghi trực tiếp số4. Với descriptor này, native setter gọi writer ghi length10 rồi10 byte sentinel; getter kế tiếp coi đó không phải unlock signature hợp lệ và trả4.
- Lệnh chuẩn còn có đường yêu cầu recovery xóa userdata. Một dòng báo thành công khi getter đã bị force3 không chứng minh khóa thật.
- Token hiện tại dùng public key khác stock: stock`c21c6c4f...`, token clone`a541cc0c...`. Không có private key tương ứng trong dự án, và chưa thu được đúng chữ ký256 byte hiện lưu để sao lưu/xác minh dưới key stock. **Thực hiện lock có thể ghi đè token đang dùng; file LK backup không phải backup token RPMB.**

Các writer trong mô phỏng là **journal RAM trên host**; không có lệnh ghi thiết bị. Token-verification RESULT được mô hình hóa trong các test trạng thái, không phải kiểm chữ ký token thật của người dùng. Các test virtual chỉ xác nhận luồng mã và tham số.

Code nguồn phục hồi chính: H3 [getter](../../ghidra_export/h3_orig/functions/ffff00009078982c_FUN_ffff00009078982c.c), [setter](../../ghidra_export/h3_orig/functions/ffff000090789b80_FUN_ffff000090789b80.c), [lock command](../../ghidra_export/h3_orig/functions/ffff00009070c610_FUN_ffff00009070c610.c), [Secure ID](../../ghidra_export/h3_orig/functions/ffff0000907868c4_FUN_ffff0000907868c4.c).

## Điều đã khoanh vùng và điều chưa được chứng minh

| Giả thuyết | Đánh giá |
|---|---|
| Token sai làm getter panic ngay | Không phù hợp với getter đã chạy mô phỏng: nó trả locked4/success0 |
| Thay LK stock tự làm thay chính sách dù không lock | Có: nếu token chỉ hợp key custom hoặc không có signature hợp lệ, stock chuyển sang đường locked |
| Chỉ cần đổi báo locked để OTA thoải mái | Không giải quyết việc LK gốc nhận dữ liệu/ảnh sau khi bị OTA thay lại |
| Máy hiện tại bị hỏng `secinfo` fallback | Không phù hợp: signature và cả3 binding đều đạt |
| Chỉ cần fallback hợp lệ là lock chắc chắn an toàn | Chưa đủ: primary RPMB, toàn chuỗi xác thực/rollback và boot thật chưa kiểm chứng |
| Các ảnh boot hiện tại đều stock sạch | Sai đối với slot A: init_boot_a lệch hash ký; những5 descriptor khác đã kiểm đạt |
| Friend brick do đúng init_boot_a đang lệch trên máy này | Không có dữ liệu để suy như vậy |
| ENG preloader bảo đảm cứu máy | Không có cơ sở: các policy đã phân tích vẫn có auth; preloader thực tế hiện tại chưa xác định đúng build |

Trên các input H3 đã biết, BL2 header/payload và cert1 của stock/force/token cùng mã; tuple stock/token trùng nguyên byte. BL2 cert2 force khác trong phần signature RSA-PSS nhưng chữ ký offline vẫn hợp lệ. Không phát hiện một thay đổi mã BL2 trong các cặp này đủ giải thích ca brick; chưa biết input bạn của người dùng có bằng các cặp này không. [Audit](input_audit.json).

LK stock còn có nhánh điều kiện lập trình efuse khi signed property`com.android.build.vbmeta.miep.activated` bật và các bit phần cứng phù hợp. Property đó không thấy trong hai root vbmeta có sẵn đã parse; không quy ca brick cho efuse từ việc có một nhánh code. Không thực hiện nhánh này trên máy.

## Điều kiện để tiến tới mục tiêu OTA stock

1. Xác minh bộ ảnh stock China đúng build/máy và toàn chuỗi xác thực, không chỉ root key. Đã tìm được init_boot gốc đúng descriptor, **chưa ghi nó**. Dữ liệu hashtree system/vendor, bộ ảnh slot B đầy đủ, rollback giữ trong bảo mật và preloader actual vẫn chưa hoàn tất kiểm chứng.
2. Đánh giá đường locked gốc cùng dữ liệu máy, không ép trạng thái và không fake green. Partition Secure ID đã đạt; primary RPMB và khả năng bảo toàn/khôi phục token trước khi chuyển trạng thái chưa có bằng chứng đủ.
3. Chỉ sau khi các điều kiện trước đạt mới có cơ sở bàn việc chuyển trạng thái và thử boot stock có kiểm soát. Thành công phải gắn hash LK/ảnh thật, bootconfig/log thật và khả năng recovery; chưa có bước đó ở đợt này.
4. Kiểm gói OTA đích thực sự và chuỗi slot sau update. Theo [AOSP](https://source.android.com/docs/core/ota/ab/ab_implement), danh sách partition trong A/B OTA do build cấu hình; chưa có payload OTA của ca này để phân tích. Không tuyên bố mọi OTA đều ghi LK, hoặc rằng đổi mỗi lock flag sẽ chữa mọi lỗi cập nhật. [Định nghĩa locked](https://source.android.com/docs/security/features/verifiedboot/device-state) yêu cầu OS qua root of trust.

Đây là điều kiện/nghiên cứu, **không phải hướng dẫn flash/lock đã được chấp thuận**. Người dùng chỉ cho đọc thông tin. Không tạo script chuyển trạng thái, bỏ LK khỏi OTA hoặc chỉnh chữ ký cập nhật.

## Bảo tồn dữ liệu và tái tạo

Raw LK hai slot, secinfo, seccfg prefix, bootconfig, log và ảnh boot cần thiết được giữ ngoài Git trong đường dẫn chỉ ghi ở snapshot. Raw có thể chứa dữ liệu riêng của thiết bị. Repo chỉ có script, hash, độ dài, booleans và log trạng thái đã lọc. Không đăng raw CPUID/token/signature/provisioning hoặc dùng dữ liệu một máy thay cho máy khác.

```powershell
# Hoàn toàn offline, không chạm thiết bị:
python fresh_analysis/relock_ota_research/replay_stock_lock.py
python fresh_analysis/relock_ota_research/replay_stock_region.py
python fresh_analysis/relock_ota_research/audit_inputs.py
python fresh_analysis/relock_ota_research/check_secinfo_private.py
python fresh_analysis/relock_ota_research/check_live_avb.py --offline
python fresh_analysis/relock_ota_research/inspect_private_boot_log.py
```

`collect_readonly.py` và `check_live_avb.py` không có`--offline` sẽ đọc thiết bị bằng su đã có; chỉ dùng đúng phạm vi user cho phép. Không reboot hay gọi LK getter. Checkpoint trước nghiên cứu đã push: `before-stock-relock-ota-research-20261104`, commit`e3e27af`.
