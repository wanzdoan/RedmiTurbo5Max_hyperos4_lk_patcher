
void FUN_ffff0000b801d00c(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  lVar2 = FUN_ffff0000b804bccc(0x180000);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff0000b8050eb8
                      (s_mcupm_ffff0000b8098c15,s_tinysys_mcupm_RV33_A_xfile_ffff0000b8095383,lVar2,
                       0x180000);
    if (iVar1 < 0) {
      FUN_ffff0000b804c33c
                (s__s__MCUPM_ELF_IMG_fail__s__no_xf_ffff0000b809c633,s__MCUPMLDR__ffff0000b809a918,
                 s_tinysys_mcupm_RV33_A_xfile_ffff0000b8095383);
    }
    else {
      uVar3 = FUN_ffff0000b8049ec8(lVar2);
      uVar4 = (*param_1)(uVar3,iVar1);
      if ((uVar4 & 1) == 0) {
        FUN_ffff0000b804c33c
                  (s__s__xfile_dump_failed__ffff0000b8087fdb,s__MCUPMLDR__ffff0000b809a918);
      }
    }
    thunk_FUN_ffff0000b8082558(lVar2);
    return;
  }
  FUN_ffff0000b804c33c(s_xfile_ptr_malloc_for_mcupm_xfile_ffff0000b809f64d);
  return;
}


