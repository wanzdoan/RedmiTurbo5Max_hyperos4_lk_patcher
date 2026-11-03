# 05 — GitHub private, checkpoint và quay lại bản cũ

## Repository

Repo: [wanzdoan/RedmiTurbo5Max_hyperos4_lk_patcher](https://github.com/wanzdoan/RedmiTurbo5Max_hyperos4_lk_patcher), visibility **PRIVATE**, được tạo bằng GitHub CLI theo yêu cầu người dùng trước khi sửa docs.

Checkpoint trước lần sửa này: **`134da6d`**, tag **`boot-working-before-docs-20261103`**. Commit giữ toàn workspace lúc đó, gồm artifact đã boot, input, source/scripts, báo cáo, database, các bản cũ và ZIP. Chỉ bỏ cache Python và active tool locks.

Phần cập nhật docs có commit riêng và tag `docs-confirmed-boot-20261103`. Xem hash thực tế của tag trong Git để không phụ thuộc một hash được chép cứng vào chính commit đang tạo.

```powershell
git log --oneline --decorate -8
git show --no-patch boot-working-before-docs-20261103
git show --no-patch docs-confirmed-boot-20261103
gh repo view --json name,visibility,url
```

## Lấy lại bản trước mà không đổi workspace hiện tại

Từ root repo:

```powershell
git archive --format=zip --output=../lk-before-docs-20261103.zip boot-working-before-docs-20261103
```

ZIP chứa snapshot Git ở checkpoint. Lệnh không flash thiết bị, không sửa workspace hiện tại và không phụ thuộc cách PowerShell redirect binary. Không dùng `git show ... > file.img` trên shell chưa xác lập binary-safe redirection để lấy firmware.

Muốn làm việc tiếp từ checkpoint bằng nhánh riêng, trước hết lưu/commit mọi thay đổi hiện tại và kiểm tra working tree sạch:

```powershell
git status --short
git switch -c restore-working-20261103 boot-working-before-docs-20261103
```

Không cần hard reset hoặc force push để quay về mã cũ. Nếu tên nhánh đã tồn tại, chọn tên mới. Sau khi trích firmware từ bản cũ, đối chiếu SHA256 trong manifest của chính bản đó. Khôi phục repo chỉ khôi phục file PC; không tự khôi phục partition điện thoại.

## Quy trình các lần sửa tiếp theo

1. Xem branch, remote và working tree. Bảo đảm trạng thái trước sửa đã có commit/được push vào repo private; nếu có thay đổi chưa lưu, tạo checkpoint riêng trước.
2. Với thay đổi firmware hoặc logic patch, đặt tag checkpoint trước khi sửa. Không amend hoặc rewrite các commit đã push dùng làm mốc đã boot.
3. Sửa phạm vi cụ thể, kiểm thử phù hợp, ghi rõ source input/output hashes và tác động.
4. Commit thay đổi thành mốc mới, push và thêm tag cho bản được người dùng xác nhận hoạt động.
5. Giữ runtime confirmation riêng theo artifact SHA256; không đổi firmware dưới cùng mô tả/hash cũ.

Hướng dẫn cho agent các lần sau cũng nằm ở [AGENTS.md](../AGENTS.md). Repo chứa các bản lịch sử từng sai; việc chúng được lưu không làm chúng trở thành artifact được khuyến nghị. Dùng root README/docs và đúng commit/tag để nhận diện bản hiện hành.
