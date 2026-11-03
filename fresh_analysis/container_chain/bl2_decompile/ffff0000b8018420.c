
void FUN_ffff0000b8018420(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  lVar2 = FUN_ffff0000b804bccc(0x100000);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff0000b8050eb8
                      (s_gpueb_ffff0000b80943bc,s_tinysys_gpueb_RV33_A_xfile_ffff0000b808a8a3,lVar2,
                       0x100000);
    if (iVar1 < 0) {
      FUN_ffff0000b804c33c
                (s__s__GPUEB_ELF_IMG_fail__s_ffff0000b809704f,s__GPUEB__EXPDB__ffff0000b809d5e3,
                 s_tinysys_gpueb_RV33_A_xfile_ffff0000b808a8a3);
    }
    else {
      uVar3 = FUN_ffff0000b8049ec8(lVar2);
      uVar4 = (*param_1)(uVar3,iVar1);
      if ((uVar4 & 1) == 0) {
        FUN_ffff0000b804c33c
                  (s__s__xfile_dump_failed__ffff0000b8087fdb,s__GPUEB__EXPDB__ffff0000b809d5e3);
      }
    }
    thunk_FUN_ffff0000b8082558(lVar2);
    return;
  }
  FUN_ffff0000b804c33c(s_xfile_ptr_malloc_failed_ffff0000b809530e);
  return;
}


