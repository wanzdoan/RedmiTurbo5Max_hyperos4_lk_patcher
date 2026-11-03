
void FUN_ffff0000be029f20(uint *param_1,ulong param_2)

{
  uint *puVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  
  lVar4 = 0;
  DAT_ffff0000be0d6678 = DAT_ffff0000be0d6678 + 0x1cf00000;
  DAT_ffff0000be0d6680 = DAT_ffff0000be0d6680 + 0x1cf00000;
  DAT_ffff0000be0d6688 = DAT_ffff0000be0d6688 + 0x1cf00000;
  DAT_ffff0000be0d6690 = DAT_ffff0000be0d6690 + 0x1cf00000;
  DAT_ffff0000be0d6698 = DAT_ffff0000be0d6698 + 0x1cf00000;
  DAT_ffff0000be0d66a0 = DAT_ffff0000be0d66a0 + 0x1cf00000;
  DAT_ffff0000be0d66a8 = DAT_ffff0000be0d66a8 + 0x1cf00000;
  DAT_ffff0000be0d66b0 = DAT_ffff0000be0d66b0 + 0x1cf00000;
  DAT_ffff0000be0d66b8 = DAT_ffff0000be0d66b8 + 0x1cf00000;
  DAT_ffff0000be0d66c0 = DAT_ffff0000be0d66c0 + 0x1cf00000;
  DAT_ffff0000be0d66c8 = DAT_ffff0000be0d66c8 + 0x1cf00000;
  DAT_ffff0000be0d66d0 = DAT_ffff0000be0d66d0 + 0x1cf00000;
  DAT_ffff0000be0d66d8 = DAT_ffff0000be0d66d8 + 0x1cf00000;
  DAT_ffff0000be0d66e0 = DAT_ffff0000be0d66e0 + 0x1cf00000;
  DAT_ffff0000be0d66e8 = DAT_ffff0000be0d66e8 + 0x1cf00000;
  do {
    uVar2 = *(uint *)((long)&DAT_ffff0000be0d667c + lVar4);
    if (param_2 < (long)param_1 + (ulong)uVar2 + 8) {
      return;
    }
    uVar3 = *(uint *)((long)&DAT_ffff0000be0d6678 + lVar4);
    *param_1 = uVar3 & 0xfffff | 0x70000000;
    param_1[1] = uVar2;
    FUN_ffff0000be0607b0(param_1 + 2,(ulong)uVar3 & 0xfffffff | 0xffff000010000000);
    puVar1 = (uint *)((long)&DAT_ffff0000be0d667c + lVar4);
    lVar4 = lVar4 + 8;
    param_1 = (uint *)((long)(param_1 + 2) + ((ulong)*puVar1 & 0xfffffffc));
  } while (lVar4 != 0x80);
  return;
}


