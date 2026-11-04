
undefined8 FUN_ffff0000907b65cc(int param_1)

{
  undefined8 uVar1;
  undefined *puVar2;
  int local_4;

  local_4 = 1;
  uVar1 = FUN_ffff0000907885fc(&local_4);
  if ((int)uVar1 != 0) {
    return uVar1;
  }
  if (param_1 == 0) {
    uVar1 = FUN_ffff000090788640(3);
    if ((int)uVar1 != 0) {
      return uVar1;
    }
    FUN_ffff000090709b68(s_secure_ffff0000907ca63f,&DAT_ffff0000907e9853);
    puVar2 = &DAT_ffff0000907d78b3;
  }
  else {
    if (local_4 == 1) {
      uVar1 = FUN_ffff000090788640(2);
      return uVar1;
    }
    uVar1 = FUN_ffff000090788640(4);
    if ((int)uVar1 != 0) {
      return uVar1;
    }
    FUN_ffff000090709b68(s_secure_ffff0000907ca63f,&DAT_ffff0000907d78b3);
    puVar2 = &DAT_ffff0000907e9853;
  }
  FUN_ffff000090709b68(s_unlocked_ffff0000907ee940,puVar2);
  FUN_ffff000090709b68(s_warranty_ffff0000907e47fd,&DAT_ffff0000907e9853);
  return 0;
}
