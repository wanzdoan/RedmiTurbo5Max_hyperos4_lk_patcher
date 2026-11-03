
uint * FUN_ffff0000be0573e4(long param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  ulong uVar6;
  ulong uVar7;
  undefined1 local_14 [4];
  
  uVar7 = *(ulong *)(param_1 + 8);
  FUN_ffff0000be0571c4(param_1,&DAT_ffff0000be0b351f,1);
  uVar6 = *(ulong *)(param_1 + 8);
  while ((uVar6 & 0x3f) != 0x38) {
    FUN_ffff0000be0571c4(param_1,&DAT_ffff0000be0d1533,1);
    uVar6 = *(ulong *)(param_1 + 8);
  }
  uVar1 = (uint)(uVar7 >> 0x20);
  local_14[0] = (undefined1)(uVar1 >> 0x15);
  FUN_ffff0000be0571c4(param_1,local_14,1);
  local_14[0] = (undefined1)(uVar1 >> 0xd);
  FUN_ffff0000be0571c4(param_1,local_14,1);
  local_14[0] = (undefined1)(uVar1 >> 5);
  FUN_ffff0000be0571c4(param_1,local_14,1);
  local_14[0] = (undefined1)(uVar7 >> 0x1d);
  FUN_ffff0000be0571c4(param_1,local_14,1);
  local_14[0] = (undefined1)(uVar7 >> 0x15);
  FUN_ffff0000be0571c4(param_1,local_14,1);
  local_14[0] = (undefined1)(uVar7 >> 0xd);
  FUN_ffff0000be0571c4(param_1,local_14,1);
  local_14[0] = (undefined1)(uVar7 >> 5);
  FUN_ffff0000be0571c4(param_1,local_14,1);
  local_14[0] = (undefined1)((int)uVar7 << 3);
  FUN_ffff0000be0571c4(param_1,local_14,1);
  uVar1 = (*(uint *)(param_1 + 0x50) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x50) & 0xff00ff) << 8;
  *(uint *)(param_1 + 0x10) = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (*(uint *)(param_1 + 0x54) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x54) & 0xff00ff) << 8;
  uVar2 = (*(uint *)(param_1 + 0x58) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x58) & 0xff00ff) << 8;
  uVar3 = (*(uint *)(param_1 + 0x5c) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x5c) & 0xff00ff) << 8;
  *(uint *)(param_1 + 0x14) = uVar1 >> 0x10 | uVar1 << 0x10;
  *(uint *)(param_1 + 0x18) = uVar2 >> 0x10 | uVar2 << 0x10;
  uVar1 = (*(uint *)(param_1 + 0x60) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x60) & 0xff00ff) << 8;
  uVar2 = (*(uint *)(param_1 + 100) & 0xff00ff00) >> 8 | (*(uint *)(param_1 + 100) & 0xff00ff) << 8;
  uVar4 = (*(uint *)(param_1 + 0x68) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x68) & 0xff00ff) << 8;
  uVar5 = (*(uint *)(param_1 + 0x6c) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x6c) & 0xff00ff) << 8;
  *(uint *)(param_1 + 0x1c) = uVar3 >> 0x10 | uVar3 << 0x10;
  *(uint *)(param_1 + 0x20) = uVar1 >> 0x10 | uVar1 << 0x10;
  *(uint *)(param_1 + 0x24) = uVar2 >> 0x10 | uVar2 << 0x10;
  *(uint *)(param_1 + 0x28) = uVar4 >> 0x10 | uVar4 << 0x10;
  *(uint *)(param_1 + 0x2c) = uVar5 >> 0x10 | uVar5 << 0x10;
  return (uint *)(param_1 + 0x10);
}


