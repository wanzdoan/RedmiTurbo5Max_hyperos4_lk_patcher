// ffff000090799428  size=352  FUN_ffff000090799428

int FUN_ffff000090799428
              (int param_1,undefined *param_2,long param_3,undefined8 param_4,undefined8 param_5,
              undefined8 param_6,undefined8 param_7,undefined8 param_8,undefined8 param_9)

{
  int iVar1;
  char *pcVar2;
  long lVar3;
  undefined8 local_68;
  
  iVar1 = FUN_ffff000090798ff0();
  if (iVar1 == 0) {
    local_68 = *(undefined8 *)(*(long *)((long)param_1 * 8 + -0xffff6f6f5648) + 0x10);
    lVar3 = FUN_ffff00009077d24c();
    if (lVar3 == 0) {
      return 0xd;
    }
    if ((param_2 == (undefined *)0x0) || (param_3 == 0)) {
      param_2 = &DAT_ffff0000907df295;
      param_3 = 1;
    }
    iVar1 = FUN_ffff00009079996c(param_1,param_2,param_3,param_6,param_7,lVar3,&local_68);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009079920c(param_1,param_4,param_5,lVar3,local_68,param_8,param_9);
      FUN_ffff00009079be14(lVar3,local_68);
      thunk_FUN_ffff0000907c2ad8(lVar3);
      if (iVar1 == 0) {
        return 0;
      }
      pcVar2 = s__SEC__HKDF__hkdf_expand_failed__e_ffff0000907d6f13;
    }
    else {
      FUN_ffff00009079be14(lVar3,local_68);
      thunk_FUN_ffff0000907c2ad8(lVar3);
      pcVar2 = s__SEC__HKDF__hkdf_extract_failed__ffff0000907d6ee9;
    }
  }
  else {
    pcVar2 = s__SEC__HKDF__hash_is_valid_failed_ffff00009080182a;
  }
  FUN_ffff00009077de54(pcVar2,iVar1);
  return iVar1;
}


