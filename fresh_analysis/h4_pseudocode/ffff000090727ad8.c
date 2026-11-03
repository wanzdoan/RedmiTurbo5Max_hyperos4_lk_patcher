
void FUN_ffff000090727ad8(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  lVar2 = FUN_ffff00009077d514(0x100000);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff000090785d18
                      (s_gpueb_ffff0000907eaa9f,s_tinysys_gpueb_RV33_A_xfile_ffff0000907d908a,lVar2,
                       0x100000);
    if (iVar1 < 0) {
      FUN_ffff00009077e11c
                (s__s__GPUEB_ELF_IMG_fail__s_ffff0000907ef734,s__GPUEB__EXPDB__ffff0000907fb5a9,
                 s_tinysys_gpueb_RV33_A_xfile_ffff0000907d908a);
    }
    else {
      uVar3 = FUN_ffff00009077b334(lVar2);
      uVar4 = (*param_1)(uVar3,iVar1);
      if ((uVar4 & 1) == 0) {
        FUN_ffff00009077e11c
                  (s__s__xfile_dump_failed__ffff0000907d424e,s__GPUEB__EXPDB__ffff0000907fb5a9);
      }
    }
    thunk_FUN_ffff0000907c314c(lVar2);
    return;
  }
  FUN_ffff00009077e11c(s_xfile_ptr_malloc_failed_ffff0000907ec2c7);
  return;
}


