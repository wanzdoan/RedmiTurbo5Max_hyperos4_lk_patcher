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

## ENG unlocked/fakelock v2 với HWC tự chọn vùng (2026-11-05)

Quy trình, hashes và giới hạn nằm trong [README biến thể](../experiments/lk_h3_eng_auto_hwc_v1/README_VI.md). Chạy `assemble_auto.py`, `build_variants.py`, `validate_variants.py` trong thư mục đó; không chạy lại builder artifact H4 để kiểm các biến thể ENG này. Kết quả main ARM64: unlocked486, fakelockv2 599; mỗi ảnh thêm10 chữ ký DER và3 parser BL2. Chưa boot test.

## Sáu file ENG all-region và biến thể MTE-off (2026-11-05)

Chạy `build_mte_off.py`, `validate_mte_off.py`, `package_six.py` tại `experiments/lk_h3_eng_mte_off_v1`; xem [docs bộ sáu file](../experiments/lk_h3_eng_mte_off_v1/README_VI.md). Kiểm final bytes: unlocked534/fakelockv2647/stock-policy208 ca main, mỗi ảnh10DER+3parser. Bộ probe MTE nền ở `fresh_analysis/mte_research/probe_mte.py` có48 ca LK và168 ca preloader; không cần raw boot-LUN riêng để chạy các test ba file off, nhưng raw đó cần cho replay preloader nền. Gói ZIP chứa đúng6 ảnh, SHA256SUMS và manifest; package script kiểm CRC/hash và tạo ZIP tái lập. Không flash/boot test.

## H4 stock-policy auto HWC dùng bộ nạp ENG H3 (2026-11-06)

Xem [quy trình và giới hạn](../experiments/lk_h4_eng_policy_auto_hwc_v1/README_VI.md). Chạy `build_port.py`, `validate_port.py`, `validate_behavior.py`, `package_release.py` trong thư mục thử nghiệm đó. Mỗi ảnh có390 trường hợp main có đối chứng/fixture,3parser ENG và10DER; normal và MTE_OFF có report/hash riêng. Không chạy builder H4 force-unlock cũ để tái tạo nhánh này. Đối chiếu getter/setter native không phải thực hiện relock máy; chưa xác nhận preloader H4 hoặc boot thực tế.

## H4 recovery-fix v2 (2026-11-06)

[Quy trình](../experiments/lk_h4_eng_policy_recovery_fix_v2/README_VI.md): `build_fix.py`, `validate_fix.py`, `validate_regression.py`, `package_release.py` tại thư mục v2. Regression dùng API probes v1 với bytes v2, ghi report v2 riêng; không ghi đè artifacts/probes cũ. Gate Secure ID bị bỏ ở một boot call site, verifier gốc kiểm riêng vẫn có kết quả/lỗi như trước. Mocks không phải trạng thái/token/provisioning máy thật.

## V3: đối chứng signed BL2 và unlocked (2026-11-06)

[Quy trình](../experiments/lk_h4_policy_bl2_control_v3/README_VI.md): `build_controls.py`, `validate_controls.py`, `package_release.py`. Hai policy giữ nguyên main v2, do đó main CPU evidence kế thừa qua byte identity; chạy mới parser/DER/preloader và AVB/loader với đúng ảnh cuối. Unlocked control chạy getter force3 thật, tách rõ khỏi policy. Whole AVB replay/negative controls ở `fresh_analysis/h4_recovery_v3_20261106/compare_clean_rom.py`; fixtures không thay thế dữ liệu bảo mật/boot trên máy.


## H4 fakelock v1 trên nền policy v3 đã boot (2026-11-06)

[Quy trình và scope](../experiments/lk_h4_fakelock_v1/README_VI.md): chạy `build_fakelock_h4.py`, `validate_fakelock_h4.py`, `validate_boot_policy.py`, `package_release.py` trong thư mục `experiments/lk_h4_fakelock_v1`. Builder khóa hash cả ba nguồn, giữ signed BL2/AEE/DTBs và DER, chỉ đổi main/prefix. Report chính có 402 ca main (398 H4 + 4 tham chiếu H3); report boot-policy có 24 ca caller bổ sung, tổng426; 10DER/3parser, preloader native boundary và native AVB/loader clean + corrupted init_boot. Lỗi hash vẫn return3, không gọi là verify thành công. ROM/hardware/secure-world fixtures và kết quả máy thật được tách rõ. Không chạy builder cũ để tạo ảnh mới hoặc ghi đè ảnh đã boot.


## Bộ locked/unlocked H4 ba file (2026-11-06)

[Thư mục và nguồn](../experiments/lk_h4_locked_unlocked_bundle_v1/README_VI.md): chạy `build_bundle.py`, `validate_selection.py`, `package_release.py`. Chỉ chọn/copy đúng hash nguồn, không patch lại. 150 ca mục tiêu gồm72HWC/6key/72MTE; mọi DER/parser/AVB từ nguồn áp dụng qua exact byte identity. Kiểm ZIP CRC/hash/tái lập, hashes các ảnh nguồn vẫn nguyên. Không cần chạy lại toàn bộ main/AVB cho một bộ sao chép nguyên byte.


Bổ sung locked MTE_DEFAULT vào bộ trên: cùng lệnh tái tạo tạo **bốn file**, `package_release.py` xuất `lk_h4_locked_unlocked_four_files.zip` và giữ ZIP ba file cũ. Kiểm174 ca (72HWC/6key/96MTE); so cả cặp locked và unlocked chỉ khác nhánh MTE/prefix, khóa nguồn v3 normal bằng SHA8ac64a98…. Không có firmware patch mới hoặc lượt boot mới.
