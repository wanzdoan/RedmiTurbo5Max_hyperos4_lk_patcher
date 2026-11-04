# Dựng lại bản LK dùng khóa token từ LK H3 gốc

Artifact: [lk_hyperos3_STOCK_TOKEN_KEY_MATCH_TEST.img](lk_hyperos3_STOCK_TOKEN_KEY_MATCH_TEST.img), **4.083.104 byte**.

SHA256: `69089ebb26e070c202213b19615cad5dcd9202a0ea35e521e4dce18a25599a90`.

Được build từ `lk_hyperos3_original/lk.img` SHA256 `e23e07cab7754cc99c9484765b9ec758b2c8d5110dfb3e0eb6009415719bf4cb`, không lấy bản H3 đã ép getter unlocked hoặc bản ENG làm nền. Bản mới **trùng từng byte** với container LK vừa sao lưu từ slot A của máy; không gồm phần đuôi toàn zero của phân vùng16MiB.

## Những gì builder thực sự làm

1. Lấy đúng modulus RSA2048,256 byte của khóa xác minh token từ bản sao LK đang dùng. Kiểm hash khóa và việc LK/AEE trong bản tham chiếu dùng cùng khóa.
2. Thay modulus tại payload main LK`0x1121b2` và payload AEE`0xd2464`. Giữ nguyên exponent65537 và toàn bộ mã kiểm tra chữ ký, token, mã chip/tên máy, getter lock.
3. Thêm một prefix hash90 byte trước DER của main cert2 và AEE cert2, với hash header/payload vừa cập nhật. Giữ nguyên tất cả DER có chữ ký của LK H3 gốc.
4. Giữ nguyên BL2, DTB/DTBO, certificate1, các record khác và padding. Tổng file tăng160 byte do căn từng record16 byte.

Kết quả không chỉ tương tự về cơ chế mà **trùng toàn bộ bytes và SHA256 với bản tham chiếu đang trên máy**. Điều này được xác nhận sau khi dựng từ stock; builder không sao chép toàn bộ ảnh tham chiếu làm output.

## Điều kiện unlock

Đây là bản **khớp khóa token hiện có**, không phải getter luôn trả unlocked. Không có private key của người tạo khóa, không tạo hay cài token mới, không đọc/ghi RPMB. Chép public key chỉ giúp LK chấp nhận chữ ký từ khóa riêng tương ứng; vẫn cần token đúng máy và hợp lệ. Không có xác nhận unlock trên một thiết bị khác.

Bản H3 gốc, bản tham chiếu16MiB, các bản patch H3/H4/ENG trước và thiết lập điện thoại đều giữ nguyên. Không flash trong tác vụ này.

## Kiểm chứng

[manifest.json](manifest.json) ghi input, offset và hash khóa. [validation.json](validation.json) xác nhận:

- Output trùng byte với container tham chiếu, build xác định và input sai hash bị từ chối.
- Toàn bộ mã máy giữ nguyên từ stock, chỉ đổi hai vùng modulus256 byte cùng hai record prefix.
- 10 DER chứng chỉ giữ nguyên và chữ ký RSA-PSS offline hợp lệ.
- 8 ca chạy parser BL2 thật: main/AEE, ba mask mỗi thành phần và hai control đổi prefix. Hash được chọn đúng, không có mock ASN.1/OID/kết quả xác thực; chỉ bypass PAC/logging.

Không có lần boot thử mới hoặc mô phỏng toàn chuỗi boot; việc trùng ảnh đọc từ máy không thay thế một thử nghiệm phần cứng riêng.

## Tái tạo

```powershell
python experiments/lk_h3_token_key_clone_v1/build_token_key_clone.py
python experiments/lk_h3_token_key_clone_v1/validate_token_key_clone.py
```

Input tham chiếu ở [input/reference_current_lk_container.img](input/reference_current_lk_container.img); [reference_source.json](input/reference_source.json) ghi hash phân vùng gốc16MiB và phần đuôi zero được bỏ trong bản sao lưu nhỏ này. Không sửa file readback gốc trong Downloads.

Checkpoint trước patch: `efda172`, tag`before-h3-token-key-clone-20261104` trong repo private. Các file preloader người dùng cung cấp đã được lưu cùng checkpoint, nguyên byte; không có patch preloader nào trong kết quả này.
