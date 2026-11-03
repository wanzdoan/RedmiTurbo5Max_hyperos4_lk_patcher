
void FUN_ffff0000be063194(long param_1)

{
  undefined4 uVar1;
  int iVar2;
  undefined8 uVar3;
  char *pcVar4;
  undefined4 uVar5;
  int local_34;
  
  if (param_1 == 0) {
    pcVar4 = s_kedump__failed_to_load_device_tr_ffff0000be0ade2c;
  }
  else {
    iVar2 = FUN_ffff0000be05620c(param_1,s__logstore_ffff0000be0bb550);
    if (-1 < iVar2) {
      local_34 = 0;
      uVar3 = FUN_ffff0000be056494(param_1,iVar2,s_version_ffff0000be0bf4cc,&local_34);
      if (local_34 < 1) {
        FUN_ffff0000be063b40(s_kedump__failed_to_get__logstore__ffff0000be0ba473);
        FUN_ffff0000be05fd98(s_kedump__failed_to_get__logstore__ffff0000be0ba473);
      }
      else {
        iVar2 = FUN_ffff0000be060cb8(uVar3,&DAT_ffff0000be0b5b67);
        DAT_ffff0000be1271a8 = iVar2 == 0;
        uVar5 = 0x600000;
        uVar1 = uVar5;
        if (!(bool)DAT_ffff0000be1271a8) {
          uVar1 = 0x300000;
        }
        FUN_ffff0000be063b40(s_kedump__get_expdb_reserved_other_ffff0000be0c4840,uVar1);
        if (DAT_ffff0000be1271a8 == '\0') {
          uVar5 = 0x300000;
        }
        FUN_ffff0000be05fd98(s_kedump__get_expdb_reserved_other_ffff0000be0c4840,uVar5);
      }
      return;
    }
    pcVar4 = s_kedump__failed_to_get__logstore_n_ffff0000be0b5b41;
  }
  FUN_ffff0000be063b40(pcVar4);
  FUN_ffff0000be05fd98(pcVar4);
  return;
}


