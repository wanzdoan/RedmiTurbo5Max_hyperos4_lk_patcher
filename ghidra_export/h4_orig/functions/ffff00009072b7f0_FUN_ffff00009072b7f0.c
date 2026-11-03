// ffff00009072b7f0  size=200  FUN_ffff00009072b7f0

void FUN_ffff00009072b7f0(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  lVar2 = FUN_ffff00009077d514(0x180000);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff000090785d18
                      (s_mcupm_ffff0000907f29e1,s_tinysys_mcupm_RV33_A_xfile_ffff0000907ec361,lVar2,
                       0x180000);
    if (iVar1 < 0) {
      FUN_ffff00009077e11c
                (s__s__MCUPM_ELF_IMG_fail__s__no_xf_ffff0000907f9a30,s__MCUPMLDR__ffff0000907f6268,
                 s_tinysys_mcupm_RV33_A_xfile_ffff0000907ec361);
    }
    else {
      uVar3 = FUN_ffff00009077b334(lVar2);
      uVar4 = (*param_1)(uVar3,iVar1);
      if ((uVar4 & 1) == 0) {
        FUN_ffff00009077e11c
                  (s__s__xfile_dump_failed__ffff0000907d424e,s__MCUPMLDR__ffff0000907f6268);
      }
    }
    thunk_FUN_ffff0000907c314c(lVar2);
    return;
  }
  FUN_ffff00009077e11c(s_xfile_ptr_malloc_for_mcupm_xfile_ffff0000907feed8);
  return;
}


