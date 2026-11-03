
int FUN_ffff000090787514(undefined8 param_1)

{
  undefined1 *puVar1;
  int iVar2;
  undefined1 auStack_c3f [3087];
  undefined1 *local_18;
  
  FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_GetSecinfoPartition_ffff0000907f55d8);
  FUN_ffff00009077ee50(auStack_c3f,0,0xc0f);
  FUN_ffff00009077ee50(auStack_c3f,0,0xc0f);
  local_18 = auStack_c3f;
  iVar2 = FUN_ffff0000907873b4(0,0xc0f,&local_18);
  puVar1 = local_18;
  if (iVar2 == 0) {
    iVar2 = FUN_ffff00009077f348(local_18,s_SECINFO_BOOT__ffff0000907ffe2b,0xd);
    if (iVar2 == 0) {
      FUN_ffff00009077e11c(s_secinfo_magic_is_match__ffff0000907ea0dc);
      FUN_ffff00009077eb20(param_1,puVar1 + 0x40f,0x800);
      iVar2 = FUN_ffff00009078745c(param_1);
      if (iVar2 == 0) {
        FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_GetSecinfoPartition_ffff0000907f55d8);
      }
      else {
        FUN_ffff00009077e11c(s_EccVerfySecinfo_failed__ffff0000907dd42a);
      }
    }
    else {
      FUN_ffff00009077e11c(s_secinfo_magic_is_not_match__ffff0000907ded08);
      iVar2 = 0xf;
    }
  }
  else {
    FUN_ffff00009077e11c(s_GetPartitionInfoBootFail_failed__ffff0000907f383e);
    iVar2 = -1;
  }
  return iVar2;
}


