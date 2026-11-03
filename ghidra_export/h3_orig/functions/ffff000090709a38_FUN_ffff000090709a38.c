// ffff000090709a38  size=148  FUN_ffff000090709a38

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090709a38(undefined8 param_1,long param_2)

{
  long lVar1;
  long lVar2;
  undefined8 *puVar3;
  
  if (param_2 != 0) {
    lVar1 = FUN_ffff00009077ee2c();
    lVar2 = FUN_ffff00009077ee2c(param_2);
    puVar3 = (undefined8 *)FUN_ffff00009077d24c(0x18);
    if (((puVar3 == (undefined8 *)0x0) || (lVar1 == 0)) || (lVar2 == 0)) {
      if (puVar3 != (undefined8 *)0x0) {
        thunk_FUN_ffff0000907c2ad8();
      }
      if (lVar1 != 0) {
        thunk_FUN_ffff0000907c2ad8(lVar1);
      }
      if (lVar2 != 0) {
        thunk_FUN_ffff0000907c2ad8(lVar2);
        return;
      }
    }
    else {
      puVar3[1] = lVar1;
      puVar3[2] = lVar2;
      *puVar3 = _DAT_ffff00009083d168;
      _DAT_ffff00009083d168 = puVar3;
    }
  }
  return;
}


