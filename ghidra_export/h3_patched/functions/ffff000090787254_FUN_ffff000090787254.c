// ffff000090787254  size=284  FUN_ffff000090787254

int FUN_ffff000090787254(undefined8 param_1)

{
  undefined1 *puVar1;
  int iVar2;
  undefined1 auStack_c3f [3087];
  undefined1 *local_18;
  
  FUN_ffff00009077de54(s__s_IN_ffff0000907dd321,s_GetSecinfoPartition_ffff0000907f5454);
  FUN_ffff00009077eb88(auStack_c3f,0,0xc0f);
  FUN_ffff00009077eb88(auStack_c3f,0,0xc0f);
  local_18 = auStack_c3f;
  iVar2 = FUN_ffff0000907870f4(0,0xc0f,&local_18);
  puVar1 = local_18;
  if (iVar2 == 0) {
    iVar2 = FUN_ffff00009077f080(local_18,s_SECINFO_BOOT__ffff0000907ffc83,0xd);
    if (iVar2 == 0) {
      FUN_ffff00009077de54(s_secinfo_magic_is_match__ffff0000907e9f81);
      FUN_ffff00009077e858(param_1,puVar1 + 0x40f,0x800);
      iVar2 = FUN_ffff00009078719c(param_1);
      if (iVar2 == 0) {
        FUN_ffff00009077de54(s__s_OUT_ffff0000907fa7b2,s_GetSecinfoPartition_ffff0000907f5454);
      }
      else {
        FUN_ffff00009077de54(s_EccVerfySecinfo_failed__ffff0000907dd370);
      }
    }
    else {
      FUN_ffff00009077de54(s_secinfo_magic_is_not_match__ffff0000907dec4e);
      iVar2 = 0xf;
    }
  }
  else {
    FUN_ffff00009077de54(s_GetPartitionInfoBootFail_failed__ffff0000907f369b);
    iVar2 = -1;
  }
  return iVar2;
}


