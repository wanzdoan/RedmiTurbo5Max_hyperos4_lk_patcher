
bool FUN_ffff000090768c94(int param_1)

{
  bool bVar1;
  int local_4;
  
  if (param_1 == 0) {
    thunk_FUN_ffff000090798af8(&DAT_ffff0000907d0a72);
  }
  else if (param_1 == 1) {
    thunk_FUN_ffff000090798af8(s_recovery_ffff0000907e5ba4);
  }
  else {
    FUN_ffff00009077e11c(s__AVB__invalid_boot_image_type_ffff0000907e38c2);
  }
  local_4 = 4;
  FUN_ffff000090788a10(&local_4);
  bVar1 = local_4 != 3;
  FUN_ffff00009077e11c(s__AVB__img_auth_required____d_ffff0000907f53f0,bVar1);
  return bVar1;
}


