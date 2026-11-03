
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075ada8
               (undefined8 param_1,int param_2,int param_3,int param_4,int param_5,
               undefined8 param_6,int param_7)

{
  int iVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  
  if (param_2 != 1) {
    uVar2 = param_5 * ((uint)(param_3 * param_4 * 0x78) / 1000) * 0x7d;
    uVar5 = uVar2 / 16000;
    uVar6 = uVar2 / 16000;
    uVar3 = (0xea0 - uVar2 / 16000) * -0xc + 0x3b5380;
    uVar7 = (uVar6 * 0x104) / 1000;
    uVar4 = uVar3 / 1000;
    if (uVar3 / 1000 <= (uVar6 * 0x104) / 1000) {
      uVar4 = uVar7;
    }
    iVar1 = (uVar2 / 64000) * 0x10000;
    uVar3 = 0x40000000;
    if (param_7 != 0) {
      uVar3 = 0x80000000;
    }
    _DAT_ffff000032170030 = (uVar3 | uVar2 / 64000) + uVar7 * 0x10000;
    _DAT_ffff000032170034 = uVar3 + iVar1 + (uVar5 * 0xe6) / 1000;
    _DAT_ffff00003217003c = 0xf30 - uVar2 / 64000;
    _DAT_ffff0000321700a8 = (uVar5 * 0x118) / 1000 + uVar4 * 0x10000;
    _DAT_ffff0000321700ac = uVar2 / 64000 + iVar1 + 0x10000;
    _DAT_ffff000032170040 = 0x8f300000;
    if (param_2 == 0) {
      _DAT_ffff0000321700b0 = 0;
    }
    uVar4 = (uVar5 * 0x122) / 1000;
    _DAT_ffff0000321700dc = uVar4 * 0x10000;
    _DAT_ffff0000321700d0 = uVar4 + ((uVar6 * 300) / 1000) * 0x10000;
    uVar4 = (uVar6 * 0x10e) / 1000;
    _DAT_ffff0000321700d4 = _DAT_ffff0000321700dc + uVar4;
    _DAT_ffff0000321700d8 = (uVar6 * 0x10e) / 1000 + uVar4 * 0x10000;
    _DAT_ffff0000321700dc = _DAT_ffff0000321700dc + (uVar5 * 0x122) / 1000;
    _DAT_ffff0000321700e8 =
         (uVar6 * 0x6e) / 1000 +
         (uint)((ulong)((uVar2 / 16000) * 0x80 + uVar5 * -8) * 0x4189375 >> 0x24) * 0x10000;
    _DAT_ffff0000321701a8 = _DAT_ffff0000321701a8 | 0x20000000;
  }
  return;
}


