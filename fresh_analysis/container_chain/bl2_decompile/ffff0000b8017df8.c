
undefined4 FUN_ffff0000b8017df8(void)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  int iVar4;
  undefined4 uVar5;
  
  lVar3 = DAT_ffff0000b80e8d58;
  if (DAT_ffff0000b811b0f4 == 0) {
    DAT_ffff0000b811b0f4 = 1;
    uVar1 = 0;
    if (DAT_ffff0000b811b0e8 == '\0') {
      uVar1 = 0x600;
    }
    uVar2 = 0;
    if (DAT_ffff0000b811b0f0 == '\0') {
      uVar2 = 0xc800;
    }
    *(uint *)(DAT_ffff0000b80e8d58 + 0x690) =
         *(uint *)(DAT_ffff0000b80e8d58 + 0x690) | uVar1 | uVar2 | 0x20;
    uVar1 = 0;
    if (DAT_ffff0000b811b0e8 == '\0') {
      uVar1 = 0x600;
    }
    uVar2 = 0;
    if (DAT_ffff0000b811b0f0 == '\0') {
      uVar2 = 0xc800;
    }
    *(uint *)(lVar3 + 0x694) = *(uint *)(lVar3 + 0x694) | uVar1 | uVar2 | 0x20;
    *(uint *)(lVar3 + 0x698) = *(uint *)(lVar3 + 0x698) | 0x80000000;
    DAT_ffff0000b811b0f4 = DAT_ffff0000b811b0f4 + 1;
    iVar4 = FUN_ffff0000b8017c60();
    if (iVar4 == 0) {
      uVar5 = 0;
    }
    else {
      uVar5 = 0xffffffff;
      FUN_ffff0000b804c33c(s__GZ_INIT__REMAP__MT_V3__gz_remap_ffff0000b8092507,0xffffffff);
    }
    DAT_ffff0000b811b0e8 = '\x01';
    DAT_ffff0000b811b0f0 = '\x01';
    DAT_ffff0000b80e8d58 = 0;
  }
  else {
    uVar5 = 0;
  }
  return uVar5;
}


