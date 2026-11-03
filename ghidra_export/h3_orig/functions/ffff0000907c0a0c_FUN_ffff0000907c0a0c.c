// ffff0000907c0a0c  size=16  FUN_ffff0000907c0a0c

bool FUN_ffff0000907c0a0c(byte *param_1,byte *param_2,ulong param_3)

{
  byte bVar1;
  byte *pbVar2;
  byte *pbVar3;
  byte *pbVar4;
  ulong uVar5;
  ulong uVar6;
  byte bVar7;
  byte *pbVar8;
  ulong uVar9;
  
  bVar1 = 0;
  if (param_3 == 0) {
    return false;
  }
  if (param_3 == 1) {
    uVar5 = 1;
    pbVar3 = param_2;
    pbVar4 = param_1;
  }
  else {
    uVar6 = param_3 & 0xfffffffffffffffe;
    bVar7 = 0;
    bVar1 = 0;
    pbVar3 = param_2 + uVar6;
    pbVar4 = param_1 + uVar6;
    uVar5 = param_3 & 1;
    pbVar2 = param_1 + 1;
    pbVar8 = param_2 + 1;
    uVar9 = uVar6;
    do {
      uVar9 = uVar9 - 2;
      bVar7 = bVar7 | pbVar8[-1] ^ pbVar2[-1];
      bVar1 = bVar1 | *pbVar8 ^ *pbVar2;
      pbVar2 = pbVar2 + 2;
      pbVar8 = pbVar8 + 2;
    } while (uVar9 != 0);
    bVar1 = bVar1 | bVar7;
    if (uVar6 == param_3) goto LAB_ffff0000907c0aa8;
  }
  do {
    uVar5 = uVar5 - 1;
    bVar1 = bVar1 | *pbVar3 ^ *pbVar4;
    pbVar3 = pbVar3 + 1;
    pbVar4 = pbVar4 + 1;
  } while (uVar5 != 0);
LAB_ffff0000907c0aa8:
  return bVar1 != 0;
}


