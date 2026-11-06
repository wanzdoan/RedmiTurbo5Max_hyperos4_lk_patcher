# Chỉ mục docs

| Tài liệu | Nội dung |
|---|---|
| [01 — Logic và patch](01-logic-and-patch.md) | Chuỗi boot, cơ chế H4 mới, 16 byte ARM64, ASN.1 và hybrid |
| [02 — Input/layout](02-inputs-and-layout.md) | Hash input, 15 record, nguồn từng member và quy ước địa chỉ |
| [03 — Build/validation](03-build-and-validation.md) | Công cụ, lệnh tái tạo, scripts kiểm thử và đóng gói |
| [04 — Runtime/preloader](04-runtime-and-preloader.md) | ROM, hai xác nhận người dùng, phạm vi tương thích |
| [05 — Git/rollback](05-versioning-and-rollback.md) | Repo private, checkpoint, tags và khôi phục bản cũ |
| [06 — Evidence/giới hạn](06-evidence-and-limits.md) | Các test, mocks, báo cáo reverse và source phục hồi |
| [07 — Nghiên cứu 5G/NR](07-nr5g-investigation.md) | Policy, các phép thử đã hoàn nguyên, sự cố màn hình và bộ thử region riêng |
| [08 — Chốt vùng ROM trong LK](08-lk-rom-region.md) | Chốt CN/Global thật, bản thử LK riêng, kiểm thử CPU và giới hạn về 5G |
| [09 — Unlock LK ENG mới](09-h3-eng-unlock.md) | Input ngày04-11, getter riêng, BL2 ENG giữ nguyên và kiểm thử |
| [10 — Lock thật và OTA China](10-stock-relock-and-ota.md) | Đọc máy qua su, hash init_boot lệch, Secinfo hợp lệ và điều kiện còn thiếu trước lock |
| [Changelog](CHANGELOG.md) | Các mốc phát hiện, boot và versioning |

Ngày cập nhật: 2026-11-06. Tài liệu hiện tại ưu tiên bộ `docs`; các báo cáo reverse trong `fresh_analysis` lưu quá trình điều tra và phạm vi tại lúc chạy.

Quan sát binary, replay ARM64 có mô hình hóa, suy luận và báo cáo người dùng là những loại evidence riêng. Các JSON test ghi “no hardware execution” vẫn đúng đối với probe đó. [runtime_confirmation.json](../fresh_analysis/output/runtime_confirmation.json) bổ sung kết quả máy thật do người dùng xác nhận; không biến các probe cũ thành whole-device tests.

Repo private: [wanzdoan/RedmiTurbo5Max_hyperos4_lk_patcher](https://github.com/wanzdoan/RedmiTurbo5Max_hyperos4_lk_patcher). Không tiếp tục điều tra hoặc tự flash preloader sau yêu cầu dừng của người dùng.

Ngày 2026-11-05: [ENG unlocked/fakelock v2 + HWC tự chọn vùng](../experiments/lk_h3_eng_auto_hwc_v1/README_VI.md), hai artifact riêng, chưa boot test; nghiên cứu lock/OTA đã dừng theo yêu cầu.

Bộ cuối 2026-11-05: [6 file ENG all-region + MTE mặc định/off](../experiments/lk_h3_eng_mte_off_v1/README_VI.md). [Luồng preloader → LK → kernel MTE và evidence](../fresh_analysis/mte_research/README_VI.md). Mỗi nhánh unlocked/fakelock v2/stock-policy có hai file riêng, chưa boot test.

[11 — LK ENG và preloader H3/H4](11-eng-preloader-compatibility.md): yêu cầu mở lại ngày05-11; stock input đúng hash, policy/DFD, chứng chỉ, ma trận boot và giới hạn chưa kiểm chứng.

[Fenrir so với fakelock v2](../fresh_analysis/fenrir_review/README_VI.md): nhận diện nền H3 stock, toàn bộ diff main/AEE, certificate/parser, root handoff và seccfg; chưa xác nhận boot/TEE/Integrity.

[Hai hybrid Fenrir + v2, nền H3 stock/debug](../experiments/lk_fenrir_v2_hybrid_v1/README_VI.md): input tách riêng, root init_boot hash path, public locked/green/seccfg, HWC và guard bảo trì phiên bản; chưa boot test.

[Mới nhất: Fenrir gốc chỉ thêm HWC auto/MTE-off](../experiments/lk_fenrir_only_auto_hwc_mte_off_v1/README_VI.md). User báo hybrid trước không dùng được; bản mới chưa boot test và không kế thừa các patch thêm của hybrid.

[Audit lỗi POCO H3 và chốt CN/Global còn thiếu](../experiments/lk_fenrir_cn_global_gates_v2/README_VI.md): bản HWC-only8d616... bị user báo chỉ về preloader; bốn chốt vùng đã tái hiện/sửa offline, chưa boot test e6bd99c9....

[H4 hybrid đã boot f57 — biến thể riêng chỉ thêm MTE-off](../experiments/lk_h4_booted_hybrid_mte_off_v1/README_VI.md): source được xác nhận, output7873361e... chưa boot; không thêm HWC/chốt vùng/Fenrir/v2.

[ENG stock-policy China-only, hai bản MTE](../experiments/lk_eng_stock_policy_cn_only_v1/README_VI.md): từ ENG gốc6f386, giữ HWC/kiểm China gốc, thêm China4096 cạnh ENG2048, không Global; chưa boot test.

[H4 stock-policy theo nhánh stock_policy_auto_hwc trong ZIP H3](../experiments/lk_h4_eng_policy_auto_hwc_v1/README_VI.md): hai biến thể normal/MTE_OFF, định danh đúng bản tham chiếu, chức năng đã port và giới hạn preloader H4. Chưa flash/boot/lock máy.

[Native audit preloader H4 với H4 stock-policy mới và ENG H3](../fresh_analysis/h4_preloader_lk_acceptance_20261106/README_VI.md): kiểm locator/chứng chỉ/hash có đối chứng lỗi; chưa phải chấp nhận hoặc boot trên thiết bị.

[H4 ENG policy v2 — ứng viên sửa recovery khi locked](../experiments/lk_h4_eng_policy_recovery_fix_v2/README_VI.md): user báo v1 MTE_OFF vào recovery trên China/Global; native replay xác định gate Secure ID tại boot caller H4 khác ENG H3. V2 giữ riêng; người dùng sau đó báo vẫn vào recovery.

[Phân tích sau v2 vẫn recovery](../fresh_analysis/h4_recovery_v3_20261106/README_VI.md): AVB slot/loader native trên ROM China thật, cùng cmdline/packet dưới fixtures; chưa biết nguyên nhân máy thật. [V3 tách biến số bộ nạp/trạng thái khóa](../experiments/lk_h4_policy_bl2_control_v3/README_VI.md): main v2 giữ nguyên, đổi riêng signed BL2 về bộ của H4 f57 đã boot; diagnostic unlocked riêng. **Người dùng xác nhận Policy v3 MTE_OFF (`6282d02b…`) vào Android ngày 2026-11-06**; hai biến thể còn lại chưa có xác nhận boot.


[Integrity H3/H4 với cùng Policy H4 v3 MTE_OFF](../fresh_analysis/h4_integrity_20261106/README_VI.md): user báo banking H4 hoạt động, H3 đủ ba mức Integrity và H4 không có mức nào; H4 China không root, kiểm bằng app Play Integrity API Checker. Bốn ca sender native và đối chiếu stock; chưa có verdict/ADB/chứng thư thật, chưa có bản sửa được chứng minh.


[H4 fakelock v1 theo H3 ENG fakelock v2 mới nhất](../experiments/lk_h4_fakelock_v1/README_VI.md): một ảnh auto HWC/MTE_OFF riêng, dùng nền policy v3 đã boot và getter H4 unlocked; giữ BL2 H3 thương mại của v3. 426 ca main, chứng chỉ/parser, AVB/loader và ca init_boot lỗi hash được kiểm offline. Chưa boot ảnh mới; nhánh điều tra Integrity tạm dừng theo yêu cầu.


[Bộ hiện hành: H4 locked tự nhận vùng và unlocked hai chế độ MTE](../experiments/lk_h4_locked_unlocked_bundle_v1/README_VI.md): ba file giữ nguyên bytes từ nguồn, locked dùng v3 MTE_OFF đã boot; unlocked DEFAULT/OFF tách riêng. DEFAULT theo cấu hình gốc, không ép ON. Gác fakelock theo yêu cầu; giữ toàn bộ lịch sử.


Bộ locked/unlocked đã [bổ sung locked MTE_DEFAULT, đủ bốn file](../experiments/lk_h4_locked_unlocked_bundle_v1/README_VI.md), SHA8ac64a98… giữ nguyên v3 normal; chưa có xác nhận boot normal. Tổng174 ca native HWC/key/MTE; ZIP ba file cũ giữ nguyên.


[Bộ đúng yêu cầu sau sửa: cả locked/unlocked H4 đều all-region/auto HWC](../experiments/lk_h4_all_region_four_files_v2/README_VI.md). V2 bốn file, hai unlocked mới DEFAULT/OFF, hai locked giữ nguyên. Sửa thiếu sót unlocked v1 chưa có auto HWC. 632 ca main native và các kiểm DER/parser/preloader/AVB/loader; hai unlocked mới chưa được xác nhận boot.
