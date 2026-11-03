
undefined8 FUN_ffff0000be09332c(long param_1,long param_2)

{
  undefined8 uVar1;
  ulong uVar2;
  long lVar3;
  ulong uVar4;
  ulong local_b0 [16];
  
  uVar1 = 0x7247;
  if ((param_1 != 0) && (param_2 != 0)) {
    local_b0[0xe] = 0;
    local_b0[0xf] = 0;
    local_b0[0xc] = 0;
    local_b0[0xd] = 0;
    uVar4 = *(ulong *)(param_1 + 0x68) & 0x3f;
    local_b0[10] = 0;
    local_b0[0xb] = 0;
    local_b0[8] = 0;
    local_b0[9] = 0;
    local_b0[6] = 0;
    local_b0[7] = 0;
    local_b0[4] = 0;
    local_b0[5] = 0;
    local_b0[2] = 0;
    local_b0[3] = 0;
    local_b0[0] = 0;
    local_b0[1] = 0;
    FUN_ffff0000be0607b0(local_b0,param_1,uVar4);
    lVar3 = 0x38;
    if (0x37 < uVar4) {
      lVar3 = 0x78;
    }
    uVar2 = *(long *)(param_1 + 0x68) << 3;
    uVar1 = 0x40;
    if (0x37 < uVar4) {
      uVar1 = 0x80;
    }
    uVar2 = (uVar2 & 0xff00ff00ff00ff00) >> 8 | (uVar2 & 0xff00ff00ff00ff) << 8;
    uVar2 = (uVar2 & 0xffff0000ffff0000) >> 0x10 | (uVar2 & 0xffff0000ffff) << 0x10;
    *(undefined1 *)((long)local_b0 + uVar4) = 0x80;
    *(ulong *)((long)local_b0 + lVar3) = uVar2 >> 0x20 | uVar2 << 0x20;
    uVar1 = FUN_ffff0000be093160(param_1,local_b0,uVar1);
    if ((int)uVar1 == 0) {
      lVar3 = *(long *)(param_1 + 0x40);
      *(uint *)(param_1 + 100) =
           (*(uint *)(lVar3 + 0x34) & 0xff00) << 8 | *(int *)(lVar3 + 0x34) << 0x18 |
           *(uint *)(lVar3 + 0x34) >> 8 & 0xff00 | *(uint *)(lVar3 + 0x34) >> 0x18;
      *(uint *)(param_1 + 0x60) =
           (*(uint *)(lVar3 + 0x38) & 0xff00) << 8 | *(int *)(lVar3 + 0x38) << 0x18 |
           *(uint *)(lVar3 + 0x38) >> 8 & 0xff00 | *(uint *)(lVar3 + 0x38) >> 0x18;
      *(uint *)(param_1 + 0x5c) =
           (*(uint *)(lVar3 + 0x3c) & 0xff00) << 8 | *(int *)(lVar3 + 0x3c) << 0x18 |
           *(uint *)(lVar3 + 0x3c) >> 8 & 0xff00 | *(uint *)(lVar3 + 0x3c) >> 0x18;
      *(uint *)(param_1 + 0x58) =
           (*(uint *)(lVar3 + 0x40) & 0xff00) << 8 | *(int *)(lVar3 + 0x40) << 0x18 |
           *(uint *)(lVar3 + 0x40) >> 8 & 0xff00 | *(uint *)(lVar3 + 0x40) >> 0x18;
      *(uint *)(param_1 + 0x54) =
           (*(uint *)(lVar3 + 0x44) & 0xff00) << 8 | *(int *)(lVar3 + 0x44) << 0x18 |
           *(uint *)(lVar3 + 0x44) >> 8 & 0xff00 | *(uint *)(lVar3 + 0x44) >> 0x18;
      *(uint *)(param_1 + 0x50) =
           (*(uint *)(lVar3 + 0x48) & 0xff00) << 8 | *(int *)(lVar3 + 0x48) << 0x18 |
           *(uint *)(lVar3 + 0x48) >> 8 & 0xff00 | *(uint *)(lVar3 + 0x48) >> 0x18;
      *(uint *)(param_1 + 0x4c) =
           (*(uint *)(lVar3 + 0x4c) & 0xff00) << 8 | *(int *)(lVar3 + 0x4c) << 0x18 |
           *(uint *)(lVar3 + 0x4c) >> 8 & 0xff00 | *(uint *)(lVar3 + 0x4c) >> 0x18;
      *(uint *)(param_1 + 0x48) =
           (*(uint *)(lVar3 + 0x50) & 0xff00) << 8 | *(int *)(lVar3 + 0x50) << 0x18 |
           *(uint *)(lVar3 + 0x50) >> 8 & 0xff00 | *(uint *)(lVar3 + 0x50) >> 0x18;
      FUN_ffff0000be0607b0(param_2,(uint *)(param_1 + 0x48),0x20);
      uVar1 = 0;
    }
  }
  return uVar1;
}


