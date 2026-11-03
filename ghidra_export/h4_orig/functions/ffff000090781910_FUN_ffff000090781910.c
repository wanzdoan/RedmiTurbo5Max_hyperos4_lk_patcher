// ffff000090781910  size=164  FUN_ffff000090781910

void FUN_ffff000090781910(code *param_1)

{
  int iVar1;
  char *pcVar2;
  ulong local_28;
  long local_8;
  
  local_8 = 0;
  local_28 = 0;
  iVar1 = FUN_ffff000090783718(s_adsp_shared_reserved_ffff0000907e0d70,&local_8,&local_28);
  if (iVar1 < 0) {
    pcVar2 = s__s__mrdump_get_reserved_info_fai_ffff0000907e39d9;
  }
  else {
    if (local_8 != 0) {
      (*param_1)(local_8,0x100000);
      if (local_28 >> 0xb < 0x901) {
        return;
      }
      (*param_1)(local_8 + 0x200400,0x100000);
      return;
    }
    pcVar2 = s__s__mblock_allocation_failed_ffff0000907d0d73;
  }
  FUN_ffff00009077e11c(pcVar2,s__ADSP__EXPDB__ffff0000907dec33);
  return;
}


