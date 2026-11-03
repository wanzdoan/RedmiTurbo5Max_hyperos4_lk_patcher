// ffff0000907362b0  size=72  FUN_ffff0000907362b0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907362b0(long param_1)

{
  undefined8 uVar1;
  
  if (((param_1 != 0) && (*(code **)(param_1 + 0x10) != (code *)0x0)) &&
     (*(long *)(param_1 + 0x20) != 0)) {
    if (*(char *)(param_1 + 8) == '\x01') {
      _DAT_ffff00009086dbd8 = &DAT_ffff000090804acc;
      _DAT_ffff00009086dbd0 = param_1;
      (**(code **)(param_1 + 0x10))(DAT_ffff000090804ad4,param_1 + 0x30);
      return 0;
    }
    if (*(char *)(param_1 + 8) == '\x02') {
      uVar1 = FUN_ffff0000907362f8();
      return uVar1;
    }
  }
  return 0xfffffff8;
}


