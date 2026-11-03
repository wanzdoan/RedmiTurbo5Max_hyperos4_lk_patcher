
void FUN_ffff0000be015f88(undefined8 param_1)

{
  int iVar1;
  ulong uVar2;
  
  if ((DAT_ffff0000be0d6118 == '\0' || DAT_ffff0000be108738 == 0) ||
     ((iVar1 = FUN_ffff0000be060fa0(s_system_ffff0000be0b865c,&DAT_ffff0000be107b44,0x20),
      iVar1 != 0 &&
      (iVar1 = FUN_ffff0000be060fa0(s_super_ffff0000be0b2cdf,&DAT_ffff0000be107b44,0x20), iVar1 != 0
      )))) {
    uVar2 = FUN_ffff0000be01755c(param_1);
  }
  else {
    uVar2 = FUN_ffff0000be017408(param_1);
  }
  if ((uVar2 & 1) != 0) {
    return;
  }
  if (DAT_ffff0000be108730 == 0) {
    DAT_ffff0000be108730 = FUN_ffff0000be013604(s_max_download_size_ffff0000be0bf4d4);
  }
  FUN_ffff0000be0135a4(s_max_download_size_ffff0000be0bf4d4,DAT_ffff0000be108730);
  return;
}


