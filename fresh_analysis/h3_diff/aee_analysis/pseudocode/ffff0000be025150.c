
void FUN_ffff0000be025150(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  lVar2 = FUN_ffff0000be05f1bc(0x180000);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff0000be066394
                      (s_mcupm_ffff0000be0bbe22,s_tinysys_mcupm_RV33_A_xfile_ffff0000be0b7680,lVar2,
                       0x180000);
    if (iVar1 < 0) {
      FUN_ffff0000be05fd98
                (s__s__MCUPM_ELF_IMG_fail__s__no_xf_ffff0000be0c0867,s__MCUPMLDR__ffff0000be0be278,
                 s_tinysys_mcupm_RV33_A_xfile_ffff0000be0b7680);
    }
    else {
      uVar3 = FUN_ffff0000be05d228(lVar2);
      uVar4 = (*param_1)(uVar3,iVar1);
      if ((uVar4 & 1) == 0) {
        FUN_ffff0000be05fd98
                  (s__s__xfile_dump_failed__ffff0000be0a6c67,s__MCUPMLDR__ffff0000be0be278);
      }
    }
    thunk_FUN_ffff0000be09937c(lVar2);
    return;
  }
  FUN_ffff0000be05fd98(s_xfile_ptr_malloc_for_mcupm_xfile_ffff0000be0c3f68);
  return;
}


