
void FUN_ffff0000be022330(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  
  lVar2 = FUN_ffff0000be05f1bc(0x100000);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff0000be066394
                      (s_gpueb_ffff0000be0b63d7,s_tinysys_gpueb_RV33_A_xfile_ffff0000be0aa30f,lVar2,
                       0x100000);
    if (iVar1 < 0) {
      FUN_ffff0000be05fd98
                (s__s__GPUEB_ELF_IMG_fail__s_ffff0000be0b9b60,s__GPUEB__EXPDB__ffff0000be0c19fc,
                 s_tinysys_gpueb_RV33_A_xfile_ffff0000be0aa30f);
    }
    else {
      uVar3 = FUN_ffff0000be05d228(lVar2);
      uVar4 = (*param_1)(uVar3,iVar1);
      if ((uVar4 & 1) == 0) {
        FUN_ffff0000be05fd98
                  (s__s__xfile_dump_failed__ffff0000be0a6c67,s__GPUEB__EXPDB__ffff0000be0c19fc);
      }
    }
    thunk_FUN_ffff0000be09937c(lVar2);
    return;
  }
  FUN_ffff0000be05fd98(s_xfile_ptr_malloc_failed_ffff0000be0b7642);
  return;
}


