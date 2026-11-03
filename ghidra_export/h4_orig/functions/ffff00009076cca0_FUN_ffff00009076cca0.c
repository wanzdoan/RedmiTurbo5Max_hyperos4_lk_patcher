// ffff00009076cca0  size=168  FUN_ffff00009076cca0

long FUN_ffff00009076cca0(long param_1,uint param_2,uint param_3)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  
  lVar3 = 0;
  uVar2 = (uint)*(byte *)(param_1 + 8) << 0x18 | (uint)*(byte *)(param_1 + 9) << 0x10 |
          (uint)*(byte *)(param_1 + 10) << 8 | (uint)*(byte *)(param_1 + 0xb);
  uVar1 = uVar2 + param_2;
  if ((-1 < (int)param_2) && (!CARRY4(uVar2,param_2))) {
    if (((CARRY4(uVar1,param_3)) ||
        (uVar2 = (*(uint *)(param_1 + 4) & 0xff00ff00) >> 8 |
                 (*(uint *)(param_1 + 4) & 0xff00ff) << 8,
        (uVar2 >> 0x10 | uVar2 << 0x10) < uVar1 + param_3)) ||
       ((uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
                 (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8, 0x10 < (uVar1 >> 0x10 | uVar1 << 0x10)
        && ((CARRY4(param_3,param_2) ||
            (uVar1 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
                     (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8,
            (uVar1 >> 0x10 | uVar1 << 0x10) < param_3 + param_2)))))) {
      lVar3 = 0;
    }
    else {
      lVar3 = param_1 + ((ulong)*(byte *)(param_1 + 8) << 0x18 |
                         (ulong)*(byte *)(param_1 + 9) << 0x10 | (ulong)*(byte *)(param_1 + 10) << 8
                        | (ulong)*(byte *)(param_1 + 0xb)) + (ulong)param_2;
    }
  }
  return lVar3;
}


