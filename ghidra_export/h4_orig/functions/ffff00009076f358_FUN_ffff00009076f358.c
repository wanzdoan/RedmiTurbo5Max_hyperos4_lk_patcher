// ffff00009076f358  size=140  FUN_ffff00009076f358

long FUN_ffff00009076f358(long param_1,ulong param_2)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  ulong uVar5;
  
  uVar2 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
  uVar2 = uVar2 >> 0x10 | uVar2 << 0x10;
  uVar1 = uVar2 + param_2;
  if (!CARRY8((ulong)uVar2,param_2)) {
    uVar5 = (ulong)*(byte *)(param_1 + 9) << 0x10;
    uVar4 = (*(uint *)(param_1 + 0x20) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x20) & 0xff00ff) << 8;
    uVar3 = (*(uint *)(param_1 + 4) & 0xff00ff00) >> 8 | (*(uint *)(param_1 + 4) & 0xff00ff) << 8;
    if (uVar1 <= ((uVar3 >> 0x10 | uVar3 << 0x10) - (uVar4 >> 0x10 | uVar4 << 0x10)) -
                 ((uint)uVar5 | (uint)*(byte *)(param_1 + 10) << 8 | (uint)*(byte *)(param_1 + 0xb)
                 | (uint)*(byte *)(param_1 + 8) << 0x18)) {
      uVar4 = (uint)uVar1;
      uVar4 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
      *(uint *)(param_1 + 0x24) = uVar4 >> 0x10 | uVar4 << 0x10;
      return param_1 + (uVar5 | (ulong)*(byte *)(param_1 + 8) << 0x18 |
                       (ulong)CONCAT11(*(byte *)(param_1 + 10),*(byte *)(param_1 + 0xb))) +
             (long)(int)uVar2;
    }
  }
  return 0;
}


