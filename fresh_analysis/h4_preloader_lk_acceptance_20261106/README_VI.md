# Preloader H4 với LK H4 mới và ENG H3 — kiểm offline06-11

**Các bước xác thực đã kiểm offline đều qua cho hai LK H4 mới và nhánh ENG H3. Chưa xác nhận preloader thực trên điện thoại chấp nhận hoặc boot được toàn máy.** Không flash, lock, reboot, erase, đọc fuse/RPMB hoặc thay bytes firmware trong đợt này.

Đúng input người dùng yêu cầu: `preloader_hyperos4/preloader_dash.bin`,1064924byte, SHA256`104bb972383b39771ec5bcc4bc79207244306de1d40f370ed2ab3863fb7ceb77`. Nó trùng stock trong thư mục `images` của ROM H4 đã đối chiếu trước đó.

## Kết quả theo ảnh

| LK | BL2 | Locator | Cert2 và hai hash BL2 | Parser hash main |
|---|---|---|---|---|
| H4 unlocked đã được user báo boot, `f57d19c9...` | H3 thương mại | Đạt | Đạt offline | Đạt |
| H3 ENG gốc, `6f386241...` | ENG H3 | Đạt | Đạt offline | Đạt |
| H3 stock-policy auto HWC, `fe60552c...` | ENG H3 | Đạt | Đạt offline | Đạt |
| H3 stock-policy auto HWC MTE_OFF, `b2535248...` | ENG H3 | Đạt | Đạt offline | Đạt |
| H4 stock-policy auto HWC mới, `da05dc16...` | ENG H3 | Đạt | Đạt offline | Đạt |
| H4 stock-policy auto HWC MTE_OFF mới, `4206c420...` | ENG H3 | Đạt | Đạt offline | Đạt |
| H4 gốc đối chứng, `82b4e1f2...` | H4 thương mại | Đạt | Đạt offline | Đạt |

Full hashes và kết quả trong [evidence.json](evidence.json). Bảng chỉ mô tả những thành phần đã chạy dưới đây, không phải toàn generic loader hoặc chứng thực của thiết bị.

Hai H4 mới và hai H3 stock-policy có **nguyên byte cùng bộ BL2/cert1/cert2** với ENG gốc: SHA256`d38558bd370dfa3e0240fc3bee381760941bc5f52116d17a2346527654bcc77c`. Bộ của H4 unlocked đã boot là`c3f0595fcc4a0b52ea70a4fce8e368ded61025c9b85788be3f718f16953a00c6`, khác ENG.

Locator native tìm BL2 ở`0x13acb0` trong hai H4 mới/H4 unlocked, và`0x132950` trong ENG gốc. Nó đọc header từng container, không giả định offset cố định.

## Thực sự kiểm những gì?

1. **Locator preloader gốc raw`0x7f424`:** đọc header/magic/tên/alignment, trả đúng offset. Storage chỉ đọc file host. Một ảnh sai magic bị từ chối.
2. **Cert1 và binding:** toán học host kiểm chữ ký RSA-PSS cert1/cert2; SPKI image do cert1 ràng buộc phải trùng cert2. Root SPKI của mọi input giống tham chiếu H4 unlocked đã boot. Chưa đọc root phần cứng.
3. **Coordinator cert2 gốc raw`0xa6508`:** thực thi ASN.1/trích khóa`0xa5c68`, so khóa`0xa6760`, chọn TBS`0xa6100`, kiểm PSS`0x9c700` và vòng MGF bằng ARM64 của đúng preloader H4. Không mock key equality hoặc ép signature-valid. Khóa tin cậy được cấp từ cert1 đã kiểm offline; đường thu thập khóa thiết bị chưa chạy đầy đủ.
4. **Backend mật mã:** SHA256 dùng `hashlib`; RSA public exponentiation dùng `pow(signature,65537,modulus)`. Đảo byte, kiểm PSS/MGF và so digest cuối chạy native. Heap, readiness, mode RSA2048/SHA256 và guard là fixtures; PAC/logging bỏ qua. Không gọi crypto hardware.
5. **Nội dung BL2:** helper gốc`0xa4d6c` căn payload16byte/tính hash; comparator gốc`0xa0f58` so với header/payload digest lấy từ cert2 hợp lệ. Đây là gọi trực tiếp thành phần kiểm hash; chưa chạy toàn wrapper nạp/xác thực ảnh.
6. **BL2→main:** chạy parser đúng họ ENG/H3/H4 với ba mask; hash main/header được chọn phải khớp ảnh. Chưa chạy đầy đủ signature/load main của BL2.
7. **Policy:**48ca accessor native với SBC/state/lỗi query là fixtures; có auth-required0/1. Không suy fixture thành trạng thái/fuse thực trên máy.

Ánh xạ preloader dùng **linked VA = raw offset + `0x02000f00`**, không dùng địa chỉ carrier lịch sử dịch`-0x1000`. Giữ indirect pointers trong firmware, chỉ seed guard/mode/size tại pointee.

## Đối chứng lỗi

Ba họ BL2 riêng biệt, mỗi họ thử sai chữ ký cert2, sửa signed digest trong TBS, nhầm khóa tin cậy, sai magic cert2, sửa header, sửa payload, sai chữ ký cert1. **21ca phát hiện lỗi đúng ranh giới đã kiểm**, cộng một locator sai magic.

Sửa header/payload vẫn cho cert2 return0 vì certificate không đổi; comparator hash sau đó phải mismatch và đã mismatch. Không gọi đó là ảnh xác thực thành công. Chữ ký cert1 được kiểm bằng host; không quảng cáo là chạy native toàn chuỗi cert1.

Tổng:7positive cert2 native,7cặp hash,8locator (gồm1âm tính),21parser BL2,21đối chứng lỗi và48policy. JSON giữ error gốc của ca âm tính.

## Kết luận và giới hạn

**Chưa thấy chặn vì ENG hoặc mismatch chứng chỉ/hash/layout ở những bước preloader H4 đã thực thi.** Đây là bằng chứng mạnh hơn chỉ kiểm DER hoặc parser trích field, nhưng chưa đủ nói flash preloader H4 chắc chắn không brick.

Chưa đo root fuse, SBC/state thật, primary provisioning, counter chống hạ phiên bản; chưa chạy toàn generic loader/rollback, nạp vào RAM và bàn giao phần cứng. Main H3 có thể vẫn không tương thích cấu hình H4 dù BL2 qua xác thực. Không nâng kết quả H3 ENG thành boot với preloader H4.

Xác nhận boot H4 unlocked trước đây chỉ thuộc ảnh`f57d19c9...`. Hai H4 stock-policy mới chưa có xác nhận boot/lock/banking/Integrity hoặc stock preloader H4 trên thiết bị. Không flash trong tác vụ này.

Tái tạo: `python fresh_analysis/h4_preloader_lk_acceptance_20261106/audit.py`. Không dùng Python`-O`. Script khóa SHA input, ghi report riêng, không gọi ADB/fastboot/builder. Checkpoint đã push:`before-h4-preloader-eng-lk-check-20261106` tại`9052835`; repo private.
