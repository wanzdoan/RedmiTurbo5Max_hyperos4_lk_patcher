
bool FUN_ffff00009076979c(int param_1)

{
  bool bVar1;
  int local_4;

  if (param_1 == 0) {
    thunk_FUN_ffff000090798c64(&DAT_ffff0000907c8ac4);
  }
  else if (param_1 == 1) {
    thunk_FUN_ffff000090798c64(s_recovery_ffff0000907de253);
  }
  else {
    FUN_ffff00009077ea84(s__AVB__invalid_boot_image_type_ffff0000907dbff2);
  }
  local_4 = 4;
  FUN_ffff0000907885fc(&local_4);
  bVar1 = local_4 != 3;
  FUN_ffff00009077ea84(s__AVB__img_auth_required____d_ffff0000907ee0f7,bVar1);
  return bVar1;
}
