// ffff000090781710  size=272  FUN_ffff000090781710

void FUN_ffff000090781710(long param_1)

{
  undefined4 uVar1;
  int iVar2;
  undefined8 uVar3;
  char *pcVar4;
  undefined4 uVar5;
  int local_34;
  
  if (param_1 == 0) {
    pcVar4 = s_kedump__failed_to_load_device_tr_ffff0000907deb87;
  }
  else {
    iVar2 = FUN_ffff00009076d75c(param_1,s__logstore_ffff0000907f1cd7);
    if (-1 < iVar2) {
      local_34 = 0;
      uVar3 = FUN_ffff00009076dcd4(param_1,iVar2,s_version_ffff0000907f79b4,&local_34);
      if (local_34 < 1) {
        FUN_ffff000090781820(s_kedump__failed_to_get__logstore__ffff0000907f03fe);
        FUN_ffff00009077de54(s_kedump__failed_to_get__logstore__ffff0000907f03fe);
      }
      else {
        iVar2 = FUN_ffff00009077ede4(uVar3,&DAT_ffff0000907e9e5d);
        DAT_ffff00009087d050 = iVar2 == 0;
        uVar5 = 0x600000;
        uVar1 = uVar5;
        if (!(bool)DAT_ffff00009087d050) {
          uVar1 = 0x300000;
        }
        FUN_ffff000090781820(s_kedump__get_expdb_reserved_other_ffff0000907ffbad,uVar1);
        if (DAT_ffff00009087d050 == '\0') {
          uVar5 = 0x300000;
        }
        FUN_ffff00009077de54(s_kedump__get_expdb_reserved_other_ffff0000907ffbad,uVar5);
      }
      return;
    }
    pcVar4 = s_kedump__failed_to_get__logstore_n_ffff0000907e9e37;
  }
  FUN_ffff000090781820(pcVar4);
  FUN_ffff00009077de54(pcVar4);
  return;
}


