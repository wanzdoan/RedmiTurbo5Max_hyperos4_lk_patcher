// ffff0000907689cc  size=136  FUN_ffff0000907689cc

bool FUN_ffff0000907689cc(int param_1)

{
  bool bVar1;
  int local_4;
  
  if (param_1 == 0) {
    thunk_FUN_ffff000090798838(&DAT_ffff0000907d0a5b);
  }
  else if (param_1 == 1) {
    thunk_FUN_ffff000090798838(s_recovery_ffff0000907e5a79);
  }
  else {
    FUN_ffff00009077de54(s__AVB__invalid_boot_image_type_ffff0000907e37da);
  }
  local_4 = 4;
  FUN_ffff000090788750(&local_4);
  bVar1 = local_4 != 3;
  FUN_ffff00009077de54(s__AVB__img_auth_required____d_ffff0000907f526c,bVar1);
  return bVar1;
}


