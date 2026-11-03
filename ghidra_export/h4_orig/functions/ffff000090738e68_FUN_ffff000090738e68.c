// ffff000090738e68  size=336  FUN_ffff000090738e68

void FUN_ffff000090738e68(uint *param_1,ulong param_2)

{
  uint *puVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  
  lVar4 = 0;
  DAT_ffff000090819340 = DAT_ffff000090819340 + 0x1cf00000;
  DAT_ffff000090819348 = DAT_ffff000090819348 + 0x1cf00000;
  DAT_ffff000090819350 = DAT_ffff000090819350 + 0x1cf00000;
  DAT_ffff000090819358 = DAT_ffff000090819358 + 0x1cf00000;
  DAT_ffff000090819360 = DAT_ffff000090819360 + 0x1cf00000;
  DAT_ffff000090819368 = DAT_ffff000090819368 + 0x1cf00000;
  DAT_ffff000090819370 = DAT_ffff000090819370 + 0x1cf00000;
  DAT_ffff000090819378 = DAT_ffff000090819378 + 0x1cf00000;
  DAT_ffff000090819380 = DAT_ffff000090819380 + 0x1cf00000;
  DAT_ffff000090819388 = DAT_ffff000090819388 + 0x1cf00000;
  DAT_ffff000090819390 = DAT_ffff000090819390 + 0x1cf00000;
  DAT_ffff000090819398 = DAT_ffff000090819398 + 0x1cf00000;
  DAT_ffff0000908193a0 = DAT_ffff0000908193a0 + 0x1cf00000;
  DAT_ffff0000908193a8 = DAT_ffff0000908193a8 + 0x1cf00000;
  DAT_ffff0000908193b0 = DAT_ffff0000908193b0 + 0x1cf00000;
  do {
    uVar2 = *(uint *)((long)&DAT_ffff000090819344 + lVar4);
    if (param_2 < (long)param_1 + (ulong)uVar2 + 8) {
      return;
    }
    uVar3 = *(uint *)((long)&DAT_ffff000090819340 + lVar4);
    *param_1 = uVar3 & 0xfffff | 0x70000000;
    param_1[1] = uVar2;
    FUN_ffff00009077eb20(param_1 + 2,(ulong)uVar3 & 0xfffffff | 0xffff000010000000);
    puVar1 = (uint *)((long)&DAT_ffff000090819344 + lVar4);
    lVar4 = lVar4 + 8;
    param_1 = (uint *)((long)(param_1 + 2) + ((ulong)*puVar1 & 0xfffffffc));
  } while (lVar4 != 0x80);
  return;
}


