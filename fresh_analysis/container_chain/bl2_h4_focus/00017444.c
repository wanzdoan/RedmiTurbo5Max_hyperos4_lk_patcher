// File offset entry: 0x17444

undefined8 FUN_ffff0000b8017444(void)

{
  int iVar1;
  int iVar2;
  int iVar3;
  ulong uVar4;
  char *pcVar5;
  
  iVar1 = FUN_ffff0000b800a01c();
  uVar4 = FUN_ffff0000b801729c();
  if ((uVar4 & 1) == 0) {
    pcVar5 = s_load_geniezone_ffff0000b8098b1d;
  }
  else {
    iVar2 = FUN_ffff0000b80172b8();
    iVar3 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804c33c
              (s__PROFILE_______s_takes__d_ms_ffff0000b808a666,s_load_geniezone_ffff0000b8098b1d,
               iVar3 - iVar1);
    if (iVar2 != 0) {
      return 0;
    }
    iVar1 = FUN_ffff0000b800a01c();
    FUN_ffff0000b8025404();
    pcVar5 = s_smmu_init_ffff0000b8087f96;
  }
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804c33c(s__PROFILE_______s_takes__d_ms_ffff0000b808a666,pcVar5,iVar2 - iVar1);
  return 0;
}


