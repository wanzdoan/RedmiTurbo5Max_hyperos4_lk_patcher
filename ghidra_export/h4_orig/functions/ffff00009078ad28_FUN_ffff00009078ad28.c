// ffff00009078ad28  size=500  FUN_ffff00009078ad28

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff00009078ad28(void)

{
  long lVar1;
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
    uVar2 = local_60;
    uVar3 = 0;
    lVar4 = 1;
    do {
      if ((uVar2 >> (lVar4 - 1U & 0x3f) & 1) == 0) {
        lVar1 = (ulong)uVar3 * 0x20;
        *(int *)(lVar1 + -0xffff6f776730) = (int)lVar4;
        FUN_ffff00009071ebf4(0x82000415,2,6,lVar4,0,0,0,0,&local_60);
        *(int *)(lVar1 + -0xffff6f77672c) = (int)local_60;
        *(undefined8 *)(lVar1 + -0xffff6f776728) = uStack_58;
        *(undefined8 *)(lVar1 + -0xffff6f776720) = local_50;
        FUN_ffff00009071ebf4(0x82000415,2,4,lVar4,7,0,0,0,&local_60);
        uVar3 = uVar3 + 1;
        *(uint *)(lVar1 + -0xffff6f776718) = (uint)(local_60 >> 0x22) & 3;
      }
      lVar4 = lVar4 + 1;
    } while (lVar4 != 0x40);
  }
  return uVar3;
}


