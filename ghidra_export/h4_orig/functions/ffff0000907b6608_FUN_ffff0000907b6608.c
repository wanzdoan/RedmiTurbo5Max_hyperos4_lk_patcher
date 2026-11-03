// ffff0000907b6608  size=224  FUN_ffff0000907b6608

undefined8 FUN_ffff0000907b6608(int param_1)

{
  int iVar1;
  undefined8 uVar2;
  undefined *puVar3;
  int local_4;
  
  local_4 = 1;
  iVar1 = FUN_ffff000090788a10(&local_4);
  if (iVar1 != 0) {
    local_4 = 4;
  }
  if (param_1 == 0) {
    uVar2 = FUN_ffff000090788a54(3);
    if ((int)uVar2 != 0) {
      return uVar2;
    }
    FUN_ffff000090709a58(s_secure_ffff0000907d254d,&DAT_ffff0000907f0cf7);
    puVar3 = &DAT_ffff0000907df35d;
  }
  else {
    if (local_4 == 1) {
      uVar2 = FUN_ffff000090788a54(2);
      return uVar2;
    }
    uVar2 = FUN_ffff000090788a54(4);
    if ((int)uVar2 != 0) {
      return uVar2;
    }
    FUN_ffff000090709a58(s_secure_ffff0000907d254d,&DAT_ffff0000907df35d);
    puVar3 = &DAT_ffff0000907f0cf7;
  }
  FUN_ffff000090709a58(s_unlocked_ffff0000907f5cb2,puVar3);
  FUN_ffff000090709a58(s_warranty_ffff0000907ebcc2,&DAT_ffff0000907f0cf7);
  return 0;
}


