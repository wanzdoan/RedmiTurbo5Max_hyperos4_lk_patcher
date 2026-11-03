
undefined8 FUN_ffff0000b80478e0(long param_1,int param_2,ulong *param_3,ulong *param_4)

{
  ulong *puVar1;
  uint uVar2;
  uint uVar3;
  undefined8 uVar4;
  ulong uVar5;
  
  uVar4 = FUN_ffff0000b8047008();
  if (-1 < (int)uVar4) {
    uVar2 = (uint)*(byte *)(param_1 + 0x10) << 0x18 | (uint)*(byte *)(param_1 + 0x11) << 0x10 |
            (uint)*(byte *)(param_1 + 0x12) << 8 | (uint)*(byte *)(param_1 + 0x13);
    if ((CARRY4(uVar2,param_2 * 0x10)) ||
       (uVar3 = (*(uint *)(param_1 + 4) & 0xff00ff00) >> 8 |
                (*(uint *)(param_1 + 4) & 0xff00ff) << 8,
       (ulong)(uVar3 >> 0x10 | uVar3 << 0x10) - 0x10 < (ulong)(uVar2 + param_2 * 0x10))) {
      uVar4 = 0xfffffffc;
    }
    else {
      uVar4 = 0;
      puVar1 = (ulong *)(param_1 + ((ulong)*(byte *)(param_1 + 0x10) << 0x18 |
                                    (ulong)*(byte *)(param_1 + 0x11) << 0x10 |
                                    (ulong)*(byte *)(param_1 + 0x12) << 8 |
                                   (ulong)*(byte *)(param_1 + 0x13)) + (long)param_2 * 0x10);
      uVar5 = *puVar1;
      uVar5 = (uVar5 & 0xff00ff00ff00ff00) >> 8 | (uVar5 & 0xff00ff00ff00ff) << 8;
      uVar5 = (uVar5 & 0xffff0000ffff0000) >> 0x10 | (uVar5 & 0xffff0000ffff) << 0x10;
      *param_3 = uVar5 >> 0x20 | uVar5 << 0x20;
      uVar5 = puVar1[1];
      uVar5 = (uVar5 & 0xff00ff00ff00ff00) >> 8 | (uVar5 & 0xff00ff00ff00ff) << 8;
      uVar5 = (uVar5 & 0xffff0000ffff0000) >> 0x10 | (uVar5 & 0xffff0000ffff) << 0x10;
      *param_4 = uVar5 >> 0x20 | uVar5 << 0x20;
    }
  }
  return uVar4;
}


