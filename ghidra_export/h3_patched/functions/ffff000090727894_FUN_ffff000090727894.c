// ffff000090727894  size=200  FUN_ffff000090727894

void FUN_ffff000090727894(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  lVar2 = FUN_ffff00009077d24c(0x100000);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff000090785a50
                      (s_gpueb_ffff0000907ea905,s_tinysys_gpueb_RV33_A_xfile_ffff0000907d8ff3,lVar2,
                       0x100000);
    if (iVar1 < 0) {
      FUN_ffff00009077de54
                (s__s__GPUEB_ELF_IMG_fail__s_ffff0000907ef5ac,s__GPUEB__EXPDB__ffff0000907fb40d,
                 s_tinysys_gpueb_RV33_A_xfile_ffff0000907d8ff3);
    }
    else {
      uVar3 = FUN_ffff00009077b06c(lVar2);
      uVar4 = (*param_1)(uVar3,iVar1);
      if ((uVar4 & 1) == 0) {
        FUN_ffff00009077de54
                  (s__s__xfile_dump_failed__ffff0000907d4211,s__GPUEB__EXPDB__ffff0000907fb40d);
      }
    }
    thunk_FUN_ffff0000907c2ad8(lVar2);
    return;
  }
  FUN_ffff00009077de54(s_xfile_ptr_malloc_failed_ffff0000907ec12d);
  return;
}


