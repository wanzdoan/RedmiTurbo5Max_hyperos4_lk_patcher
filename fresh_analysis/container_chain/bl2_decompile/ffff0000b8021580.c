
void FUN_ffff0000b8021580(uint *param_1,ulong param_2)

{
  uint *puVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  
  lVar4 = 0;
  DAT_ffff0000b80e9118 = DAT_ffff0000b80e9118 + 0x1cf00000;
  DAT_ffff0000b80e9120 = DAT_ffff0000b80e9120 + 0x1cf00000;
  DAT_ffff0000b80e9128 = DAT_ffff0000b80e9128 + 0x1cf00000;
  DAT_ffff0000b80e9130 = DAT_ffff0000b80e9130 + 0x1cf00000;
  DAT_ffff0000b80e9138 = DAT_ffff0000b80e9138 + 0x1cf00000;
  DAT_ffff0000b80e9140 = DAT_ffff0000b80e9140 + 0x1cf00000;
  DAT_ffff0000b80e9148 = DAT_ffff0000b80e9148 + 0x1cf00000;
  DAT_ffff0000b80e9150 = DAT_ffff0000b80e9150 + 0x1cf00000;
  DAT_ffff0000b80e9158 = DAT_ffff0000b80e9158 + 0x1cf00000;
  DAT_ffff0000b80e9160 = DAT_ffff0000b80e9160 + 0x1cf00000;
  DAT_ffff0000b80e9168 = DAT_ffff0000b80e9168 + 0x1cf00000;
  DAT_ffff0000b80e9170 = DAT_ffff0000b80e9170 + 0x1cf00000;
  DAT_ffff0000b80e9178 = DAT_ffff0000b80e9178 + 0x1cf00000;
  DAT_ffff0000b80e9180 = DAT_ffff0000b80e9180 + 0x1cf00000;
  DAT_ffff0000b80e9188 = DAT_ffff0000b80e9188 + 0x1cf00000;
  do {
    uVar2 = *(uint *)((long)&DAT_ffff0000b80e911c + lVar4);
    if (param_2 < (long)param_1 + (ulong)uVar2 + 8) {
      return;
    }
    uVar3 = *(uint *)((long)&DAT_ffff0000b80e9118 + lVar4);
    *param_1 = uVar3 & 0xfffff | 0x70000000;
    param_1[1] = uVar2;
    FUN_ffff0000b804cd40(param_1 + 2,(ulong)uVar3 & 0xfffffff | 0xffff000010000000);
    puVar1 = (uint *)((long)&DAT_ffff0000b80e911c + lVar4);
    lVar4 = lVar4 + 8;
    param_1 = (uint *)((long)(param_1 + 2) + ((ulong)*puVar1 & 0xfffffffc));
  } while (lVar4 != 0x80);
  return;
}


