
void FUN_ffff0000907912d4(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  
  iVar1 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
  if (iVar1 < 0) {
    pcVar2 = s__UFS__fdt_path_offset___failed___ffff0000907fa9df;
  }
  else {
    iVar1 = FUN_ffff00009076e6d4(param_1,iVar1,s_atag_ufs_ffff0000907f0799,&DAT_ffff00009090a464,8);
    if (iVar1 == 0) {
      return;
    }
    pcVar2 = s__UFS__set_FDT_atag_ufs_failed____ffff0000907d52dc;
  }
  FUN_ffff00009077e11c(pcVar2,iVar1);
  return;
}


