// File offset entry: 0x16fb8

undefined8 FUN_ffff0000b8016fb8(void)

{
  int iVar1;
  int iVar2;
  int iVar3;
  ulong uVar4;
  char *pcVar5;
  
  iVar1 = FUN_ffff0000b800a01c();
  uVar4 = FUN_ffff0000b8016e10();
  if ((uVar4 & 1) == 0) {
    pcVar5 = s_load_geniezone_ffff0000b8097a44;
  }
  else {
    iVar2 = FUN_ffff0000b8016e2c();
    iVar3 = FUN_ffff0000b800a01c();
    FUN_ffff0000b804beb0
              (s__PROFILE_______s_takes__d_ms_ffff0000b8089606,s_load_geniezone_ffff0000b8097a44,
               iVar3 - iVar1);
    if (iVar2 != 0) {
      return 0;
    }
    iVar1 = FUN_ffff0000b800a01c();
    FUN_ffff0000b8024f78();
    pcVar5 = s_smmu_init_ffff0000b8086f61;
  }
  iVar2 = FUN_ffff0000b800a01c();
  FUN_ffff0000b804beb0(s__PROFILE_______s_takes__d_ms_ffff0000b8089606,pcVar5,iVar2 - iVar1);
  return 0;
}


