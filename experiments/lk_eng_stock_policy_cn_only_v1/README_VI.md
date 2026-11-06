# LK ENG gốc: stock-policy China, hai bản MTE

Theo yêu cầu ngày2026-11-06: lấy **đúng file ENG gốc**, giữ chính sách trạng thái khóa và HWC/kiểm vùng China gốc; không lấy Fenrir, fakelock hoặc bản đã mở Global làm nền.

| Biến thể | File | SHA256 |
|---|---|---|
| Bình thường, chính sách MTE gốc | [LK CHINA](lk_ENG_STOCK_POLICY_CHINA_ONLY_TEST.img) | `cfd5c47422590843371f2e2b119491ba13aebe800f0850a8214bf69d6010c7c9` |
| Ép tắt MTE phía kernel | [LK CHINA MTE_OFF](lk_ENG_STOCK_POLICY_CHINA_ONLY_MTE_OFF_TEST.img) | `acd35520ae9d2e0ac6511c6b28ece743793b24c240370b077dde6539e7f7c525` |

Mỗi file4.043.792 byte. [ZIP cả hai file](lk_eng_stock_policy_cn_only_two_files.zip) · [release_manifest.json](release_manifest.json). **Chưa flash hoặc boot test.**

## Đúng phạm vi China và stock-policy

Input là [LK ENG gốc](../lk_h3_eng_unlock_v1/input/lk_eng_original.img), SHA256 `6f386241b32f018e4f65459ede0364b2cd856a89bbf469dee241223b6557f71d`. ENG là tên quy ước của input debug/userdebug đã phân tích; không khẳng định build `eng` chính thức của hãng.

- Giữ nguyên getter đọc trạng thái lưu bền, setter, wrapper, AVB policy/flags, cache boot-state, fastboot report, root handoff và các đường maintenance gốc.
- Không ép unlocked3, locked4 hoặc green; không fake `flash.locked`, seccfg hay trạng thái public. Stock-policy không tự khóa lại máy đang unlocked.
- Không thêm patch HWC. Nó tiếp tục xuất **HWC phần cứng gốc: CN trên máy China**, không ép GLOBAL và không chọn HWC theo countrycode Global. Không ép một thiết bị phần cứng Global thành CN.
- Giữ nguyên nhánh kiểm vùng China trong `oem flash_check`, bao gồm các bypass factory/config và hành vi theo trạng thái unlocked đã có trong nguồn.
- Chỉ mở rộng callback khóa boot để nhận **China4096** bên cạnh khóa **ENG2048** gốc. Khóa **Global4096 không được thêm vào trust**, khóa lạ không trusted. Không phải cơ chế chặn tuyệt đối mọi ROM ngoài CN khi thiết bị đang unlocked hoặc có bypass cấu hình gốc.

Ảnh China đã sửa `init_boot` để root vẫn có thể bị từ chối khi trạng thái thật là locked và hash không khớp. Đây là stock-policy theo yêu cầu, không phải fakelock/root-allow như Fenrir. Không chạy lock, đọc/ghi RPMB, efuse hoặc flash thiết bị trong đợt này.

## Thay đổi byte

Callback gốc tại payload `0xc5888..0xc595f` được thay trong đúng216 byte, gồm code và fingerprint China32 byte bên trong vùng hàm. Không dùng vùng từ chối Global `0x68ec0` làm chỗ chứa digest như nhánh CN/Global cũ, vì nhánh đó vẫn phải giữ nguyên code/điều kiện.

Callback mới kiểm size/pointer; với ENG2048 so đúng modulus gốc; với4096 tính SHA256512 byte bằng hàm native `0xa4b20` rồi so duy nhất digest China:

`8a0ecaf46f057182eebfd3ce7f7ff04c7e97d9898e54a0df20ed3d9a0f9bef1e`.

Nguồn public key là `lk_hyperos3_original/lk.img`, SHAe23e07ca..., modulus CN tại payload `0x138638`. Không lấy private key hoặc dữ liệu định danh máy. Global digest không nằm trong callback mới. Xác thực chữ ký boot vẫn là code gốc; tin thêm khóa không sửa ảnh boot bị sai chữ ký/hash.

Bản MTE_OFF thêm đúng một lệnh tại payload `0x34d20`: `20020054` → `11000014`, luôn chọn nhánh suppress gốc xuất `arm64.nomte`. Bản bình thường giữ instruction/cờ MTE gốc, không cưỡng bức bật MTE. Off chỉ yêu cầu kernel tắt MTE, không đảo ngược hoạt động preloader đã chạy.

Main cert2 thêm prefix180 byte và cập nhật size/alignment; DER ký gốc giữ nguyên. Tăng tổng file176 byte so input. BL2, AEE, DTB/DTBO/certificates của chính ENG giữ nguyên byte. Mọi byte main ngoài callback và lệnh MTE tùy bản đều bằng input.

## Tái tạo và kiểm thử

```powershell
python experiments/lk_eng_stock_policy_cn_only_v1/assemble_callback.py
python experiments/lk_eng_stock_policy_cn_only_v1/build_cn_only.py
python experiments/lk_eng_stock_policy_cn_only_v1/validate_cn_only.py
python experiments/lk_eng_stock_policy_cn_only_v1/package_two.py
```

Không dùng Python `-O`. Builder buộc đúng hash nguồn và fingerprint China, kiểm các vùng HWC/flash-region/lock/AVB/report nguyên byte. Assembly không có relocation chưa xử lý, dài đúng216 byte. Build tái lập và input sai bị từ chối.

Mỗi file qua348 ca native:50key/corruption/size/null/hash-error,112flash-region giữ China,12predicate theo trạng thái gốc,147HWC gốc,24MTE,3parser BL2 ENG; cộng10 chữ ký DER kiểm offline. Các phép SHA256/memcmp và control flow chạy thật. Efuse, cache/query, formatter/log, MTE MMIO và getter trong control có nhãn được mô hình hóa; không phải boot toàn SoC hoặc đọc trạng thái máy thật.

`default_validation.json`/`mte_off_validation.json` tách riêng từng hash. Byte public reports giữ nguyên; MTE-off không thêm unlock/fakelock. Chưa chứng minh root, relock, OTA, Integrity hay tương thích mọi preloader.

Checkpoint đầu: `before-eng-stock-policy-china-only-20261106` tại `449f57a`. Commit đó cũng giữ việc người dùng đổi tên file H4 MTE-off thành `lk_hyperos4_MTE_OFF_TEST.img`; bytes/hash H4 không đổi. Các bản cũ được giữ lại.
