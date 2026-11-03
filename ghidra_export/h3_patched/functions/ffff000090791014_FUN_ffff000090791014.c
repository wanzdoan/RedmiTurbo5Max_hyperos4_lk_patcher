// ffff000090791014  size=128  FUN_ffff000090791014

void FUN_ffff000090791014(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  
  iVar1 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea59a);
  if (iVar1 < 0) {
    pcVar2 = s__UFS__fdt_path_offset___failed___ffff0000907fa843;
  }
  else {
    iVar1 = FUN_ffff00009076e40c(param_1,iVar1,s_atag_ufs_ffff0000907f0611,&DAT_ffff00009090a3a4,8);
    if (iVar1 == 0) {
      return;
    }
    pcVar2 = s__UFS__set_FDT_atag_ufs_failed____ffff0000907d529f;
  }
  FUN_ffff00009077de54(pcVar2,iVar1);
  return;
}


