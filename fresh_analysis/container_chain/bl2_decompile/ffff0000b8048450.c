
ulong FUN_ffff0000b8048450
                (long param_1,undefined4 param_2,undefined8 param_3,uint param_4,long *param_5)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  ulong uVar5;
  long lVar6;
  long local_60;
  uint local_54;
  
  uVar5 = FUN_ffff0000b8047008();
  if ((int)uVar5 < 0) {
    return uVar5;
  }
  uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
  uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
  if (uVar1 < 0x11) {
    return 0xfffffff6;
  }
  uVar2 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
  iVar4 = FUN_ffff0000b8048998(param_1,0x10,uVar2 >> 0x10 | uVar2 << 0x10);
  if (iVar4 != 0) {
    return 0xfffffff4;
  }
  if (0x11 < uVar1) {
    *(undefined4 *)(param_1 + 0x14) = 0x11000000;
  }
  local_54 = 0;
  lVar6 = FUN_ffff0000b8047fb0(param_1,param_2,param_3,&local_54);
  local_60 = lVar6;
  if (lVar6 == 0) {
    uVar5 = (ulong)local_54;
  }
  else {
    uVar1 = param_4 + 3 & 0xfffffffc;
    uVar2 = local_54 + 3 & 0xfffffffc;
    uVar5 = FUN_ffff0000b8048b84(param_1,lVar6 + 0xc,uVar2,uVar1);
    if ((int)uVar5 == 0) {
      iVar4 = uVar1 - uVar2;
      uVar1 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
      uVar2 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8;
      uVar1 = (uVar1 >> 0x10 | uVar1 << 0x10) + iVar4;
      uVar2 = (uVar2 >> 0x10 | uVar2 << 0x10) + iVar4;
      uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
      uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
      uVar3 = (param_4 & 0xff00ff00) >> 8 | (param_4 & 0xff00ff) << 8;
      *(uint *)(param_1 + 0x24) = uVar1 >> 0x10 | uVar1 << 0x10;
      *(uint *)(param_1 + 0xc) = uVar2 >> 0x10 | uVar2 << 0x10;
      *(uint *)(lVar6 + 4) = uVar3 >> 0x10 | uVar3 << 0x10;
      local_60 = lVar6;
      goto LAB_ffff0000b8048588;
    }
  }
  if ((int)uVar5 == -1) {
    uVar5 = FUN_ffff0000b80485b4(param_1,param_2,param_3,param_4,&local_60);
  }
  if ((int)uVar5 != 0) {
    return uVar5;
  }
LAB_ffff0000b8048588:
  *param_5 = local_60 + 0xc;
  return 0;
}


