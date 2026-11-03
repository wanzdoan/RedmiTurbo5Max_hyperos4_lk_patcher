// ffff00009072b558  size=200  FUN_ffff00009072b558

void FUN_ffff00009072b558(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  lVar2 = FUN_ffff00009077d24c(0x180000);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff000090785a50
                      (s_mcupm_ffff0000907f283e,s_tinysys_mcupm_RV33_A_xfile_ffff0000907ec1c7,lVar2,
                       0x180000);
    if (iVar1 < 0) {
      FUN_ffff00009077de54
                (s__s__MCUPM_ELF_IMG_fail__s__no_xf_ffff0000907f9894,s__MCUPMLDR__ffff0000907f60c7,
                 s_tinysys_mcupm_RV33_A_xfile_ffff0000907ec1c7);
    }
    else {
      uVar3 = FUN_ffff00009077b06c(lVar2);
      uVar4 = (*param_1)(uVar3,iVar1);
      if ((uVar4 & 1) == 0) {
        FUN_ffff00009077de54
                  (s__s__xfile_dump_failed__ffff0000907d4211,s__MCUPMLDR__ffff0000907f60c7);
      }
    }
    thunk_FUN_ffff0000907c2ad8(lVar2);
    return;
  }
  FUN_ffff00009077de54(s_xfile_ptr_malloc_for_mcupm_xfile_ffff0000907fed30);
  return;
}


