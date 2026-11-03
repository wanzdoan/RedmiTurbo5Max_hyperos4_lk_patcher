
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff00009078ad28(void)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  long lVar4;
  ulong local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  FUN_ffff00009071ebf4(0x82000415,2,9,0,0,0,0,0,&local_60);
  if (local_60 == 0) {
    uVar3 = 0;
    _DAT_ffff00001042fe00 = _DAT_ffff00001042fe00 | 2;
    _DAT_ffff00001042fe80 = _DAT_ffff00001042fe80 | 2;
    _DAT_ffff00001042f410 = _DAT_ffff00001042f410 | 2;
    _DAT_ffff00001042fe40 = _DAT_ffff00001042fe40 | 2;
    _DAT_ffff00001042fec0 = _DAT_ffff00001042fec0 | 2;
    _DAT_ffff00001052fe00 = _DAT_ffff00001052fe00 | 2;
    _DAT_ffff00001052fe80 = _DAT_ffff00001052fe80 | 2;
    _DAT_ffff00001052f410 = _DAT_ffff00001052f410 | 2;
    _DAT_ffff00001052fe40 = _DAT_ffff00001052fe40 | 2;
    _DAT_ffff00001052fec0 = _DAT_ffff00001052fec0 | 2;
  }
  else {
    FUN_ffff00009071ebf4(0x82000415,2,7,0,0,0,0,0,&local_60);
    uVar1 = local_60;
    uVar3 = 0;
    lVar4 = 1;
    do {
      if ((uVar1 >> (lVar4 - 1U & 0x3f) & 1) == 0) {
        uVar2 = (ulong)uVar3;
        (&DAT_ffff0000908898d0)[uVar2 * 8] = (int)lVar4;
        FUN_ffff00009071ebf4(0x82000415,2,6,lVar4,0,0,0,0,&local_60);
        (&DAT_ffff0000908898d4)[uVar2 * 8] = (int)local_60;
        (&DAT_ffff0000908898d8)[uVar2 * 4] = uStack_58;
        (&DAT_ffff0000908898e0)[uVar2 * 4] = local_50;
        FUN_ffff00009071ebf4(0x82000415,2,4,lVar4,7,0,0,0,&local_60);
        uVar3 = uVar3 + 1;
        (&DAT_ffff0000908898e8)[uVar2 * 8] = (uint)(local_60 >> 0x22) & 3;
      }
      lVar4 = lVar4 + 1;
    } while (lVar4 != 0x40);
  }
  return uVar3;
}


