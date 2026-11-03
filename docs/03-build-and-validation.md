# 03 — Build và kiểm chứng

## Môi trường đã dùng

Windows/PowerShell, Python 3.12, Ghidra 12.1.2 tại `D:/Tools/ghidra`. Package hiện tại: capstone 5.0.9, unicorn 2.1.4, cryptography 50.0.0, pyelftools 0.33, lz4 4.4.5. Đây là phiên bản đã dùng, không phải kết luận phiên bản khác không chạy được.

Builder chỉ cần Python standard library; crypto/replay dùng các package tương ứng. Không cần decompile lại để build từ input và script đã giữ.

Chạy từ thư mục root repo. Không dùng Python `-O`: builder hiện dùng assert để chặn sai input/layout/prologue; `-O` tắt assert.

## Nhận diện file

```powershell
Get-FileHash -Algorithm SHA256 -LiteralPath .\fresh_analysis\output\lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img
```

Hash chuẩn: `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`.

## Build tái tạo

```powershell
python fresh_analysis/build_hybrid.py
```

Input: `lk_hyperos4_original/lk.img` và `lk_hyperos3_original/lk.img`, khóa đúng SHA256. Script tạo intermediate main H4 patch/prefix rồi thay trọn BL2 triple bằng H3. Đầu ra và build JSON ở `fresh_analysis/output`.

H3 patched chỉ là bằng chứng reverse; không phải donor toàn bộ ảnh. Sai SHA256 input phải phân tích lại build, không chỉ sửa hằng hash hoặc bỏ assert.

## Test cần thiết

```powershell
python fresh_analysis/validate_candidate.py
python fresh_analysis/validate_hybrid.py
python fresh_analysis/preloader/validate_candidate_parser.py
```

- `validate_candidate.py`: 328 ca actual main getter, wrappers/AVB và controls; intermediate tạo trong bộ nhớ.
- `validate_hybrid.py`: deterministic rebuild, byte identities, record layout, chữ ký và digest, gắn main/cert của đúng artifact cuối với code/snapshot đã test.
- `validate_candidate_parser.py`: 6 ca actual H3/H4 parser trên cert của hybrid cuối.

Các test cần extracted binaries và snapshot đã giữ trong `fresh_analysis`. Chúng không kết nối hoặc flash máy.

Các probe bổ sung nếu cần tái dựng điều tra:

```powershell
python fresh_analysis/container_chain/verify_container.py
python fresh_analysis/preloader/emulate_bl2_parser.py
python fresh_analysis/h3_diff/bl2_policy_emulation.py
```

Đọc mocks và scope trước khi diễn giải PASS. Test parse, signature math và whole-device boot là các bằng chứng khác nhau.

## Đầu ra bị loại và manifest lịch sử

`build_candidate.py` khi chạy độc lập tạo `fresh_analysis/rejected/lk_h4_main_only_REJECTED.img`: giữ BL2 H4, chỉ phục vụ đối chứng. Không nhầm file này với hybrid đã boot.

Builder không tự biết kết quả người dùng test sau đó. Build manifest/JSON test có thể ghi `NOT_HARDWARE_VERIFIED`; đó là scope tự động lúc build/probe. Kết quả máy thật nằm trong `output/runtime_confirmation.json` và docs04, lưu riêng để rebuild không xoá.

## Source phục hồi

- Main H4: `fresh_analysis/h4_pseudocode`, 2.328 hàm nhận diện.
- BL2 H4: `fresh_analysis/container_chain/bl2_decompile`, 1.526 hàm và focused loader exports.
- AEE H4: `fresh_analysis/h3_diff/aee_analysis/pseudocode`, 1.827 hàm.
- Preloader: focused export 220 hàm trong `fresh_analysis/preloader/decompile_h4`.

Có functions.tsv, database và log. Tổng 5.681 file native H4 không phải full source gốc, không chứng minh mọi entry tự nhận đúng. Carrier ELF chỉ phục vụ Ghidra, không nạp thay .img.

## Đóng gói

```powershell
python fresh_analysis/package_results.py
```

ZIP gồm root README, docs và fresh_analysis. Input firmware root đã được giữ trong Git; ZIP evidence không nhất thiết chứa toàn bộ input root, nên khi chuyển workspace cần clone repo private hoặc đặt lại đúng input.

Nếu chỉ sửa docs, không cần chạy lại toàn bộ CPU tests; kiểm link/tính nhất quán metadata, firmware hash và archive là đủ. Khi đổi firmware hoặc builder, chạy lại kiểm thử phù hợp và tạo commit/tag riêng trước khi thử máy.
