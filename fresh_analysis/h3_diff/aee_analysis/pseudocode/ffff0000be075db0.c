
int FUN_ffff0000be075db0
              (int param_1,undefined *param_2,long param_3,undefined8 param_4,undefined8 param_5,
              undefined8 param_6,undefined8 param_7,undefined8 param_8,undefined8 param_9)

{
  int iVar1;
  char *pcVar2;
  long lVar3;
  undefined8 local_68;
  
  iVar1 = FUN_ffff0000be075978();
  if (iVar1 == 0) {
    local_68 = *(undefined8 *)((&DAT_ffff0000be1b4f88)[param_1] + 0x10);
    lVar3 = FUN_ffff0000be05f1bc();
    if (lVar3 == 0) {
      return 0xd;
    }
    if ((param_2 == (undefined *)0x0) || (param_3 == 0)) {
      param_2 = &DAT_ffff0000be0ae340;
      param_3 = 1;
    }
    iVar1 = FUN_ffff0000be0762f4(param_1,param_2,param_3,param_6,param_7,lVar3,&local_68);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000be075b94(param_1,param_4,param_5,lVar3,local_68,param_8,param_9);
      FUN_ffff0000be07879c(lVar3,local_68);
      thunk_FUN_ffff0000be09937c(lVar3);
      if (iVar1 == 0) {
        return 0;
      }
      pcVar2 = s__SEC__HKDF__hkdf_expand_failed__e_ffff0000be0a89b9;
    }
    else {
      FUN_ffff0000be07879c(lVar3,local_68);
      thunk_FUN_ffff0000be09937c(lVar3);
      pcVar2 = s__SEC__HKDF__hkdf_extract_failed__ffff0000be0a898f;
    }
  }
  else {
    pcVar2 = s__SEC__HKDF__hash_is_valid_failed_ffff0000be0c5b54;
  }
  FUN_ffff0000be05fd98(pcVar2,iVar1);
  return iVar1;
}


