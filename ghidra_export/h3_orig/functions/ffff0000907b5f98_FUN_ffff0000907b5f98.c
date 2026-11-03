// ffff0000907b5f98  size=220  FUN_ffff0000907b5f98

undefined8 FUN_ffff0000907b5f98(int param_1)

{
  undefined8 uVar1;
  undefined *puVar2;
  int local_4;
  
  local_4 = 1;
  uVar1 = FUN_ffff000090788750(&local_4);
  if ((int)uVar1 != 0) {
    return uVar1;
  }
  if (param_1 == 0) {
    uVar1 = FUN_ffff000090788794(3);
    if ((int)uVar1 != 0) {
      return uVar1;
    }
    FUN_ffff000090709acc(s_secure_ffff0000907d2510,&DAT_ffff0000907f0b86);
    puVar2 = &DAT_ffff0000907df2ba;
  }
  else {
    if (local_4 == 1) {
      uVar1 = FUN_ffff000090788794(2);
      return uVar1;
    }
    uVar1 = FUN_ffff000090788794(4);
    if ((int)uVar1 != 0) {
      return uVar1;
    }
    FUN_ffff000090709acc(s_secure_ffff0000907d2510,&DAT_ffff0000907df2ba);
    puVar2 = &DAT_ffff0000907f0b86;
  }
  FUN_ffff000090709acc(s_unlocked_ffff0000907f5b3b,puVar2);
  FUN_ffff000090709acc(s_warranty_ffff0000907ebb3f,&DAT_ffff0000907f0b86);
  return 0;
}


