
bool FUN_ffff0000b8048998(long param_1,int param_2,int param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  
  uVar1 = (*(uint *)(param_1 + 0x10) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x10) & 0xff00ff) << 8;
  uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
  if (((0x27 < uVar1) &&
      (uVar2 = (*(uint *)(param_1 + 8) & 0xff00ff00) >> 8 | (*(uint *)(param_1 + 8) & 0xff00ff) << 8
      , uVar2 = uVar2 >> 0x10 | uVar2 << 0x10, uVar1 + param_2 <= uVar2)) &&
     (uVar1 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8, uVar1 = uVar1 >> 0x10 | uVar1 << 0x10,
     uVar2 + param_3 <= uVar1)) {
    uVar2 = (*(uint *)(param_1 + 0x20) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x20) & 0xff00ff) << 8;
    uVar3 = (*(uint *)(param_1 + 4) & 0xff00ff00) >> 8 | (*(uint *)(param_1 + 4) & 0xff00ff) << 8;
    return (uVar3 >> 0x10 | uVar3 << 0x10) < (uVar2 >> 0x10 | uVar2 << 0x10) + uVar1;
  }
  return true;
}


