
/* ensure_path_mounted_at(std::string const&, std::string const&) */

int ensure_path_mounted_at(string *param_1,string *param_2)

{
  uint uVar1;

  uVar1 = android::fs_mgr::EnsurePathMounted((vector *)&DAT_0057bcf0,param_1,param_2);
  return (uVar1 & 1) - 1;
}
