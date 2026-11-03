
undefined8 FUN_ffff0000907b2a58(long param_1,undefined8 param_2,uint param_3,uint *param_4)

{
  byte bVar1;
  uint uVar2;
  undefined8 uVar3;
  uint uVar4;
  long lVar5;
  byte *pbVar6;
  ulong uVar7;
  byte *pbVar8;
  
  bVar1 = *(byte *)(param_1 + 1);
  if (((char)bVar1 < '\0') && (uVar7 = (ulong)bVar1 & 0x7f, (int)uVar7 != 0)) {
    uVar4 = 0;
    lVar5 = uVar7 + 1;
    pbVar8 = (byte *)(param_1 + 2);
    do {
      uVar2 = (int)uVar7 - 1;
      uVar7 = (ulong)uVar2;
      uVar4 = (uint)*pbVar8 | uVar4 << 8;
      pbVar8 = pbVar8 + 1;
    } while (uVar2 != 0);
  }
  else {
    uVar4 = (uint)bVar1;
    lVar5 = 1;
  }
  pbVar8 = (byte *)(param_1 + 1) + lVar5;
  uVar3 = 0x101028;
  if ((*pbVar8 == 0) && (pbVar6 = pbVar8 + 1, (char)*pbVar6 < '\0')) {
    uVar4 = uVar4 - 1;
    *param_4 = uVar4;
  }
  else {
    *param_4 = uVar4;
    pbVar6 = pbVar8;
  }
  if (uVar4 <= param_3) {
    FUN_ffff0000907ad234(param_2,pbVar6,uVar4);
    uVar3 = 0;
  }
  return uVar3;
}


