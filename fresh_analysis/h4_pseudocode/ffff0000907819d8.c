
void FUN_ffff0000907819d8(long param_1)

{
  undefined4 uVar1;
  int iVar2;
  undefined8 uVar3;
  char *pcVar4;
  undefined4 uVar5;
  int local_34;
  
  if (param_1 == 0) {
    pcVar4 = s_kedump__failed_to_load_device_tr_ffff0000907dec41;
  }
  else {
    iVar2 = FUN_ffff00009076da24(param_1,s__logstore_ffff0000907f1e7a);
    if (-1 < iVar2) {
      local_34 = 0;
      uVar3 = FUN_ffff00009076df9c(param_1,iVar2,s_version_ffff0000907f7b3e,&local_34);
      if (local_34 < 1) {
        FUN_ffff000090781ae8(s_kedump__failed_to_get__logstore__ffff0000907f0586);
        FUN_ffff00009077e11c(s_kedump__failed_to_get__logstore__ffff0000907f0586);
      }
      else {
        iVar2 = FUN_ffff00009077f0ac(uVar3,&DAT_ffff0000907e9fb8);
        DAT_ffff00009087d0e8 = iVar2 == 0;
        uVar5 = 0x600000;
        uVar1 = uVar5;
        if (!(bool)DAT_ffff00009087d0e8) {
          uVar1 = 0x300000;
        }
        FUN_ffff000090781ae8(s_kedump__get_expdb_reserved_other_ffff0000907ffd55,uVar1);
        if (DAT_ffff00009087d0e8 == '\0') {
          uVar5 = 0x300000;
        }
        FUN_ffff00009077e11c(s_kedump__get_expdb_reserved_other_ffff0000907ffd55,uVar5);
      }
      return;
    }
    pcVar4 = s_kedump__failed_to_get__logstore_n_ffff0000907e9f92;
  }
  FUN_ffff000090781ae8(pcVar4);
  FUN_ffff00009077e11c(pcVar4);
  return;
}


