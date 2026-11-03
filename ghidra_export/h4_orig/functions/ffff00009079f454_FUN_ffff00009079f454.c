// ffff00009079f454  size=116  FUN_ffff00009079f454

int FUN_ffff00009079f454(long param_1)

{
  ushort uVar1;
  int iVar2;
  uint uVar3;
  ulong uVar4;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e064d,0x97c,
               s_mp______void__0__ffff0000907ed71b);
  }
  uVar3 = *(int *)(param_1 + 8) - 1;
  if ((uVar3 == 0) && (**(short **)(param_1 + 0x10) == 0)) {
    iVar2 = 1;
  }
  else {
    uVar1 = (*(short **)(param_1 + 0x10))[uVar3];
    iVar2 = uVar3 * 2;
    uVar4 = (ulong)uVar1;
    if (uVar1 != 0) {
      do {
        uVar3 = (uint)uVar4;
        iVar2 = iVar2 + 1;
        uVar4 = uVar4 >> 8;
      } while (0xff < uVar3);
    }
  }
  return iVar2;
}


