// ffff0000907996e8  size=352  FUN_ffff0000907996e8

int FUN_ffff0000907996e8
              (int param_1,undefined *param_2,long param_3,undefined8 param_4,undefined8 param_5,
              undefined8 param_6,undefined8 param_7,undefined8 param_8,undefined8 param_9)

{
  int iVar1;
  char *pcVar2;
  long lVar3;
  undefined8 local_68;
  
  iVar1 = FUN_ffff0000907992b0();
  if (iVar1 == 0) {
    local_68 = *(undefined8 *)(*(long *)((long)param_1 * 8 + -0xffff6f6f5588) + 0x10);
    lVar3 = FUN_ffff00009077d514();
    if (lVar3 == 0) {
      return 0xd;
    }
    if ((param_2 == (undefined *)0x0) || (param_3 == 0)) {
      param_2 = &DAT_ffff0000907df338;
      param_3 = 1;
    }
    iVar1 = FUN_ffff000090799c2c(param_1,param_2,param_3,param_6,param_7,lVar3,&local_68);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000907994cc(param_1,param_4,param_5,lVar3,local_68,param_8,param_9);
      FUN_ffff00009079c0d4(lVar3,local_68);
      thunk_FUN_ffff0000907c314c(lVar3);
      if (iVar1 == 0) {
        return 0;
      }
      pcVar2 = s__SEC__HKDF__hkdf_expand_failed__e_ffff0000907d6fa9;
    }
    else {
      FUN_ffff00009079c0d4(lVar3,local_68);
      thunk_FUN_ffff0000907c314c(lVar3);
      pcVar2 = s__SEC__HKDF__hkdf_extract_failed__ffff0000907d6f7f;
    }
  }
  else {
    pcVar2 = s__SEC__HKDF__hash_is_valid_failed_ffff000090801a06;
  }
  FUN_ffff00009077e11c(pcVar2,iVar1);
  return iVar1;
}


